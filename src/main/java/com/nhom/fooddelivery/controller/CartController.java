package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.entity.Food;
import com.nhom.fooddelivery.entity.Order;
import com.nhom.fooddelivery.entity.OrderDetail;
import com.nhom.fooddelivery.entity.Shop;
import com.nhom.fooddelivery.entity.User;
import com.nhom.fooddelivery.repository.FoodRepository;
import com.nhom.fooddelivery.repository.OrderRepository;
import com.nhom.fooddelivery.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CartController {
    private static final DateTimeFormatter ORDER_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/foods/detail/{id}")
    public String showFoodDetail(@PathVariable Long id, Model model) {
        Optional<Food> food = foodRepository.findById(id);
        if (food.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("food", food.get());
        return "checkout/food-detail";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long foodId,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        if (isAnonymous(session)) {
            return "redirect:/login";
        }
        Map<Long, Integer> cart = getCart(session);
        cart.put(foodId, cart.getOrDefault(foodId, 0) + Math.max(quantity, 1));
        return "redirect:/cart";
    }

    @PostMapping("/cart/buy-now")
    public String buyNow(@RequestParam Long foodId,
                         @RequestParam(defaultValue = "1") int quantity,
                         HttpSession session) {
        if (isAnonymous(session)) {
            return "redirect:/login";
        }
        Map<Long, Integer> cart = getCart(session);
        cart.clear();
        cart.put(foodId, Math.max(quantity, 1));
        return "redirect:/checkout";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, HttpSession session) {
        if (isAnonymous(session)) {
            return "redirect:/login";
        }
        CartSummary summary = buildCartSummary(session);
        model.addAttribute("items", summary.items());
        model.addAttribute("total", summary.total());
        return "checkout/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        if (isAnonymous(session)) {
            return "redirect:/login";
        }
        CartSummary summary = buildCartSummary(session);
        model.addAttribute("items", summary.items());
        model.addAttribute("total", summary.total());
        return "checkout/checkout";
    }

    @GetMapping("/checkout/orders")
    public String orderHistory(Model model, HttpSession session) {
        if (isAnonymous(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("currentUser");
        List<OrderHistoryItem> orders = new ArrayList<>();

        for (Order order : orderRepository.findOrderHistoryByCustomerId(currentUser.getId())) {
            List<OrderLineItem> details = new ArrayList<>();
            if (order.getOrderDetails() != null) {
                for (OrderDetail detail : order.getOrderDetails()) {
                    Food food = detail.getFood();
                    String foodName = food != null ? food.getName() : "Món ăn";
                    int quantity = detail.getQuantity() == null ? 0 : detail.getQuantity();
                    double linePrice = detail.getPrice() == null ? 0 : detail.getPrice();
                    details.add(new OrderLineItem(foodName, quantity, linePrice));
                }
            }

            String shopName = order.getShop() != null ? order.getShop().getName() : "Không rõ cửa hàng";
            String shipperName = order.getShipper() != null ? order.getShipper().getFullName() : "Chưa có shipper";
            String createdAtText = order.getCreatedAt() == null ? "" : order.getCreatedAt().format(ORDER_TIME_FORMATTER);

            OrderHistoryItem item = new OrderHistoryItem();
            item.setId(order.getId());
            item.setShopName(shopName);
            item.setStatus(order.getStatus());
            item.setStatusLabel(getStatusLabel(order.getStatus()));
            item.setAddress(order.getAddress());
            item.setPhone(order.getPhone());
            item.setTotal(order.getTotalPrice() == null ? 0 : order.getTotalPrice());
            item.setCreatedAtText(createdAtText);
            item.setShipperName(shipperName);
            item.setDetails(details);
            item.setCanReview("DELIVERED".equals(order.getStatus()) && order.getShipper() != null && order.getShipperRating() == null);
            item.setReviewed(order.getShipperRating() != null);
            item.setShipperRating(order.getShipperRating());
            item.setShipperReview(order.getShipperReview());
            orders.add(item);
        }

        model.addAttribute("orders", orders);
        return "checkout/order-history";
    }

    @PostMapping("/checkout/place-order")
    @Transactional
    public String placeOrder(@RequestParam String fullName,
                             @RequestParam String phone,
                             @RequestParam String address,
                             @RequestParam String paymentMethod,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (isAnonymous(session)) {
            return "redirect:/login";
        }

        if (fullName == null || fullName.isBlank()
                || phone == null || phone.isBlank()
                || address == null || address.isBlank()
                || paymentMethod == null || paymentMethod.isBlank()) {
            redirectAttributes.addFlashAttribute("checkoutError", "Vui lòng nhập đầy đủ thông tin thanh toán.");
            return "redirect:/checkout";
        }

        Map<Long, Integer> cart = getCart(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("checkoutError", "Giỏ hàng đang trống.");
            return "redirect:/checkout";
        }

        User sessionUser = (User) session.getAttribute("currentUser");
        User customer = userRepository.findById(sessionUser.getId()).orElse(null);
        if (customer == null) {
            return "redirect:/login";
        }

        Map<Long, PendingOrder> pendingOrders = new HashMap<>();

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Food food = foodRepository.findById(entry.getKey()).orElse(null);
            if (food == null || food.getShop() == null) {
                redirectAttributes.addFlashAttribute("checkoutError", "Có món ăn không hợp lệ, vui lòng kiểm tra lại giỏ hàng.");
                return "redirect:/checkout";
            }

            int quantity = Math.max(entry.getValue(), 1);
            double price = food.getPrice() == null ? 0 : food.getPrice();
            Shop shop = food.getShop();

            PendingOrder pendingOrder = pendingOrders.computeIfAbsent(
                    shop.getId(),
                    ignored -> new PendingOrder(shop)
            );

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setFood(food);
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(price);
            pendingOrder.orderDetails().add(orderDetail);
            pendingOrder.addTotal(price * quantity);
        }

        for (PendingOrder pendingOrder : pendingOrders.values()) {
            Order order = new Order();
            order.setCustomer(customer);
            order.setShop(pendingOrder.shop());
            order.setPhone(phone.trim());
            order.setAddress(address.trim());
            order.setStatus("READY");
            order.setTotalPrice(pendingOrder.total());

            List<OrderDetail> details = pendingOrder.orderDetails();
            for (OrderDetail detail : details) {
                detail.setOrder(order);
            }
            order.setOrderDetails(details);
            orderRepository.save(order);
        }

        cart.clear();
        customer.setFullName(fullName.trim());
        customer.setPhone(phone.trim());
        userRepository.save(customer);
        session.setAttribute("currentUser", customer);

        redirectAttributes.addFlashAttribute("checkoutSuccess", "Đặt hàng thành công. Bạn có thể theo dõi trạng thái đơn hàng tại đây.");
        return "redirect:/checkout/orders";
    }

    @PostMapping("/checkout/orders/review")
    public String reviewDeliveredOrder(@RequestParam Long orderId,
                                       @RequestParam Integer rating,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        if (isAnonymous(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("currentUser");
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getCustomer() == null || !currentUser.getId().equals(order.getCustomer().getId())) {
            redirectAttributes.addFlashAttribute("checkoutError", "Không tìm thấy đơn hàng hợp lệ để đánh giá.");
            return "redirect:/checkout/orders";
        }

        if (!"DELIVERED".equals(order.getStatus()) || order.getShipper() == null) {
            redirectAttributes.addFlashAttribute("checkoutError", "Đơn hàng này chưa sẵn sàng để xác nhận và đánh giá.");
            return "redirect:/checkout/orders";
        }

        if (order.getShipperRating() != null) {
            redirectAttributes.addFlashAttribute("checkoutError", "Đơn hàng này đã được đánh giá.");
            return "redirect:/checkout/orders";
        }

        int normalizedRating = Math.max(1, Math.min(rating == null ? 5 : rating, 5));
        order.setShipperRating(normalizedRating);
        order.setShipperReview(null);
        orderRepository.save(order);

        redirectAttributes.addFlashAttribute("checkoutSuccess", "Đã xác nhận nhận hàng thành công và gửi đánh giá cho shipper.");
        return "redirect:/checkout/orders";
    }

    @PostMapping("/checkout/orders/cancel")
    public String cancelOrder(@RequestParam Long orderId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (isAnonymous(session)) {
            return "redirect:/login";
        }

        User currentUser = (User) session.getAttribute("currentUser");
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getCustomer() == null || !currentUser.getId().equals(order.getCustomer().getId())) {
            redirectAttributes.addFlashAttribute("checkoutError", "Không tìm thấy đơn hàng hợp lệ để hủy.");
            return "redirect:/checkout/orders";
        }

        if (!"READY".equals(order.getStatus())) {
            redirectAttributes.addFlashAttribute("checkoutError", "Chỉ có thể hủy đơn khi đang chờ shipper nhận.");
            return "redirect:/checkout/orders";
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        redirectAttributes.addFlashAttribute("checkoutSuccess", "Đơn hàng đã được hủy thành công.");
        return "redirect:/checkout/orders";
    }

    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long foodId,
                             @RequestParam int quantity,
                             @RequestParam(required = false) String action,
                             HttpSession session) {

        Map<Long, Integer> cart = getCart(session);
        int nextQuantity = quantity;

        if ("decrease".equals(action)) {
            nextQuantity = quantity - 1;
        } else if ("increase".equals(action)) {
            nextQuantity = quantity + 1;
        }

        if (nextQuantity <= 0) {
            cart.remove(foodId); // số lượng <=0 thì xóa luôn
        } else {
            cart.put(foodId, nextQuantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long foodId,
                                 HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        cart.remove(foodId);
        return "redirect:/cart";
    }


    private boolean isAnonymous(HttpSession session) {
        return session.getAttribute("currentUser") == null;
    }

    private Map<Long, Integer> getCart(HttpSession session) {
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new LinkedHashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private CartSummary buildCartSummary(HttpSession session) {
        Map<Long, Integer> cart = getCart(session);
        List<CartItem> items = new ArrayList<>();
        double total = 0;

        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Optional<Food> foodOpt = foodRepository.findById(entry.getKey());
            if (foodOpt.isEmpty()) {
                continue;
            }
            Food food = foodOpt.get();
            int quantity = entry.getValue();
            double price = food.getPrice() == null ? 0 : food.getPrice();
            double subtotal = price * quantity;
            total += subtotal;
            items.add(new CartItem(food, quantity, subtotal));
        }

        return new CartSummary(items, total);
    }

    public static class CartItem {
        private final Food food;
        private final int quantity;
        private final double subtotal;

        public CartItem(Food food, int quantity, double subtotal) {
            this.food = food;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        public Food getFood() {
            return food;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getSubtotal() {
            return subtotal;
        }
    }

    private record CartSummary(List<CartItem> items, double total) {
    }

    private String getStatusLabel(String status) {
        if (status == null) {
            return "Không xác định";
        }
        return switch (status) {
            case "READY" -> "Chờ shipper nhận";
            case "SHIPPING" -> "Đang giao";
            case "DELIVERED" -> "Đã giao";
            case "CANCELLED" -> "Đã hủy";
            default -> status;
        };
    }

    private static class PendingOrder {
        private final Shop shop;
        private final List<OrderDetail> orderDetails = new ArrayList<>();
        private double total;

        private PendingOrder(Shop shop) {
            this.shop = shop;
        }

        private Shop shop() {
            return shop;
        }

        private List<OrderDetail> orderDetails() {
            return orderDetails;
        }

        private double total() {
            return total;
        }

        private void addTotal(double value) {
            total += value;
        }
    }

    public static class OrderHistoryItem {
        private Long id;
        private String shopName;
        private String status;
        private String statusLabel;
        private String address;
        private String phone;
        private double total;
        private String createdAtText;
        private String shipperName;
        private List<OrderLineItem> details;
        private boolean canReview;
        private boolean reviewed;
        private Integer shipperRating;
        private String shipperReview;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public String getStatusLabel() {
            return statusLabel;
        }

        public void setStatusLabel(String statusLabel) {
            this.statusLabel = statusLabel;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getCreatedAtText() {
            return createdAtText;
        }

        public void setCreatedAtText(String createdAtText) {
            this.createdAtText = createdAtText;
        }

        public String getShipperName() {
            return shipperName;
        }

        public void setShipperName(String shipperName) {
            this.shipperName = shipperName;
        }

        public List<OrderLineItem> getDetails() {
            return details;
        }

        public void setDetails(List<OrderLineItem> details) {
            this.details = details;
        }

        public boolean isCanReview() {
            return canReview;
        }

        public void setCanReview(boolean canReview) {
            this.canReview = canReview;
        }

        public boolean isReviewed() {
            return reviewed;
        }

        public void setReviewed(boolean reviewed) {
            this.reviewed = reviewed;
        }

        public Integer getShipperRating() {
            return shipperRating;
        }

        public void setShipperRating(Integer shipperRating) {
            this.shipperRating = shipperRating;
        }

        public String getShipperReview() {
            return shipperReview;
        }

        public void setShipperReview(String shipperReview) {
            this.shipperReview = shipperReview;
        }
    }

    public static class OrderLineItem {
        private final String foodName;
        private final int quantity;
        private final double price;

        public OrderLineItem(String foodName, int quantity, double price) {
            this.foodName = foodName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getFoodName() {
            return foodName;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }
    }
}
