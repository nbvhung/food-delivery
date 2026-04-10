package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.entity.Order;
import com.nhom.fooddelivery.entity.Shipper;
import com.nhom.fooddelivery.entity.User;
import com.nhom.fooddelivery.repository.OrderRepository;
import com.nhom.fooddelivery.repository.ShipperRepository;
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
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    // TIÊM THÊM SHIPPER REPOSITORY VÀO ĐÂY
    @Autowired
    private ShipperRepository shipperRepository;

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


    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        // LẤY THÊM HỒ SƠ SHIPPER ĐỂ HIỂN THỊ BIỂN SỐ XE LÊN GIAO DIỆN
        Shipper shipperProfile = shipperRepository.findByUserId(currentUser.getId());

        model.addAttribute("shipper", currentUser); // Dữ liệu User (Tên, SDT, Avatar)
        model.addAttribute("shipperProfile", shipperProfile); // Dữ liệu Shipper (Biển số xe, Loại xe)

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

        // 1. Cập nhật Avatar (Vẫn lưu ở bảng User)
        if (avatar != null && !avatar.isBlank()) {
            currentUser.setAvatar(avatar.trim());
            userRepository.save(currentUser);
            session.setAttribute("currentUser", currentUser);
        }

        // 2. Cập nhật Biển số xe (Lưu sang bảng Shipper)
        if (licensePlate != null && !licensePlate.isBlank()) {
            String normalizedPlate = licensePlate.trim().toUpperCase();
            Shipper shipperProfile = shipperRepository.findByUserId(currentUser.getId());

            if (shipperProfile != null && !normalizedPlate.equalsIgnoreCase(shipperProfile.getLicensePlate())) {

                // Ở đây mình cập nhật trực tiếp biển số xe luôn.
                // Nếu Hùng muốn duyệt biển số xe (pendingLicensePlate) thì cần thêm trường đó vào Entity Shipper nhé!
                shipperProfile.setLicensePlate(normalizedPlate);
                shipperRepository.save(shipperProfile);

                ra.addFlashAttribute("message", "update_profile_success");
            }
        }

        return "redirect:/shipper/profile";
    }

    @GetMapping("/register")
    public String showShipperRegister(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";

        if ("SHIPPER".equals(user.getRole().name())) return "redirect:/";

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
