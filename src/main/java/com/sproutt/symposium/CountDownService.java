package com.sproutt.symposium;

import java.util.concurrent.atomic.AtomicInteger;
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
			log.error("CountDown is already 0, {}", message);
			throw new RuntimeException("CountDown is already 0");
		}
	}

	public int getCountDown() {
		return countDown.get();
	}
}
