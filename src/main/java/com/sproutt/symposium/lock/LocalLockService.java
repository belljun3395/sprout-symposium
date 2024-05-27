package com.sproutt.symposium.lock;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
public class LocalLockService implements LockService {

	private final Map<Long, Pair<Long, Boolean>> locks = new ConcurrentHashMap<>();
	private final Queue<Long> waitingQueue = new ConcurrentLinkedQueue<>();

	@Override
	public boolean lock(Long id, Long userId) {
		// id에 대한 lock이 이미 사용 중인 경우
		if (locks.containsKey(id)) {
			// waitingQueue에 userId가 없는 경우 추가
			if (!waitingQueue.contains(userId)) {
				waitingQueue.add(userId);
			}

			// id에 대한 lock이 사용 중인 경우
			if (locks.get(id).getRight()) {
				return false;
			} else {
				// id에 대한 lock이 사용 중이지 않은 경우
				Long peek = waitingQueue.peek();
				// waitingQueue의 첫 번째 요소가 userId인 경우
				if (peek != null && peek.equals(userId)) {
					locks.put(id, Pair.of(userId, true));
					waitingQueue.poll();
					return true;
				} else {
					// waitingQueue의 첫 번째 요소가 userId가 아닌 경우
					return false;
				}
			}
		}

		// id에 대한 lock이 없거나 사용 중이 아닌 경우
		// userId가 사용 중 임을 표시
		locks.put(id, Pair.of(userId, true));
		return true;
	}

	@Override
	public void unlock(Long id, Long userId) {
		if (locks.containsKey(id) && locks.get(id).getLeft().equals(userId)) {
			locks.put(id, Pair.of(userId, false));
		}
	}
}
