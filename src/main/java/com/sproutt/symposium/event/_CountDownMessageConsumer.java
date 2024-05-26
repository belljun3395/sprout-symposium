package com.sproutt.symposium.event;

import com.sproutt.symposium.queue.MyMessageHandler;
import com.sproutt.symposium.queue._MyMessageConsumer;
import com.sproutt.symposium.queue._MyMessageQ;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class _CountDownMessageConsumer extends _MyMessageConsumer<CountDownMessage> {

	public _CountDownMessageConsumer(
			List<MyMessageHandler<CountDownMessage>> myMessageHandlers,
			_MyMessageQ<CountDownMessage> myMessageQ) {
		super(myMessageHandlers, myMessageQ);
	}

	@Override
	@Scheduled(fixedRate = 1000)
	public void consume() {
		_CountDownMessageQ myMessageQ = (_CountDownMessageQ) getMyMessageQ();
		if (myMessageQ == null) {
			return;
		}

		while (!myMessageQ.isEmpty()) {
			process();
			while (!myMessageQ.isAck()) {}
		}
	}
}
