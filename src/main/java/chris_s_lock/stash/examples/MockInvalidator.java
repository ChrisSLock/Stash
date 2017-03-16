//Copyright 2017 Chris Lock.  All Rights Reserved.
package chris_s_lock.stash.examples;

import chris_s_lock.stash.Invalidation;
import chris_s_lock.stash.Invalidator;

/**
 * An example implementation of an Invalidator.
 * 
 * This version simply invokes the invalidationRequired() method directly, but
 * distributed version would use some other event source 
 * @author chrislock
 */
public class MockInvalidator<K> extends Invalidator<K>{
	public MockInvalidator() {
		super(MockInvalidator.class.getSimpleName());
	}

	@Override
	public void invalidate(Invalidation<K> invalidation) {
		invalidationRequired(invalidation);
	}	
}
