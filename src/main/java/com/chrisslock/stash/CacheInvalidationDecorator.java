//Copyright 2017 Chris Lock.  All Rights Reserved.
package com.chrisslock.stash;

import com.google.common.cache.Cache;

/**
 * A cache decorator for  a Guava Cache that supports an external invalidation mechanism, allowing
 * invalidations to be propagated to other instances of caches.
 * @author chrislock
 */
public class CacheInvalidationDecorator<K, V> extends CacheDecorator<K, V> {
	private final Invalidator<K> invalidator;
	private final InvalidationCallback<K> callback;
	
	public CacheInvalidationDecorator(Cache<K, V> cache, Invalidator<K> invalidator) {
		super(cache);
		this.invalidator = invalidator;
		this.callback = new InvalidationCallback<K>() {
			@Override
			public void onInvalidate(K key) {
				doInvalidate(key);
			}
		};
		this.invalidator.addListener(callback);
	}
	
	public void close() {
		this.invalidator.removeListener(callback);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void invalidate(Object key) {
		doInvalidate(key);  
		Invalidation<K> invalidation = new Invalidation.InvalidationBuilder<K>()
			.setCacheKey((K)key)
			.setContext(invalidator.getInvalidationContext())
			.build();
		invalidator.invalidate(invalidation);
	}
	
	private void doInvalidate(Object key) {
		super.invalidate(key);
	}
}
