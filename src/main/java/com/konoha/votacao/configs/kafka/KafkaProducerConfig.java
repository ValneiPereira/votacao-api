package com.konoha.votacao.configs.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.konoha.votacao.dto.MessageDTO;


@Configuration
public class KafkaProducerConfig {

	@Value(value="${kafka.bootstrapAddress}")
	private String bootstrapAddress;
	
	/**
	 * { link @ProducerFactory} possui as estratégias para configuração 
	 * de um {link @Producer}.
	 * 
	 * @return
	 */
	@Bean
	public ProducerFactory<String, MessageDTO> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	/**
	 * { link @KafkaTemplate} encapsula um { link @Producer } e define métodos para
	 * enviar mensagens. Ambas são thread-safe.
	 * 
	 * @return
	 */
	@Bean
	public KafkaTemplate<String, MessageDTO> kafkaTemplate(){
		return new KafkaTemplate<>(producerFactory());
	}
	
}
