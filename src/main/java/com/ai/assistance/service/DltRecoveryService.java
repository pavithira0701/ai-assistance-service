package com.ai.assistance.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ai.assistance.dto.OrderCreatedEvent;

@Service
public class DltRecoveryService {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public DltRecoveryService(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void recover(OrderCreatedEvent event) {
		kafkaTemplate.send("order-created", event.getOrderId().toString(), event);
		System.out.println("===== DLT EVENT REPUBLISHED TO order-created =====");
		System.out.println(event);
	}
}