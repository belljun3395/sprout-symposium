package com.sproutt.symposium.lock;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
public class LocalLockService implements LockService {

	/**
	 * key: lockId<br>
	 * value: Pair&lt;userId, isLocked&gt;<br>
	 */
	private final Map<Long, Pair<Long, Boolean>> locks = new ConcurrentHashMap<>();

	/** userId를 대기시키기 위한 Queue */
	private final Queue<Long> waitingQueue = new ConcurrentLinkedQueue<>();

	@Override
	public boolean lock(Long lockId, Long userId) {
		// waitingQueue에 userId가 존재하는지 확인
		if (!waitingQueue.contains(userId)) {
			waitingQueue.add(userId);
		}

		Long peek = waitingQueue.peek();
		// waitingQueue의 첫 번째 요소가 userId인지 확인
		if (peek != null && peek.equals(userId)) {
			locks.put(lockId, Pair.of(userId, true));
			waitingQueue.poll();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void unlock(Long id, Long userId) {
		if (locks.containsKey(id) && locks.get(id).getLeft().equals(userId)) {
			locks.put(id, Pair.of(userId, false));
		}
	}
}
