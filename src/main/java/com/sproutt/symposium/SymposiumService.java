package com.sproutt.symposium;

import com.sproutt.symposium.event.CountDownMessage;
import com.sproutt.symposium.event._CountDownMessageTemplate;
import com.sproutt.symposium.repository.CountDownMessageRecords;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SymposiumService {

	private final Logger log = LoggerFactory.getLogger(SymposiumService.class);
	private final _CountDownMessageTemplate messageTemplate;
	private final CountDownMessageRecords messageRepo;

	private AtomicLong atomicUserId = new AtomicLong(0L);

	public SymposiumService(
			_CountDownMessageTemplate messageTemplate, CountDownMessageRecords messageRepo) {
		this.messageTemplate = messageTemplate;
		this.messageRepo = messageRepo;
	}

	public String execute() {
		Long userId = atomicUserId.getAndIncrement();
		LocalDateTime now = LocalDateTime.now();
		log.info("[{}] User {} is trying to execute", now, userId);

		CountDownMessage message = new CountDownMessage("Counting down for user " + userId, userId);
		messageTemplate.send(message);

		message.onProcessed();
		messageRepo.save(message);

		long leftCount = messageRepo.leftCount(message.getId());
		message.setLeftCount(leftCount);

		return message.toString();
	}
}
