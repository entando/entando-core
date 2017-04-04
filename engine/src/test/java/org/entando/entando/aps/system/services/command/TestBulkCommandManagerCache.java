package org.entando.entando.aps.system.services.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.entando.entando.aps.system.services.command.util.BulkCommandContainer;

import junit.framework.TestCase;

public class TestBulkCommandManagerCache extends TestCase /* BaseTestCase*/ {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testClean() {
		for (int i = 0; i < 10; i++) {
			String name = "test" + i;
			FakeBulkCommand command = new FakeBulkCommand(new ArrayList<String>(), null, null);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -1*((i+1)*10));
			command.setFakeEndingTime(cal.getTime());
			_bulkCommandManager.addCommand(name, command);
		}
		
		Map<String, Map<String, BulkCommandContainer>> map =((BulkCommandManager)_bulkCommandManager).getCommands();
		assertEquals(10, map.size());

		((BulkCommandManager)_bulkCommandManager).cleanCache();
		map =((BulkCommandManager)_bulkCommandManager).getCommands();
		
		assertEquals(5, map.size());
	}

	private IBulkCommandManager _bulkCommandManager = new BulkCommandManager();

}
