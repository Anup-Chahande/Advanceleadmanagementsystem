package com.app.Controler;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.Dto.Employeedto;
import com.app.Dto.Manageerdto;
import com.app.Entity.Employee;
import com.app.Entity.Manager;
import com.app.Entity.Notifications;
import com.app.Entity.Role;
import com.app.Entity.USER;
import com.app.Repositry.Employeerepo;
import com.app.Repositry.Leadrepositry;
import com.app.Repositry.ManagerRepository;
import com.app.Repositry.Notifcationsrepo;
import com.app.Repositry.Rolerepositry;
import com.app.Repositry.Userreop;
import com.app.Service.Employeeservice;
import com.app.Service.ExcelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ADMIN")
public class AdminControler {
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

	@Autowired
	ManagerRepository mangrepo;

	@Autowired
	Employeeservice empservice;

	@Autowired
	Notifcationsrepo notirepo;

	@Autowired
	private ExcelService excelService;

	@PostMapping("/addemployee")
	@PreAuthorize("hasRole('ADMIN')")
	public String addemployee(@Valid @RequestBody Employeedto empd, BindingResult result) {
		return empservice.addemployee(empd, result);

	}

	@GetMapping("/getallemp")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Employee> getallemployee() {

		return empservice.getallemployee();

	}

	@DeleteMapping("/deleteemp/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deleteemployee(@PathVariable Long id) {

		return empservice.deleteemployee(id);
	}

	@GetMapping("/getempbyid/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public Employee getemployeebyid(@PathVariable Long id) {

		return empservice.getemployeebyid(id);
	}

	@PutMapping("/updateemp/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String updateemployee(@PathVariable Long id, @RequestBody Employee emp) {

		return empservice.updateemployee(id, emp);
	}

	@GetMapping("/searchemployename")
	@PreAuthorize("hasRole('ADMIN')")
	public Object serarchemployeebyname(@RequestParam String name) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		List<Employee> list = emprepo.findByCompanyAndNameContainingIgnoreCase(admin.getCompany(), name);

		if (list.isEmpty()) {
			return "No Records Found";
		}

		return list;
	}

	@GetMapping("/getallmanager")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Manager> getallmanager() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		return mangrepo.findByCompany(admin.getCompany());
	}

	@PostMapping("/addmanager")
	@PreAuthorize("hasRole('ADMIN')")
	public String addmanager(@Valid @RequestBody Manageerdto managerdto, BindingResult result) {

		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}

		if (userrepo.existsByEmail(managerdto.getEmail())) {

			return "email is already exist";
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		Role managerrole = roleRepository.findByName("MANAGER").orElse(null);

		if (managerrole == null) {
			managerrole = new Role();
			managerrole.setName("MANAGER");

			managerrole = roleRepository.save(managerrole);
		}

		USER user = new USER();
		user.setUsername(managerdto.getUsername());
		user.setPassword(encoder.encode(managerdto.getPassword()));
		user.setEmail(managerdto.getEmail());
		user.setCompany(admin.getCompany());
		user.setRole(managerrole);

		user = userrepo.save(user);

		Manager manager = new Manager();

		manager.setName(managerdto.getUsername());
		manager.setEmail(managerdto.getEmail());
		manager.setPhone(managerdto.getPhone());
		manager.setCompany(admin.getCompany());
		manager.setUser(user);
		mangrepo.save(manager);

		return "Manager added sucesfully";

	}

	@PutMapping("/updatemanager/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String updatemanager(@Valid @RequestBody Manageerdto managerdto, BindingResult result,
			@PathVariable Long id) {
		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		Manager manager = mangrepo.findById(id).orElseThrow(() -> new RuntimeException("Manager not found"));
		if (!manager.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("Access Denied");
		}
		manager.setName(managerdto.getUsername());
		manager.setEmail(managerdto.getEmail());
		manager.setPhone(managerdto.getPhone());

		USER user = manager.getUser();
		user.setEmail(managerdto.getEmail());
		user.setUsername(managerdto.getUsername());
		userrepo.save(user);
		mangrepo.save(manager);

		return "Manager updated sucesfully";

	}

	@DeleteMapping("/deletemanager/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String deletemanager(@PathVariable Long id) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		Manager manager = mangrepo.getById(id);

		if (!manager.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("Access Denied");
		}
		USER user = manager.getUser();
		List<Employee> employees = emprepo.findByManager(manager);

		for (Employee emp : employees) {
			emp.setManager(null);
		}

		emprepo.saveAll(employees);

		mangrepo.delete(manager);
		userrepo.delete(user);
		return "Manager Deleted Sucesfully";

	}

	@PutMapping("/assignemployee/{eid}/{mid}")
	@PreAuthorize("hasRole('ADMIN')")
	public String assignemployee(@PathVariable Long eid, @PathVariable Long mid) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		Manager manager = mangrepo.getById(mid);

		Employee employee = emprepo.getById(eid);
		
		if (!manager.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("Access Denied");
		}

		if (!employee.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("Access Denied");
		}
		
		employee.setManager(manager);

		emprepo.save(employee);

		Notifications notification = new Notifications();
		notification.setTitle("New Employee Assigned");
		notification.setMessage(employee.getName() + " has joined your team.");
		notification.setUser(manager.getUser());
		notirepo.save(notification);

		return employee.getName() + "assigned to" + manager.getName();

	}

	@PostMapping("/upload")
	@PreAuthorize("hasRole('ADMIN')")
	public String uploadExcel(@RequestParam("file") MultipartFile file) {

		return excelService.uploadLeads(file);

	}

}
