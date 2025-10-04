package com.ecommerce.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;



@Entity
public class CartItem {
	
	@Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//one user can have many cart items
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	
	//one product can be added many times
	@ManyToOne
	@JoinColumn(name ="product_id")
	private Product product;
	private int quantity;
	 public CartItem() {}

	    public CartItem(User user, Product product, int quantity) {
	        this.user = user;
	        this.product = product;
	        this.quantity = quantity;
	    }

	    // Getters and Setters
	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }

	    public User getUser() { return user; }
	    public void setUser(User user) { this.user = user; }

	    public Product getProduct() { return product; }
	    public void setProduct(Product product) { this.product = product; }

	    public int getQuantity() { return quantity; }
	    public void setQuantity(int quantity) { this.quantity = quantity; }
	

}
