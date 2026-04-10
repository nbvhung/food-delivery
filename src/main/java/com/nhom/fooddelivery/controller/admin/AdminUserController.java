package com.nhom.fooddelivery.controller.admin;

import com.nhom.fooddelivery.constant.UserRole;
import com.nhom.fooddelivery.entity.Shipper;
import com.nhom.fooddelivery.entity.User;
import com.nhom.fooddelivery.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired private UserRepository userRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private FoodRepository foodRepo;

    @Autowired private ShipperRepository shipperRepo;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        return user != null && user.getRole() == UserRole.ADMIN;
    }

    @GetMapping
    public String listUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("users", userRepo.findAll());
        return "admin/users";
    }

    @GetMapping("/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";

        User user = userRepo.findById(id).orElse(null);
        if (user != null) {
            if ("ACTIVED".equals(user.getStatus())) {
                user.setStatus("BANNED");
                ra.addFlashAttribute("message", "ban_success");
            } else {
                user.setStatus("ACTIVED");
                ra.addFlashAttribute("message", "unban_success");
            }
            userRepo.save(user);
        }
        return "redirect:/admin/users";
    }

    // DUYỆT SHIPPER
    @GetMapping("/approve-shipper/{id}")
    public String approveShipper(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";

        User user = userRepo.findById(id).orElse(null);
        if (user != null && "PENDING_SHIPPER".equals(user.getStatus())) {
            user.setRole(UserRole.SHIPPER);
            user.setStatus("ACTIVED");
            userRepo.save(user);

            ra.addFlashAttribute("message", "approve_shipper_success"); // Báo SweetAlert
        }
        return "redirect:/admin/users";
    }

    // TỪ CHỐI SHIPPER
    @GetMapping("/reject-shipper/{id}")
    public String rejectShipper(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";

        User user = userRepo.findById(id).orElse(null);
        if (user != null && "PENDING_SHIPPER".equals(user.getStatus())) {

            user.setStatus("ACTIVED");
            userRepo.save(user);

            // Xóa hồ sơ xe (biển số, loại xe) đi để DB được sạch
            Shipper badShipper = shipperRepo.findByUserId(id);
            if (badShipper != null) {
                shipperRepo.delete(badShipper);
            }

            ra.addFlashAttribute("message", "reject_shipper_success");
        }
        return "redirect:/admin/users";
    }
}