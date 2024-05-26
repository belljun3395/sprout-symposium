package com.sproutt.symposium.event;

import com.sproutt.symposium.CountDownService;
import com.sproutt.symposium.queue.MyMessageHandler;
import com.sproutt.symposium.repository.CountDownMessageRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CountDownHandler implements MyMessageHandler<CountDownMessage> {

	private final Logger log = LoggerFactory.getLogger(CountDownHandler.class);
	private final _CountDownMessageTemplate countDownMessageTemplate;
	private final CountDownService countDownService;
	private final CountDownMessageRecords countDownMessageRecords;

	public CountDownHandler(
			_CountDownMessageTemplate countDownMessageTemplate,
			CountDownService countDownService,
			CountDownMessageRecords countDownMessageRecords) {
		this.countDownMessageTemplate = countDownMessageTemplate;
		this.countDownService = countDownService;
		this.countDownMessageRecords = countDownMessageRecords;
	}

	@Override
	public void onMessage(CountDownMessage message) {
		log.info("Handling {} User message {}", message.getId(), message.getMessage());
		try {
			countDownService.countDown(message.getId().toString());

			message.onDone();
			countDownMessageRecords.update(message);

			log.info(">>> Success Handle message : {}", message);
		} catch (Exception e) {
			message.onFailed();
			countDownMessageRecords.update(message);

			log.warn(">>> Fail Handle message : {}", message);
		} finally {
			countDownMessageTemplate.ack();
		}
	}
}
