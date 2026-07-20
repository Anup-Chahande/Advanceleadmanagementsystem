package com.app.Ai;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.Aidto.AIRequest;
import com.app.Aidto.ChatRequest;
import com.app.Aidto.Content;
import com.app.Aidto.GeminiResponse;
import com.app.Aidto.Part;
import com.app.Entity.Lead;
import com.app.Repositry.Leadrepositry;

@Service
public class AiService {

	@Autowired
	RestTemplate resttampleate;

	@Autowired
	Leadrepositry leadrepo;

	@Value("${gemini.api.key}")
	private String apikey;

	public String chat(ChatRequest msg) {
		String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
				+ apikey;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Part part = new Part();
		String prompt = "";
		if (msg.getLeadId() == null) {

			

			prompt = """
					You are CRM Assistant.

					You help users with:
					- Leads
					- Customers
					- Emails
					- WhatsApp messages
					- Follow-ups
					- Sales

					Only answer CRM-related questions.

					User Question:
					""" + msg.getMessage();

		} else {

			Lead lead = leadrepo.findById(msg.getLeadId()).orElseThrow(() -> new RuntimeException("Lead not found"));

			prompt = """
					You are an AI Assistant for a Lead Management CRM.

					Your responsibilities:
					- Help employees, managers, and admins with CRM tasks.
					- Use the lead details below as context.
					- Follow the employee's request exactly.

					Company Information:
					- Company Name: Techgicus priavte limited

					Rules:
					- Return only the requested output.
					- Do not explain your reasoning.
					- Do not add notes, warnings, or disclaimers.
					- Do not mention missing, empty, or null fields.
					- If any lead information is unavailable, simply ignore it.
					- Always use "Techgicus priavte limited" when referring to the company.
					- Do not use markdown unless explicitly requested.
					- Be professional, concise, and business-friendly.
					- Never use placeholders such as [Your Company Name],[Your Name], [Your Email], or [Your Phone] etc.

					Lead Details:
					Name: %s
					Email: %s
					Phone: %s
					City: %s
					Area: %s
					Lead Type: %s
					Lead Status: %s
					Customer Budget: %s
					Remark: %s

					Employee Request:
					%s
					""".formatted(
					    lead.getName(),
					    lead.getEmail(),
					    lead.getPhone(),
					    lead.getCity(),
					    lead.getArea(),
					    lead.getLeadtype(),
					    lead.getLeadstatus(),
					    lead.getCustomerBudget(),
					    lead.getRemark(),
					    msg.getMessage()
					);
		}

		part.setText(prompt);
		Content content = new Content();
		content.setParts(List.of(part));

		AIRequest request = new AIRequest();
		request.setContents(List.of(content));
		HttpEntity<AIRequest> entity = new HttpEntity<>(request, headers);

		ResponseEntity<GeminiResponse> response = resttampleate.postForEntity(url, entity, GeminiResponse.class

		);
		GeminiResponse gemini = response.getBody();

		return gemini.getCandidates().get(0).getContent().getParts().get(0).getText();
	}

}
