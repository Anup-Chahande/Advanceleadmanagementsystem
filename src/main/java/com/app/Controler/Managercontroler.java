package com.app.Controler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Dto.ManagerDashboardDto;
import com.app.Entity.Employee;
import com.app.Entity.Lead;
import com.app.Entity.Manager;
import com.app.Entity.USER;
import com.app.Enum.LeadStatus;
import com.app.Repositry.Employeerepo;
import com.app.Repositry.Leadrepositry;
import com.app.Repositry.ManagerRepository;
import com.app.Repositry.Userreop;

@RestController
@RequestMapping("/MANAGER")
public class Managercontroler {

	@Autowired
	Userreop userrepo;

	@Autowired
	ManagerRepository managerrepo;

	@Autowired
	Employeerepo emprepo;
	
	@Autowired
	Leadrepositry leadrepo;

	@GetMapping("/myprofile")
	@PreAuthorize("hasRole('MANAGER')")
	public Manager managermyprofile() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER user = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		return managerrepo.findByUser(user);

	}

	@GetMapping("/myemployees")
	@PreAuthorize("hasRole('MANAGER')")
	public List<Employee> myemployees() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER user = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		Manager manager = managerrepo.findByUser(user);
		List<Employee> emp = emprepo.findByManager(manager);
		return emp;

	}

	@GetMapping("/myteamleads")
	@PreAuthorize("hasRole('MANAGER')")
	public List<Lead> myteamleads() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER user = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		Manager manager = managerrepo.findByUser(user);

		List<Employee> employee = emprepo.findByManager(manager);
		List<Lead> myleads = new ArrayList<>();
		
		for (Employee employe : employee) {
			
			List<Lead> leads =
				    leadrepo.findByAssignedToAndCompany(
				        employe.getUser(),
				        user.getCompany()
				    );			myleads.addAll(leads);
			
		}
		
		return myleads;

	}
	
	@GetMapping("/dashboard")
	@PreAuthorize("hasRole('MANAGER')")
	public ManagerDashboardDto dashboard() {

	    String email = SecurityContextHolder.getContext().getAuthentication().getName();
	    USER user = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

	    Manager manager = managerrepo.findByUser(user);
	    List<Employee> employees = emprepo.findByManager(manager);

	    ManagerDashboardDto dto = new ManagerDashboardDto();
	    dto.setTotalEmployees(employees.size());

	    for (Employee emp : employees) {

	    	List<Lead> leads =
	    		    leadrepo.findByAssignedToAndCompany(
	    		        emp.getUser(),
	    		        user.getCompany()
	    		    );
	        dto.setTotalLeads(dto.getTotalLeads() + leads.size());

	        for (Lead lead : leads) {

	            if (lead.getLeadstatus() == LeadStatus.NEW)
	                dto.setNewLeads(dto.getNewLeads() + 1);

	            if (lead.getLeadstatus() == LeadStatus.CONTACTED)
	                dto.setContacted(dto.getContacted() + 1);

	            if (lead.getLeadstatus() == LeadStatus.FOLLOWUP)
	                dto.setFollowup(dto.getFollowup() + 1);

	            if (lead.getLeadstatus() == LeadStatus.WON)
	                dto.setWon(dto.getWon() + 1);

	            if (lead.getLeadstatus() == LeadStatus.LOST)
	                dto.setLost(dto.getLost() + 1);
	        }
	    }

	    return dto;
	}

}
