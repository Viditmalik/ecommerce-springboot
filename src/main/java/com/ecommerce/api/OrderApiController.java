package com.ecommerce.api;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.service.OrderService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

   //user place a new order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("You are not logged in");
        }

        Order order = orderService.placeOrder(user);
        if (order == null) {
            return ResponseEntity.badRequest().body("Your cart is empty!");
        }

        return ResponseEntity.ok(order);
    }

   
    @GetMapping("/my")
    public ResponseEntity<?> getMyOrders(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("You are not logged in");
        }

        List<Order> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }

   
    @GetMapping
    public ResponseEntity<?> getAllOrders(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Admin only");
        }

        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

   
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id,
                                               @RequestParam String status,
                                               HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Admin only");
        }

        orderService.updateStatus(id, status);
        return ResponseEntity.ok("Order status updated successfully");
    }
}
