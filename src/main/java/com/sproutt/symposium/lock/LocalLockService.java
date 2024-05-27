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
		// lockId 대한 락 정보가 있는 경우
		if (locks.containsKey(lockId)) {
			// waitingQueue에 userId가 없는 경우
			if (!waitingQueue.contains(userId)) {
				waitingQueue.add(userId);
			}

			Pair<Long, Boolean> lock = locks.get(lockId);
			Boolean isLocked = lock.getRight();
			// lockId에 대한 락이 사용 중이 아닌 경우
			if (!isLocked) {
				Long peek = waitingQueue.peek();

				// waitingQueue의 첫 번째 요소가 userId인 경우
				if (peek != null && peek.equals(userId)) {
					locks.put(lockId, Pair.of(userId, true));
					waitingQueue.poll();
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}

		// userId에게 락 정보가 없는 경우
		locks.put(lockId, Pair.of(userId, true));
		return true;
	}

	@Override
	public void unlock(Long id, Long userId) {
		if (locks.containsKey(id) && locks.get(id).getLeft().equals(userId)) {
			locks.put(id, Pair.of(userId, false));
		}
	}
}
