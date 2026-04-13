package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.constant.UserRole;
import com.nhom.fooddelivery.entity.Shop;
import com.nhom.fooddelivery.entity.User;
import com.nhom.fooddelivery.repository.ShopRepository;
import com.nhom.fooddelivery.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopRepository shopRepository;

    @GetMapping
    public String showProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        model.addAttribute("user", currentUser);

        if (currentUser.getRole() == UserRole.MERCHANT) {
            Shop merchantShop = shopRepository.findByOwnerId(currentUser.getId());
            model.addAttribute("merchantShop", merchantShop);
        }

        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @RequestParam("fullName") String fullName,
            @RequestParam("phone") String phone,
            @RequestParam(value = "avatarFile", required = false) MultipartFile file,
            HttpSession session,
            RedirectAttributes ra) {

        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) return "redirect:/login";

        currentUser.setFullName(fullName);
        currentUser.setPhone(phone);

        if (file != null && !file.isEmpty()) {
            try {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s+", "");

                Path uploadPath = Paths.get("src/main/resources/static/images/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

                Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

                currentUser.setAvatar("/images/" + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                ra.addFlashAttribute("message", "upload_error");
                return "redirect:/profile";
            }
        }

        userRepository.save(currentUser);
        session.setAttribute("currentUser", currentUser);
        ra.addFlashAttribute("message", "update_profile_success");

        return "redirect:/";
    }
}
