package com.ai.assistance.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.ai.assistance.client.ProductClient;
import com.ai.assistance.service.model.ProductResponse;

@Component
public class ProductTools {

	private final ProductClient productClient;

	public ProductTools(ProductClient productClient) {
		this.productClient = productClient;
	}

	@Tool(description = "Get product details using the product ID")
	public ProductResponse getProduct(Long productId) {
		return productClient.getProduct(productId);
	}
}