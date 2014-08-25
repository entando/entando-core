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

import org.entando.entando.aps.system.services.actionlog.ActionLogDAO;
import org.entando.entando.aps.system.services.actionlog.IActionLogDAO;
import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.util.DateConverter;
import java.util.List;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordSearchBean;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamComment;
import org.entando.entando.aps.system.services.actionlog.model.IActionLogRecordSearchBean;

public class TestActionLogDAO extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this._helper.cleanRecords();
	}

	public void _testGetActionRecords() {
		IActionLogRecordSearchBean bean = null;
		List<Integer> ids = this._actionLoggerDAO.getActionRecords(bean);
		this.compareIds(new Integer[]{}, ids);
		ActionLogRecord record1 = this._helper.createActionRecord(1, "username1", "actionName1",
				"namespace1", DateConverter.parseDate("01/01/2009 00:00", "dd/MM/yyyy HH:mm"), "params1");
		ActionLogRecord record2 = this._helper.createActionRecord(2, "username2", "actionName2",
				"namespace2", DateConverter.parseDate("01/01/2009 10:00", "dd/MM/yyyy HH:mm"), "params2");
		ActionLogRecord record3 = this._helper.createActionRecord(3, "username123", "actionName123",
				"namespace123", DateConverter.parseDate("02/01/2009 12:00", "dd/MM/yyyy HH:mm"), "params123");
		this._helper.addActionRecord(record1);
		this._helper.addActionRecord(record2);
		this._helper.addActionRecord(record3);

		ids = this._actionLoggerDAO.getActionRecords(bean);
		this.compareIds(new Integer[]{1, 2, 3}, ids);

		ActionLogRecordSearchBean searchBean = this._helper.createSearchBean("name", "Name", "space", "arams", null, null);
		ids = this._actionLoggerDAO.getActionRecords(searchBean);
		this.compareIds(new Integer[]{1, 2, 3}, ids);

		searchBean = this._helper.createSearchBean("name", "Name", "space", "arams", DateConverter.parseDate("01/01/2009 10:01", "dd/MM/yyyy HH:mm"), null);
		ids = this._actionLoggerDAO.getActionRecords(searchBean);
		this.compareIds(new Integer[]{3}, ids);

		searchBean = this._helper.createSearchBean(null, null, null, null, null, DateConverter.parseDate("01/01/2009 10:01", "dd/MM/yyyy HH:mm"));
		ids = this._actionLoggerDAO.getActionRecords(searchBean);
		this.compareIds(new Integer[]{1, 2}, ids);

		searchBean = this._helper.createSearchBean(null, "Name", null, null, DateConverter.parseDate("01/01/2009 09:01", "dd/MM/yyyy HH:mm"),
				DateConverter.parseDate("01/01/2009 10:01", "dd/MM/yyyy HH:mm"));
		ids = this._actionLoggerDAO.getActionRecords(searchBean);
		this.compareIds(new Integer[]{2}, ids);

	}

	public void _testActionLogSearch() {
		IActionLogRecordSearchBean bean = null;
		List<Integer> ids = this._actionLoggerDAO.getActionRecords(bean);
		this.compareIds(new Integer[]{}, ids);
		ActionLogRecord record1 = this._helper.createActionRecord(1, "username1", "actionName1",
				"namespace1", DateConverter.parseDate("01/01/2009 00:00", "dd/MM/yyyy HH:mm"), "params1");
		ActionLogRecord record2 = this._helper.createActionRecord(2, "username2", "actionName2",
				"namespace2", DateConverter.parseDate("01/01/2009 10:00", "dd/MM/yyyy HH:mm"), "params2");
		ActionLogRecord record3 = this._helper.createActionRecord(3, "username123", "actionName123",
				"namespace123", DateConverter.parseDate("02/01/2009 12:00", "dd/MM/yyyy HH:mm"), "params123");
		this._helper.addActionRecord(record1);
		this._helper.addActionRecord(record2);
		this._helper.addActionRecord(record3);

		ActionLogRecordSearchBean searchBean = this._helper.createSearchBean(null, "Name", null, null, DateConverter.parseDate("02/01/2009 10:01", "dd/MM/yyyy HH:mm"),
				DateConverter.parseDate("02/01/2009 14:01", "dd/MM/yyyy HH:mm"));
		ids = this._actionLoggerDAO.getActionRecords(searchBean);
		this.compareIds(new Integer[]{3}, ids);
	}

	public void _testAddGetDeleteActionRecord() {
		ActionLogRecord record1 = this._helper.createActionRecord(1, "username1", "actionName1",
				"namespace1", DateConverter.parseDate("01/01/2009 00:00", "dd/MM/yyyy HH:mm"), "params1");
		ActionLogRecord record2 = this._helper.createActionRecord(2, "username2", "actionName2",
				"namespace2", DateConverter.parseDate("01/02/2009 00:00", "dd/MM/yyyy HH:mm"), "params2");

		this._actionLoggerDAO.addActionRecord(record1);
		this._actionLoggerDAO.addActionRecord(record2);
		ActionLogRecord addedRecord1 = this._actionLoggerDAO.getActionRecord(record1.getId());
		this.compareActionRecords(record1, addedRecord1);
		ActionLogRecord addedRecord2 = this._actionLoggerDAO.getActionRecord(record2.getId());
		this.compareActionRecords(record2, addedRecord2);

		this._actionLoggerDAO.deleteActionRecord(record1.getId());
		assertNull(this._actionLoggerDAO.getActionRecord(record1.getId()));

		this._actionLoggerDAO.deleteActionRecord(record2.getId());
		assertNull(this._actionLoggerDAO.getActionRecord(record2.getId()));
	}

	public void testAddDeleteCommentRecord() {
		ActionLogRecord record1 = this._helper.createActionRecord(1, "username1", "actionName1",
				"namespace1", DateConverter.parseDate("01/01/2009 00:00", "dd/MM/yyyy HH:mm"), "params1");
		ActionLogRecord record2 = this._helper.createActionRecord(2, "username2", "actionName2",
				"namespace2", DateConverter.parseDate("01/02/2009 00:00", "dd/MM/yyyy HH:mm"), "params2");

		this._actionLoggerDAO.addActionRecord(record1);
		this._actionLoggerDAO.addActionRecord(record2);
		ActionLogRecord addedRecord1 = this._actionLoggerDAO.getActionRecord(record1.getId());
		this.compareActionRecords(record1, addedRecord1);
		ActionLogRecord addedRecord2 = this._actionLoggerDAO.getActionRecord(record2.getId());
		this.compareActionRecords(record2, addedRecord2);

		this._actionLoggerDAO.addActionCommentRecord(100, addedRecord1.getId(), "admin", "test comment 1");

		this._actionLoggerDAO.addActionCommentRecord(101, addedRecord1.getId(), "admin", "test comment 2");
		List<ActivityStreamComment> actionCommentRecords = this._actionLoggerDAO.getActionCommentRecords(addedRecord1.getId());

		assertEquals(2, actionCommentRecords.size());
		assertEquals(100, actionCommentRecords.get(0).getId());
		assertEquals(101, actionCommentRecords.get(1).getId());
		
		this._actionLoggerDAO.deleteActionCommentRecord(100,addedRecord1.getId());
		
		actionCommentRecords = this._actionLoggerDAO.getActionCommentRecords(addedRecord1.getId());

		assertEquals(1, actionCommentRecords.size());
		
		assertEquals("test comment 2", actionCommentRecords.get(0).getCommentText());
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
		ActionLogDAO actionLoggerDAO = new ActionLogDAO();
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		actionLoggerDAO.setDataSource(dataSource);
		this._actionLoggerDAO = actionLoggerDAO;

		this._helper = new ActionLoggerTestHelper(this.getApplicationContext());
	}

	@Override
	protected void tearDown() throws Exception {
		this._helper.cleanRecords();
		super.tearDown();
	}

	private IActionLogDAO _actionLoggerDAO;
	private ActionLoggerTestHelper _helper;

}
