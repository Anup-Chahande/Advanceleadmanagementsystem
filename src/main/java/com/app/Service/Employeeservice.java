package com.app.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.Dto.Employeedto;
import com.app.Entity.Employee;
import com.app.Entity.Lead;
import com.app.Entity.Role;
import com.app.Entity.USER;
import com.app.Repositry.Employeerepo;
import com.app.Repositry.Leadrepositry;
import com.app.Repositry.Rolerepositry;
import com.app.Repositry.Userreop;

import jakarta.validation.Valid;

@Service
public class Employeeservice {

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	Userreop userrepo;

	@Autowired
	Rolerepositry roleRepository;

	@Autowired
	Employeerepo emprepo;

	@Autowired
	Leadrepositry leadrepo;

	public String addemployee(@Valid @RequestBody Employeedto empd, BindingResult result) {

		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}

		if (userrepo.existsByEmail(empd.getEmail())) {

			return "email is already exist";
		}

		Role employeeRole = roleRepository.findByName("EMPLOYEE").orElse(null);

		if (employeeRole == null) {

			employeeRole = new Role();
			employeeRole.setName("EMPLOYEE");

			employeeRole = roleRepository.save(employeeRole);
		}

		USER user = new USER();

		user.setUsername(empd.getUsername());
		user.setPassword(encoder.encode(empd.getPassword()));
		user.setEmail(empd.getEmail());
		user.setRole(employeeRole);
		USER userobj = userrepo.save(user);

		Employee emp = new Employee();
		emp.setName(empd.getUsername());
		emp.setCity(empd.getCity());
		emp.setDob(empd.getDob());
		emp.setEmail(empd.getEmail());
		emp.setNumber(empd.getNumber());
		emp.setDesignation(empd.getDesignation());
		emp.setJoiningDate(empd.getJoiningDate());
		emp.setDepartment(empd.getDepartment());
		emp.setUser(userobj);

		emprepo.save(emp);

		return "Employee added ";

	}

	public List<Employee> getallemployee() {

		List<Employee> list = emprepo.findAll();
		for (Employee employee : list) {

			if (employee.getUser() != null) {
				employee.setUser(null);

			}
		}
		return list;

	}

	public String deleteemployee(@PathVariable Long id) {

		Employee emp = emprepo.getById(id);
		USER user = emp.getUser();

		List<Lead> leads = leadrepo.findByAssignedTo(user);
		for (Lead lead : leads) {

			lead.setAssignedTo(null);
			leadrepo.save(lead);

		}

		emprepo.delete(emp);
		userrepo.delete(user);

		return "Employee deleted";
	}

	public Employee getemployeebyid(@PathVariable Long id) {

		return emprepo.getById(id);
	}

	public String updateemployee(@PathVariable Long id, @RequestBody Employee emp) {

		Employee existing = emprepo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

		existing.setName(emp.getName());
		existing.setEmail(emp.getEmail());
		existing.setCity(emp.getCity());
		existing.setDob(emp.getDob());
		existing.setNumber(emp.getNumber());
		existing.setDepartment(emp.getDepartment());
		existing.setDesignation(emp.getDesignation());
		existing.setJoiningDate(emp.getJoiningDate());
		
		
		USER user = existing.getUser();
		user.setUsername(emp.getName());
		user.setEmail(emp.getEmail());

		userrepo.save(user);
		emprepo.save(existing);

		return "Employee updated successfully";
	}

}
