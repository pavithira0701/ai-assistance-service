package com.ai.assistance.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ai.assistance.service.model.OrderResponse;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {

	@GetMapping("/api/orders/{id}")
	OrderResponse getOrder(@PathVariable Long id);
}
