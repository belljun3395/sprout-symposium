package com.sproutt.symposium.lock;

public interface LockService {

	boolean lock(Long id, Long userId);

	void unlock(Long id, Long userId);
}
