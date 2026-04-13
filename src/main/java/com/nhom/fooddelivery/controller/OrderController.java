package com.nhom.fooddelivery.controller;

import com.nhom.fooddelivery.entity.*;
import com.nhom.fooddelivery.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.nhom.fooddelivery.constant.UserRole.SHIPPER;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    // Shipper nhận đơn
    @PostMapping("/accept")
    public String acceptOrder(
            @RequestParam Long orderId,
            HttpSession session
    ){
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER){
            return "redirect:/login";
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && "READY".equals(order.getStatus())) {
            order.setShipper(currentUser);
            order.setStatus("ACCEPTED");
            orderRepository.save(order);
        }

        return "redirect:/shipper/waiting";
    }

    @PostMapping("/pickup")
    public String pickupOrder(
            @RequestParam Long orderId,
            HttpSession session
    ) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER) {
            return "redirect:/login";
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null
                && "ACCEPTED".equals(order.getStatus())
                && order.getShipper() != null
                && currentUser.getId().equals(order.getShipper().getId())) {
            order.setStatus("SHIPPING");
            orderRepository.save(order);
        }

        return "redirect:/shipper/delivering";
    }

    // Shipper hoàn thành giao hàng
    @PostMapping("/complete")
    public String completeOrder(
            @RequestParam Long orderId,
            HttpSession session
    ){
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null || currentUser.getRole() != SHIPPER) {
            return "redirect:/login";
        }

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order != null
                && "SHIPPING".equals(order.getStatus())
                && order.getShipper() != null
                && currentUser.getId().equals(order.getShipper().getId())){
            order.setStatus("DELIVERED");
            order.setDeliveredAt(LocalDateTime.now());
            orderRepository.save(order);
        }

        return "redirect:/shipper/delivering";
    }
}