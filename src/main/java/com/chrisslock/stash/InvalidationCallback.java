//Copyright 2017 Chris Lock.  All Rights Reserved.
package com.chrisslock.stash;

/**
 * A base class for an Invalidator callback.
 * @author chrislock
 */
public interface InvalidationCallback<K> {
	/**
	 * Called when the invalidator receives a call to invalidate().  Implementors
	 * should take any action needed to locally invalidate the record represented 
	 * in the message.
	 */
	public abstract void onInvalidate(K message);
}
