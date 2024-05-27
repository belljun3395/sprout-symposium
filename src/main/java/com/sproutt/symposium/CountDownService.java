package com.sproutt.symposium;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CountDownService {

	private final Logger log = LoggerFactory.getLogger(CountDownService.class);
	private AtomicInteger countDown = new AtomicInteger(1000);

	public void countDown(String message) {
		if (countDown.getAndDecrement() > 0) {
			log.info("Counting down to {} by UserId {}", countDown, message);
		} else {
			StringFormattedMessage formattedMessage =
					new StringFormattedMessage("CountDown is already 0, %s", message);
			throw new RuntimeException(formattedMessage.getFormattedMessage());
		}
	}
}
