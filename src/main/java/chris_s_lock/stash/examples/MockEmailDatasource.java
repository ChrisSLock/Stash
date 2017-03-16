//Copyright 2017 Chris Lock.  All Rights Reserved.
package chris_s_lock.stash.examples;

import java.util.HashMap;
import java.util.Map;

/**
 * An example mock database of user emails.
 * @author chrislock
 */
public class MockEmailDatasource {
	private static final Map<String, String> datasource;
	
	static {
		datasource = new HashMap<String, String>();
		datasource.put("Harry", "harry@gmail.com");
		datasource.put("Sally", "sally@gmail.com");
		datasource.put("Debbie", "debbie@gmail.com");
	}
	
	public Map<String, String> data() {
		return datasource;
	}
}
