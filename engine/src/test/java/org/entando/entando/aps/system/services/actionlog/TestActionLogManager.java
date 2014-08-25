/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.actionlog;

import java.util.List;

import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordSearchBean;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.DateConverter;

public class TestActionLogManager extends BaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this._helper.cleanRecords();
	}
	
	public void testGetActionRecords() throws Throwable {
		List<Integer> ids = this._actionLoggerManager.getActionRecords(null);
		this.compareIds(new Integer [] {}, ids);
		
		ActionLogRecord record1 = this._helper.createActionRecord(1, "username1", "actionName1", 
				"namespace1", DateConverter.parseDate("01/01/2009 00:00", "dd/MM/yyyy HH:mm"), "params1");
		ActionLogRecord record2 = this._helper.createActionRecord(2, "username2", "actionName2", 
				"namespace2", DateConverter.parseDate("01/01/2009 10:00", "dd/MM/yyyy HH:mm"), "params2");
		ActionLogRecord record3 = this._helper.createActionRecord(3, "username123", "actionName123", 
				"namespace123", DateConverter.parseDate("02/01/2009 12:00", "dd/MM/yyyy HH:mm"), "params123");
		this._helper.addActionRecord(record1);
		this._helper.addActionRecord(record2);
		this._helper.addActionRecord(record3);
		
		ids = this._actionLoggerManager.getActionRecords(null);
		this.compareIds(new Integer [] { 1, 2, 3 }, ids);
		
		ActionLogRecordSearchBean searchBean = this._helper.createSearchBean("name", "Name", "space", "arams", null, null);
		ids = this._actionLoggerManager.getActionRecords(searchBean);
		this.compareIds(new Integer [] { 1, 2, 3 }, ids);
		
		searchBean = this._helper.createSearchBean("name", "Name", "space", "arams", DateConverter.parseDate("01/01/2009 10:01", "dd/MM/yyyy HH:mm"), null);
		ids = this._actionLoggerManager.getActionRecords(searchBean);
		this.compareIds(new Integer [] { 3 }, ids);
		
		searchBean = this._helper.createSearchBean(null, null, null, null, null, DateConverter.parseDate("01/01/2009 10:01", "dd/MM/yyyy HH:mm"));
		ids = this._actionLoggerManager.getActionRecords(searchBean);
		this.compareIds(new Integer [] { 1, 2 }, ids);
		
		searchBean = this._helper.createSearchBean(null, "Name", null, null, DateConverter.parseDate("01/01/2009 09:01", "dd/MM/yyyy HH:mm"), 
				DateConverter.parseDate("01/01/2009 10:01", "dd/MM/yyyy HH:mm"));
		ids = this._actionLoggerManager.getActionRecords(searchBean);
		this.compareIds(new Integer [] { 2 }, ids);
		
	}
	
	public void testAddGetDeleteActionRecord() throws Throwable {
		ActionLogRecord record1 = this._helper.createActionRecord(0, "username1", "actionName1", "namespace1", null, "params1");
		ActionLogRecord record2 = this._helper.createActionRecord(0, "username2", "actionName2", "namespace2", null, "params2");
		
		this._actionLoggerManager.addActionRecord(record1);
		this._actionLoggerManager.addActionRecord(record2);
		super.waitThreads(IActionLogManager.LOG_APPENDER_THREAD_NAME_PREFIX);
		
		ActionLogRecord addedRecord1 = this._actionLoggerManager.getActionRecord(record1.getId());
		this.compareActionRecords(record1, addedRecord1);
		ActionLogRecord addedRecord2 = this._actionLoggerManager.getActionRecord(record2.getId());
		this.compareActionRecords(record2, addedRecord2);
		
		this._actionLoggerManager.deleteActionRecord(record1.getId());
		assertNull(this._actionLoggerManager.getActionRecord(record1.getId()));
		
		this._actionLoggerManager.deleteActionRecord(record2.getId());
		assertNull(this._actionLoggerManager.getActionRecord(record2.getId()));
	}
	
	private void compareIds(Integer[] expected, List<Integer> received) {
		assertEquals(expected.length, received.size());
		for (Integer id : expected) {
			if (!received.contains(id)) {
				fail("Id \"" + id + "\" not found");
			}
		}
	}
	
	private void compareActionRecords(ActionLogRecord expected, ActionLogRecord received) {
		assertEquals(expected.getId(), received.getId());
		assertEquals(expected.getUsername(), received.getUsername());
		assertEquals(expected.getActionName(), received.getActionName());
		assertEquals(expected.getNamespace(), received.getNamespace());
		assertEquals(expected.getParameters(), received.getParameters());
		assertEquals(DateConverter.getFormattedDate(expected.getActionDate(), "ddMMyyyyHHmm"), 
				DateConverter.getFormattedDate(received.getActionDate(), "ddMMyyyyHHmm"));
	}
	
	private void init() {
		this._actionLoggerManager = (IActionLogManager) this.getService(SystemConstants.ACTION_LOGGER_MANAGER);
		this._helper = new ActionLoggerTestHelper(this.getApplicationContext());
	}
	
	@Override
	protected void tearDown() throws Exception {
		this._helper.cleanRecords();
		super.tearDown();
	}
	
	private IActionLogManager _actionLoggerManager;
	private ActionLoggerTestHelper _helper;
	
}