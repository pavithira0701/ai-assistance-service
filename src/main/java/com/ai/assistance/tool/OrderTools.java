package com.ai.assistance.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.ai.assistance.client.OrderClient;
import com.ai.assistance.service.model.OrderResponse;

import feign.FeignException;

@Component
public class OrderTools {

	private final OrderClient orderClient;

	public OrderTools(OrderClient orderClient) {
		this.orderClient = orderClient;
	}

	@Tool(description = "Get the details of an order using its order ID")
	public String getOrder(Long orderId) {
		if (orderId == null || orderId <= 0) {
			return "A positive order ID is required.";
		}

		try {

			OrderResponse order = orderClient.getOrder(orderId);
			if (order == null) {
				return "Order service returned no details for order ID " + orderId + ".";
			}

			return """
					Order found:
					Order ID: %d
					Product ID: %d
					Quantity: %d
					""".formatted(order.getOrderId(), order.getProductId(), order.getQuantity());

		} catch (FeignException.NotFound e) {

			return "Order with ID " + orderId + " was not found.";

		} catch (FeignException e) {

			return "Order service is currently unavailable. " + "Please try again later.";
		}
	}
}
