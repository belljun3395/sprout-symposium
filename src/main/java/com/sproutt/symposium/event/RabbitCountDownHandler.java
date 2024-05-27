package com.sproutt.symposium.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.sproutt.symposium.CountDownService;
import com.sproutt.symposium.repository.CountDownMessageRecords;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RabbitCountDownHandler {

	private final Logger log = LoggerFactory.getLogger(RabbitCountDownHandler.class);
	private final CountDownService countDownService;
	private final CountDownMessageRecords countDownMessageRecords;
	private final ObjectMapper objectMapper;

	public RabbitCountDownHandler(
			CountDownService countDownService,
			CountDownMessageRecords countDownMessageRecords,
			ObjectMapper objectMapper) {
		this.countDownService = countDownService;
		this.countDownMessageRecords = countDownMessageRecords;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = "queue.waiting.consumer", ackMode = "MANUAL", concurrency = "3")
	public void onMessage(Message message, @Nullable Channel channel) throws IOException {
		byte[] body = message.getBody();
		CountDownMessage countDownMessage = objectMapper.readValue(body, CountDownMessage.class);
		Thread currentThread = Thread.currentThread();
		log.info("[{}]Received message: {}", currentThread, countDownMessage);

		try {
			countDownService.countDown(countDownMessage.getId().toString());

			countDownMessage.onDone();
			countDownMessageRecords.update(countDownMessage);

			log.info("[{}] Success Handle message : {}", currentThread.getName(), countDownMessage);
		} catch (Exception e) {
			countDownMessage.onFailed();
			countDownMessageRecords.update(countDownMessage);

			log.warn("[{}] Fail Handle message : {}", currentThread.getName(), countDownMessage);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}
}
