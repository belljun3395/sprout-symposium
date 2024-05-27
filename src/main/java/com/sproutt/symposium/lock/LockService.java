package com.sproutt.symposium.lock;

public interface LockService {

	/**
	 * lockId에 대한 lock을 userId에게 할당한다.
	 *
	 * @return lock 할당 성공 여부
	 */
	boolean lock(Long id, Long userId);

	/** lockId에 대한 lock을 해제한다. */
	void unlock(Long id, Long userId);
}
