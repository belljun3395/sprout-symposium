package com.sproutt.symposium.event;

import com.sproutt.symposium.queue._MyMessageQ;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class _CountDownMessageQ extends _MyMessageQ<CountDownMessage> {

	private final Logger log = LoggerFactory.getLogger(_CountDownMessageQ.class);
	private final AtomicBoolean isStart = new AtomicBoolean(true);
	private final AtomicBoolean isAck = new AtomicBoolean(false);

	@Override
	public CountDownMessage poll() {
		if (isStart.get()) {
			isStart.set(false);
			isAck.set(true);
			log.info("CountDownMessageQ is started.");
			return null;
		}
		if (!isAck.get()) {
			log.info("CountDownMessageQ is not ack.");
			return null;
		}
		CountDownMessage message = getQueue().poll();
		isAck.set(false);
		return message;
	}

	public boolean isAck() {
		return isAck.get();
	}

	public boolean ack() {
		isAck.set(true);
		isStart.set(true);
		return isAck.get();
	}
}
