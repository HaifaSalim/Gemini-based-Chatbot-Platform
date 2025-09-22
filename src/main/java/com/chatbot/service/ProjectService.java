package com.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.chatbot.model.Project;
import com.chatbot.model.User;
import com.chatbot.repository.ProjectRepository;
import com.chatbot.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {
	private final ProjectRepository projectRepository;

	public ProjectService(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	public Project createProject(String name, String description, User user) {
		Project project = new Project();
		project.setName(name);
		project.setDescription(description);
		project.setUser(user);
		return projectRepository.save(project);
	}

	public List<Project> getProjectsForUser(Long userId) {
		return projectRepository.getByUserId(userId);
	}
}
