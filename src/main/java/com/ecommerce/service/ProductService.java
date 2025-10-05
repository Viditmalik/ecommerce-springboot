package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;


@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository

	@Autowired
	private OrderItemRepository orderItemRepository;
	
	//Method to list all the products
	 public List<Product> getAllProducts() {
	        return productRepository.findAll();
	    }
	 public Product getProductById(Long id) {
	        return productRepository.findById(id).orElse(null);
	    }

	    public Product saveProduct(Product product) {
	        return productRepository.save(product);
	    }

	   	    @Transactional
	    public void deleteProduct(Long productId) {
	        // Delete all order items that reference this product
	        orderItemRepository.deleteByProductId(productId);

	        // Now delete the product itself
	        productRepository.deleteById(productId);
	    }
}
