package com.sproutt.symposium.repository;

import com.sproutt.symposium.event.CountDownMessage;
import com.sproutt.symposium.event.CountDownMessageState;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CountDownMessageRecords {

	private final Logger log = LoggerFactory.getLogger(CountDownMessageRecords.class);

	private final Map<Long, CountDownMessage> records = new ConcurrentHashMap<>();

	public void save(CountDownMessage message) {
		records.put(message.getId(), message);
	}

	public void update(CountDownMessage message) {
		records.put(message.getId(), message);
	}

	public long leftCount(Long id) {
		Set<Long> keys = records.keySet().stream().filter(key -> key < id).collect(Collectors.toSet());
		long count = 0;
		for (Long key : keys) {
			CountDownMessage message = records.get(key);
			if (message.getState() == CountDownMessageState.PROCESSED) {
				count++;
			}
		}
		return count;
	}
}
