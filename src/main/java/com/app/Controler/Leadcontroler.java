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
		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		lead.setLeadstatus(LeadStatus.NEW);
		lead.setCompany(admin.getCompany());
		lead.setStaus(Status.ACTIVE);
		lead.setExecutedBy(admin.getUsername());
		leadrepo.save(lead);
		return "lead registered";
	}

	@GetMapping("/alllead")
	@PreAuthorize("hasRole('ADMIN')")
	public List<Lead> getalllead() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		return leadrepo.findByCompany(admin.getCompany());

	}

	@GetMapping("/leadbyid/{id}")
	@PreAuthorize("hasRole('ADMIN')")

	public Lead getleadbyid(@PathVariable long id) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		Lead lead = leadrepo.getById(id);
		if (!lead.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("Access Denied");
		}

		return lead;

	}

	@DeleteMapping("/deletebyid/{id}")
	@PreAuthorize("hasRole('ADMIN')")

	public String deletebyid(@PathVariable long id) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));
		Lead lead = leadrepo.getById(id);

		if (!lead.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("You cannot delete another company's lead");
		}

		leadrepo.deleteById(id);

		return "lead Deleted";

	}

	@PutMapping("/updatelead/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public String updateLead(@PathVariable Long id, @RequestBody Lead lead) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));
		Lead leadid = leadrepo.getById(id);

		if (!leadid.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("You cannot update another company's lead");
		}

		lead.setId(id);
		leadrepo.save(lead);

		return "Lead updated";
	}

	@PutMapping("/assignlead/{leadId}/{employeeId}")
	@PreAuthorize("hasRole('ADMIN')")
	public String assignlead(@PathVariable Long leadId, @PathVariable Long employeeId) {

		Lead lead = leadrepo.getById(leadId);

		Employee emp = emprepo.getById(employeeId);
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		if (!lead.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("Access Denied");
		}

		if (!emp.getCompany().getId().equals(admin.getCompany().getId())) {
			throw new RuntimeException("Access Denied");
		}

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
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		USER admin = userrepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));

		List<Lead> list = leadrepo.findByCompanyAndNameContainingIgnoreCase(admin.getCompany(), name);
		if (list.isEmpty()) {

			return "no Records Found ";

		}

		return list;

	}


	
	@GetMapping("/leadcount")
	@PreAuthorize("hasRole('ADMIN')")
	public Map<String, Long> getcount() {

	    String email = SecurityContextHolder.getContext()
	            .getAuthentication()
	            .getName();

	    USER admin = userrepo.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("Admin not found"));

	    Map<String, Long> counts = new HashMap<>();

	    counts.put("totalLeads",
	            leadrepo.countByCompany(admin.getCompany()));

	    counts.put("newLeads",
	            leadrepo.countByCompanyAndLeadstatus(admin.getCompany(), LeadStatus.NEW));

	    counts.put("hotLeads",
	            leadrepo.countByCompanyAndLeadtype(admin.getCompany(), Leadtype.HOT));

	    counts.put("warmLeads",
	            leadrepo.countByCompanyAndLeadtype(admin.getCompany(), Leadtype.WARM));

	    counts.put("coldLeads",
	            leadrepo.countByCompanyAndLeadtype(admin.getCompany(), Leadtype.COLD));

	    counts.put("wonLeads",
	            leadrepo.countByCompanyAndLeadstatus(admin.getCompany(), LeadStatus.WON));

	    counts.put("lostLeads",
	            leadrepo.countByCompanyAndLeadstatus(admin.getCompany(), LeadStatus.LOST));

	    return counts;
	}
}
