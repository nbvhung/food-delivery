package com.nhom.fooddelivery;

import com.nhom.fooddelivery.constant.UserRole;
import com.nhom.fooddelivery.entity.*;
import com.nhom.fooddelivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired private UserRepository userRepo;
    @Autowired private ShipperRepository shipperRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private CategoryRepository categoryRepo;
    @Autowired private FoodRepository foodRepo;
    @Autowired private OrderRepository orderRepo;

    @Override
    public void run(String... args) throws Exception {
//         1. Kiểm tra nếu DB đã có dữ liệu thì không nạp nữa
        if (userRepo.count() > 0) return;

        // 2. Tạo User mẫu (Admin, Chủ quán, Shipper)
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("123"); // Lưu ý: Nên dùng BCrypt sau này
        admin.setFullName("Admin");
        admin.setRole(UserRole.ADMIN);
        userRepo.save(admin);

        User owner = new User();
        owner.setUsername("nguyen van a");
        owner.setPassword("123");
        owner.setFullName("Nguyễn Van A");
        owner.setRole(UserRole.MERCHANT);
        userRepo.save(owner);

        User owner2 = new User();
        owner2.setUsername("le thi c");
        owner2.setPassword("123");
        owner2.setFullName("Lê Thị C");
        owner2.setRole(UserRole.MERCHANT);
        userRepo.save(owner2);

        User owner3 = new User();
        owner3.setUsername("nguyen van d");
        owner3.setPassword("123");
        owner3.setFullName("Lê Thị D");
        owner3.setRole(UserRole.MERCHANT);
        userRepo.save(owner3);

        User shipper = new User();
        shipper.setUsername("bui van b");
        shipper.setPassword("123");
        shipper.setFullName("Nguyễn Văn B");
        shipper.setRole(UserRole.SHIPPER);
        userRepo.save(shipper);

        Shipper shipperProfile = new Shipper();
        shipperProfile.setUser(shipper);
        shipperProfile.setLicensePlate("34F1-01234");
        shipperProfile.setVehicleType("Honda Wave");
        shipperRepo.save(shipperProfile);

        // 3. Tạo Danh mục (Category)
        Category categoryRice = new Category(); categoryRice.setName("Cơm");
        Category categoryMilkTea = new Category(); categoryMilkTea.setName("Trà Sữa");
        Category categoryBread = new Category(); categoryBread.setName("Bánh mì");
        categoryRepo.saveAll(Arrays.asList(categoryRice, categoryMilkTea, categoryBread));

        // 4. Tạo Quán ăn (Shop) cho chủ quán trên
        Shop shop = new Shop();
        shop.setName("Cơm Gà Hội An");
        shop.setAddress("123 Cầu Giấy, Hà Nội");
        shop.setImage("/images/com-ga-shop.jpg");
        shop.setOwner(owner);
        shop.setStatus("ACTIVE");
        shopRepo.save(shop);

        Shop shop2 = new Shop();
        shop2.setName("Bánh mì Bami");
        shop2.setAddress("123 Hà Đông, Hà Nội");
        shop2.setImage("/images/quan-banh-mi.jpeg");
        shop2.setOwner(owner2);
        shop2.setStatus("ACTIVE");
        shopRepo.save(shop2);

        Shop shop3 = new Shop();
        shop3.setName("Trà sữa DingTea");
        shop3.setAddress("123 Tràng Thi, Hà Nội");
        shop3.setImage("/images/quan-tra-sua.jpg");
        shop3.setOwner(owner3);
        shop3.setStatus("ACTIVE");
        shopRepo.save(shop3);

        // 5. Tạo Món ăn (Food)
        Food f1 = new Food();
        f1.setName("Cơm Gà Xối Mỡ");
        f1.setPrice(45000.0);
        f1.setDescription("ngon vl");
        f1.setShop(shop);
        f1.setCategory(categoryRice);
        f1.setImage("/images/com-ga-xoi-mo.png");

        Food f3 = new Food();
        f3.setName("Cơm Gà Xé");
        f3.setPrice(35000.0);
        f3.setDescription("cũng ngon vl");
        f3.setShop(shop);
        f3.setCategory(categoryRice);
        f3.setImage("/images/com-ga-xe.png");

        Food f4 = new Food();
        f4.setName("Cơm Gà Sốt Cay");
        f4.setPrice(50000.0);
        f4.setDescription("cũng ngon vl!!!");
        f4.setShop(shop);
        f4.setCategory(categoryRice);
        f4.setImage("/images/com-ga-sot-cay.png");

        Food f2 = new Food();
        f2.setName("Trà Sữa Trân Châu");
        f2.setPrice(30000.0);
        f2.setShop(shop3);
        f2.setCategory(categoryMilkTea);
        f2.setImage("/images/Tra-Sua-Tran-Chau.png");

        Food f5 = new Food();
        f5.setName("Trà Sữa Khoai Môn");
        f5.setPrice(25000.0);
        f5.setShop(shop3);
        f5.setCategory(categoryMilkTea);
        f5.setImage("/images/tra-sua-khoai-mon.png");

        Food f6 = new Food();
        f6.setName("Matcha Latte");
        f6.setPrice(35000.0);
        f6.setShop(shop3);
        f6.setCategory(categoryMilkTea);
        f6.setImage("/images/matcha-latte.png");

        Food f7 = new Food();
        f7.setName("Bánh mì chảo");
        f7.setPrice(50000.0);
        f7.setShop(shop2);
        f7.setCategory(categoryBread);
        f7.setImage("/images/banh-mi-chao.png");

        Food f8 = new Food();
        f8.setName("Bánh mì thập cẩm");
        f8.setPrice(30000.0);
        f8.setShop(shop2);
        f8.setCategory(categoryBread);
        f8.setImage("/images/banh_mi_thap_cam.png");

        Food f9 = new Food();
        f9.setName("Bánh mì không");
        f9.setPrice(100000.0);
        f9.setShop(shop2);
        f9.setCategory(categoryBread);
        f9.setImage("/images/banh-mi-khong.png");

        foodRepo.saveAll(Arrays.asList(f1, f2, f3, f4, f5, f6, f7, f8, f9));

        Order completedOrder = new Order();
        completedOrder.setAddress("39 Yên Xá - Thanh Trì");
        completedOrder.setShipper(shipper);
        completedOrder.setStatus("DELIVERED");
        completedOrder.setTotalPrice(50000.0);
        completedOrder.setPhone("0987");
        completedOrder.setShipperRating(4);
        orderRepo.save(completedOrder);

        Order readyOrder1= new Order();
        readyOrder1.setShipper(shipper);
        readyOrder1.setAddress("Phùng Khoang - Nam Từ Liêm");
        readyOrder1.setStatus("READY");
        readyOrder1.setTotalPrice(150000.0);
        readyOrder1.setPhone("0987654321");
        readyOrder1.setShipperRating(5);
        orderRepo.save(readyOrder1);

        Order readyOrder2 = new Order();
        readyOrder2.setShipper(shipper);
        readyOrder2.setAddress("Văn Quán - Hà Đông");
        readyOrder2.setStatus("READY");
        readyOrder2.setTotalPrice(30000.0);
        readyOrder2.setPhone("0918273645");
        readyOrder2.setShipperRating(5);
        orderRepo.save(readyOrder2);

        Order shippingOrder1 = new Order();
        shippingOrder1.setShipper(shipper);
        shippingOrder1.setAddress("123 Nguyễn Trãi - Thanh Xuân");
        shippingOrder1.setStatus("SHIPPING");
        shippingOrder1.setTotalPrice(35000.0);
        shippingOrder1.setPhone("54321");
        shippingOrder1.setShipperRating(4);
        orderRepo.save(shippingOrder1);

        Order shippingOrder2 = new Order();
        shippingOrder2.setShipper(shipper);
        shippingOrder2.setAddress("84 Chùa Láng - Đống Đa");
        shippingOrder2.setStatus("SHIPPING");
        shippingOrder2.setTotalPrice(40000.0);
        shippingOrder2.setPhone("67890");
        shippingOrder2.setShipperRating(5);
        orderRepo.save(shippingOrder2);

        System.out.println(">>> ĐÃ NẠP DỮ LIỆU MẪU THÀNH CÔNG!");
    }
}
