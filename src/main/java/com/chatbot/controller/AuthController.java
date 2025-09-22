package com.chatbot.controller;

import com.chatbot.JwtUtil;
import com.chatbot.model.User;
import com.chatbot.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final UserService userService;
	private final JwtUtil jwtUtil;

	public AuthController(UserService userService, JwtUtil jwtUtil) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String password = body.get("password");
		User user = userService.register(email, password);
		String token = jwtUtil.generateToken(user.getEmail());
		return ResponseEntity.ok(Map.of("token", token));
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
		String email = body.get("email");
		String password = body.get("password");
		return userService.authenticate(email, password)
				.map(u -> ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(u.getEmail()))))
				.orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "invalid_credentials")));
	}
}