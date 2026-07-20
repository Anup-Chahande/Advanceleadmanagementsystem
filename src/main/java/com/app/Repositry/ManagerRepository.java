package com.app.Repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.Manager;
import com.app.Entity.USER;

public interface ManagerRepository extends JpaRepository<Manager, Long>{

	public Manager findByUser(USER user);

}