package com.ai.assistance.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.ai.assistance.client.OrderClient;
import com.ai.assistance.service.model.OrderResponse;

@Component
public class OrderTools {

    private final OrderClient orderClient;

    public OrderTools(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @Tool(description = "Get the details of an order using its order ID")
    public OrderResponse getOrder(Long orderId) {

        return orderClient.getOrder(orderId);
    }
}