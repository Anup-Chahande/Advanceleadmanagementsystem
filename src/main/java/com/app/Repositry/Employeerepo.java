package com.app.Repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Entity.Employee;
import com.app.Entity.Manager;
import com.app.Entity.USER;

public interface Employeerepo extends JpaRepository<Employee, Long>{

	
	public List<Employee> findByNameContainingIgnoreCase(String name);
	
  public  Employee findByUser(USER user);

  public List<Employee> findByManager(Manager manager);
	
}
