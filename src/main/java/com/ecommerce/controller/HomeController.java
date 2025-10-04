package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // You can add any attributes here if needed
        return "dashboard"; // This will load dashboard.html
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/dashboard";
    }
}
