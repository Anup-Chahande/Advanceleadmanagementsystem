package com.app.Controler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Entity.Notifications;
import com.app.Entity.USER;
import com.app.Repositry.Notifcationsrepo;
import com.app.Repositry.Userreop;

@RestController
public class Notificationcontroler {

	@Autowired
	Notifcationsrepo notirepo;
	
	@Autowired
	Userreop userrepo;

	@GetMapping("/notifications")
	@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE')")
	public List<Notifications>  viewnotifications() {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		USER user =userrepo.findByEmail(email) .orElseThrow(() -> new RuntimeException("User not found"));
		
		return  notirepo.findByUserOrderByIdDesc(user);
		

	}

	
	@PutMapping("/notification/read/{id}")
	public String markAsRead(@PathVariable Long id) {

	    Notifications notification = notirepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Notification not found"));

	    notification.setRead(true);

	    notirepo.save(notification);

	    return "Notification marked as read";
	}
	
	@DeleteMapping("/notification/{id}")
	public String deleteNotification(@PathVariable Long id) {

	    notirepo.deleteById(id);

	    return "Notification deleted";
	}
	
	
}
