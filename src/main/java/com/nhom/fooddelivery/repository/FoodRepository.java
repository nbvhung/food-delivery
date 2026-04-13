package com.nhom.fooddelivery.repository;

import com.nhom.fooddelivery.entity.Food;
import com.nhom.fooddelivery.entity.Category;
import com.nhom.fooddelivery.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository; // Dòng này cực kỳ quan trọng
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface   FoodRepository extends JpaRepository<Food, Long> {

    // Lấy tất cả món theo shop
    List<Food> findByShop(Shop shop);
    List<Food> findByShopOrderByIdDesc(Shop shop);

    // Lấy tất cả món theo category
    List<Food> findByCategory(Category category);

    // Lấy món theo shop + category
    List<Food> findByShopAndCategory(Shop shop, Category category);

    // Tìm món theo tên
    List<Food> findByNameContainingIgnoreCase(String keyword);

    // 1. Lọc trong khoảng Min - Max
    Page<Food> findByPriceBetween(Double min, Double max, Pageable pageable);

    // 2. Lọc chỉ theo Min (Giá lớn hơn hoặc bằng)
    Page<Food> findByPriceGreaterThanEqual(Double min, Pageable pageable);

    // 3. Lọc chỉ theo Max (Giá nhỏ hơn hoặc bằng)
    Page<Food> findByPriceLessThanEqual(Double max, Pageable pageable);

    // Trong file FoodRepository.java
    Page<Food> findByCategoryId(Long categoryId, Pageable pageable);

    // Cập nhật lại logic nếu muốn kết hợp cả Lọc Giá + Danh Mục (Nâng cao):
    Page<Food> findByCategoryIdAndPriceBetween(Long categoryId, Double min, Double max, Pageable pageable);

    // Tìm món ăn theo tên, chứa từ khóa và không phân biệt hoa thường
    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
