package com.sproutt.symposium.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class _MyMessageQ<T extends MyMessage> {

	private final Queue<T> queue = new ConcurrentLinkedQueue<>();

	public void add(T message) {
		queue.add(message);
	}

	public T poll() {
		return queue.poll();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public int size() {
		return queue.size();
	}

	protected Queue<T> getQueue() {
		return queue;
	}
}
