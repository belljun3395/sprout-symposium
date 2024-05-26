package com.sproutt.symposium.queue;

public abstract class MyMessageTemplate<T extends MyMessage> {

	private final _MyMessageQ<T> messageQ;

	public MyMessageTemplate(_MyMessageQ<T> messageQ) {
		this.messageQ = messageQ;
	}

	public void send(T message) {
		messageQ.add(message);
	}

	public _MyMessageQ<T> getMessage() {
		return messageQ;
	}
}
