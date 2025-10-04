package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;

import java.util.List;

@Service
public class CartService {
	

	

	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	 public List<CartItem> getCartItems(User user) {
	        return cartRepository.findByUser(user);
	        
	    }
	 public List<CartItem> getAllCarts() {   // NEW: Admin can see all carts
	        return cartRepository.findAll();
	    }
	 
	 public void addTocart(User user,Long productId,int quantity)
	 {
		Product product= productRepository.findById(productId).orElse(null);
		if(product==null) return;
		
		CartItem cartItem = new CartItem(user,product,quantity);
		cartRepository.save(cartItem);
	 }
	 public void removeFromCart(Long cartItemId) {
	        cartRepository.deleteById(cartItemId);
	    }



}
