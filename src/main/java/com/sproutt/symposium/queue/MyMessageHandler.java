package com.sproutt.symposium.queue;

public interface MyMessageHandler<T extends MyMessage> {

	void onMessage(T message);
}
