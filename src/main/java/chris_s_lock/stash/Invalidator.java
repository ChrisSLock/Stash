//Copyright 2017 Chris Lock.  All Rights Reserved.
package chris_s_lock.stash;

import java.util.ArrayList;
import java.util.List;

/**
 * A base class for Invalidators.  Provides an interface through which subscribers can listen
 * for invalidation events. Used by CacheInvalidationDecorator and LoadingCacheInvalidationDecorator
 * for invalidation.
 * 
 * hint: Implement around a pub/sub service such as PubNub to have invalidation for as distributed 
 * application. 
 * @author chrislock
 */
public abstract class Invalidator<K> {
	private List<InvalidationCallback<K>> callbacks = new ArrayList<InvalidationCallback<K>>();

	public abstract void invalidate(K key);
	
	public void invalidationRequired(K key) {
		List<InvalidationCallback<K>> copy = new ArrayList<InvalidationCallback<K>>(callbacks);
		copy.parallelStream().forEach(callback -> callback.onInvalidate(key));
	}

	public void addListener(InvalidationCallback<K> callback) {
		callbacks.add(callback);
	}

	public void removeListener(InvalidationCallback<K> callback) {
		callbacks.remove(callback);
	}
}
