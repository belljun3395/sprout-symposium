package com.sproutt.symposium.queue;

public abstract class MyMessage {

	private final String message;

	public MyMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
