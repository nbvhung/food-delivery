package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.entity.*;
import com.nhom.fooddelivery.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.nhom.fooddelivery.constant.UserRole.SHIPPER;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShipperRepository shipperRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        Long readyCount = orderRepository.countByStatus("READY");
        List<Order> deliveringOrders = orderRepository.findByShipperIdAndStatus(currentUser.getId(), "SHIPPING");
        Long deliveringCount = (long) deliveringOrders.size();
        Long deliveredCount =orderRepository.countByShipperIdAndStatus(currentUser.getId(), "DELIVERED");
        Double avgRating = Optional.ofNullable(orderRepository.getAverageRatingByShipper(currentUser.getId())).orElse(0.0);

        Shipper shipperProfile = shipperRepository.findByUserId(currentUser.getId());

        model.addAttribute("readyCount", readyCount);
        model.addAttribute("deliveringCount", deliveringCount);
        model.addAttribute("deliveredCount", deliveredCount);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("deliveringOrders", deliveringOrders);
        model.addAttribute("shipper", currentUser);

        model.addAttribute("shipperProfile", shipperProfile);
        return "shipper/shipper-dashboard";
    }

    @GetMapping("/waiting")
    public String waiting(HttpSession session, Model model){
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        List<Order> readyOrders = orderRepository.findByStatus("READY");
        List<Order> acceptedOrders = orderRepository.findByShipperIdAndStatus(currentUser.getId(), "ACCEPTED");

        readyOrders.addAll(acceptedOrders);
        readyOrders.sort(Comparator.comparing(Order::getCreatedAt));

        Map<Long, String> orderFoodSummary = new HashMap<>();

        for (Order order : readyOrders) {
            List<OrderDetail> details = orderDetailRepository.findByOrderId(order.getId());

            String foodSummary = details.stream()
                    .map(detail -> detail.getFood().getName() + " x" + detail.getQuantity())
                    .collect(Collectors.joining(", "));

            orderFoodSummary.put(order.getId(), foodSummary);
        }

        model.addAttribute("orders", readyOrders);
        model.addAttribute("orderFoodSummary", orderFoodSummary);

        return "shipper/order-waiting";
    }

    @GetMapping("/delivering")
    public String delivering(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        List<Order> orders = orderRepository.findByShipperIdAndStatus(currentUser.getId(), "SHIPPING");
        model.addAttribute("orders", orders);
        return "shipper/order-delivering";
    }

    @GetMapping("/stats")
    public String stats(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        Long deliveredCount = orderRepository.countByShipperIdAndStatus(currentUser.getId(), "DELIVERED");
        Double totalEarnings = Optional.ofNullable(orderRepository.sumEarningsByShipper(currentUser.getId())).orElse(0.0);
        Double avgRating = Optional.ofNullable(orderRepository.getAverageRatingByShipper(currentUser.getId())).orElse(0.0);
        List<Order> completedOrders = orderRepository.findByShipperIdAndStatus(currentUser.getId(), "DELIVERED");

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        String totalEarningsStr = nf.format(totalEarnings);

        model.addAttribute("deliveredCount", deliveredCount);
        model.addAttribute("totalEarnings", totalEarningsStr);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("completedOrders", completedOrders);

        return "shipper/order-stats";
    }


    @GetMapping("/register")
    public String showShipperRegister(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";

        // Nếu đã là SHIPPER thì vào thẳng dashboard
        if (user.getRole() != null && "SHIPPER".equals(user.getRole().name())) {
            return "redirect:/shipper/dashboard";
        }

        // Nếu status là PENDING_SHIPPER thì hiện trang thông báo
        if ("PENDING_SHIPPER".equals(user.getStatus())) {
            return "/shipper/shipper-pending";
        }

        return "shipper/shipper-register";
    }

    @PostMapping("/register")
    public String processShipperRegister(
            @RequestParam("phone") String phone,
            @RequestParam("licensePlate") String licensePlate,
            @RequestParam("vehicleType") String vehicleType,
            HttpSession session,
            RedirectAttributes ra) {

        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser != null) {
            currentUser.setPhone(phone);
            currentUser.setStatus("PENDING_SHIPPER");
            userRepository.save(currentUser);

            Shipper newShipper = new Shipper();
            newShipper.setUser(currentUser);
            newShipper.setLicensePlate(licensePlate);
            newShipper.setVehicleType(vehicleType);
            shipperRepository.save(newShipper);

            ra.addFlashAttribute("message", "pending_shipper");
            // Cập nhật lại session để giao diện đổi ngay trạng thái
            session.setAttribute("currentUser", currentUser);
        }
        return "redirect:/";
    }

    @GetMapping("/register/review")
    public String reviewShipperRegister(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        Shipper shipperProfile = shipperRepository.findByUserId(currentUser.getId());

        if (shipperProfile == null) {
            return "redirect:/shipper/register";
        }

        model.addAttribute("user", currentUser);
        model.addAttribute("shipperProfile", shipperProfile);

        return "shipper/shipper-register-review";
    }

    @PostMapping("/register/modify")
    public String modifyShipperRegisterForm(
            @RequestParam("phone") String phone,
            @RequestParam("licensePlate") String licensePlate,
            @RequestParam("vehicleType") String vehicleType,
            HttpSession session,
            RedirectAttributes ra
    ) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }
        Shipper shipper = shipperRepository.findByUserId(currentUser.getId());
        if (shipper == null) {
            return "redirect:/shipper/register";
        }

        currentUser.setPhone(phone.trim());
        userRepository.save(currentUser);

        shipper.setLicensePlate(licensePlate.trim().toUpperCase());
        shipper.setVehicleType(vehicleType);
        shipperRepository.save(shipper);

        ra.addFlashAttribute("message", "update_shipper_success");
        return "redirect:/shipper/register/review";
    }

    @PostMapping("/update-avatar")
    public String quickUpdateAvatar(@RequestParam("avatarFile") MultipartFile file, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s+", "");
                Path uploadPath = Paths.get("src/main/resources/static/images/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

                currentUser.setAvatar("/images/" + fileName);
                userRepository.save(currentUser);
                session.setAttribute("currentUser", currentUser);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/shipper/dashboard";
    }
}
