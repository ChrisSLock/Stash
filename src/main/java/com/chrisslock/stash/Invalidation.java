//Copyright 2017 Chris Lock.  All Rights Reserved.
package com.chrisslock.stash;

import java.util.UUID;

/**
 * Describes attributes of a specific Invalidation
 * @author chrislock
 */
public class Invalidation<K> {
	private final String id;
	private final String context;
	private final K cacheKey;
	private final boolean allowRefresh;
	
	private Invalidation(String id, String context, K cacheKey, boolean allowRefresh) {
		this.id = id;
		this.context = context;
		this.cacheKey = cacheKey;
		this.allowRefresh = allowRefresh;
	}

	public String getId() {
		return id;
	}

	public String getContext() {
		return context;
	}

	public K getCacheKey() {
		return cacheKey;
	}

	public boolean isAllowRefresh() {
		return allowRefresh;
	}
	
	public static class  InvalidationBuilder<K> {
		private String id;
		private String context;
		private K cacheKey;
		private boolean allowRefresh;
		
		public InvalidationBuilder() {}
		
		public InvalidationBuilder<K> buildUpon(Invalidation<K> invalidation) {
			this.id = invalidation.id;
			this.context = invalidation.context;
			this.cacheKey = invalidation.cacheKey;
			this.allowRefresh = invalidation.allowRefresh;
			return this;
		}
		
		public InvalidationBuilder<K> setContext(String context) {
			this.context = context;
			return this;
		}

		public InvalidationBuilder<K> setCacheKey(K cacheKey) {
			this.cacheKey = cacheKey;
			return this;
		}

		public InvalidationBuilder<K> setAllowRefresh(boolean allowRefresh) {
			this.allowRefresh = allowRefresh;
			return this;
		}
		
		public Invalidation<K> build() {
			return new Invalidation<K>(id == null ? UUID.randomUUID().toString() : id, context, cacheKey, allowRefresh);
		}
	}
}

