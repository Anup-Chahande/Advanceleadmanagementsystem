package com.app.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.Company;
import com.app.Entity.Customer;

public interface Customerrepo extends JpaRepository<Customer, Long>{
	boolean existsByEmail(String email);

	boolean existsByEmailAndCompany(String email, Company company);
}
