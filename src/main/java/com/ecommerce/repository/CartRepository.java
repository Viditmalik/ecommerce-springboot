package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;

import java.util.List;


@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
     // Get cart items for a specific user
	 List<CartItem> findByUser(User user); 
		
	
}
