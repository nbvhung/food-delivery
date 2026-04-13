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

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ShopRepository shopRepository;


    // ==============================
    // FORM REGISTER
    // ==============================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {

        model.addAttribute("user", new User());

        return "auth/register";
    }


    // ==============================
    // XỬ LÝ REGISTER
    // ==============================
    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {

        // Kiểm tra username tồn tại
        if (userRepo.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            model.addAttribute("user", user); // Giữ lại thông tin user đã nhập
            return "auth/register";
        }

        //  Kiểm tra số điện thoại tồn tại
        if (userRepo.findByPhone(user.getPhone()) != null) {
            model.addAttribute("error", "Số điện thoại này đã được đăng ký!");
            model.addAttribute("user", user); // Giữ lại thông tin user đã nhập
            return "auth/register";
        }

        // Mặc định role customer
        user.setRole(UserRole.CUSTOMER);
        user.setStatus("ACTIVED");

        userRepo.save(user);

        return "redirect:/login";
    }


    // ==============================
    // FORM LOGIN
    // ==============================
    @GetMapping("/login")
    public String showLoginForm() {

        return "auth/login";
    }


    // ==============================
    // XỬ LÝ LOGIN
    // ==============================
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {

        User user = userRepo.findByUsername(username);

        // kiểm tra tài khoản
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            return "auth/login";
        }

        // lưu user vào session
        session.setAttribute("currentUser", user);

        // phân quyền
        UserRole role = user.getRole();

        if (role == UserRole.ADMIN) {
            return "redirect:/admin/dashboard";
        }

        if (role == UserRole.MERCHANT) {

            Shop shop = shopRepository.findByOwnerId(user.getId());

            if (shop != null) {
                return "redirect:/shops/" + shop.getId();
            } else {
                return "redirect:/shops/register";
            }
        }

        if (role == UserRole.SHIPPER) {
            return "redirect:/shipper/dashboard";
        }

        // CUSTOMER
        return "redirect:/";
    }


    // ==============================
    // LOGOUT
    // ==============================
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }

}