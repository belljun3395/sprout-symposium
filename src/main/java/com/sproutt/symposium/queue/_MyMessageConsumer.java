package com.sproutt.symposium.queue;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class _MyMessageConsumer<T extends MyMessage> {

	private final Logger log = LoggerFactory.getLogger(_MyMessageConsumer.class);
	private final List<MyMessageHandler<T>> myMessageHandlers;
	private final _MyMessageQ<T> myMessageQ;

	public _MyMessageConsumer(
			List<MyMessageHandler<T>> myMessageHandlers, _MyMessageQ<T> myMessageQ) {
		this.myMessageHandlers = myMessageHandlers;
		this.myMessageQ = myMessageQ;
	}

	public void consume() {
		if (myMessageQ == null) {
			return;
		}

		while (!myMessageQ.isEmpty()) {
			process();
		}
	}

	protected void process() {
		T message = myMessageQ.poll();
		if (message != null) {
			handleMessage(message);
		}
	}

	private void handleMessage(T message) {
		myMessageHandlers.forEach(handler -> handler.onMessage(message));
	}

	public List<MyMessageHandler<T>> getMyMessageHandlers() {
		return myMessageHandlers;
	}

	public _MyMessageQ<T> getMyMessageQ() {
		return myMessageQ;
	}
}
