package com.ai.assistance.tool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ai.assistance.client.ProductClient;
import com.ai.assistance.service.model.ProductResponse;

@ExtendWith(MockitoExtension.class)
class ProductToolsTest {

    @Mock
    private ProductClient productClient;

    @Test
    void getProductFormatsProductDetails() {
        ProductResponse product = new ProductResponse();
        product.setProductId(12L);
        product.setModel("XPS 13");
        product.setCategory("Laptop");
        product.setQuantity(4);
        when(productClient.getProduct(12L)).thenReturn(product);

        String result = new ProductTools(productClient).getProduct(12L);

        assertEquals("Product found:\nProduct ID: 12\nProduct: XPS 13\nCategory: Laptop\nAvailable Quantity: 4\n", result);
    }
}
