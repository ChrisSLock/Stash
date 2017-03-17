//Copyright 2017 Chris Lock.  All Rights Reserved.
package com.chrisslock.stash;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;

/**
 * A base class for Cache Decorators
 * @author chrislock
 */
public abstract class CacheDecorator<K, V> implements Cache<K, V> {
	protected final Cache<K, V> cache;

	public CacheDecorator(Cache<K, V> cache) {
		this.cache = cache;
	}

	@Override
	public ConcurrentMap<K, V> asMap() {
		return cache.asMap();
	}

	@Override
	public void cleanUp() {
		cache.cleanUp();
	}

	@Override
	public V get(K arg0, Callable<? extends V> arg1) throws ExecutionException {
		return cache.get(arg0, arg1);
	}

	@Override
	public ImmutableMap<K, V> getAllPresent(Iterable<?> arg0) {
		return cache.getAllPresent(arg0);
	}

	@Override
	public V getIfPresent(Object arg0) {
		return cache.getIfPresent(arg0);
	}

	@Override
	public void invalidate(Object arg0) {
		cache.invalidate(arg0);
	}

	@Override
	public void invalidateAll() {
		cache.invalidateAll();
	}

	@Override
	public void invalidateAll(Iterable<?> arg0) {
		cache.invalidateAll(arg0);
	}

	@Override
	public void put(K arg0, V arg1) {
		cache.put(arg0, arg1);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> arg0) {
		cache.putAll(arg0);
	}

	@Override
	public long size() {
		return cache.size();
	}

	@Override
	public CacheStats stats() {
		return cache.stats();
	}
}
