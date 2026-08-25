package com.ai.assistance;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
public class AiAssistanceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiAssistanceServiceApplication.class, args);
	}

}
