package com.app.Repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.Company;
import com.app.Entity.Manager;
import com.app.Entity.USER;

public interface ManagerRepository extends JpaRepository<Manager, Long>{

	public Manager findByUser(USER user);

	public List<Manager> findByCompany(Company company);

}