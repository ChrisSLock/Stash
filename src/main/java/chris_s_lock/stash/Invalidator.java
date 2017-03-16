//Copyright 2017 Chris Lock.  All Rights Reserved.
package chris_s_lock.stash;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

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
	private final String invalidationContext;
	private final List<InvalidationCallback<K>> callbacks;
	private final Cache<String, Invalidation<K>> dedupCache;
	private final ExecutorService threadPool; 

	public Invalidator(String invalidationContext) {
		this.invalidationContext = invalidationContext;
		this.callbacks = new ArrayList<InvalidationCallback<K>>();
		this.threadPool = Executors.newCachedThreadPool();
		this.dedupCache = CacheBuilder.newBuilder()
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.build();
	}
	
	public abstract void invalidate(Invalidation<K> invalidation);
	
	public final String getInvalidationContext() {
		return invalidationContext;
	}
	
	public void invalidationRequired(Invalidation<K> invalidation) {
		List<InvalidationCallback<K>> copy = new ArrayList<InvalidationCallback<K>>(callbacks);
		for (InvalidationCallback<K> callback : copy) {
			threadPool.submit(new Runnable() {
				@Override
				public void run() {
					doCallback(callback, invalidation);
				}
			});
		}
	}

	public void addListener(InvalidationCallback<K> callback) {
		callbacks.add(callback);
	}

	public void removeListener(InvalidationCallback<K> callback) {
		callbacks.remove(callback);
	}
	
	private void doCallback(InvalidationCallback<K> callback, Invalidation<K> invalidation) {
		Invalidation<K> cachedInvalidation = dedupCache.getIfPresent(invalidation.getId());
		if (cachedInvalidation == null) {
			callback.onInvalidate(invalidation.getCacheKey());
			dedupCache.put(invalidation.getId(), invalidation);
		}
	}
}
