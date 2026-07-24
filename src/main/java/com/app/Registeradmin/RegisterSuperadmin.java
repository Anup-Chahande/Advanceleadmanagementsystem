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
public class RegisterSuperadmin implements CommandLineRunner{
	
	@Autowired
	Userreop userepo;
	
	@Autowired
	Rolerepositry rolerepo;
	
	  @Autowired
	    private PasswordEncoder encoder;  
	
	                                                  
	@Override
	public void run(String... args) throws Exception {
		
		Role superAdminRole  = rolerepo.findByName("SUPER_ADMIN").orElse(null);

		if (superAdminRole  == null) {

			superAdminRole  = new Role();
			superAdminRole .setName("SUPER_ADMIN");

			superAdminRole  = rolerepo.save(superAdminRole );
		}
		    
		  if(!userepo.existsByRole(superAdminRole )) {
		 USER superAdmin = new USER();
		    
		 
		
		 superAdmin .setUsername("SUPER_ADMIN");
		 superAdmin .setEmail("anupchahande2814@gmail.com");
		 superAdmin .setRole(superAdminRole );
		 superAdmin .setPassword(encoder.encode("admin123"));
		 superAdmin .setCompany(null);
		  userepo.save(superAdmin );
		  
		  }
		
		
		
	}

}
