package com.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import com.chatbot.model.User;
import com.chatbot.service.*;

import java.util.Map;
import java.util.Optional;
import com.chatbot.model.Project;
import com.chatbot.model.Prompt;
import com.chatbot.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prompts")
public class PromptController {

	private final PromptService promptService;

	@Autowired
	public PromptController(PromptService promptService) {
		this.promptService = promptService;
	}

	@PostMapping("/{projectId}")
	public ResponseEntity<Prompt> addPrompt(@PathVariable Long projectId, @RequestBody Prompt promptRequest) {
		try {
			String content = promptRequest.getContent();
			if (content == null || content.trim().isEmpty()) {
				return ResponseEntity.badRequest().build();
			}

			Prompt savedPrompt = promptService.addPrompt(projectId, content.trim());
			return ResponseEntity.ok(savedPrompt);
		} catch (RuntimeException e) {
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<List<Prompt>> getPrompts(@PathVariable Long projectId) {
		try {
			List<Prompt> prompts = promptService.getPromptsForProject(projectId);
			return ResponseEntity.ok(prompts);
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
	}
}
