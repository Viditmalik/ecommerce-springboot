package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

 // Place Order
    @PostMapping("/place")
    public String placeOrder(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        Order order = orderService.placeOrder(user);
        if(order == null){
            model.addAttribute("message", "Your cart is empty!");
            return "cart";
        }

        return "redirect:/orders/my";
    }

    // My Orders
    @GetMapping("/my")
    public String myOrders(HttpSession session, Model model){
        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";

        List<Order> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orders);
        return "my-orders";
    }


    // ADMIN: View All Orders
    @GetMapping
    public String allOrders(HttpSession session, Model model){
        User user = (User) session.getAttribute("loggedUser");
        if(user == null || !"ADMIN".equals(user.getRole())) return "redirect:/login";

        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("currentUser", user);
        return "all-orders";
    }

    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        orderService.updateStatus(id, status);

        // ✅ redirect to admin manage orders page
        return "redirect:/orders/manage";
    }
 // ADMIN: Manage Orders Page
    @GetMapping("/manage")
    public String manageOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("currentUser", user);
        return "manage-orders"; // Thymeleaf template name
    }

}
