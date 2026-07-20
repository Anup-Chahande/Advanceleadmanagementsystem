package com.app.Registeradmin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.Entity.Role;
import com.app.Entity.USER;
import com.app.Repositry.Rolerepositry;
import com.app.Repositry.Userreop;

@Component
public class Registeradmin implements CommandLineRunner{
	
	@Autowired
	Userreop userepo;
	
	@Autowired
	Rolerepositry rolerepo;
	
	  @Autowired
	    private PasswordEncoder encoder;  
	
	                                                  
	@Override
	public void run(String... args) throws Exception {
		
		Role adminrole = rolerepo.findByName("ADMIN").orElse(null);

		if (adminrole == null) {

		    adminrole = new Role();
		    adminrole.setName("ADMIN");

		    adminrole = rolerepo.save(adminrole);
		}
		    
		  if(!userepo.existsByRole(adminrole)) {
		 USER admin= new USER();
		    
		 
		
		  admin.setUsername("admin1");
		  admin.setEmail("anupchahande2814@gmail.com");
		  admin.setRole(adminrole);
		  admin.setPassword(encoder.encode("admin123"));
		  userepo.save(admin);
		  
		  }
		
		
		
	}

}
