package com.app.Repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.Role;

public interface Rolerepositry extends JpaRepository<Role, Integer>{
	
    Optional<Role> findByName(String name);

	
	

}
