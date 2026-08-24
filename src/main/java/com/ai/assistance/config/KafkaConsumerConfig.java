package com.ai.assistance.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;

import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import org.springframework.util.backoff.FixedBackOff;

import com.ai.assistance.dto.OrderCreatedEvent;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Bean
	public ConsumerFactory<String, OrderCreatedEvent> orderConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "ai-assistance-group");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		JsonDeserializer<OrderCreatedEvent> jsonDeserializer = new JsonDeserializer<>(OrderCreatedEvent.class, false);
		jsonDeserializer.addTrustedPackages("com.ai.assistance.dto");
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
	}

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(KafkaTemplate<String, Object> kafkaTemplate) {
		return new DeadLetterPublishingRecoverer(kafkaTemplate);
	}

	@Bean
	public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {
		FixedBackOff backOff = new FixedBackOff(2000L, 2L);
		return new DefaultErrorHandler(recoverer, backOff);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> orderKafkaListenerContainerFactory(
			DefaultErrorHandler errorHandler) {
		ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(orderConsumerFactory());
		factory.setCommonErrorHandler(errorHandler);
		System.out.println("========== AI KAFKA LISTENER FACTORY CREATED ==========");
		return factory;
	}
}