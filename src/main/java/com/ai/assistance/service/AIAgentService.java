package com.ai.assistance.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.ai.assistance.tool.OrderTools;
import com.ai.assistance.tool.ProductTools;

@Service
public class AIAgentService {

	private final ChatClient chatClient;
	private final OrderTools orderTools;
	private final ProductTools productTools;

	public AIAgentService(ChatClient chatClient, OrderTools orderTools, ProductTools productTools) {

		this.chatClient = chatClient;
		this.orderTools = orderTools;
		this.productTools = productTools;
	}

	public String ask(String question) {

		return chatClient.prompt().user(question).tools(orderTools,productTools).call().content();
	}
}