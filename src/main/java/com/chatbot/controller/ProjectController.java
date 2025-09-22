package com.chatbot.controller;

import com.chatbot.JwtUtil;
import com.chatbot.model.Project;
import com.chatbot.model.User;
import com.chatbot.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.chatbot.model.Project;
import com.chatbot.model.User;
import com.chatbot.repository.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, JwtUtil jwtUtil) {
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping
	public ResponseEntity<?> createProject(@RequestBody Project p) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = auth.getName();

		Optional<User> owner = userRepository.findByEmail(userEmail);
		if (owner.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("error", "User not found"));
		}

		p.setUser(owner.get());
		Project saved = projectRepository.save(p);
		return ResponseEntity.ok(saved);
	}

	@GetMapping("/my-projects")
	public ResponseEntity<List<Project>> getMyProjects() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String userEmail = auth.getName();

		Optional<User> user = userRepository.findByEmail(userEmail);
		if (user.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		List<Project> projects = projectRepository.getByUserId(user.get().getId());
		return ResponseEntity.ok(projects);
	}

	@GetMapping("/user/{userId}")
	public Optional<Project> byUserId(@PathVariable Long userId) {
		return projectRepository.findById(userId);
	}
}
