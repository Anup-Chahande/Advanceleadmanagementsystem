package com.app.Controler;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

import com.app.Dto.Changepassword;
import com.app.Dto.Emailrequst;
import com.app.Dto.Employeedto;
import com.app.Entity.Customer;
import com.app.Entity.Employee;
import com.app.Entity.Lead;
import com.app.Entity.Notifications;
import com.app.Entity.Role;
import com.app.Entity.USER;
import com.app.Enum.LeadStatus;
import com.app.Repositry.Customerrepo;
import com.app.Repositry.Employeerepo;
import com.app.Repositry.Leadrepositry;
import com.app.Repositry.Notifcationsrepo;
import com.app.Repositry.Rolerepositry;
import com.app.Repositry.Userreop;
import com.app.Service.Employeeservice;

import jakarta.validation.Valid;

@RestController
public class Employeecontroler {

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
	Employeeservice empservice;
	
   @Autowired
   Customerrepo custrepo;
   

	@Autowired
	Notifcationsrepo notirepo;
	
	@Autowired
	JavaMailSender mailsender;
	

	@GetMapping("EMPLOYEE/myleads")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public List<Lead> myLeads() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER user = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

		return leadrepo.findByAssignedTo(user);
	}



	@PutMapping("/EMPLOYEE/changeleadstatus/{id}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public String changeleadstatus(@PathVariable Long id, @RequestBody Lead lead) {

		Lead leads = leadrepo.getById(id);

		leads.setLeadstatus(lead.getLeadstatus());

		leadrepo.save(leads);
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER user = userrepo.findByEmail(email)
		        .orElseThrow(() -> new RuntimeException("User not found"));

		Employee employee = emprepo.findByUser(user);
		
		if(leads.getLeadstatus()==LeadStatus.WON) {
			 if (!custrepo.existsByEmail(leads.getEmail())) {

		            Customer customer = new Customer();
		            customer.setName(leads.getName());
		            customer.setPhone(leads.getPhone());
		            customer.setArea(leads.getArea());
		            customer.setCity(leads.getCity());
		            customer.setEmail(leads.getEmail());
		            customer.setDob(leads.getDob());

		            custrepo.save(customer);
		            
		            Notifications notification = new Notifications();

		            notification.setTitle("Lead Won");
		            notification.setMessage(lead.getAssignedTo() + " won lead " + lead.getName());

		            notification.setUser(employee.getManager().getUser());

		            notirepo.save(notification);
		            
		            
		        }
		}
		

		return " lead Status Updated";
	}

	@PutMapping("/EMPLOYEE/changestatus/{id}")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public String changestatus(@PathVariable Long id, @RequestBody Lead lead) {

		Lead leads = leadrepo.getById(id);

		leads.setStaus(lead.getStaus());

		leadrepo.save(leads);

		return " Status Updated";
	}

	@GetMapping("/EMPLOYEE/myprofile")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public Employee employeyeprofile() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		USER user = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		Employee emp = emprepo.findByUser(user);
		return emp;

	}

	@PutMapping("/EMPLOYEE/changepassword")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public String changepassword(@RequestBody Changepassword empd) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER user = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		if(!encoder.matches(empd.getOldpassword(), user.getPassword())) {
			
			return "password dosent match";
			
		}
		
            user.setPassword(encoder.encode(empd.getNewpassword()));
            userrepo.save(user);
            
			return "password updated";		
		
	}
	
	
	@PostMapping("/email")
	@PreAuthorize("hasRole('EMPLOYEE')")
	public String sendaigenratedmail(@RequestBody Emailrequst emailrequest) {
		
		 SimpleMailMessage msg = new SimpleMailMessage();
		 msg.setTo(emailrequest.getTo());
		 msg.setSubject(emailrequest.getSubject());
		 msg.setText(emailrequest.getBody());
		 
		 mailsender.send(msg);
		 return "email sent sucesfully";
		
	}
	
	
	
	
	
}
