package com.app.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.Passwordotpreset;

public interface Forgetpassworrepo extends JpaRepository<Passwordotpreset, Long>{
	
	public Passwordotpreset findByEmail(String email);
	

}
