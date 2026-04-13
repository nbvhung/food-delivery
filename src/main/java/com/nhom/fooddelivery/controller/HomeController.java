package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.entity.Food;
import com.nhom.fooddelivery.entity.Category;
import com.nhom.fooddelivery.repository.CategoryRepository;
import com.nhom.fooddelivery.repository.FoodRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/")
    public String index(Model model, HttpSession session,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "id,desc") String sort,
                        @RequestParam(required = false) Double minPrice,
                        @RequestParam(required = false) Double maxPrice,
                        @RequestParam(required = false) Long categoryId,
                        @RequestParam(required = false) String keyword) {

        int pageSize = 9;

        // 1. Xử lý sắp xếp
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        Sort.Direction direction = sortParts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, sortBy));

        Page<Food> foodPage;

        // 1. ƯU TIÊN TÌM KIẾM (Nếu có gõ chữ vào ô Search)
        if (keyword != null && !keyword.isEmpty()) {
            foodPage = foodRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        // 2. LỌC THEO DANH MỤC
        else if (categoryId != null) {
            foodPage = foodRepository.findByCategoryId(categoryId, pageable);
        }
        // 3. LỌC THEO GIÁ
        else if (minPrice != null && maxPrice != null) {
            foodPage = foodRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        } else if (minPrice != null) {
            foodPage = foodRepository.findByPriceGreaterThanEqual(minPrice, pageable);
        } else if (maxPrice != null) {
            foodPage = foodRepository.findByPriceLessThanEqual(maxPrice, pageable);
        }
        // 4. MẶC ĐỊNH HIỆN TẤT CẢ
        else {
            foodPage = foodRepository.findAll(pageable);
        }

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        model.addAttribute("keyword", keyword); // Nhớ dòng này để giữ chữ trong ô Search

        // 3. Đẩy dữ liệu sang JSP
        // Normalize image paths so /images/*.png resolves even if DB still has .jpg/.jpeg
        for (Food food : foodPage.getContent()) {
            food.setImage(normalizeImagePath(food.getImage()));
        }

        model.addAttribute("foods", foodPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", foodPage.getTotalPages());
        model.addAttribute("currentSort", sort);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("categoryId", categoryId);


        // 4. Giỏ hàng
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        int cartCount = 0;
        if (cart != null) {
            for (int qty : cart.values()) cartCount += qty;
        }
        model.addAttribute("cartCount", cartCount);

        return "index";
    }

    private String normalizeImagePath(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            return imagePath;
        }
        String webPath = imagePath.startsWith("/") ? imagePath : "/" + imagePath;
        if (resourceExists(webPath)) {
            return webPath;
        }
        String lower = webPath.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            String pngPath = webPath.replaceAll("(?i)\\.jpe?g$", ".png");
            if (resourceExists(pngPath)) {
                return pngPath;
            }
        }
        return webPath;
    }

    private boolean resourceExists(String webPath) {
        Resource resource = resourceLoader.getResource("classpath:/static" + webPath);
        return resource.exists();
    }
}
