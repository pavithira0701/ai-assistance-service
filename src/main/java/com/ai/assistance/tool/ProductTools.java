package com.ai.assistance.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.ai.assistance.client.ProductClient;
import com.ai.assistance.service.model.ProductResponse;

import feign.FeignException;

@Component
public class ProductTools {

	private final ProductClient productClient;

	public ProductTools(ProductClient productClient) {
		this.productClient = productClient;
	}

	@Tool(description = "Get product details using the product ID")
	public String getProduct(Long productId) {
		if (productId == null || productId <= 0) {
			return "A positive product ID is required.";
		}

		try {

			ProductResponse product = productClient.getProduct(productId);
			if (product == null) {
				return "Product service returned no details for product ID " + productId + ".";
			}

			return """
					Product found:
					Product ID: %d
					Product: %s
					Category: %s
					Available Quantity: %d
					""".formatted(product.getProductId(), product.getModel(), product.getCategory(),
					product.getQuantity());

		} catch (FeignException.NotFound e) {

			return "Product with ID " + productId + " was not found.";

		} catch (FeignException e) {

			return "Product service is currently unavailable. " + "Please try again later.";
		}
	}
}
