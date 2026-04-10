package com.nhom.fooddelivery.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shippers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate; // Biển số xe

    @Column(name = "vehicle_type", nullable = false, length = 100)
    private String vehicleType;  // Loại xe

    // Quan hệ 1-1: Mỗi User chỉ có 1 hồ sơ Shipper
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}