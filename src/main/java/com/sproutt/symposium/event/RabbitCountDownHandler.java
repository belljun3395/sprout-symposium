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

	@RabbitListener(queues = "queue.waiting", concurrency = "1", ackMode = "MANUAL")
	public void onMessage(Message message, @Nullable Channel channel) throws IOException {
		byte[] body = message.getBody();
		CountDownMessage countDownMessage = objectMapper.readValue(body, CountDownMessage.class);
		log.info("Received message: {}", countDownMessage);

		try {
			countDownService.countDown(countDownMessage.getId().toString());

			countDownMessage.onDone();
			countDownMessageRecords.update(countDownMessage);

			log.info(">>> Success Handle message : {}", countDownMessage);
		} catch (Exception e) {
			countDownMessage.onFailed();
			countDownMessageRecords.update(countDownMessage);

			log.warn(">>> Fail Handle message : {}", countDownMessage);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}
}
