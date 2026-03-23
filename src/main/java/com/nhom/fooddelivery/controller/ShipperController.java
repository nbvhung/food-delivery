package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.entity.Order;
import com.nhom.fooddelivery.entity.User;
import com.nhom.fooddelivery.repository.OrderRepository;
import com.nhom.fooddelivery.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.nhom.fooddelivery.constant.UserRole.SHIPPER;

@Controller
@RequestMapping("/shipper")
public class ShipperController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

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

        model.addAttribute("readyCount", readyCount);
        model.addAttribute("deliveringCount", deliveringCount);
        model.addAttribute("deliveredCount", deliveredCount);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("deliveringOrders", deliveringOrders);
        model.addAttribute("shipper", currentUser);
        return "shipper/shipper-dashboard";
    }

    @GetMapping("/waiting")
    public String waiting(HttpSession session, Model model){
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        List<Order> orders = orderRepository.findByStatus("READY");
        model.addAttribute("orders", orders);
        return "shipper/order-waiting";
    }

    @GetMapping("/delivering")
    public String delivering(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        List<Order> orders =
                orderRepository.findByShipperIdAndStatus(currentUser.getId(), "SHIPPING");
        model.addAttribute("orders", orders);
        return "shipper/order-delivering";
    }

    @GetMapping("/stats")
    public String stats(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        Long deliveredCount =
                orderRepository.countByShipperIdAndStatus(currentUser.getId(), "DELIVERED");

        Double totalEarnings =
                Optional.ofNullable(orderRepository.sumEarningsByShipper(currentUser.getId())).orElse(0.0);

        Double avgRating =
                Optional.ofNullable(orderRepository.getAverageRatingByShipper(currentUser.getId())).orElse(0.0);

        List<Order> completedOrders =
                orderRepository.findByShipperIdAndStatus(currentUser.getId(), "DELIVERED");

        // Format giá tiền VN
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        String totalEarningsStr = nf.format(totalEarnings);

        model.addAttribute("deliveredCount", deliveredCount);
        model.addAttribute("totalEarnings", totalEarningsStr);
        model.addAttribute("avgRating", avgRating);
        model.addAttribute("completedOrders", completedOrders);

        return "shipper/order-stats";
    }


    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        model.addAttribute("shipper", currentUser);
        return "shipper/shipper-profile";
    }

    @PostMapping("/profile")
    public String updateProfile (
            @RequestParam(required = false) String avatar,
            @RequestParam(required = false) String licensePlate,
            HttpSession session,
            RedirectAttributes ra
    ){
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        if (avatar != null && !avatar.isBlank()) {
            currentUser.setAvatar(avatar.trim());
        }

        if (licensePlate != null && !licensePlate.isBlank()) {
            String normalizedPlate = licensePlate.trim().toUpperCase();
            if (!normalizedPlate.equalsIgnoreCase(Optional.ofNullable(currentUser.getLicensePlate()).orElse(""))
                && !normalizedPlate.equalsIgnoreCase(Optional.ofNullable(currentUser.getPendingLicensePlate()).orElse(""))) {
                currentUser.setPendingLicensePlate(normalizedPlate);
                ra.addFlashAttribute("message", "pending_license_plate");
            }
        }

        userRepository.save(currentUser);
        session.setAttribute("currentUser", currentUser);
        return "redirect:/shipper/profile";
    }

    @GetMapping("/register")
    public String showShipperRegister(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";

        // Nếu đã là Shipper rồi thì không cần đăng ký nữa
        if (user.getRole() == SHIPPER) return "redirect:/";

        return "shipper/shipper-register";
    }

    //  Xử lý gửi yêu cầu
    @PostMapping("/register")
    public String processShipperRegister(HttpSession session, RedirectAttributes ra) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            currentUser.setStatus("PENDING_SHIPPER");
            userRepository.save(currentUser);
            ra.addFlashAttribute("message", "pending_shipper");
        }
        return "redirect:/";
    }
}
