package com.sproutt.symposium.lock;

import com.sproutt.symposium.redis.RedisLockRepository;
import com.sproutt.symposium.redis.RedisQueueRepository;
import org.springframework.stereotype.Service;

@Service
public class RedisLockService implements LockService {

	private final RedisLockRepository redisLockRepository;

	private final RedisQueueRepository redisQueueRepository;

	public RedisLockService(
			RedisLockRepository redisLockRepository, RedisQueueRepository redisQueueRepository) {
		this.redisLockRepository = redisLockRepository;
		this.redisQueueRepository = redisQueueRepository;
	}

	@Override
	public boolean lock(Long lockId, Long userId) {
		if (!redisQueueRepository.isContain(userId.toString())) {
			redisQueueRepository.add(userId.toString());
		}

		String peek = redisQueueRepository.peek();
		if (peek != null && peek.equals(userId.toString())) {
			redisLockRepository.lock(lockId);
			redisQueueRepository.poll();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void unlock(Long id, Long userId) {
		redisLockRepository.unlock(id);
	}
}
