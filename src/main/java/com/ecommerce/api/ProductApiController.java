package com.ecommerce.api;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.service.ProductService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    @Autowired
    private ProductService productService;

  
    @GetMapping
    public ResponseEntity<?> getAllProducts(HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return ResponseEntity.status(401).body("You are not logged in");
        }
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return ResponseEntity.status(401).body("You are not logged in");
        }
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(404).body("Product not found");
        }
        return ResponseEntity.ok(product);
    }

   //add new product by admin
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Admin only");
        }
        productService.saveProduct(product);
        return ResponseEntity.ok("Product added successfully");
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody Product product,
                                           HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Admin only");
        }
        product.setId(id);
        productService.saveProduct(product);
        return ResponseEntity.ok("Product updated successfully");
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Admin only");
        }
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
