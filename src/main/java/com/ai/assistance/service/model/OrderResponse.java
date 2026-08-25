package com.ai.assistance.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderResponse {
	@JsonProperty("id")
	private Long orderId;
	private Long productId;
	private int quantity;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public OrderResponse() {
		super();
	}

	public OrderResponse(Long orderId, Long productId, int quantity) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}

}
