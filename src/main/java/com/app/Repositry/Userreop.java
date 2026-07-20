package com.app.Repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Entity.Role;
import com.app.Entity.USER;

@Repository
public interface Userreop extends JpaRepository<USER, Integer>{

	    Optional<USER> findByUsername(String username);
	    Optional<USER> findByEmail(String email);

	    
	   boolean existsByRole(Role adminrole);
	   boolean existsByEmail(String email);


}

