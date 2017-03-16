//Copyright 2017 Chris Lock.  All Rights Reserved.
package chris_s_lock.stash;

import org.junit.Test;

import static org.junit.Assert.*;
import chris_s_lock.stash.examples.MockEmailDao;
import chris_s_lock.stash.examples.MockEmailDatasource;
import chris_s_lock.stash.examples.MockInvalidator;

public class InvalidatingCacheTest {
	@Test
	public void testInvalidatingCache() throws InterruptedException {
		//a shared invalidator, a pub/sub mechamism or service, such as pubnub
		Invalidator<String> invalidator = new MockInvalidator<String>();
		
		/*
		 * To test with pub nub, and true distributed invalidation, comment out the  MockInvalidator,
		 * uncomment the line below.
		 * 
		 * You will need to get a publish key and subscribed key from pubnub.com.  It's free.
		 */
		//Invalidator<String> invalidator = new PubNubInvalidator<String>("YOUR_PUBLISH_KEY", "YOUR_SUBSCRIBE_KEY");
		
		//a shared datasource, likely a remote database or data service
		MockEmailDatasource datasource = new MockEmailDatasource();
		
		//first instance of the dao accessing the datasource
		MockEmailDao dao1 = new MockEmailDao(datasource, invalidator);
		//a second instance the dao, sharing the same datasource but on another app server 
		MockEmailDao dao2 = new MockEmailDao(datasource, invalidator);
		
		//fetch a record through the dao, which will return and cache the result
		String harrysEmail = dao1.get("Harry");
		String debbiesEmail = dao1.get("Debbie");
		String sallysEmail = dao1.get("Sally");
		
		assertTrue("harry@gmail.com".equals(harrysEmail));
		assertTrue("sally@gmail.com".equals(sallysEmail));
		assertTrue("debbie@gmail.com".equals(debbiesEmail));
		
		//the second app server receives a requests to change records
		dao2.delete("Harry");
		dao2.save("Sally", "sally@facebook.com");
		
		//sleep a tick to wait for eventual consistency
		Thread.sleep(5000);
		
		//the first app server will have had its cache invalidated, so it now reflects the correct state
		harrysEmail = dao1.get("Harry");
		debbiesEmail = dao1.get("Debbie");
		sallysEmail = dao1.get("Sally");
		
		assertNull(harrysEmail);
		assertTrue("sally@facebook.com".equals(sallysEmail));
		assertTrue("debbie@gmail.com".equals(debbiesEmail));
	}
}
