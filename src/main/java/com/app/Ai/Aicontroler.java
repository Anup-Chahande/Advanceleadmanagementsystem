package com.app.Ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Aidto.ChatRequest;

@RestController
@RequestMapping("/ai")
public class Aicontroler {

	@Autowired
	AiService aiservice;
	
	@PostMapping("/chat")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
	public String chat(@RequestBody ChatRequest msg) {
		
		
		return aiservice.chat(msg);
	}
	
	
	
	
	
}
