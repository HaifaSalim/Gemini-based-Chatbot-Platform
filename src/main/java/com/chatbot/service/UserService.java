package com.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.chatbot.model.User;
import com.chatbot.repository.UserRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public User register(String email, String password) {

		if (userRepository.findByEmail(email).isPresent()) {
			throw new RuntimeException("User already exists with email: " + email);
		}

		User user = new User();
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));

		return userRepository.save(user);
	}

	public Optional<User> authenticate(String email, String password) {
		Optional<User> userOpt = userRepository.findByEmail(email);

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (passwordEncoder.matches(password, user.getPassword())) {
				return Optional.of(user);
			}
		}

		return Optional.empty();
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Optional<User> getByEmail(String email) {
		return userRepository.getByEmail(email);
	}

	public User save(User user) {
		return userRepository.save(user);
	}
}