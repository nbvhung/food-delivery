package com.nhom.fooddelivery.controller.admin;

import com.nhom.fooddelivery.constant.UserRole;
import com.nhom.fooddelivery.entity.User;
import com.nhom.fooddelivery.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.NumberFormat;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserRepository userRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private FoodRepository foodRepo;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if(currentUser==null ||  currentUser.getRole()!=UserRole.ADMIN){
            return "redirect:/login";
        }

        model.addAttribute("totalUsers",  userRepo.count());
        model.addAttribute("totalShops",  shopRepo.count());
        model.addAttribute("totalFoods",  foodRepo.count());
        model.addAttribute("totalOrders",  orderRepo.count());

        double COMMISSION_RATE = 0.10;

        Double totalRevenue = Optional.ofNullable(orderRepo.getTotalSystemRevenue()).orElse(0.0);
        Double totalCommission = totalRevenue * COMMISSION_RATE;

        List<Object[]> shopStats = orderRepo.getRevenueReportByShop();
        List<Map<String, Object>> reportList = new ArrayList<>();

        for (Object[] stat : shopStats) {
            Map<String, Object> row = new HashMap<>();
            double shopRevenue = stat[1] != null ? (double) stat[1] : 0.0;
            row.put("shopName", stat[0]);
            row.put("revenue", shopRevenue);
            row.put("ownerName", stat[2]);
            row.put("commission", shopRevenue * COMMISSION_RATE);
            reportList.add(row);
        }

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        model.addAttribute("totalRevenueStr", nf.format(totalRevenue));
        model.addAttribute("totalCommissionStr", nf.format(totalCommission));
        model.addAttribute("reportList", reportList);
        model.addAttribute("commissionPercent", (int)(COMMISSION_RATE * 100));

        return "admin/dashboard";
    }
}