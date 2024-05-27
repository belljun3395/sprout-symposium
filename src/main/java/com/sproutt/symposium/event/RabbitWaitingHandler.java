package com.sproutt.symposium.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.sproutt.symposium.CountDownService;
import com.sproutt.symposium.repository.CountDownMessageRecords;
import java.io.IOException;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class RabbitWaitingHandler {

	private final Logger log = LoggerFactory.getLogger(RabbitWaitingHandler.class);
	private final CountDownService countDownService;
	private final CountDownMessageRecords countDownMessageRecords;
	private final RabbitTemplate messageTemplate;
	private final ObjectMapper objectMapper;

	public RabbitWaitingHandler(
			CountDownService countDownService,
			CountDownMessageRecords countDownMessageRecords,
			RabbitTemplate messageTemplate,
			ObjectMapper objectMapper) {
		this.countDownService = countDownService;
		this.countDownMessageRecords = countDownMessageRecords;
		this.messageTemplate = messageTemplate;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = "queue.waiting", concurrency = "1", ackMode = "MANUAL")
	public void onMessage(Message message, @Nullable Channel channel) throws IOException {
		try {
			byte[] body = message.getBody();
			CountDownMessage countDownMessage = objectMapper.readValue(body, CountDownMessage.class);
			int countDown = countDownService.getCountDown();
			if (countDown > 0) {
				long leftCount = countDownMessageRecords.leftCount(countDownMessage.getId());
				countDownMessage.onProcessed();
				countDownMessage.setLeftCount(leftCount);
				log.info("Pass message to CountDownService: {}", countDownMessage);
			} else {
				StringFormattedMessage formattedMessage =
						new StringFormattedMessage("CountDown is already 0, %s", countDownMessage.getId());
				throw new RuntimeException(formattedMessage.getFormattedMessage());
			}
			messageTemplate.convertAndSend("direct.waiting.consumer", "direct.waiting.consumer", message);
		} finally {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}
}
