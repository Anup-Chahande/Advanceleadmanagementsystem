package com.app.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.Entity.USER;
import com.app.Repositry.Userreop;

@Service
public class Myuserdetialsservice  implements UserDetailsService{

	@Autowired
	Userreop user;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
	Optional<USER> users = 	user.findByEmail(username);
		
	
	       if(users.isPresent()) {
	    	   
	    	USER userobj =   users.get();
	    	   
	    	return User.builder()
	    			.username(userobj.getEmail())
	    			.password(userobj.getPassword())
	    			.roles(userobj.getRole().getName())
	    			.build();
	    	   
	    	   
	    	   
	       }
	
	
	
	       else {
	   		
	   		throw new UsernameNotFoundException(username);
	   	}	}

}
