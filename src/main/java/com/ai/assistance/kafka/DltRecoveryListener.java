package com.ai.assistance.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.ai.assistance.dto.OrderCreatedEvent;
import com.ai.assistance.service.DltRecoveryService;

@Component
public class DltRecoveryListener {

	private final DltRecoveryService dltRecoveryService;

	public DltRecoveryListener(DltRecoveryService dltRecoveryService) {
		this.dltRecoveryService = dltRecoveryService;
	}

	@KafkaListener(topics = "order-created-dlt", groupId = "ai-assistance-dlt-recovery-group", containerFactory = "orderKafkaListenerContainerFactory", autoStartup = "false")
	public void recover(OrderCreatedEvent event) {
		System.out.println("===== DLT EVENT RECEIVED =====");
		System.out.println(event);
		dltRecoveryService.recover(event);
	}
}