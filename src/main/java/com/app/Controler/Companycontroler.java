package com.app.Controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Dto.CompanyAdminDto;
import com.app.Dto.Companydto;
import com.app.Entity.Company;
import com.app.Entity.Role;
import com.app.Entity.USER;
import com.app.Repositry.CompanyRepo;
import com.app.Repositry.Rolerepositry;
import com.app.Repositry.Userreop;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/SUPERADMIN")
public class Companycontroler {

	@Autowired
	CompanyRepo comprepo;

	@Autowired
	private Userreop userRepo;

	@Autowired
	private Rolerepositry roleRepo;

	@Autowired
	private PasswordEncoder encoder;

	@PostMapping("/addcompany")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public String addcompany(@RequestBody Companydto companydto) {

		Company company = new Company();
		company.setCompanyName(companydto.getCompanyName());
		company.setAddress(companydto.getAddress());
		company.setEmail(companydto.getEmail());
		company.setPhone(companydto.getPhone());

		comprepo.save(company);
		return "company saved sucessfuly";
	}

	@PostMapping("/addadmin/{companyId}")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public String createCompanyAdmin(@PathVariable Long companyId, @Valid @RequestBody CompanyAdminDto dto,
			BindingResult result) {

		if (result.hasErrors()) {
			return result.getFieldError().getDefaultMessage();
		}

		Company company = comprepo.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));

		if (userRepo.existsByEmail(dto.getEmail())) {
			return "Email already exists";
		}

		Role adminRole = roleRepo.findByName("ADMIN").orElse(null);

		if (adminRole == null) {
			adminRole = new Role();
			adminRole.setName("ADMIN");
			adminRole = roleRepo.save(adminRole);
		}

		USER admin = new USER();

		admin.setUsername(dto.getUsername());
		admin.setEmail(dto.getEmail());
		admin.setPassword(encoder.encode(dto.getPassword()));

		admin.setRole(adminRole);

		admin.setCompany(company);

		userRepo.save(admin);

		return "Company Admin Created Successfully";

	}

	@GetMapping("/allcompanies")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public List<Company> getAllCompanies() {

		return comprepo.findAll();
	}

	@GetMapping("/company/{id}")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public Company getCompanyById(@PathVariable Long id) {

		return comprepo.getById(id);
	}
	
	
	@PutMapping("/company/{id}")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public String updateCompany(@PathVariable Long id,
	                            @RequestBody Companydto dto) {

	    Company company = comprepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Company not found"));

	    company.setCompanyName(dto.getCompanyName());
	    company.setAddress(dto.getAddress());
	    company.setEmail(dto.getEmail());
	    company.setPhone(dto.getPhone());

	    comprepo.save(company);

	    return "Company Updated Successfully";
	}
	
	
	
	@PutMapping("/company/{id}/activate")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public String activateCompany(@PathVariable Long id) {

	    Company company = comprepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Company not found"));

	    company.setActive(true);
	    comprepo.save(company);

	    return "Company Activated Successfully";
	}
	
	
	@PutMapping("/company/{id}/deactivate")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public String deactivateCompany(@PathVariable Long id) {

	    Company company = comprepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Company not found"));
	    
	    

	    company.setActive(false);
	    comprepo.save(company);

	    return "Company Deactivated Successfully";
	}
	
	@GetMapping("/company/{companyId}/admins")
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	public List<USER> getCompanyAdmins(@PathVariable Long companyId) {

	    Company company = comprepo.findById(companyId)
	            .orElseThrow(() -> new RuntimeException("Company not found"));

	    return userRepo.findByCompanyIdAndRole_Name(
	            company.getId(),
	            "ADMIN"
	    );
	}

}
