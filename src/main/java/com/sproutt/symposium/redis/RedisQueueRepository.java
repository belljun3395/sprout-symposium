package com.sproutt.symposium.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisQueueRepository {

	/**
	 * key: queue<br>
	 * value: userId
	 */
	private final RedisTemplate<String, String> redisTemplate;

	public RedisQueueRepository(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void add(String userId) {
		redisTemplate.opsForList().rightPush("queue", userId);
	}

	public String peek() {
		return redisTemplate.opsForList().index("queue", 0);
	}

	public String poll() {
		return redisTemplate.opsForList().leftPop("queue");
	}

	public boolean isContain(String userId) {
		return redisTemplate.opsForList().range("queue", 0, -1).contains(userId);
	}
}
