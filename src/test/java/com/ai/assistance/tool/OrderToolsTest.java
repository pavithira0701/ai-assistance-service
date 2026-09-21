package com.ai.assistance.tool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ai.assistance.client.OrderClient;
import com.ai.assistance.service.model.OrderResponse;

@ExtendWith(MockitoExtension.class)
class OrderToolsTest {
    @Mock private OrderClient orderClient;

    @Test
    void getOrderFormatsOrderDetails() {
        when(orderClient.getOrder(21L)).thenReturn(new OrderResponse(21L, 7L, 3));
        String result = new OrderTools(orderClient).getOrder(21L);
        assertEquals("Order found:\nOrder ID: 21\nProduct ID: 7\nQuantity: 3\n", result);
    }
}
