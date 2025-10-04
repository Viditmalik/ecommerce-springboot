package com.ecommerce.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.ecommerce.entity.User;
import com.ecommerce.service.CartService;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	//view cart
	@GetMapping
	public String viewCart(HttpSession session, Model model) {
	    User user = (User) session.getAttribute("loggedUser");
	    if(user == null) return "redirect:/login";

	    model.addAttribute("currentUser", user); 
	    model.addAttribute("cartItems", cartService.getCartItems(user));

	    return "cart";
	}

	
	
	//Add product to cart only bu user
	@GetMapping("/add/{productId}")
	public String addProducts(@PathVariable Long productId,@RequestParam(defaultValue="1")int quantity,HttpSession session)
	{
		User user = (User)session.getAttribute("loggedUser");
		if(user==null || !"USER".equals(user.getRole()))
		{
			return "redirect:/login";
		}
		cartService.addTocart(user,productId,quantity);
		return "redirect:/cart";
	}
	
	
	//remove from cart only user through user id
	@GetMapping("/remove/{id}")
	public String removeFromCart(@PathVariable Long id,HttpSession session)
	{
		User user = (User)session.getAttribute("loggedUser");
		if(user==null || !"USER".equals(user.getRole()))
		{
			return "redirect:/login";
		}
		cartService.removeFromCart(id);
		return "redirect:/cart";
	}
	
	
	
	

}
