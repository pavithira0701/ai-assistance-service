package com.ai.assistance.service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import com.ai.assistance.dto.OrderCreatedEvent;
import com.ai.assistance.dto.OrderRequest;

@Service
public class OpenAIService {

	private final OpenAiChatModel chatModel;

	public OpenAIService(OpenAiChatModel chatModel) {
		this.chatModel = chatModel;
	}

	public String askAI(String question) {
		return chatModel.call(question);
	}

	public String generateOrderSummary(OrderRequest orderRequest) {

		String prompt = """
				You are an order management assistant.
				Analyze the following order:
				Order ID: %d
				Product ID: %d
				Quantity: %d
				Provide a short, professional summary of this order.
				""".formatted(orderRequest.getOrderId(), orderRequest.getProductId(), orderRequest.getQuantity());

		return chatModel.call(prompt);
	}

	public String generateOrderSummary(OrderCreatedEvent event) {
		// to induce failure
				//throw new RuntimeException("TEST: OpenAI service failure");

		String prompt = """
				You are an order management assistant.
				Analyze the following newly created order:
				Order ID: %d
				Product ID: %d
				Quantity: %d

				Provide a short, professional summary of this order.
				""".formatted(event.getOrderId(), event.getProductId(), event.getQuantity());

		return chatModel.call(prompt);
	}
}