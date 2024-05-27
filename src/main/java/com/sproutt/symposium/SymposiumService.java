package com.sproutt.symposium;

import com.sproutt.symposium.lock.LocalLockService;
import com.sproutt.symposium.lock.LockService;
import com.sproutt.symposium.lock.RedisLockService;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SymposiumService {

	private final Logger log = LoggerFactory.getLogger(SymposiumService.class);
	private final LockService lockService;
	private final CountDownService countDownService;
	private AtomicLong atomicUserId = new AtomicLong(0L);

	public SymposiumService(RedisLockService lockService, CountDownService countDownService) {
		this.lockService = lockService;
		this.countDownService = countDownService;
	}

	public String execute() {
		Long lockId = 1L;
		Long userId = atomicUserId.incrementAndGet();
		LocalDateTime now = LocalDateTime.now();
		log.info("[{}] User {} is trying to execute", now, userId);

		while (!lockService.lock(lockId, userId)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			countDownService.countDown(userId + " is executing");
		} finally {
			lockService.unlock(lockId, userId);
		}

		return "[" + now + "] User " + userId + " executed successfully";
	}
}
