package com.nhom.fooddelivery.repository;
import com.nhom.fooddelivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Tìm các đơn hàng đang chờ Shipper đến nhận (Quán đã làm xong)
    List<Order> findByStatus(String status);

    List<Order> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @Query("""
            SELECT DISTINCT o
            FROM Order o
            LEFT JOIN FETCH o.shop
            LEFT JOIN FETCH o.shipper
            LEFT JOIN FETCH o.orderDetails od
            LEFT JOIN FETCH od.food
            WHERE o.customer.id = :customerId
            ORDER BY o.createdAt DESC
            """)
    List<Order> findOrderHistoryByCustomerId(@Param("customerId") Long customerId);

    Long countByStatus(String status);

    // Tính tổng doanh thu
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.shop.id = :shopId AND o.status = 'DELIVERED'")
    Double getTotalRevenue(@Param("shopId") Long shopId);

    // Tính doanh thu trong tháng
    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o " +
           "WHERE o.shop.id = :shopId " +
           "AND o.status = 'DELIVERED' " +
           "AND MONTH(o.deliveredAt) = MONTH(CURRENT_DATE) " +
           "AND YEAR(o.deliveredAt) = YEAR(CURRENT_DATE)")
    Double getMonthlyRevenue(@Param("shopId") Long shopId);

    //Thống kê số đơn hàng thành công
    long countByShopIdAndStatus(Long shopId, String status);
    long countByShopId(Long shopId);

    // Tìm các đơn mà Shipper cụ thể đang đi giao
    List<Order> findByShipperIdAndStatus(Long shipperId, String status);

    // Thống kê: Đếm số đơn đã giao thành công của 1 Shipper
    Long countByShipperIdAndStatus(Long shipperId, String status);

    // Tính thu nhập: tổng tiền ship của các đơn hành giao thành công
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.shipper.id = :shipperId AND o.status = 'DELIVERED'")
    Double sumEarningsByShipper(@Param("shipperId") Long shipperId);

    // Điểm đánh giá trung bình
    @Query("SELECT AVG(o.shipperRating) FROM Order o WHERE o.shipper.id = :shipperId AND o.shipperRating IS NOT NULL")
    Double getAverageRatingByShipper(@Param("shipperId") Long shipperId);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.status = 'DELIVERED'")
    Double getTotalSystemRevenue();

    @Query("SELECT o.shop.name, SUM(o.totalPrice), o.shop.owner.fullName " +
            "FROM Order o WHERE o.status = 'DELIVERED' GROUP BY o.shop.id, o.shop.name, o.shop.owner.fullName")
    List<Object[]> getRevenueReportByShop();
}
