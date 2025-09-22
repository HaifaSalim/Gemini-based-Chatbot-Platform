package com.chatbot.controller;

import com.chatbot.model.Project;
import com.chatbot.model.Prompt;
import com.chatbot.repository.ProjectRepository;
import com.chatbot.repository.PromptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	@Autowired
	private PromptRepository promptRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Value("${gemini.api.key}")
	private String geminiApiKey;

	private final RestTemplate restTemplate = new RestTemplate();

	@PostMapping("/send")
	public ResponseEntity<Map<String, String>> sendMessage(@RequestBody Map<String, Object> payload) {
		System.out.println("Current authentication: "
				+ org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication());

		String userMessage = (String) payload.get("message");
		Long projectId = Long.valueOf(payload.get("projectId").toString());

		try {
			Project project = projectRepository.findById(projectId)
					.orElseThrow(() -> new RuntimeException("Project not found"));

			List<Prompt> prompts = promptRepository.findByProjectId(project.getId());

			String context = prompts.stream().map(Prompt::getContent).collect(Collectors.joining("\n"));

			String fullMessage = context.isBlank() ? userMessage : context + "\n\nUser: " + userMessage;

			String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/"
					+ "gemini-1.5-flash:generateContent?key=" + geminiApiKey;

			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("contents", new Object[] { new HashMap<String, Object>() {
				{
					put("parts", new Object[] { new HashMap<String, String>() {
						{
							put("text", fullMessage);
						}
					} });
				}
			} });

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

			ResponseEntity<Map> response = restTemplate.exchange(geminiApiUrl, HttpMethod.POST, entity, Map.class);

			String botReply = "No response";
			if (response.getBody() != null) {
				var candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
				if (candidates != null && !candidates.isEmpty()) {
					Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
					var parts = (List<Map<String, String>>) content.get("parts");
					if (parts != null && !parts.isEmpty()) {
						botReply = parts.get(0).get("text");
					}
				}
			}

			Map<String, String> result = new HashMap<>();
			result.put("reply", botReply);
			return ResponseEntity.ok(result);

		} catch (HttpClientErrorException e) {
			System.err.println("Gemini API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
			Map<String, String> error = new HashMap<>();
			error.put("reply", "Error communicating with AI: " + e.getStatusCode());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		} catch (Exception e) {
			System.err.println("Unexpected error: " + e.getMessage());
			e.printStackTrace();
			Map<String, String> error = new HashMap<>();
			error.put("reply", "Unexpected error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
}
