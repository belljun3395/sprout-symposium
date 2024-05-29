package com.sproutt.symposium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CountDownService {

	private final Logger log = LoggerFactory.getLogger(CountDownService.class);
	private Long countDown = 1000L;

	public void countDown(String message) {
		if (countDown > 0) {
			countDown--;
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
