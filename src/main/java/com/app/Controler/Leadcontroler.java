package com.app.Controler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Entity.Employee;
import com.app.Entity.Lead;
import com.app.Entity.Notifications;
import com.app.Entity.USER;
import com.app.Enum.LeadStatus;
import com.app.Enum.Leadtype;
import com.app.Enum.Status;
import com.app.Repositry.Employeerepo;
import com.app.Repositry.Leadrepositry;
import com.app.Repositry.Notifcationsrepo;
import com.app.Repositry.Userreop;

@RestController
@RequestMapping("/ADMIN")
public class Leadcontroler {

	@Autowired
	Leadrepositry leadrepo;

	@Autowired
	Userreop userrepo;

	@Autowired
	Employeerepo emprepo;
	
	
	@Autowired
	Notifcationsrepo notifyrepo;

	@PostMapping("/createlead")
	@PreAuthorize("hasRole('ADMIN')")
	public String registerlead(@RequestBody Lead lead) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		Optional<USER> usser = userrepo.findByEmail(email);
		lead.setLeadstatus(LeadStatus.NEW);
		lead.setStaus(Status.ACTIVE);
		lead.setExecutedBy(usser.get().getUsername());
		leadrepo.save(lead);
		return "lead registered";
	}

	@GetMapping("/alllead")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Lead> getalllead() {

		return leadrepo.findAll();

	}

	@GetMapping("/leadbyid/{id}")
	@PreAuthorize("hasRole('ADMIN')")

	public Lead getleadbyid(@PathVariable long id) {

		return leadrepo.getById(id);

	}

	@DeleteMapping("/deletebyid/{id}")
	@PreAuthorize("hasRole('ADMIN')")

	public String deletebyid(@PathVariable long id) {
		leadrepo.deleteById(id);

		return "lead Deleted";

	}

	@PutMapping("/updatelead/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String updateLead(@PathVariable Long id, @RequestBody Lead lead) {

		lead.setId(id);
		leadrepo.save(lead);

		return "Lead updated";
	}

	@PutMapping("/assignlead/{leadId}/{employeeId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String assignlead(@PathVariable Long leadId, @PathVariable Long employeeId) {

		Lead lead = leadrepo.getById(leadId);

		Employee emp = emprepo.getById(employeeId);

		lead.setAssignedTo(emp.getUser());
		leadrepo.save(lead);
		
		Notifications notify = new Notifications();
		notify.setTitle("new Lead assigned");
		notify.setMessage("a new lead assigend to u");
		notify.setUser(emp.getUser());
		notifyrepo.save(notify);
		
		

		return lead.getName() + " assigned to " + emp.getUser().getUsername();

	}

	@GetMapping("/findbyleadname")
	@PreAuthorize("hasRole('ADMIN')")
	public Object findbyleadname(@RequestParam String name) {

		List<Lead> list = leadrepo.findByNameContainingIgnoreCase(name);
		if (list.isEmpty()) {

			return "no Records Found ";

		}

		return list;

	}

	@GetMapping("/leadcount")
	@PreAuthorize("hasRole('ADMIN')")
	public Map<String, Long> getcount() {
		Map<String, Long> counts = new HashMap<>();

		counts.put("totalLeads", leadrepo.count());
		counts.put("newleads", leadrepo.countByLeadstatus(LeadStatus.NEW));
		counts.put("hotLeads", leadrepo.countByLeadtype(Leadtype.HOT));
		counts.put("warmLeads", leadrepo.countByLeadtype(Leadtype.WARM));
		counts.put("coldLeads", leadrepo.countByLeadtype(Leadtype.COLD));
		counts.put("wonLeads", leadrepo.countByLeadstatus(LeadStatus.WON));
		counts.put("lostLeads", leadrepo.countByLeadstatus(LeadStatus.LOST));

		return counts;

	}

}
