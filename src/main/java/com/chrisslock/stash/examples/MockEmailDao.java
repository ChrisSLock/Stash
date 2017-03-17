//Copyright 2017 Chris Lock.  All Rights Reserved.
package com.chrisslock.stash.examples;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.chrisslock.stash.Invalidator;
import com.chrisslock.stash.LoadingCacheInvalidationDecorator;

/**
 * An Example dao, showing how to use the Invalidation Decorators.
 * 
 * Below is a dao that provides CRUD methods for a fictional email database.  
 * It uses an internal Guava LoadingCache to behave as a local write-through cache.
 * 
 * Any save() or delete() will invalidate any cached value for the user, both in this
 * instance, and any other instance of this dao which shares an Invalidator.
 * @author chrislock
 */
public class MockEmailDao {
	private final MockEmailDatasource datasource;
	private final LoadingCache<String, String> cache;
	
	/**
	 * Create an instance of this dao using the specified email datasource and invalidator
	 */
	public MockEmailDao(MockEmailDatasource datasource, final Invalidator<String> invalidator) {
		this.datasource = datasource;
		this.cache = new LoadingCacheInvalidationDecorator<String, String>(
				CacheBuilder.newBuilder()
					.expireAfterWrite(1, TimeUnit.MINUTES)
					.build(
						new CacheLoader<String, String>() {
							@Override
							public String load(String key) throws Exception {
								String obj = datasource.data().get(key);
								if (obj == null) throw new NoSuchObjectException();
								return obj;
							}
						}), invalidator);
	}

	/**
	 * Loads an email from the database, caching the result.
	 */
	public String get(String user) {
		try {
			return cache.get(user);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof NoSuchObjectException) return null;
			throw new RuntimeException("Error reading value for " + user, e.getCause());
		}
	}
	
	/**
	 * Saves an email to the database
	 */
	public void save(String user, String email) {
		datasource.data().put(user, email);
		cache.invalidate(user);
	}
	
	/**
	 * Deletes an email from the database
	 */
	public void delete(String user) {
		datasource.data().remove(user);
		cache.invalidate(user);
	}
	
	@SuppressWarnings("serial")
	private static final class NoSuchObjectException extends Exception {}
}
