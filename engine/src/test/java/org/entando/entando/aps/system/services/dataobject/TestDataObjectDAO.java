/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.dataobject;

import java.util.List;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;

/**
 * Test del Data Access Object per gli oggetti DataObject.
 *
 * @author E.Santoboni
 */
public class TestDataObjectDAO extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.dispose();
	}

	public void testDeleteAddDataObject() throws Throwable {
		try {
			DataObject mockDataObject = this.getMockDataObject();
			this.deleteDataObject(mockDataObject);
			this.addDataObject(mockDataObject);
		} catch (Throwable e) {
			throw e;
		}
	}

	private void deleteDataObject(DataObject dataObject) throws ApsSystemException {
		this._dataObjectDao.deleteEntity(dataObject.getId());
		DataObjectRecordVO dataObjectRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(dataObject.getId());
		assertNull(dataObjectRecord);
	}

	private void addDataObject(DataObject mockDataObject) throws ApsSystemException {
		_dataObjectDao.addEntity(mockDataObject);
		DataObjectRecordVO contentRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(mockDataObject.getId());
		assertEquals(mockDataObject.getDescription(), contentRecord.getDescription());
		assertEquals(mockDataObject.getStatus(), contentRecord.getStatus());
		assertFalse(contentRecord.isOnLine());
	}

	public void testGetAllDataObjectIds() throws Throwable {
		List<String> dataObjectIds1 = this._dataObjectDao.getAllEntityId();
		List<String> dataObjectIds2 = this._contentManager.searchId(null);
		assertEquals(dataObjectIds1.size(), dataObjectIds2.size());
		for (int i = 0; i < dataObjectIds1.size(); i++) {
			String contentId = dataObjectIds1.get(i);
			assertTrue(dataObjectIds2.contains(contentId));
		}
	}

	public void testInsertRemoveOnlineDataObject() throws Throwable {
		try {
			DataObject mockDataObject = this.getMockDataObject();
			this.insertOnLineDataObject(mockDataObject);
			this.getAllDataObjectsOnLine(mockDataObject);
			this.removeOnLineDataObject(mockDataObject);
		} catch (Throwable e) {
			throw e;
		}
	}

	private void insertOnLineDataObject(DataObject mockContent) throws ApsSystemException {
		this._dataObjectDao.insertOnLineContent(mockContent);
		DataObjectRecordVO contentRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(mockContent.getId());
		assertTrue(contentRecord.isOnLine());
	}

	private void getAllDataObjectsOnLine(DataObject mockContent) throws ApsSystemException {
		List<String> list = this._dataObjectDao.getAllEntityId();
		assertTrue(list.contains(mockContent.getId()));
	}

	private void removeOnLineDataObject(DataObject content) throws ApsSystemException {
		this._dataObjectDao.removeOnLineContent(content);
		DataObjectRecordVO contentRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(content.getId());
		assertFalse(contentRecord.isOnLine());
	}

	public void testUpdateContent() throws Throwable {
		try {
			DataObject mockContent = this.getMockDataObject();
			mockContent.setDescription("New Description");
			mockContent.setStatus(DataObject.STATUS_READY);
			this.updateContent(mockContent);
		} catch (Throwable t) {
			throw t;
		}
	}

	public void testGetGroupUtilizers() throws Throwable {
		List<String> contentIds = _dataObjectDao.getGroupUtilizers("customers");
		assertNotNull(contentIds);
		assertEquals(5, contentIds.size());
		assertTrue(contentIds.contains("ART102"));
		assertTrue(contentIds.contains("ART111"));
		assertTrue(contentIds.contains("ART122"));
		assertTrue(contentIds.contains("RAH101"));
		assertTrue(contentIds.contains("ART112"));
	}

	private void updateContent(DataObject mockContent) throws ApsSystemException {
		this._dataObjectDao.updateEntity(mockContent);
		DataObjectRecordVO contentRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(mockContent.getId());
		assertEquals(mockContent.getDescription(), contentRecord.getDescription());
		assertEquals(mockContent.getStatus(), contentRecord.getStatus());
		assertFalse(contentRecord.isOnLine());
	}

	private DataObject getMockDataObject() {
		DataObject content = this._contentManager.createDataObject("ART");

		content.setId("temp");
		content.setMainGroup(Group.FREE_GROUP_NAME);

		content.addGroup("firstGroup");
		content.addGroup("secondGroup");
		content.addGroup("thirdGroup");

		AttributeInterface attribute = new MonoTextAttribute();
		attribute.setName("temp");
		attribute.setDefaultLangCode("it");
		attribute.setRenderingLang("it");
		attribute.setSearchable(true);
		attribute.setType("Monotext");
		content.addAttribute(attribute);
		content.setDefaultLang("it");
		content.setDefaultModel("content_viewer");
		content.setDescription("temp");
		content.setListModel("Monolist");
		content.setRenderingLang("it");
		content.setStatus("Bozza");
		content.setTypeCode("ART");
		content.setTypeDescription("Articolo rassegna stampa");
		return content;
	}

	private void dispose() throws Exception {
		DataObject mockContent = this.getMockDataObject();
		try {
			this._dataObjectDao.deleteEntity(mockContent.getId());
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	private void init() throws Exception {
		this._dataObjectDao = new DataObjectDAO();
		try {
			this._contentManager = (IDataObjectManager) this.getService("DataObjectManager");
			DataObject mockContent = this.getMockDataObject();
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
			this._dataObjectDao.setDataSource(dataSource);
			ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
			this._dataObjectDao.setLangManager(langManager);
			this._dataObjectDao.addEntity(mockContent);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	private DataObjectDAO _dataObjectDao;

	private IDataObjectManager _contentManager;

}
