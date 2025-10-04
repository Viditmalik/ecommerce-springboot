package com.ecommerce.api;

import com.ecommerce.entity.User;
import com.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserService userService;

    /**
     * ✅ Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("Please fill all fields");
        }

        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ResponseEntity.badRequest().body("User already exists with this email");
        }

        user.setRole("USER");
        userService.register(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    /**
     * ✅ Login user
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginDetails, HttpSession session) {
        
        // Use loginDetails to access email and password from the JSON body
        String email = loginDetails.getEmail();
        String password = loginDetails.getPassword();

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Email and password are required.");
        }

        User loggedUser = userService.login(email, password); 

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password"); 
        }

        
        session.setAttribute("loggedUser", loggedUser); 
        
        
        return ResponseEntity.ok(loggedUser); 
    }

    /**
     * ✅ Logout user
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully!");
    }

    /**
     * ✅ Get logged-in user info
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return ResponseEntity.status(401).body("You are not logged in");
        }
        return ResponseEntity.ok(user);
    }

    /**
     * ✅ Admin: Get all registered users
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || !"ADMIN".equals(loggedUser.getRole())) {
            return ResponseEntity.status(403).body("Access denied: Admin only");
        }

        return ResponseEntity.ok(userService.getAllUsers());
    }
}
