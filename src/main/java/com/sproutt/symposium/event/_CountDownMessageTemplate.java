package com.sproutt.symposium.event;

import com.sproutt.symposium.queue.MyMessageTemplate;
import org.springframework.stereotype.Component;

@Component
public class _CountDownMessageTemplate extends MyMessageTemplate<CountDownMessage> {

	public _CountDownMessageTemplate(_CountDownMessageQ messageQ) {
		super(messageQ);
	}

	public boolean ack() {
		return ((_CountDownMessageQ) getMessage()).ack();
	}
}
