package com.ai.assistance.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderResponse(
		@JsonProperty("id") Long orderId,
		Long productId,
		int quantity) {
}