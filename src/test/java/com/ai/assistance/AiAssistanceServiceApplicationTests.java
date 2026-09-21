package com.ai.assistance;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;
class AiAssistanceServiceApplicationTests {

	@Test
	void applicationClassIsAvailable() {
		assertTrue(AiAssistanceServiceApplication.class.isAnnotationPresent(SpringBootApplication.class));
	}

}
