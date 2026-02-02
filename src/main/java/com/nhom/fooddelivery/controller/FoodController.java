package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.entity.*;
import com.nhom.fooddelivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/foods")
public class FoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Hàm format tiền VNĐ
    private String formatPrice(Number price) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(price);
    }

    // =======================
    // 1️⃣ HIỂN THỊ DANH SÁCH FOOD
    // =======================
    @GetMapping
    public String listFoods(Model model) {
        List<Food> foods = foodRepository.findAll();

        // Format giá cho từng food
        for (Food food : foods) {
            String formatted = formatPrice(food.getPrice());
            food.setPriceFormatted(formatted);
        }

        model.addAttribute("foods", foods);
        return "auth/Food/food-list";
    }

    // =======================
    // 2️⃣ XEM CHI TIẾT FOOD
    // =======================
    @GetMapping("/{id}")
    public String foodDetail(@PathVariable Long id, Model model) {
        Food food = foodRepository.findById(id).orElse(null);

        if (food == null) {
            return "redirect:/foods";
        }

        // Format lại giá để hiển thị
        food.setPriceFormatted(formatPrice(food.getPrice()));

        model.addAttribute("food", food);
        return "auth/Food/food-detail";
    }

    // =======================
    // 3️⃣ FORM THÊM FOOD
    // =======================
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("food", new Food());
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "auth/Food/food-form";
    }

    // =======================
    // 4️⃣ LƯU FOOD
    // =======================
    @PostMapping("/save")
    public String saveFood(
            @ModelAttribute Food food,
            @RequestParam Long shopId,
            @RequestParam Long categoryId
    ) {
        Shop shop = shopRepository.findById(shopId).orElse(null);
        Category category = categoryRepository.findById(categoryId).orElse(null);

        food.setShop(shop);
        food.setCategory(category);

        foodRepository.save(food);
        return "redirect:/foods";
    }

    // =======================
    // 5️⃣ FORM SỬA FOOD
    // =======================
    @GetMapping("/edit/{id}")
    public String editFood(@PathVariable Long id, Model model) {
        Food food = foodRepository.findById(id).orElse(null);
        model.addAttribute("food", food);
        model.addAttribute("shops", shopRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "auth/Food/food-form";
    }

    // =======================
    // 6️⃣ XÓA FOOD
    // =======================
    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable Long id) {
        foodRepository.deleteById(id);
        return "redirect:/foods";
    }

    // =======================
    // 7️⃣ JSON (test)
    // =======================
    @GetMapping("/api")
    @ResponseBody
    public List<Food> getFoodsJson() {
        return foodRepository.findAll();
    }
}
