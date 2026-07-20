package com.app.Controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Entity.Customer;
import com.app.Repositry.Customerrepo;

@RestController
public class Customercuntroler {
    @Autowired
	Customerrepo custrepo;
	
	@GetMapping("ADMIN/allcustomer")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Customer> getallcustomer() {
		
		return custrepo.findAll();
		
	}
	
	
}
