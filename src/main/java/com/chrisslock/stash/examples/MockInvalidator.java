//Copyright 2017 Chris Lock.  All Rights Reserved.
package com.chrisslock.stash.examples;

import com.chrisslock.stash.Invalidation;
import com.chrisslock.stash.Invalidator;

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
