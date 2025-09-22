package com.chatbot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.chatbot.model.Project;
import com.chatbot.model.Prompt;
import com.chatbot.repository.ProjectRepository;
import com.chatbot.repository.PromptRepository;

@Service
public class PromptService {
	private final PromptRepository promptRepository;
    private final ProjectRepository projectRepository;

    public PromptService(PromptRepository promptRepository, ProjectRepository projectRepository) {
        this.promptRepository = promptRepository;
        this.projectRepository = projectRepository;
    }

    public Prompt addPrompt(Long projectId, String content) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        Prompt prompt = new Prompt();
        prompt.setProject(project);
        prompt.setContent(content);
        return promptRepository.save(prompt);
    }

    public List<Prompt> getPromptsForProject(Long projectId) {
        return promptRepository.findByProjectId(projectId);
    }
}

