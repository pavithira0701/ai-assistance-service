package com.ai.assistance.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ai.assistance.dto.OrderCreatedEvent;
import com.ai.assistance.service.OpenAIService;
import jakarta.annotation.PostConstruct;

@Component
public class OrderEventConsumer {

	private final OpenAIService openAIService;

	public OrderEventConsumer(OpenAIService openAIService) {
		this.openAIService = openAIService;
	}

	@PostConstruct
	public void init() {
		System.out.println("========== AI KAFKA CONSUMER CREATED ==========");
	}

	@KafkaListener(topics = "order-created", containerFactory = "orderKafkaListenerContainerFactory")
	public void consumeOrderEvent(OrderCreatedEvent event) {

		System.out.println("=================================");
		System.out.println("ORDER EVENT RECEIVED");
		System.out.println("Order ID    : " + event.getOrderId());
		System.out.println("Product ID  : " + event.getProductId());
		System.out.println("Quantity    : " + event.getQuantity());
		System.out.println("=================================");
		
		String summary = openAIService.generateOrderSummary(event);

	    System.out.println("========== AI SUMMARY ==========");
	    System.out.println(summary);
	    System.out.println("================================");
	}
}