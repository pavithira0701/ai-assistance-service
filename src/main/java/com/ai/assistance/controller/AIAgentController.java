package com.ai.assistance.controller;

import org.springframework.web.bind.annotation.*;

import com.ai.assistance.client.OrderClient;
import com.ai.assistance.service.AIAgentService;
import com.ai.assistance.service.model.OrderResponse;

@RestController
@RequestMapping("/ai")
public class AIAgentController {

	private final AIAgentService aiAgentService;

	public AIAgentController(AIAgentService aiAgentService) {
		this.aiAgentService = aiAgentService;
	}

	@GetMapping("/ask")
	public String ask(@RequestParam String question) {
		return aiAgentService.ask(question);
	}
}