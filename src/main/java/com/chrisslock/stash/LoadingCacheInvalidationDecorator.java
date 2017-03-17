//Copyright 2017 Chris Lock.  All Rights Reserved.
package com.chrisslock.stash;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;

/**
 * A cache decorator for  a Guava LoadingCache that supports an external invalidation mechanism, allowing
 * invalidations to be propagated to other instances of caches.
 * @author chrislock
 */
public class LoadingCacheInvalidationDecorator<K, V> extends CacheInvalidationDecorator<K, V> implements LoadingCache<K, V> {
	protected final LoadingCache<K, V> loadingCache;
	
	public LoadingCacheInvalidationDecorator(LoadingCache<K, V> cache, Invalidator<K> invalidator) {
		super(cache, invalidator);
		this.loadingCache = cache;
	}

	@Override
	@Deprecated
	public V apply(K arg0) {
		return loadingCache.apply(arg0);
	}

	@Override
	public V get(K arg0) throws ExecutionException {
		return loadingCache.get(arg0);
	}

	@Override
	public ImmutableMap<K, V> getAll(Iterable<? extends K> arg0) throws ExecutionException {
		return loadingCache.getAll(arg0);
	}

	@Override
	public V getUnchecked(K arg0) {
		return loadingCache.getUnchecked(arg0);
	}

	@Override
	public void refresh(K arg0) {
		loadingCache.refresh(arg0);
	}
}
