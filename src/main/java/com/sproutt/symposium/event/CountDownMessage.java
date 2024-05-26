package com.sproutt.symposium.event;

import com.sproutt.symposium.queue.MyMessage;

public class CountDownMessage extends MyMessage {

	private static final Long NOT_SET_LEFT_COUNT = -1L;

	private final Long id;

	private CountDownMessageState state;

	private Long leftCount;

	public CountDownMessage(String message, Long id) {
		super(message);
		this.id = id;
		this.state = CountDownMessageState.NEW;
		this.leftCount = NOT_SET_LEFT_COUNT;
	}

	public void onProcessed() {
		this.state = CountDownMessageState.PROCESSED;
	}

	public void setLeftCount(long leftCount) {
		this.leftCount = leftCount;
	}

	public void onDone() {
		this.state = CountDownMessageState.DONE;
		this.leftCount = 0L;
	}

	public void onFailed() {
		this.state = CountDownMessageState.FAILED;
	}

	public Long getId() {
		return id;
	}

	public Long getLeftCount() {
		return leftCount;
	}

	public CountDownMessageState getState() {
		return state;
	}

	@Override
	public String toString() {
		return "{"
				+ "\"id\" : "
				+ getId()
				+ ","
				+ "\"message\" : \""
				+ getMessage()
				+ "\","
				+ "\"state\" : \""
				+ getState()
				+ "\""
				+ ","
				+ "\"leftCount\" : "
				+ getLeftCount()
				+ "}";
	}
}
