package com.chatbot.repository;


import com.chatbot.model.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
    List<Prompt> findByProjectId(Long projectId);
}
