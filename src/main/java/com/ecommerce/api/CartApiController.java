package com.ecommerce.api;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;
import com.ecommerce.service.CartService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;

   
    @GetMapping
    public ResponseEntity<?> getUserCart(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("Please login first");
        }

        List<CartItem> items = cartService.getCartItems(user);
        if (items.isEmpty()) {
            return ResponseEntity.ok("Your cart is empty!");
        }
        return ResponseEntity.ok(items);
    }

    
    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"USER".equals(user.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Only USER can add to cart");
        }

        cartService.addTocart(user, productId, quantity);
        return ResponseEntity.ok("Product added to cart successfully!");
    }

   
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"USER".equals(user.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Only USER can remove items");
        }

        cartService.removeFromCart(id);
        return ResponseEntity.ok("Item removed successfully!");
    }

   
    @GetMapping("/all")
    public ResponseEntity<?> getAllCarts(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Admin only");
        }

        return ResponseEntity.ok(cartService.getAllCarts());
    }
}
