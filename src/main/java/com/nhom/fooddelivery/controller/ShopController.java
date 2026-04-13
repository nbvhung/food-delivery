package com.nhom.fooddelivery.controller;
import com.nhom.fooddelivery.constant.UserRole;
import com.nhom.fooddelivery.entity.Category;
import com.nhom.fooddelivery.entity.Food;
import com.nhom.fooddelivery.entity.Shop;
import com.nhom.fooddelivery.entity.User;
import com.nhom.fooddelivery.repository.CategoryRepository;
import com.nhom.fooddelivery.repository.FoodRepository;
import com.nhom.fooddelivery.repository.OrderRepository;
import com.nhom.fooddelivery.repository.ShopRepository;
import com.nhom.fooddelivery.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shops")
public class ShopController {

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // ==========================================
    // PUBLIC AREA
    // ==========================================

    @GetMapping
    public String listShops(Model model) {
        List<Shop> shops = shopRepository.findByStatus("ACTIVE");
        model.addAttribute("shops", shops);
        return "merchant/shop-list";
    }

    @GetMapping("/{id}")
    public String shopDetail(@PathVariable Long id, HttpSession session, Model model) {
        Shop shop = shopRepository.findById(id).orElse(null);
        if (shop == null) {
            return "redirect:/shops";
        }

        User currentUser = getCurrentUser(session);
        boolean isOwner = currentUser != null
                && shop.getOwner() != null
                && currentUser.getId().equals(shop.getOwner().getId());

        if (!isOwner && !"ACTIVE".equalsIgnoreCase(shop.getStatus())) {
            return "redirect:/shops";
        }

        List<Food> foods = foodRepository.findByShopOrderByIdDesc(shop);

        model.addAttribute("shop", shop);
        model.addAttribute("foods", foods);
        model.addAttribute("isOwner", isOwner);
        return "merchant/shop-detail";
    }

    @GetMapping("/my-shop")
    public String myShop(HttpSession session, RedirectAttributes ra) {
        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Shop shop = shopRepository.findByOwnerId(merchant.getId());
        if (shop == null) {
            ra.addFlashAttribute("error", "Bạn chưa có cửa hàng, hãy đăng ký trước.");
            return "redirect:/shops/register";
        }

        return "redirect:/shops/" + shop.getId();
    }

    // ==========================================
    // MERCHANT AREA
    // ==========================================

    @GetMapping("/register")
    public String showRegisterForm(HttpSession session, Model model, RedirectAttributes ra) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        Shop existingShop = shopRepository.findByOwnerId(currentUser.getId());
        if (existingShop != null) {
            ra.addFlashAttribute("info", "Bạn đã có cửa hàng trong hệ thống.");
            return "redirect:/shops/" + existingShop.getId();
        }

        model.addAttribute("shop", new Shop());
        return "merchant/shop-register";
    }

    @PostMapping("/register")
    public String processRegisterShop(
            @ModelAttribute Shop shop,
            @RequestParam("imageFile") MultipartFile imageFile,
            HttpSession session,
            RedirectAttributes ra) {

        User currentUser = getCurrentUser(session);
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (shopRepository.existsByOwner(currentUser)) {
            Shop existingShop = shopRepository.findByOwnerId(currentUser.getId());
            if (existingShop != null) {
                ra.addFlashAttribute("info", "Bạn đã gửi đăng ký cửa hàng trước đó.");
                return "redirect:/shops/" + existingShop.getId();
            }
        }

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                shop.setImage(saveImage(imageFile));
            }
        } catch (IOException ex) {
            ra.addFlashAttribute("error", "Không thể tải ảnh lên, vui lòng thử lại.");
            return "redirect:/shops/register";
        }

        shop.setName(safeTrim(shop.getName()));
        shop.setAddress(safeTrim(shop.getAddress()));
        shop.setOwner(currentUser);
        shop.setStatus("PENDING");

        shopRepository.save(shop);

        if (currentUser.getRole() != UserRole.MERCHANT) {
            currentUser.setStatus("PENDING_MERCHANT");
            userRepository.save(currentUser);
            session.setAttribute("currentUser", currentUser);
        }

        ra.addFlashAttribute("message", "pending");
        return "redirect:/shops";
    }

    @GetMapping("/edit")
    public String editShop(HttpSession session, Model model, RedirectAttributes ra) {
        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Shop shop = shopRepository.findByOwnerId(merchant.getId());
        if (shop == null) {
            ra.addFlashAttribute("error", "Bạn chưa có cửa hàng để chỉnh sửa.");
            return "redirect:/shops/register";
        }

        model.addAttribute("shop", shop);
        return "merchant/shop-form";
    }

    @PostMapping("/update")
    public String updateShop(
            @ModelAttribute Shop shopData,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpSession session,
            RedirectAttributes ra) {

        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Shop currentShop = shopRepository.findByOwnerId(merchant.getId());
        if (currentShop == null) {
            ra.addFlashAttribute("error", "Không tìm thấy cửa hàng để cập nhật.");
            return "redirect:/shops/register";
        }

        currentShop.setName(safeTrim(shopData.getName()));
        currentShop.setAddress(safeTrim(shopData.getAddress()));

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                currentShop.setImage(saveImage(imageFile));
            } catch (IOException ex) {
                ra.addFlashAttribute("error", "Không thể tải ảnh lên, vui lòng thử lại.");
                return "redirect:/shops/edit";
            }
        }

        shopRepository.save(currentShop);
        ra.addFlashAttribute("success", "Cập nhật thông tin quán thành công.");
        return "redirect:/shops/edit";
    }

    @GetMapping("/revenue")
    public String viewRevenue(HttpSession session, Model model, RedirectAttributes ra) {
        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Shop shop = shopRepository.findByOwnerId(merchant.getId());
        if (shop == null) {
            ra.addFlashAttribute("error", "Bạn chưa có cửa hàng để xem doanh thu.");
            return "redirect:/shops/register";
        }

        Long shopId = shop.getId();

        Double totalRevenue = Optional.ofNullable(orderRepository.getTotalRevenue(shopId)).orElse(0.0);
        Double monthlyRevenue = Optional.ofNullable(orderRepository.getMonthlyRevenue(shopId)).orElse(0.0);

        long totalOrders = orderRepository.countByShopId(shopId);
        long deliveredCount = orderRepository.countByShopIdAndStatus(shopId, "DELIVERED");
        long readyCount = orderRepository.countByShopIdAndStatus(shopId, "READY");
        long shippingCount = orderRepository.countByShopIdAndStatus(shopId, "SHIPPING");

        model.addAttribute("shop", shop);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("deliveredCount", deliveredCount);
        model.addAttribute("readyCount", readyCount);
        model.addAttribute("shippingCount", shippingCount);
        model.addAttribute("currentMonth", LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy")));

        return "merchant/shop-revenue";
    }

    // =======================
    // FOOD MANAGEMENT
    // =======================

    @GetMapping("/foods")
    public String listFoods(HttpSession session, Model model, RedirectAttributes ra) {
        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Shop shop = shopRepository.findByOwnerId(merchant.getId());
        if (shop == null) {
            ra.addFlashAttribute("error", "Bạn chưa có cửa hàng, hãy đăng ký trước.");
            return "redirect:/shops/register";
        }

        List<Food> foods = foodRepository.findByShopOrderByIdDesc(shop);
        model.addAttribute("shop", shop);
        model.addAttribute("foods", foods);
        return "merchant/food-list";
    }

    @GetMapping("/foods/{id}")
    public String foodDetail(@PathVariable Long id, HttpSession session, Model model) {
        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Food food = foodRepository.findById(id).orElse(null);
        if (!isFoodOwner(food, merchant.getId())) {
            return "redirect:/shops/foods";
        }

        model.addAttribute("food", food);
        model.addAttribute("merchantView", true);
        return "merchant/food-detail";
    }

    @GetMapping("/foods/create")
    public String createFood(HttpSession session, Model model, RedirectAttributes ra) {
        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Shop shop = shopRepository.findByOwnerId(merchant.getId());
        if (shop == null) {
            ra.addFlashAttribute("error", "Bạn chưa có cửa hàng, hãy đăng ký trước.");
            return "redirect:/shops/register";
        }

        model.addAttribute("shop", shop);
        model.addAttribute("food", new Food());
        model.addAttribute("categories", categoryRepository.findAll());
        return "merchant/food-form";
    }

    @PostMapping("/foods/save")
    public String saveFood(
            @ModelAttribute Food food,
            @RequestParam Long categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpSession session,
            RedirectAttributes ra) {

        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Shop shop = shopRepository.findByOwnerId(merchant.getId());
        if (shop == null) {
            ra.addFlashAttribute("error", "Bạn chưa có cửa hàng, hãy đăng ký trước.");
            return "redirect:/shops/register";
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            ra.addFlashAttribute("error", "Danh mục không hợp lệ.");
            return "redirect:/shops/foods/create";
        }

        if (food.getPrice() == null || food.getPrice() <= 0) {
            ra.addFlashAttribute("error", "Giá món ăn phải lớn hơn 0.");
            return "redirect:/shops/foods/create";
        }

        food.setName(safeTrim(food.getName()));
        food.setDescription(safeTrim(food.getDescription()));
        food.setShop(shop);
        food.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                food.setImage(saveImage(imageFile));
            } catch (IOException ex) {
                ra.addFlashAttribute("error", "Không thể tải ảnh món ăn lên.");
                return "redirect:/shops/foods/create";
            }
        }

        foodRepository.save(food);
        ra.addFlashAttribute("success", "Thêm món ăn thành công.");
        return "redirect:/shops/foods";
    }

    @GetMapping("/foods/edit/{id}")
    public String editFood(@PathVariable Long id, HttpSession session, Model model) {
        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Food food = foodRepository.findById(id).orElse(null);
        if (!isFoodOwner(food, merchant.getId())) {
            return "redirect:/shops/foods";
        }

        model.addAttribute("food", food);
        model.addAttribute("shop", food.getShop());
        model.addAttribute("categories", categoryRepository.findAll());
        return "merchant/food-form";
    }

    @PostMapping("/foods/update/{id}")
    public String updateFood(
            @PathVariable Long id,
            @ModelAttribute Food foodData,
            @RequestParam Long categoryId,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            HttpSession session,
            RedirectAttributes ra) {

        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Food food = foodRepository.findById(id).orElse(null);
        if (!isFoodOwner(food, merchant.getId())) {
            return "redirect:/shops/foods";
        }

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            ra.addFlashAttribute("error", "Danh mục không hợp lệ.");
            return "redirect:/shops/foods/edit/" + id;
        }

        if (foodData.getPrice() == null || foodData.getPrice() <= 0) {
            ra.addFlashAttribute("error", "Giá món ăn phải lớn hơn 0.");
            return "redirect:/shops/foods/edit/" + id;
        }

        food.setName(safeTrim(foodData.getName()));
        food.setPrice(foodData.getPrice());
        food.setDescription(safeTrim(foodData.getDescription()));
        food.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                food.setImage(saveImage(imageFile));
            } catch (IOException ex) {
                ra.addFlashAttribute("error", "Không thể tải ảnh món ăn lên.");
                return "redirect:/shops/foods/edit/" + id;
            }
        }

        foodRepository.save(food);
        ra.addFlashAttribute("success", "Cập nhật món ăn thành công.");
        return "redirect:/shops/foods";
    }

    @GetMapping("/foods/delete/{id}")
    public String deleteFood(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        User merchant = getCurrentMerchant(session);
        if (merchant == null) {
            return "redirect:/login";
        }

        Food food = foodRepository.findById(id).orElse(null);
        if (!isFoodOwner(food, merchant.getId())) {
            return "redirect:/shops/foods";
        }

        foodRepository.delete(food);
        ra.addFlashAttribute("success", "Đã xóa món ăn.");
        return "redirect:/shops/foods";
    }

    @GetMapping("/api")
    @ResponseBody
    public List<Shop> getShopsJson() {
        return shopRepository.findAll();
    }

    private User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("currentUser");
    }

    private User getCurrentMerchant(HttpSession session) {
        User currentUser = getCurrentUser(session);
        if (currentUser == null || currentUser.getRole() != UserRole.MERCHANT) {
            return null;
        }
        return currentUser;
    }

    private boolean isFoodOwner(Food food, Long merchantId) {
        return food != null
                && food.getShop() != null
                && food.getShop().getOwner() != null
                && merchantId.equals(food.getShop().getOwner().getId());
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = imageFile.getOriginalFilename();
        String safeName = originalName == null ? "image" : originalName.replaceAll("[^a-zA-Z0-9._-]", "");
        if (safeName.isBlank()) {
            safeName = "image";
        }

        String fileName = System.currentTimeMillis() + "_" + safeName;
        Path targetFile = uploadPath.resolve(fileName);
        Files.copy(imageFile.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

        return "/images/" + fileName;
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }
}
