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
package org.entando.entando.aps.system.services.dataobject.entity;

import java.util.Date;
import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import org.entando.entando.aps.system.services.dataobject.model.DataObjectRecordVO;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;

/**
 * @author E.Santoboni
 */
public class TestDataObjectEntityManager extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testSearchRecords() throws Throwable {
        List<ApsEntityRecord> dataObjects = this._dataObjectManager.searchRecords(null);
        assertNotNull(dataObjects);
        assertEquals(25, dataObjects.size());

        EntitySearchFilter typeFilter = new EntitySearchFilter(IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", false);
        EntitySearchFilter[] filters1 = {typeFilter};
        dataObjects = this._dataObjectManager.searchRecords(filters1);
        assertEquals(11, dataObjects.size());

        EntitySearchFilter creationOrderFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        creationOrderFilter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters2 = {typeFilter, creationOrderFilter};
        String[] order2 = {"ART122", "ART121", "ART120", "ART112", "ART111", "ART104", "ART102", "ART187", "ART180", "ART179", "ART1"};

        dataObjects = this._dataObjectManager.searchRecords(filters2);
        assertEquals(order2.length, dataObjects.size());
        this.verifyOrder(dataObjects, order2);

        EntitySearchFilter descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "descriz", true);
        EntitySearchFilter[] filters3 = {typeFilter, creationOrderFilter, descrFilter};
        String[] order3 = {"ART187", "ART180", "ART179"};
        dataObjects = this._dataObjectManager.searchRecords(filters3);
        assertEquals(order3.length, dataObjects.size());
        this.verifyOrder(dataObjects, order3);

        EntitySearchFilter statusFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY, false, "AF", true);
        EntitySearchFilter[] filters4 = {typeFilter, creationOrderFilter, descrFilter, statusFilter};
        String[] order4 = {"ART187", "ART179"};
        dataObjects = this._dataObjectManager.searchRecords(filters4);
        assertEquals(order4.length, dataObjects.size());
        this.verifyOrder(dataObjects, order4);

        EntitySearchFilter onLineFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY, false);
        EntitySearchFilter[] filters5 = {typeFilter, creationOrderFilter, descrFilter, statusFilter, onLineFilter};
        String[] order5 = {"ART187"};
        dataObjects = this._dataObjectManager.searchRecords(filters5);
        assertEquals(order5.length, dataObjects.size());
        this.verifyOrder(dataObjects, order5);

        onLineFilter.setNullOption(true);
        EntitySearchFilter[] filters6 = {typeFilter, creationOrderFilter, descrFilter, statusFilter, onLineFilter};
        String[] order6 = {"ART179"};
        dataObjects = this._dataObjectManager.searchRecords(filters6);
        assertEquals(order6.length, dataObjects.size());
        this.verifyOrder(dataObjects, order6);
    }

    private void verifyOrder(List<ApsEntityRecord> dataObjects, String[] order) {
        for (int i = 0; i < dataObjects.size(); i++) {
            DataObjectRecordVO vo = (DataObjectRecordVO) dataObjects.get(i);
            assertEquals(order[i], vo.getId());
        }
    }

    public void testSearchEvents() throws ApsSystemException {
        EntitySearchFilter filterIt = new EntitySearchFilter("Titolo", true, "it", false);
        filterIt.setLangCode("it");
        EntitySearchFilter[] filters = {filterIt};
        List<String> dataObjects = this._dataObjectManager.searchId("EVN", filters);
        assertTrue(dataObjects.isEmpty());

        filterIt = new EntitySearchFilter("Titolo", true, "it", true);
        filterIt.setLangCode("it");
        EntitySearchFilter[] filters1 = {filterIt};
        dataObjects = this._dataObjectManager.searchId("EVN", filters1);
        assertFalse(dataObjects.isEmpty());
        String[] expectedItalianContentsId = {"EVN194", "EVN193", "EVN192", "EVN191", "EVN103"};
        assertEquals(expectedItalianContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedItalianContentsId.length; i++) {
            assertTrue(dataObjects.contains(expectedItalianContentsId[i]));
        }

        EntitySearchFilter filterEn = new EntitySearchFilter("Titolo", true, "it", true);
        filterEn.setLangCode("en");
        EntitySearchFilter[] filters2 = {filterEn};
        dataObjects = this._dataObjectManager.searchId("EVN", filters2);
        assertFalse(dataObjects.isEmpty());
        String[] expectedEnglishContentsId = {"EVN103", "EVN193", "EVN191", "EVN192", "EVN194"};
        assertEquals(expectedEnglishContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedEnglishContentsId.length; i++) {
            assertTrue(dataObjects.contains(expectedEnglishContentsId[i]));
        }

        filterEn.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters3 = {filterEn};
        dataObjects = this._dataObjectManager.searchId("EVN", filters3);
        assertEquals(expectedEnglishContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedEnglishContentsId.length; i++) {
            assertEquals(expectedEnglishContentsId[i], dataObjects.get(i));
        }

        filterEn.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters4 = {filterEn};
        dataObjects = this._dataObjectManager.searchId("EVN", filters4);
        assertEquals(expectedEnglishContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedEnglishContentsId.length; i++) {
            assertEquals(expectedEnglishContentsId[expectedEnglishContentsId.length - i - 1], dataObjects.get(i));
        }
    }

    public void testSearchEntities() throws ApsSystemException {
        EntitySearchFilter filter = new EntitySearchFilter("Data", true);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = this._dataObjectManager.searchId("ART", filters);
        String[] expectedContentsId1 = {"ART1", "ART104",
            "ART112", "ART111", "ART120", "ART121"};
        assertEquals(expectedContentsId1.length, dataObjects.size());
        for (int i = 0; i < expectedContentsId1.length; i++) {
            assertTrue(dataObjects.contains(expectedContentsId1[i]));
        }

        dataObjects = this._dataObjectManager.searchId("EVN", null);
        String[] expectedContentsId2 = {"EVN194", "EVN193", "EVN192", "EVN191", "EVN103",
            "EVN20", "EVN21", "EVN23", "EVN24", "EVN25", "EVN41"};
        assertEquals(expectedContentsId2.length, dataObjects.size());
        for (int i = 0; i < expectedContentsId2.length; i++) {
            assertTrue(dataObjects.contains(expectedContentsId2[i]));
        }
    }

    public void testLoadOrderedEvents_1() throws ApsSystemException {
        EntitySearchFilter filterForDescr = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false);
        filterForDescr.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filterForDescr};
        List<String> dataObjects = _dataObjectManager.searchId("EVN", filters);

        String[] expectedContentsId = {"EVN24", "EVN23", "EVN103", "EVN191",
            "EVN192", "EVN193", "EVN194", "EVN41", "EVN20", "EVN21", "EVN25"};
        assertEquals(expectedContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedContentsId.length; i++) {
            assertEquals(expectedContentsId[i], dataObjects.get(i));
        }

        filterForDescr.setOrder(EntitySearchFilter.DESC_ORDER);
        dataObjects = _dataObjectManager.searchId("EVN", filters);

        assertEquals(expectedContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedContentsId.length; i++) {
            assertEquals(expectedContentsId[expectedContentsId.length - i - 1], dataObjects.get(i));
        }
    }

    public void testLoadOrderedEvents_2() throws ApsSystemException {
        EntitySearchFilter filterForCreation = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        filterForCreation.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filterForCreation};

        List<String> dataObjects = _dataObjectManager.searchId("EVN", filters);
        String[] expectedOrderedContentsId = {"EVN191", "EVN192", "EVN193", "EVN194",
            "EVN103", "EVN20", "EVN23", "EVN24", "EVN25", "EVN41", "EVN21"};
        assertEquals(expectedOrderedContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[i], dataObjects.get(i));
        }

        filterForCreation.setOrder(EntitySearchFilter.DESC_ORDER);
        dataObjects = _dataObjectManager.searchId("EVN", filters);
        assertEquals(expectedOrderedContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[expectedOrderedContentsId.length - i - 1], dataObjects.get(i));
        }
    }

    public void testLoadEvents2() throws ApsSystemException {
        Date start = DateConverter.parseDate("1997-06-10", "yyyy-MM-dd");
        Date end = DateConverter.parseDate("2020-09-19", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, start, end);
        EntitySearchFilter filter2 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "Even", true);
        filter2.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter, filter2};

        List<String> dataObjects = _dataObjectManager.searchId("EVN", filters);
        assertEquals(2, dataObjects.size());
        assertEquals("EVN193", dataObjects.get(0));
        assertEquals("EVN192", dataObjects.get(1));

        EntitySearchFilter filter3 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY, false, "pronto", true);
        EntitySearchFilter[] filters2 = {filter, filter3, filter2};
        dataObjects = _dataObjectManager.searchId("EVN", filters2);
        assertEquals(0, dataObjects.size());

        EntitySearchFilter[] filters2_bis = {filter, filter2, filter3};
        dataObjects = _dataObjectManager.searchId("EVN", filters2_bis);
        assertEquals(0, dataObjects.size());

        filter2 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false);
        filter2.setOrder(EntitySearchFilter.DESC_ORDER);

        EntitySearchFilter[] filters3 = {filter, filter2};
        dataObjects = _dataObjectManager.searchId("EVN", filters3);

        String[] expectedOrderedContentsId = {"EVN25", "EVN21", "EVN20", "EVN41", "EVN193",
            "EVN192", "EVN103", "EVN23", "EVN24"};
        assertEquals(expectedOrderedContentsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedContentsId.length; i++) {
            assertEquals(expectedOrderedContentsId[i], dataObjects.get(i));
        }
    }

    public void testLoadFutureEvents1() throws ApsSystemException {
        Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter};

        List<String> dataObjects = this._dataObjectManager.searchId("EVN", filters);
        String[] expectedOrderedEntitiesId = {"EVN21", "EVN20", "EVN25", "EVN41", "EVN23",
            "EVN24", "EVN193", "EVN194"};
        assertEquals(expectedOrderedEntitiesId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEntitiesId.length; i++) {
            assertEquals(expectedOrderedEntitiesId[i], dataObjects.get(i));
        }
    }

    public void testLoadFutureEvents2() throws ApsSystemException {
        Date date = DateConverter.parseDate("2008-01-01", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, date, null);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter};

        List<String> dataObjects = this._dataObjectManager.searchId("EVN", filters);
        String[] expectedOrderedEntitiesId = {"EVN41", "EVN23", "EVN24", "EVN193", "EVN194"};
        assertEquals(expectedOrderedEntitiesId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEntitiesId.length; i++) {
            assertEquals(expectedOrderedEntitiesId[i], dataObjects.get(i));
        }
    }

    public void testLoadFutureEvents3() throws ApsSystemException {
        Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};

        List<String> dataObjects = this._dataObjectManager.searchId("EVN", filters);
        String[] expectedOrderedEntitiesId = {"EVN194", "EVN193", "EVN24",
            "EVN23", "EVN41", "EVN25", "EVN20", "EVN21"};
        assertEquals(expectedOrderedEntitiesId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEntitiesId.length; i++) {
            assertEquals(expectedOrderedEntitiesId[i], dataObjects.get(i));
        }
    }

    public void testLoadPastEvents1() throws ApsSystemException {
        Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");

        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter};

        List<String> dataObjects2 = this._dataObjectManager.searchId("EVN", filters);
        String[] expectedOrderedEntitiesId = {"EVN191", "EVN192", "EVN103",
            "EVN21", "EVN20", "EVN25", "EVN41", "EVN23"};
        assertEquals(expectedOrderedEntitiesId.length, dataObjects2.size());
        for (int i = 0; i < expectedOrderedEntitiesId.length; i++) {
            assertEquals(expectedOrderedEntitiesId[i], dataObjects2.get(i));
        }
    }

    public void testLoadPastEvents2() throws ApsSystemException {
        Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");

        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects2 = this._dataObjectManager.searchId("EVN", filters);
        String[] expectedOrderedEntitiesId = {"EVN23", "EVN41", "EVN25",
            "EVN20", "EVN21", "EVN103", "EVN192", "EVN191"};
        assertEquals(expectedOrderedEntitiesId.length, dataObjects2.size());
        for (int i = 0; i < expectedOrderedEntitiesId.length; i++) {
            assertEquals(expectedOrderedEntitiesId[i], dataObjects2.get(i));
        }
    }

    public void testLoadFutureEntityEvents1() throws Throwable {
        Date dateForTest = DateConverter.parseDate("1999-03-14", "yyyy-MM-dd");

        EntitySearchFilter filter1 = new EntitySearchFilter("DataInizio", true, dateForTest, null);
        filter1.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter1};

        List<String> dataObjects = this._dataObjectManager.searchId("EVN", filters);
        String[] expectedOrderedEnitiesId = {"EVN192", "EVN103",
            "EVN21", "EVN20", "EVN25", "EVN41", "EVN23", "EVN24", "EVN193", "EVN194"};

        assertEquals(expectedOrderedEnitiesId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEnitiesId.length; i++) {
            assertEquals(expectedOrderedEnitiesId[i], dataObjects.get(i));
        }

        filter1.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters2 = {filter1};
        dataObjects = this._dataObjectManager.searchId("EVN", filters2);

        assertEquals(expectedOrderedEnitiesId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEnitiesId.length; i++) {
            assertEquals(expectedOrderedEnitiesId[expectedOrderedEnitiesId.length - i - 1], dataObjects.get(i));
        }
    }

    public void testLoadFutureEntityEvents2() throws Throwable {
        Date startDateForTest = DateConverter.parseDate("1999-03-14", "yyyy-MM-dd");
        EntitySearchFilter filter1 = new EntitySearchFilter("DataInizio", true, startDateForTest, null);
        filter1.setOrder(EntitySearchFilter.ASC_ORDER);
        Date endDateForTest = DateConverter.parseDate("2017-09-12", "yyyy-MM-dd");
        EntitySearchFilter filter2 = new EntitySearchFilter("DataFine", true, null, endDateForTest);
        filter2.setOrder(EntitySearchFilter.ASC_ORDER);

        EntitySearchFilter[] filters3 = {filter1, filter2};
        List<String> dataObjects = this._dataObjectManager.searchId("EVN", filters3);
        String[] expectedOrderedEntitiesId = {"EVN192", "EVN103",
            "EVN21", "EVN20", "EVN25", "EVN41", "EVN23", "EVN24", "EVN193"};
        assertEquals(expectedOrderedEntitiesId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEntitiesId.length; i++) {
            assertEquals(expectedOrderedEntitiesId[i], dataObjects.get(i));
        }

        filter2.setOrder(EntitySearchFilter.DESC_ORDER);
        dataObjects = this._dataObjectManager.searchId("EVN", filters3);
        //l'ordinamento è lo stesso il quanto il primo ordinamento viene fatto con il filter1
        assertEquals(expectedOrderedEntitiesId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEntitiesId.length; i++) {
            assertEquals(expectedOrderedEntitiesId[i], dataObjects.get(i));
        }

        filter1.setOrder(EntitySearchFilter.DESC_ORDER);
        dataObjects = this._dataObjectManager.searchId("EVN", filters3);
        assertEquals(expectedOrderedEntitiesId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEntitiesId.length; i++) {
            assertEquals(expectedOrderedEntitiesId[expectedOrderedEntitiesId.length - i - 1], dataObjects.get(i));
        }

        filter1.setOrder(EntitySearchFilter.ASC_ORDER);
        filter2.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters4 = {filter2, filter1};
        dataObjects = this._dataObjectManager.searchId("EVN", filters4);
        String[] expectedOrderedEntitiesId2 = {"EVN192", "EVN103",
            "EVN20", "EVN21", "EVN25", "EVN41", "EVN23", "EVN24", "EVN193"};
        assertEquals(expectedOrderedEntitiesId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEntitiesId2.length; i++) {
            assertEquals(expectedOrderedEntitiesId2[i], dataObjects.get(i));
        }

        filter2.setOrder(EntitySearchFilter.DESC_ORDER);
        dataObjects = this._dataObjectManager.searchId("EVN", filters4);
        assertEquals(expectedOrderedEntitiesId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedEntitiesId2.length; i++) {
            assertEquals(expectedOrderedEntitiesId2[expectedOrderedEntitiesId2.length - i - 1], dataObjects.get(i));
        }
    }

    private void init() throws Exception {
        try {
            this._dataObjectManager = (IDataObjectManager) this.getService("DataObjectManager");
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IDataObjectManager _dataObjectManager = null;

}
