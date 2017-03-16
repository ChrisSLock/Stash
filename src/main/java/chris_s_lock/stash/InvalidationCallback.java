//Copyright 2017 Chris Lock.  All Rights Reserved.
package chris_s_lock.stash;

/**
 * A base class for an Invalidator callback.
 * @author chrislock
 */
public abstract class InvalidationCallback<K> {
	/**
	 * Called when the invalidator receives a call to invalidate().  Implementors
	 * should take any action needed to locally invalidate the record represented 
	 * in the message.
	 */
	public abstract void onInvalidate(K message);
}
