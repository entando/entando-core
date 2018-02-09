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
        DataObjectRecordVO dataObjectRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(mockDataObject.getId());
        assertEquals(mockDataObject.getDescription(), dataObjectRecord.getDescription());
        assertEquals(mockDataObject.getStatus(), dataObjectRecord.getStatus());
        assertTrue(dataObjectRecord.isOnLine());
    }

    public void testGetAllDataObjectIds() throws Throwable {
        List<String> dataObjectIds1 = this._dataObjectDao.getAllEntityId();
        List<String> dataObjectIds2 = this._dataObjectManager.searchId(null);
        assertEquals(dataObjectIds1.size(), dataObjectIds2.size());
        for (int i = 0; i < dataObjectIds1.size(); i++) {
            String dataObjectId = dataObjectIds1.get(i);
            assertTrue(dataObjectIds2.contains(dataObjectId));
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

    private void insertOnLineDataObject(DataObject mockDataObject) throws ApsSystemException {
        this._dataObjectDao.insertDataObject(mockDataObject);
        DataObjectRecordVO dataObjectRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(mockDataObject.getId());
        assertTrue(dataObjectRecord.isOnLine());
    }

    private void getAllDataObjectsOnLine(DataObject mockDataObject) throws ApsSystemException {
        List<String> list = this._dataObjectDao.getAllEntityId();
        assertTrue(list.contains(mockDataObject.getId()));
    }

    private void removeOnLineDataObject(DataObject dataObject) throws ApsSystemException {
        this._dataObjectDao.removeDataObject(dataObject);
        DataObjectRecordVO dataObjectRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(dataObject.getId());
        assertFalse(dataObjectRecord.isOnLine());
    }

    public void testUpdateDataObject() throws Throwable {
        try {
            DataObject mockDataObject = this.getMockDataObject();
            mockDataObject.setDescription("New Description");
            mockDataObject.setStatus(DataObject.STATUS_READY);
            this.updateDataObject(mockDataObject);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testGetGroupUtilizers() throws Throwable {
        List<String> dataObjectIds = _dataObjectDao.getGroupUtilizers("customers");
        assertNotNull(dataObjectIds);
        assertEquals(5, dataObjectIds.size());
        assertTrue(dataObjectIds.contains("ART102"));
        assertTrue(dataObjectIds.contains("ART111"));
        assertTrue(dataObjectIds.contains("ART122"));
        assertTrue(dataObjectIds.contains("RAH101"));
        assertTrue(dataObjectIds.contains("ART112"));
    }

    private void updateDataObject(DataObject mockDataObject) throws ApsSystemException {
        this._dataObjectDao.updateEntity(mockDataObject);
        DataObjectRecordVO dataObjectRecord = (DataObjectRecordVO) this._dataObjectDao.loadEntityRecord(mockDataObject.getId());
        assertEquals(mockDataObject.getDescription(), dataObjectRecord.getDescription());
        assertEquals(mockDataObject.getStatus(), dataObjectRecord.getStatus());
        assertTrue(dataObjectRecord.isOnLine());
    }

    private DataObject getMockDataObject() {
        DataObject dataObject = this._dataObjectManager.createDataObject("ART");

        dataObject.setId("temp");
        dataObject.setMainGroup(Group.FREE_GROUP_NAME);

        dataObject.addGroup("firstGroup");
        dataObject.addGroup("secondGroup");
        dataObject.addGroup("thirdGroup");

        AttributeInterface attribute = new MonoTextAttribute();
        attribute.setName("temp");
        attribute.setDefaultLangCode("it");
        attribute.setRenderingLang("it");
        attribute.setSearchable(true);
        attribute.setType("Monotext");
        dataObject.addAttribute(attribute);
        dataObject.setDefaultLang("it");
        dataObject.setDefaultModel("dataObject_viewer");
        dataObject.setDescription("temp");
        dataObject.setListModel("Monolist");
        dataObject.setRenderingLang("it");
        dataObject.setStatus("Bozza");
        dataObject.setTypeCode("ART");
        dataObject.setTypeDescription("Articolo rassegna stampa");
        return dataObject;
    }

    private void dispose() throws Exception {
        DataObject mockDataObject = this.getMockDataObject();
        try {
            this._dataObjectDao.deleteEntity(mockDataObject.getId());
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }

    private void init() throws Exception {
        this._dataObjectDao = new DataObjectDAO();
        try {
            this._dataObjectManager = (IDataObjectManager) this.getService("DataObjectManager");
            DataObject mockDataObject = this.getMockDataObject();
            DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
            this._dataObjectDao.setDataSource(dataSource);
            ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
            this._dataObjectDao.setLangManager(langManager);
            this._dataObjectDao.addEntity(mockDataObject);
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }

    private DataObjectDAO _dataObjectDao;

    private IDataObjectManager _dataObjectManager;

}
