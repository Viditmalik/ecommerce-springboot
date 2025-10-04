package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	//function to register users
	public User register(User user)
	{
		
		return userRepository.save(user);
		
	}
	
	//function to login users
	public User login(String email, String password) {
	    System.out.println("Login attempt: " + email);
	    try {
	        User user = userRepository.findByEmail(email);
	        System.out.println("User from DB: " + user);
	        if(user != null && user.getPassword().equals(password)) {
	            return user;
	        }
	    } catch(Exception e) {
	        e.printStackTrace(); // This will show the root cause in the console
	    }
	    return null;
	}


	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	public Object getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	
	
	
	

}
