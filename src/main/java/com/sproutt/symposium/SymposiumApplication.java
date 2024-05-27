package com.sproutt.symposium;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRabbit
@SpringBootApplication
public class SymposiumApplication {

	public static void main(String[] args) {
		SpringApplication.run(SymposiumApplication.class, args);
	}

	private static final String X_MESSAGE_TTL_KEY = "x-message-ttl";
	private static final Long X_MESSAGE_TTL = 1000 * 60 * 30L;

	@Bean
	MessageConverter ampqMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	DirectExchange waitingDirect() {
		return new DirectExchange("direct.waiting");
	}

	@Bean
	Queue waitingQueue() {
		return QueueBuilder.durable("queue.waiting")
				.withArgument(X_MESSAGE_TTL_KEY, X_MESSAGE_TTL)
				.build();
	}

	@Bean
	Binding waitingQBinding() {
		return BindingBuilder.bind(waitingQueue()).to(waitingDirect()).with("direct.waiting");
	}

	@Bean
	DirectExchange waitingConsumerDirect() {
		return new DirectExchange("direct.waiting.consumer");
	}

	@Bean
	Queue waitingConsumerQueue() {
		return QueueBuilder.durable("queue.waiting.consumer")
				.withArgument(X_MESSAGE_TTL_KEY, X_MESSAGE_TTL)
				.build();
	}

	@Bean
	Binding waitingConsumerQBinding() {
		return BindingBuilder.bind(waitingConsumerQueue())
				.to(waitingConsumerDirect())
				.with("direct.waiting.consumer");
	}
}
