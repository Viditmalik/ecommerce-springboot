package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ecommerce.entity.User;

import com.ecommerce.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.*;


@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//Register
	@GetMapping("/register")
	public String registerForm()
	{
		return "register";
		
	}
	@PostMapping("/register")
	public String register(User user, Model model) {
	    if(user == null || user.getEmail() == null || user.getPassword() == null){
	        model.addAttribute("error", "Please fill all fields");
	        return "register";
	    }

	    User existingUser = userService.findByEmail(user.getEmail());
	    user.setRole("USER");
	    if(existingUser != null){
	        model.addAttribute("error", "User already exists with this email");
	        return "register";
	    }

	    userService.register(user);
	    return "redirect:/login";
	}

	
	
	//login part
	@GetMapping("/login")
	public String loginPage()
	{
		return "login";
	}
	
	
	
	
	@PostMapping("/login")
	public String login(@RequestParam String email,@RequestParam String password, HttpSession session,Model model)
	{
		User user = userService.login(email, password);
		if(user!=null)
		{
			session.setAttribute("loggedUser", user);
			return "redirect:/home";
		}
		model.addAttribute("error", "invalid email or password");
		return "login";
		
		
		
	}
	
	
	
	//logout 
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/login";
	}

	
	@GetMapping("/home")
	public String homePage(HttpSession session, Model model) {
	    Object sessionUserObject = session.getAttribute("loggedUser");

	    if (sessionUserObject == null || !(sessionUserObject instanceof User)) { 
	        
	        return "redirect:/login";
	    }

	  	    User currentUser = (User) sessionUserObject; 
	    
	    model.addAttribute("currentUser", currentUser); 

	    return "home";
	}
	

}
