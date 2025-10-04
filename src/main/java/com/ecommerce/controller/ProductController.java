package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Show all products (for both user and admin)
    @GetMapping
    public String listProducts(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/login";

        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("currentUser", loggedUser);
        return "products";
    }

    // ADMIN: Manage Products page
 // Admin: Manage Products Page
    @GetMapping("/manage")
    public String manageProducts(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");

        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login"; // protect route
        }

        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("currentUser", user);
        return "manage-products"; // maps to manage-products.html
    }


    // Show add form (only for Admin)
    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null) return "redirect:/login";
        if(!"ADMIN".equals(user.getRole())) return "redirect:/products";

        model.addAttribute("product", new Product());
        model.addAttribute("currentUser", user);
        return "add-product";
    }

    // Add product (POST)
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null || !"ADMIN".equals(user.getRole())) return "redirect:/products";

        productService.saveProduct(product);
        return "redirect:/products/manage";
    }

    // Show edit form (GET)
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null || !"ADMIN".equals(user.getRole())) return "redirect:/products";

        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("currentUser", user);
        return "edit-product";
    }

    // Handle update (POST)
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null || !"ADMIN".equals(user.getRole())) return "redirect:/products";

        product.setId(id);
        productService.saveProduct(product);
        return "redirect:/products/manage";
    }

    // Delete product by admin
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if(user == null || !"ADMIN".equals(user.getRole())) return "redirect:/products";

        productService.deleteProduct(id);
        return "redirect:/products/manage";
    }



    

}
