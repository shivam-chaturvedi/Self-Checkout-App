package com.miniproject.self_checkout_app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.miniproject.self_checkout_app.model.User;
import com.miniproject.self_checkout_app.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository) {
		this.userRepository=userRepository;
		this.passwordEncoder=new BCryptPasswordEncoder();
	}
	
	public User signupNewUser(User user) {
		if(userRepository.existsByUsername(user.getUsername())) {
			throw new RuntimeException("Already Exists !");
		}
		user.setRole(user.getRole().toUpperCase());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user= userRepository.findByUsername(username).orElseThrow(
				()-> 
					new UsernameNotFoundException("User Not Found with username "+username)
				); 
		
		UserDetails userDetails=org.springframework.security.core.userdetails.User
		.withUsername(user.getUsername())
		.password(user.getPassword())
		.roles(user.getRole()).build();
		
		return userDetails;
	}
	
	
}
