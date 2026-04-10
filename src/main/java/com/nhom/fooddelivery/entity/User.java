package com.nhom.fooddelivery.entity;

import com.nhom.fooddelivery.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String fullName;
    @Enumerated(EnumType.STRING) // Quan trọng: Lưu chữ "ADMIN" vào DB
    private UserRole role;

    //ACTIVED, BANNED, PENDING_MERCHANT, PENDING_SHIPPER
    private String status = "ACTIVED";

    private String avatar; // Đường dẫn ảnh đại diện

    @Column(length = 15) // Giới hạn độ dài cho chuẩn SĐT
    private String phone;

    // Một người dùng có thể đặt nhiều đơn hàng
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;

    // Một người dùng (Merchant) có thể sở hữu một quán ăn
    @OneToOne(mappedBy = "owner")
    @JoinColumn(name = "shop_id")
    private Shop shop;

    // Một người dùng (Shipper) có thể đi giao nhiều đơn
    @OneToMany(mappedBy = "shipper")
    private List<Order> shippingOrders;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
}