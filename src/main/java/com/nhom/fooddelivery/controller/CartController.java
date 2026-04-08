package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.entity.Food;
import com.nhom.fooddelivery.repository.FoodRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    private FoodRepository foodRepository;

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
}
