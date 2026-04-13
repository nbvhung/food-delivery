package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.constant.UserRole;
import com.nhom.fooddelivery.entity.*;
import com.nhom.fooddelivery.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/foods")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;



    private String formatPrice(Number price) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(price);
    }



    @GetMapping
    public String listFoods(Model model) {

        List<Food> foods = foodRepository.findAll();

        for (Food food : foods) {
            food.setPriceFormatted(formatPrice(food.getPrice()));
        }

        model.addAttribute("foods", foods);

        return "merchant/food-list";
    }


    @GetMapping("/{id}")
    public String foodDetail(@PathVariable Long id, Model model) {

        Food food = foodRepository.findById(id).orElse(null);

        if (food == null) {
            return "redirect:/foods";
        }

        food.setPriceFormatted(formatPrice(food.getPrice()));

        model.addAttribute("food", food);

        return "merchant/food-detail";
    }


    @GetMapping("/create")
    public String showCreateForm(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != UserRole.MERCHANT) {
            return "redirect:/login";
        }
        return "redirect:/shops/foods/create";
    }



    @PostMapping("/save")
    public String saveFood(HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != UserRole.MERCHANT) {
            return "redirect:/login";
        }
        return "redirect:/shops/foods/save";
    }


    @GetMapping("/edit/{id}")
    public String editFood(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != UserRole.MERCHANT) {
            return "redirect:/login";
        }
        return "redirect:/shops/foods/edit/" + id;
    }



    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable Long id, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != UserRole.MERCHANT) {
            return "redirect:/login";
        }
        return "redirect:/shops/foods/delete/" + id;
    }

    @GetMapping("/api")
    @ResponseBody
    public List<Food> getFoodsJson() {

        return foodRepository.findAll();
    }
}