package com.ai.assistance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.ai.assistance.dto.OrderRequest;
import com.ai.assistance.service.OpenAIService;

@RestController
public class AIController {

	private final OpenAIService openAIService;

	public AIController(OpenAIService openAIService) {
		this.openAIService = openAIService;
	}

	@GetMapping("/ai/ask")
	public String askAI(@RequestParam String question) {
		return openAIService.askAI(question);
	}

	@PostMapping("/ai/order-summary")
	public String generateOrderSummary(@RequestBody OrderRequest orderRequest) {
		return openAIService.generateOrderSummary(orderRequest);
	}
}