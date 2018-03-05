/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General  License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General  License for more
 * details.
 */
package org.entando.entando.aps.system.services.dataobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CheckBoxAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ThreeStateAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.util.DateConverter;
import org.entando.entando.aps.system.common.entity.model.attribute.EnumeratorMapAttribute;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobject.model.SmallDataType;

/**
 * @author M. Morini - E.Santoboni
 */
public class TestDataObjectManager extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testSearchDataObjects_1_1() throws Throwable {
        List<String> dataObjectIds = this._dataObjectManager.searchId(null);
        assertNotNull(dataObjectIds);
        assertEquals(25, dataObjectIds.size());

        EntitySearchFilter creationOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "Cont", true);
        EntitySearchFilter[] filters1 = {creationOrder, descrFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters1);
        assertNotNull(dataObjectIds);
        String[] expected1 = {"RAH101", "ART102", "EVN103", "ART104", "ART111", "ART112", "ART120", "ART121", "ART122"};
        assertEquals(expected1.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected1);

        EntitySearchFilter lastEditorFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_LAST_EDITOR_FILTER_KEY, false, "admin", true);
        EntitySearchFilter[] filters2 = {creationOrder, descrFilter, lastEditorFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters2);
        assertNotNull(dataObjectIds);
        assertEquals(expected1.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected1);

        descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "Cont", true, FieldSearchFilter.LikeOptionType.RIGHT);
        EntitySearchFilter[] filters3 = {creationOrder, descrFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters3);
        assertNotNull(dataObjectIds);
        String[] expected3 = expected1;
        assertEquals(expected3.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected3);

        descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "Cont", true, FieldSearchFilter.LikeOptionType.LEFT);
        EntitySearchFilter[] filters4 = {creationOrder, descrFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters4);
        assertNotNull(dataObjectIds);
        assertTrue(dataObjectIds.isEmpty());

        descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "1", true, FieldSearchFilter.LikeOptionType.LEFT);
        EntitySearchFilter[] filters5 = {creationOrder, descrFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters5);
        assertNotNull(dataObjectIds);
        String[] expected5 = {"EVN191", "ART120"};
        assertEquals(expected5.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected5);

    }

    public void testSearchDataObjects_1_2() throws Throwable {
        EntitySearchFilter versionFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CURRENT_VERSION_FILTER_KEY, false, "0.", true);
        EntitySearchFilter[] filters3 = {versionFilter};
        List<String> dataObjectIds = this._dataObjectManager.searchId(filters3);
        assertNotNull(dataObjectIds);
        String[] expected2 = {"ART179"};
        assertEquals(expected2.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected2);

        versionFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CURRENT_VERSION_FILTER_KEY, false, ".0", true);
        EntitySearchFilter[] filters4 = {versionFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters4);
        assertNotNull(dataObjectIds);
        assertEquals(22, dataObjectIds.size());
    }

    public void testSearchDataObjects_1_4() throws Throwable {
        //forcing case insensitive search
        DataObjectSearcherDAO searcherDao = (DataObjectSearcherDAO) this.getApplicationContext().getBean("DataObjectSearcherDAO");

        List<String> dataObjectIds = this._dataObjectManager.searchId(null);
        assertNotNull(dataObjectIds);
        assertEquals(25, dataObjectIds.size());

        EntitySearchFilter creationOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "CoNt", true);
        EntitySearchFilter[] filters1 = {creationOrder, descrFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters1);
        assertNotNull(dataObjectIds);
        String[] expected1 = {"RAH101", "ART102", "EVN103", "ART104", "ART111", "ART112", "ART120", "ART121", "ART122"};
        assertEquals(expected1.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected1);

        EntitySearchFilter lastEditorFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_LAST_EDITOR_FILTER_KEY, false, "AdMin", true);
        EntitySearchFilter[] filters2 = {creationOrder, descrFilter, lastEditorFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters2);
        assertNotNull(dataObjectIds);
        assertEquals(expected1.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected1);
    }

    public void testSearchDataObjects_1_5() throws Throwable {
        //forcing case insensitive search
        DataObjectSearcherDAO searcherDao = (DataObjectSearcherDAO) this.getApplicationContext().getBean("DataObjectSearcherDAO");

        EntitySearchFilter creationOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "co", true, FieldSearchFilter.LikeOptionType.COMPLETE);
        EntitySearchFilter[] filters1 = {creationOrder, descrFilter};
        List<String> dataObjectIds = this._dataObjectManager.searchId(filters1);
        assertNotNull(dataObjectIds);
        String[] expected1 = {"ART1", "RAH1", "ART187", "RAH101", "ART102", "EVN103", "ART104", "ART111", "ART112", "EVN23", "ART120", "ART121", "ART122"};
        assertEquals(expected1.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected1);

        descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "co", true, FieldSearchFilter.LikeOptionType.RIGHT);
        EntitySearchFilter[] filters2 = {creationOrder, descrFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters2);
        assertNotNull(dataObjectIds);
        String[] expected2 = {"RAH101", "ART102", "EVN103", "ART104", "ART111", "ART112", "EVN23", "ART120", "ART121", "ART122"};
        assertEquals(expected2.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected2);

        EntitySearchFilter idFilter = new EntitySearchFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY, false, "1", true, FieldSearchFilter.LikeOptionType.LEFT);
        EntitySearchFilter[] filters3 = {creationOrder, descrFilter, idFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters3);
        assertNotNull(dataObjectIds);
        String[] expected3 = {"RAH101", "ART111", "ART121"};
        assertEquals(expected3.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected3);

        descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "co", true, FieldSearchFilter.LikeOptionType.LEFT);
        EntitySearchFilter[] filters4 = {creationOrder, descrFilter};
        dataObjectIds = this._dataObjectManager.searchId(filters4);
        assertNotNull(dataObjectIds);
        String[] expected4 = {};
        assertEquals(expected4.length, dataObjectIds.size());
    }



    public void testSearchDataObjects_2() throws Throwable {
        EntitySearchFilter creationOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        creationOrder.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter groupFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_MAIN_GROUP_FILTER_KEY, false, "coach", false);
        EntitySearchFilter[] filters = {creationOrder, groupFilter};
        List<String> dataObjectIds = this._dataObjectManager.searchId(filters);
        assertNotNull(dataObjectIds);
        String[] expected = {"EVN103", "ART104", "ART111", "ART112", "EVN25", "EVN41"};
        assertEquals(expected.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected);
    }

    public void testSearchDataObjects_3() throws Throwable {
        EntitySearchFilter modifyOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_MODIFY_DATE_FILTER_KEY, false);
        modifyOrder.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter onlineFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY, false, "encoding=", true);
        EntitySearchFilter[] filters = {modifyOrder, onlineFilter};
        List<String> dataObjectIds = this._dataObjectManager.searchId(filters);
        assertNotNull(dataObjectIds);
        String[] expected = {"ART187", "ART1", "EVN193", "EVN194", "ART180", "RAH1",
            "EVN191", "EVN192", "RAH101", "EVN103", "ART104", "ART102", "EVN23",
            "EVN24", "EVN25", "EVN41", "EVN20", "EVN21", "ART111", "ART120", "ART121", "ART122", "ART112", "ALL4"};
        assertEquals(expected.length, dataObjectIds.size());
        this.verifyOrder(dataObjectIds, expected);
    }

    public void testSearchDataObjects() throws Throwable {
        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(null, null, null);
        assertNotNull(dataObjects);
        assertEquals(15, dataObjects.size());

        EntitySearchFilter creationOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        creationOrder.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter typeFilter = new EntitySearchFilter(IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", false);
        EntitySearchFilter[] filters1 = {creationOrder, typeFilter};
        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters1, null);
        assertEquals(4, dataObjects.size());

        List<String> groupCodes = new ArrayList<String>();
        groupCodes.add("customers");
        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters1, groupCodes);
        String[] order1 = {"ART122", "ART121", "ART112", "ART111", "ART102", "ART187", "ART180", "ART1"};
        assertEquals(order1.length, dataObjects.size());
        this.verifyOrder(dataObjects, order1);

        groupCodes.add(Group.FREE_GROUP_NAME);
        EntitySearchFilter statusFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_STATUS_FILTER_KEY, false, DataObject.STATUS_DRAFT, false);
        EntitySearchFilter[] filters2 = {creationOrder, typeFilter, statusFilter};
        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters2, groupCodes);
        String[] order2 = {"ART121", "ART112", "ART102", "ART187", "ART1"};
        assertEquals(order2.length, dataObjects.size());
        this.verifyOrder(dataObjects, order2);

        EntitySearchFilter onlineFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_ONLINE_FILTER_KEY, false);
        EntitySearchFilter[] filters3 = {creationOrder, typeFilter, onlineFilter};
        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters3, groupCodes);
        String[] order3 = {"ART122", "ART121", "ART112", "ART111", "ART102", "ART187", "ART180", "ART1"};
        assertEquals(order3.length, dataObjects.size());
        this.verifyOrder(dataObjects, order3);

        onlineFilter.setNullOption(true);
        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters3, groupCodes);
        String[] order4 = {};
        assertEquals(order4.length, dataObjects.size());
        this.verifyOrder(dataObjects, order4);

        onlineFilter.setNullOption(false);
        EntitySearchFilter descrFilter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "scr", true);
        EntitySearchFilter[] filters5 = {creationOrder, typeFilter, onlineFilter, descrFilter};
        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters5, groupCodes);
        String[] order5 = {"ART187", "ART180"};
        assertEquals(order5.length, dataObjects.size());
        this.verifyOrder(dataObjects, order5);

        groupCodes.clear();
        groupCodes.add(Group.ADMINS_GROUP_NAME);
        dataObjects = this._dataObjectManager.loadDataObjectsId(null, null, groupCodes);
        assertNotNull(dataObjects);
        assertEquals(24, dataObjects.size());
    }



    public void testSearchDataObjects_2_b() throws Throwable {
        //forcing case insensitive search
        DataObjectSearcherDAO searcherDao = (DataObjectSearcherDAO) this.getApplicationContext().getBean("DataObjectSearcherDAO");

        List<String> groupCodes = new ArrayList<String>();
        groupCodes.add("customers");
        groupCodes.add(Group.FREE_GROUP_NAME);
        EntitySearchFilter creationOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        creationOrder.setOrder(EntitySearchFilter.DESC_ORDER);

        EntitySearchFilter descrFilter_1 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "eScR", true);
        EntitySearchFilter[] filters_1 = {creationOrder, descrFilter_1};
        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters_1, groupCodes);
        String[] order = {"ALL4", "ART187", "ART180"};
        assertEquals(order.length, dataObjects.size());
        this.verifyOrder(dataObjects, order);
    }

    public void testSearchDataObjects_3b() throws Throwable {
        List<String> groupCodes = new ArrayList<String>();
        groupCodes.add(Group.ADMINS_GROUP_NAME);
        EntitySearchFilter creationOrder = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        creationOrder.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {creationOrder};
        String[] categories_1 = {"general_cat2"};
        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(categories_1, filters, groupCodes);
        String[] order_a = {"ART120", "ART111"};
        assertEquals(order_a.length, dataObjects.size());
        this.verifyOrder(dataObjects, order_a);

        String[] categories_2 = {"general_cat1", "general_cat2"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories_2, filters, groupCodes);
        String[] order_b = {"ART111"};
        assertEquals(order_b.length, dataObjects.size());
        assertEquals(order_b[0], dataObjects.get(0));

        DataObject newDataObject = this._dataObjectManager.loadDataObject("EVN193", false);
        newDataObject.setId(null);
        try {
            this._dataObjectManager.saveDataObject(newDataObject);
            dataObjects = this._dataObjectManager.loadDataObjectsId(categories_1, filters, groupCodes);
            String[] order_c = {newDataObject.getId(), "ART120", "ART111"};
            assertEquals(order_c.length, dataObjects.size());
            this.verifyOrder(dataObjects, order_c);

            ICategoryManager categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
            newDataObject.addCategory(categoryManager.getCategory("general_cat2"));
            newDataObject.addCategory(categoryManager.getCategory("general_cat1"));
            newDataObject.addGroup(Group.ADMINS_GROUP_NAME);
            this._dataObjectManager.saveDataObject(newDataObject);
            dataObjects = this._dataObjectManager.loadDataObjectsId(categories_2, filters, groupCodes);
            String[] order_d = {newDataObject.getId(), "ART111"};
            assertEquals(order_d.length, dataObjects.size());
            this.verifyOrder(dataObjects, order_d);
        } catch (Throwable t) {
            throw t;
        } finally {
            this._dataObjectManager.deleteDataObject(newDataObject);
            assertNull(this._dataObjectManager.loadDataObject(newDataObject.getId(), false));
        }
    }

    private void verifyOrder(List<String> dataObjects, String[] order) {
        for (int i = 0; i < dataObjects.size(); i++) {
            assertEquals(order[i], dataObjects.get(i));
        }
    }

    public void testLoadDataObject() throws Throwable {
        DataObject dataObject = this._dataObjectManager.loadDataObject("ART111", false);
        assertEquals(DataObject.STATUS_PUBLIC, dataObject.getStatus());
        assertEquals("coach", dataObject.getMainGroup());
        assertEquals(2, dataObject.getGroups().size());
        assertTrue(dataObject.getGroups().contains("customers"));
        assertTrue(dataObject.getGroups().contains("helpdesk"));

        Map<String, AttributeInterface> attributes = dataObject.getAttributeMap();
        assertEquals(5, attributes.size());

        TextAttribute title = (TextAttribute) attributes.get("Titolo");
        assertEquals("Titolo Contenuto 3 Coach", title.getTextForLang("it"));
        assertNull(title.getTextForLang("en"));

        MonoListAttribute authors = (MonoListAttribute) attributes.get("Autori");
        assertEquals(4, authors.getAttributes().size());

        HypertextAttribute hypertext = (HypertextAttribute) attributes.get("CorpoTesto");
        assertEquals("<p>Corpo Testo Contenuto 3 Coach</p>", hypertext.getTextForLang("it").trim());
        assertNull(hypertext.getTextForLang("en"));

        DateAttribute date = (DateAttribute) attributes.get("Data");
        assertEquals("13/12/2006", DateConverter.getFormattedDate(date.getDate(), "dd/MM/yyyy"));
    }

    public void testLoadFullDataObject() throws Throwable {
        DataObject dataObject = this._dataObjectManager.loadDataObject("ALL4", false);
        assertEquals(DataObject.STATUS_PUBLIC, dataObject.getStatus());
        assertEquals(Group.FREE_GROUP_NAME, dataObject.getMainGroup());
        assertEquals(0, dataObject.getGroups().size());
        Map<String, AttributeInterface> attributes = dataObject.getAttributeMap();
        assertEquals(37, attributes.size());
        CheckBoxAttribute checkBoxAttribute = (CheckBoxAttribute) attributes.get("CheckBox");
        assertNotNull(checkBoxAttribute);
        assertNull(checkBoxAttribute.getBooleanValue());
        DateAttribute dateAttribute = (DateAttribute) attributes.get("Date");
        assertNotNull(dateAttribute);
        assertEquals("20100321", DateConverter.getFormattedDate(dateAttribute.getDate(), "yyyyMMdd"));
        DateAttribute dateAttribute2 = (DateAttribute) attributes.get("Date2");
        assertNotNull(dateAttribute2);
        assertEquals("20120321", DateConverter.getFormattedDate(dateAttribute2.getDate(), "yyyyMMdd"));
        EnumeratorAttribute enumeratorAttribute = (EnumeratorAttribute) attributes.get("Enumerator");
        assertNotNull(enumeratorAttribute);
        assertEquals("a", enumeratorAttribute.getText());
        EnumeratorMapAttribute enumeratorMapAttribute = (EnumeratorMapAttribute) attributes.get("EnumeratorMap");
        assertNotNull(enumeratorMapAttribute);
        assertEquals("02", enumeratorMapAttribute.getMapKey());
        assertEquals("Value 2", enumeratorMapAttribute.getMapValue());
        HypertextAttribute hypertextAttribute = (HypertextAttribute) attributes.get("Hypertext");
        assertNotNull(hypertextAttribute);
        assertEquals("<p>text Hypertext</p>", hypertextAttribute.getTextForLang("it"));
        TextAttribute longtextAttribute = (TextAttribute) attributes.get("Longtext");
        assertNotNull(longtextAttribute);
        assertEquals("text Longtext", longtextAttribute.getTextForLang("it"));
        MonoTextAttribute monoTextAttribute = (MonoTextAttribute) attributes.get("Monotext");
        assertNotNull(monoTextAttribute);
        assertEquals("text Monotext", monoTextAttribute.getText());
        MonoTextAttribute monoTextAttribute2 = (MonoTextAttribute) attributes.get("Monotext2");
        assertNotNull(monoTextAttribute2);
        assertEquals("aaaa@entando.com", monoTextAttribute2.getText());
        NumberAttribute numberAttribute = (NumberAttribute) attributes.get("Number");
        assertNotNull(numberAttribute);
        assertEquals(25, numberAttribute.getValue().intValue());
        NumberAttribute numberAttribute2 = (NumberAttribute) attributes.get("Number2");
        assertNotNull(numberAttribute2);
        assertEquals(85, numberAttribute2.getValue().intValue());
        TextAttribute textAttribute = (TextAttribute) attributes.get("Text");
        assertNotNull(textAttribute);
        assertEquals("text Text", textAttribute.getTextForLang("it"));
        TextAttribute textAttribute2 = (TextAttribute) attributes.get("Text2");
        assertNotNull(textAttribute2);
        assertEquals("bbbb@entando.com", textAttribute2.getTextForLang("it"));
        ThreeStateAttribute threeStateAttribute = (ThreeStateAttribute) attributes.get("ThreeState");
        assertNotNull(threeStateAttribute);
        assertEquals(Boolean.FALSE, threeStateAttribute.getBooleanValue());
        CompositeAttribute compositeAttribute = (CompositeAttribute) attributes.get("Composite");
        assertNotNull(compositeAttribute);
        assertEquals(10, compositeAttribute.getAttributeMap().size());
        ListAttribute listAttribute1 = (ListAttribute) attributes.get("ListBoolea");
        assertNotNull(listAttribute1);
        assertEquals(2, listAttribute1.getAttributeList("it").size());
        ListAttribute listAttribute2 = (ListAttribute) attributes.get("ListCheck");
        assertNotNull(listAttribute2);
        assertEquals(2, listAttribute2.getAttributeList("it").size());
        ListAttribute listAttribute3 = (ListAttribute) attributes.get("ListDate");
        assertNotNull(listAttribute3);
        assertEquals(2, listAttribute3.getAttributeList("it").size());
        ListAttribute listAttribute4 = (ListAttribute) attributes.get("ListEnum");
        assertNotNull(listAttribute4);
        assertEquals(2, listAttribute4.getAttributeList("it").size());
        ListAttribute listAttribute5 = (ListAttribute) attributes.get("ListMonot");
        assertNotNull(listAttribute5);
        assertEquals(2, listAttribute5.getAttributeList("it").size());
        ListAttribute listAttribute6 = (ListAttribute) attributes.get("ListNumber");
        assertNotNull(listAttribute6);
        assertEquals(2, listAttribute6.getAttributeList("it").size());
        ListAttribute listAttribute7 = (ListAttribute) attributes.get("List3Stat");
        assertNotNull(listAttribute7);
        assertEquals(3, listAttribute7.getAttributeList("it").size());
        MonoListAttribute monoListAttribute2 = (MonoListAttribute) attributes.get("MonoLBool");
        assertNotNull(monoListAttribute2);
        assertEquals(2, monoListAttribute2.getAttributes().size());
        MonoListAttribute monoListAttribute3 = (MonoListAttribute) attributes.get("MonoLChec");
        assertNotNull(monoListAttribute3);
        assertEquals(2, monoListAttribute3.getAttributes().size());
        MonoListAttribute monoListAttribute4 = (MonoListAttribute) attributes.get("MonoLCom");
        assertNotNull(monoListAttribute4);
        assertEquals(1, monoListAttribute4.getAttributes().size());
        MonoListAttribute monoListAttribute5 = (MonoListAttribute) attributes.get("MonoLCom2");
        assertNotNull(monoListAttribute5);
        assertEquals(2, monoListAttribute5.getAttributes().size());
        MonoListAttribute monoListAttribute6 = (MonoListAttribute) attributes.get("MonoLDate");
        assertNotNull(monoListAttribute6);
        assertEquals(2, monoListAttribute6.getAttributes().size());
        MonoListAttribute monoListAttribute7 = (MonoListAttribute) attributes.get("MonoLEnum");
        assertNotNull(monoListAttribute7);
        assertEquals(2, monoListAttribute7.getAttributes().size());
        MonoListAttribute monoListAttribute8 = (MonoListAttribute) attributes.get("MonoLHyper");
        assertNotNull(monoListAttribute8);
        assertEquals(2, monoListAttribute8.getAttributes().size());
        MonoListAttribute monoListAttribute11 = (MonoListAttribute) attributes.get("MonoLLong");
        assertNotNull(monoListAttribute11);
        assertEquals(1, monoListAttribute11.getAttributes().size());
        MonoListAttribute monoListAttribute12 = (MonoListAttribute) attributes.get("MonoLMonot");
        assertNotNull(monoListAttribute12);
        assertEquals(2, monoListAttribute12.getAttributes().size());
        MonoListAttribute monoListAttribute13 = (MonoListAttribute) attributes.get("MonoLNumb");
        assertNotNull(monoListAttribute13);
        assertEquals(2, monoListAttribute13.getAttributes().size());
        MonoListAttribute monoListAttribute14 = (MonoListAttribute) attributes.get("MonoLText");
        assertNotNull(monoListAttribute14);
        assertEquals(2, monoListAttribute14.getAttributes().size());
        MonoListAttribute monoListAttribute15 = (MonoListAttribute) attributes.get("MonoL3stat");
        assertNotNull(monoListAttribute15);
        assertEquals(3, monoListAttribute15.getAttributes().size());
        EnumeratorMapAttribute enumeratorMapAttribute2 = (EnumeratorMapAttribute) attributes.get("EnumeratorMapBis");
        assertNotNull(enumeratorMapAttribute2);
        assertEquals("01", enumeratorMapAttribute2.getMapKey());
        assertEquals("Value 1 Bis", enumeratorMapAttribute2.getMapValue());
    }

    public void testGetDataObjectTypes() {
        Map<String, SmallDataType> smallDataTypes = _dataObjectManager.getSmallDataTypesMap();
        assertEquals(4, smallDataTypes.size());
    }

    public void testCreateDataObject() {
        DataObject dataObjectType = _dataObjectManager.createDataObject("ART");
        assertNotNull(dataObjectType);
    }

    public void testCreateDataObjectWithViewPage() {
        DataObject dataObject = _dataObjectManager.createDataObject("ART");
        String viewPage = dataObject.getViewPage();
        assertEquals(viewPage, "dataObjectview");
    }

    public void testCreateDataObjectWithDefaultModel() {
        DataObject dataObject = _dataObjectManager.createDataObject("ART");
        String defaultModel = dataObject.getDefaultModel();
        assertEquals(defaultModel, "1");
    }

    public void testGetXML() throws Throwable {
        DataObject dataObject = this._dataObjectManager.createDataObject("ART");
        dataObject.setId("ART1");
        dataObject.setTypeCode("Articolo");
        dataObject.setTypeDescription("Articolo");
        dataObject.setDescription("descrizione");
        dataObject.setStatus(DataObject.STATUS_DRAFT);
        dataObject.setMainGroup("free");
        Category cat13 = new Category();
        cat13.setCode("13");
        dataObject.addCategory(cat13);
        Category cat19 = new Category();
        cat19.setCode("19");
        dataObject.addCategory(cat19);
        String xml = dataObject.getXML();
        assertNotNull(xml);
        assertTrue(xml.indexOf("<dataObject id=\"ART1\" typecode=\"Articolo\" typedescr=\"Articolo\">") != -1);
        assertTrue(xml.indexOf("<descr>descrizione</descr>") != -1);
        assertTrue(xml.indexOf("<status>" + DataObject.STATUS_DRAFT + "</status>") != -1);
        assertTrue(xml.indexOf("<category id=\"13\" />") != -1);
        assertTrue(xml.indexOf("<category id=\"19\" />") != -1);
    }

    public void testLoadDataObjects() throws ApsSystemException {
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId(null, null, null);
        assertEquals(15, dataObjects.size());
    }

    public void testLoadEvents_1() throws ApsSystemException {
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, null, null);
        String[] expectedFreeDataObjectsId = {"EVN194", "EVN193",
            "EVN24", "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
        assertEquals(expectedFreeDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedFreeDataObjectsId.length; i++) {
            assertTrue(dataObjects.contains(expectedFreeDataObjectsId[i]));
        }
        assertFalse(dataObjects.contains("EVN103"));

        List<String> groups = new ArrayList<String>();
        groups.add("coach");
        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, null, groups);
        assertEquals(expectedFreeDataObjectsId.length + 2, dataObjects.size());
        for (int i = 0; i < expectedFreeDataObjectsId.length; i++) {
            assertTrue(dataObjects.contains(expectedFreeDataObjectsId[i]));
        }
        assertTrue(dataObjects.contains("EVN103"));
        assertTrue(dataObjects.contains("EVN41"));
    }

    public void testLoadEvents_2() throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add("coach");
        groups.add(Group.ADMINS_GROUP_NAME);
        Date start = DateConverter.parseDate("1997-06-10", "yyyy-MM-dd");
        Date end = DateConverter.parseDate("2020-09-19", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, start, end);
        EntitySearchFilter filter2 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "Even", true);
        filter2.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter, filter2};

        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        assertEquals(2, dataObjects.size());
        assertEquals("EVN193", dataObjects.get(0));
        assertEquals("EVN192", dataObjects.get(1));

        filter2 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false);
        filter2.setOrder(EntitySearchFilter.DESC_ORDER);

        EntitySearchFilter[] filters2 = {filter, filter2};
        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters2, groups);

        String[] expectedOrderedDataObjectsId = {"EVN25", "EVN21", "EVN20", "EVN41", "EVN193",
            "EVN192", "EVN103", "EVN23", "EVN24"};
        assertEquals(expectedOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId.length; i++) {
            assertEquals(expectedOrderedDataObjectsId[i], dataObjects.get(i));
        }

        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters2, null);
        String[] expectedFreeOrderedDataObjectsId = {"EVN25", "EVN21", "EVN20", "EVN193",
            "EVN192", "EVN23", "EVN24"};
        assertEquals(expectedFreeOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedFreeOrderedDataObjectsId.length; i++) {
            assertEquals(expectedFreeOrderedDataObjectsId[i], dataObjects.get(i));
        }
    }

    public void testLoadEvents_2_1() throws ApsSystemException {
        //forcing case insensitive search
        DataObjectSearcherDAO searcherDao = (DataObjectSearcherDAO) this.getApplicationContext().getBean("DataObjectSearcherDAO");

        List<String> groups = new ArrayList<String>();
        groups.add("coach");
        groups.add(Group.ADMINS_GROUP_NAME);
        Date start = DateConverter.parseDate("1997-06-10", "yyyy-MM-dd");
        Date end = DateConverter.parseDate("2020-09-19", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, start, end);
        EntitySearchFilter filter2 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "even", true);
        filter2.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter, filter2};

        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        assertEquals(2, dataObjects.size());
        assertEquals("EVN193", dataObjects.get(0));
        assertEquals("EVN192", dataObjects.get(1));
    }

    public void testLoadEvents_2_2() throws ApsSystemException {
        //forcing case sensitive search
        DataObjectSearcherDAO searcherDao = (DataObjectSearcherDAO) this.getApplicationContext().getBean("DataObjectSearcherDAO");

        List<String> groups = new ArrayList<String>();
        groups.add("coach");
        groups.add(Group.ADMINS_GROUP_NAME);
        Date start = DateConverter.parseDate("1997-06-10", "yyyy-MM-dd");
        Date end = DateConverter.parseDate("2020-09-19", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, start, end);
        EntitySearchFilter filter_x1 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "even", true);
        filter_x1.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters_1 = {filter, filter_x1};

        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters_1, groups);
        assertEquals(2, dataObjects.size());

        EntitySearchFilter filter_x2 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, "Even", true);
        filter_x2.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters_2 = {filter, filter_x2};

        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters_2, groups);
        assertEquals(2, dataObjects.size());
        assertEquals("EVN193", dataObjects.get(0));
        assertEquals("EVN192", dataObjects.get(1));
    }

    public void testLoadEvents_3() throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        Date value = DateConverter.parseDate("1999-04-14", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, value, false);
        EntitySearchFilter[] filters = {filter};

        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        assertEquals(1, dataObjects.size());
        assertEquals("EVN192", dataObjects.get(0));
    }

    public void testLoadEvents_4() throws ApsSystemException {
        this.testLoadEvents_4(true);
        this.testLoadEvents_4(false);
    }

    protected void testLoadEvents_4(boolean useRoleFilter) throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        EntitySearchFilter filter1 = (useRoleFilter)
                ? EntitySearchFilter.createRoleFilter(SystemConstants.DATA_TYPE_ATTRIBUTE_ROLE_TITLE, "Ce", "TF")
                : new EntitySearchFilter("Titolo", true, "Ce", "TF");
        filter1.setLangCode("it");
        filter1.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters1 = {filter1};
        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId("EVN", null, filters1, groups);
        String[] expectedOrderedDataObjectsId = {"EVN25", "EVN41", "EVN20", "EVN21", "EVN23"};
        assertEquals(expectedOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId.length; i++) {
            assertEquals(expectedOrderedDataObjectsId[i], dataObjects.get(i));
        }
        filter1 = new EntitySearchFilter("Titolo", true, null, "TF");
        filter1.setLangCode("it");
        filter1.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters2 = {filter1};
        dataObjects = this._dataObjectManager.loadDataObjectsId("EVN", null, filters2, groups);
        String[] expectedOrderedDataObjectsId2 = {"EVN25", "EVN41", "EVN20", "EVN21", "EVN23", "EVN24"};
        assertEquals(expectedOrderedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedOrderedDataObjectsId2[i], dataObjects.get(i));
        }
    }

    public void testLoadEvents_5() throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        List<Date> allowedDates = new ArrayList<Date>();
        allowedDates.add(DateConverter.parseDate("1999-04-14", "yyyy-MM-dd"));//EVN192
        allowedDates.add(DateConverter.parseDate("2008-02-13", "yyyy-MM-dd"));//EVN23
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, allowedDates, false);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        String[] expectedOrderedDataObjectsId2 = {"EVN23", "EVN192"};
        assertEquals(expectedOrderedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedOrderedDataObjectsId2[i], dataObjects.get(i));
        }
    }

    public void testLoadEvents_6() throws ApsSystemException {
        this.testLoadEvents_6(true);
        this.testLoadEvents_6(false);
    }

    protected void testLoadEvents_6(boolean useRoleFilter) throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        List<String> allowedDescription = new ArrayList<String>();
        allowedDescription.add("Mostra");//EVN21, EVN20
        allowedDescription.add("Collezione");//EVN23
        EntitySearchFilter filter = (useRoleFilter)
                ? EntitySearchFilter.createRoleFilter(SystemConstants.DATA_TYPE_ATTRIBUTE_ROLE_TITLE, allowedDescription, true)
                : new EntitySearchFilter("Titolo", true, allowedDescription, true);
        filter.setLangCode("it");
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        String[] expectedOrderedDataObjectsId2 = {"EVN20", "EVN21", "EVN23"};
        assertEquals(expectedOrderedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedOrderedDataObjectsId2[i], dataObjects.get(i));
        }
    }

    public void testLoadEvents_7() throws ApsSystemException {
        this.testLoadEvents_7(true);
        this.testLoadEvents_7(false);
    }

    protected void testLoadEvents_7(boolean useRoleFilter) throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        List<String> allowedDescription = new ArrayList<String>();
        allowedDescription.add("Mostra Zootecnica");//EVN20
        allowedDescription.add("Title B - Event 2");//EVN192
        EntitySearchFilter filter1 = (useRoleFilter)
                ? EntitySearchFilter.createRoleFilter(SystemConstants.DATA_TYPE_ATTRIBUTE_ROLE_TITLE, allowedDescription, false)
                : new EntitySearchFilter("Titolo", true, allowedDescription, false);
        filter1.setLangCode("en");
        EntitySearchFilter filter2 = new EntitySearchFilter("DataInizio", true);
        filter2.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter1, filter2};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        String[] expectedOrderedDataObjectsId2 = {"EVN192", "EVN20"};
        assertEquals(expectedOrderedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedOrderedDataObjectsId2[i], dataObjects.get(i));
        }
    }

    public void testLoadEvents_8() throws ApsSystemException {
        this.testLoadEvents_8(true);
        this.testLoadEvents_8(false);
    }

    protected void testLoadEvents_8(boolean useRoleFilter) throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        List<String> allowedDescription = new ArrayList<String>();
        allowedDescription.add("Castello");//EVN24
        allowedDescription.add("dei bambini");//EVN24
        EntitySearchFilter filter = (useRoleFilter)
                ? EntitySearchFilter.createRoleFilter(SystemConstants.DATA_TYPE_ATTRIBUTE_ROLE_TITLE, allowedDescription, true)
                : new EntitySearchFilter("Titolo", true, allowedDescription, true);
        filter.setLangCode("it");
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        String[] expectedOrderedDataObjectsId2 = {"EVN24"};
        assertEquals(expectedOrderedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedOrderedDataObjectsId2[i], dataObjects.get(i));
        }
    }

    public void testLoadEvents_9_a() throws ApsSystemException {
        this.testLoadEvents_9_a(true);
        this.testLoadEvents_9_a(false);
    }


    protected void testLoadEvents_9_a(boolean useRoleFilter) throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        EntitySearchFilter filter = (useRoleFilter)
                ? EntitySearchFilter.createRoleFilter(SystemConstants.DATA_TYPE_ATTRIBUTE_ROLE_TITLE, "le", true)
                : new EntitySearchFilter("Titolo", true, "le", true);
        filter.setLangCode("it");
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        String[] expectedOrderedDataObjectsId2 = {"EVN25", "EVN21", "EVN23"};
        assertEquals(expectedOrderedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedOrderedDataObjectsId2[i], dataObjects.get(i));
        }
    }

    public void testLoadEvents_9_b() throws ApsSystemException {
        this.testLoadEvents_9_b(true);
        this.testLoadEvents_9_b(false);
    }

    protected void testLoadEvents_9_b(boolean useRoleFilter) throws ApsSystemException {
        //forcing case insensitive search
        DataObjectSearcherDAO searcherDao = (DataObjectSearcherDAO) this.getApplicationContext().getBean("DataObjectSearcherDAO");
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        EntitySearchFilter filter = (useRoleFilter)
                ? EntitySearchFilter.createRoleFilter(SystemConstants.DATA_TYPE_ATTRIBUTE_ROLE_TITLE, "le", true)
                : new EntitySearchFilter("Titolo", true, "le", true);
        filter.setLangCode("it");
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        String[] expectedOrderedDataObjectsId2 = {"EVN25", "EVN21", "EVN23"};
        assertEquals(expectedOrderedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedOrderedDataObjectsId2[i], dataObjects.get(i));
        }
    }





    public void testLoadEvents_1_b() throws ApsSystemException {
        //forcing case insensitive search
        DataObjectSearcherDAO searcherDao = (DataObjectSearcherDAO) this.getApplicationContext().getBean("DataObjectSearcherDAO");

        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        List<String> allowedDescription = new ArrayList<String>();
        allowedDescription.add("descrizione");//"ART179" "ART180" "ART187"
        allowedDescription.add("on line");//"ART179"
        allowedDescription.add("customers");//"ART102" "RAH101"
        EntitySearchFilter filter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false, allowedDescription, true);
        EntitySearchFilter filter2 = new EntitySearchFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY, false);
        filter2.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter, filter2};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId(null, filters, groups);
        String[] expectedOrderedDataObjectsId2 = {"ART102", "ART180", "ART187", "RAH101"};
        assertEquals(expectedOrderedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedOrderedDataObjectsId2[i], dataObjects.get(i));
        }
    }

    public void testLoadOrderedEvents_1() throws ApsSystemException {
        EntitySearchFilter filterForDescr = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_DESCR_FILTER_KEY, false);
        filterForDescr.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filterForDescr};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);

        String[] expectedFreeDataObjectsId = {"EVN24", "EVN23", "EVN191",
            "EVN192", "EVN193", "EVN194", "EVN20", "EVN21", "EVN25"};
        assertEquals(expectedFreeDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedFreeDataObjectsId.length; i++) {
            assertEquals(expectedFreeDataObjectsId[i], dataObjects.get(i));
        }

        filterForDescr.setOrder(EntitySearchFilter.DESC_ORDER);
        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);

        assertEquals(expectedFreeDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedFreeDataObjectsId.length; i++) {
            assertEquals(expectedFreeDataObjectsId[expectedFreeDataObjectsId.length - i - 1], dataObjects.get(i));
        }
    }

    public void testLoadOrderedEvents_2() throws ApsSystemException {
        EntitySearchFilter filterForCreation = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        filterForCreation.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filterForCreation};

        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);
        String[] expectedFreeOrderedDataObjectsId = {"EVN191", "EVN192", "EVN193", "EVN194",
            "EVN20", "EVN23", "EVN24", "EVN25", "EVN21"};
        assertEquals(expectedFreeOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedFreeOrderedDataObjectsId.length; i++) {
            assertEquals(expectedFreeOrderedDataObjectsId[i], dataObjects.get(i));
        }

        filterForCreation.setOrder(EntitySearchFilter.DESC_ORDER);
        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);
        assertEquals(expectedFreeOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedFreeOrderedDataObjectsId.length; i++) {
            assertEquals(expectedFreeOrderedDataObjectsId[expectedFreeOrderedDataObjectsId.length - i - 1], dataObjects.get(i));
        }
    }

    public void testLoadOrderedEvents_3() throws ApsSystemException {
        EntitySearchFilter filterForCreation = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
        filterForCreation.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter filterForDate = new EntitySearchFilter("DataInizio", true);
        filterForDate.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filterForCreation, filterForDate};

        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);
        String[] expectedFreeOrderedDataObjectsId = {"EVN21", "EVN25", "EVN24", "EVN23",
            "EVN20", "EVN194", "EVN193", "EVN192", "EVN191"};
        assertEquals(expectedFreeOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedFreeOrderedDataObjectsId.length; i++) {
            assertEquals(expectedFreeOrderedDataObjectsId[i], dataObjects.get(i));
        }

        EntitySearchFilter[] filters2 = {filterForDate, filterForCreation};

        List<String> dataObjects2 = _dataObjectManager.loadDataObjectsId("EVN", null, filters2, null);
        String[] expectedFreeOrderedDataObjectsId2 = {"EVN194", "EVN193", "EVN24",
            "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
        assertEquals(expectedFreeOrderedDataObjectsId2.length, dataObjects2.size());
        for (int i = 0; i < expectedFreeOrderedDataObjectsId2.length; i++) {
            assertEquals(expectedFreeOrderedDataObjectsId2[i], dataObjects2.get(i));
        }
    }

    public void testLoadOrderedEvents_4() throws Throwable {
        DataObject masterDataObject = this._dataObjectManager.loadDataObject("EVN193", true);
        masterDataObject.setId(null);
        DateAttribute dateAttribute = (DateAttribute) masterDataObject.getAttribute("DataInizio");
        dateAttribute.setDate(DateConverter.parseDate("17/06/2019", "dd/MM/yyyy"));
        try {
            this._dataObjectManager.saveDataObject(masterDataObject);
            this._dataObjectManager.insertDataObject(masterDataObject);
            this.waitNotifyingThread();

            EntitySearchFilter filterForDate = new EntitySearchFilter("DataInizio", true);
            filterForDate.setOrder(EntitySearchFilter.DESC_ORDER);
            EntitySearchFilter[] filters = {filterForDate};

            List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);
            String[] expectedFreeOrderedDataObjectsId = {"EVN194", masterDataObject.getId(), "EVN193", "EVN24",
                "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191"};
            assertEquals(expectedFreeOrderedDataObjectsId.length, dataObjects.size());
            for (int i = 0; i < expectedFreeOrderedDataObjectsId.length; i++) {
                assertEquals(expectedFreeOrderedDataObjectsId[i], dataObjects.get(i));
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            if (null != masterDataObject.getId() && !"EVN193".equals(masterDataObject.getId())) {
                this._dataObjectManager.removeDataObject(masterDataObject);
                this._dataObjectManager.deleteDataObject(masterDataObject);
            }
        }
    }

    public void testLoadFutureEvents_1() throws ApsSystemException {
        Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);
        String[] expectedOrderedDataObjectsId = {"EVN21", "EVN20", "EVN25", "EVN23",
            "EVN24", "EVN193", "EVN194"};
        assertEquals(expectedOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId.length; i++) {
            assertEquals(expectedOrderedDataObjectsId[i], dataObjects.get(i));
        }
    }

    public void testLoadFutureEvents_2() throws ApsSystemException {
        Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);
        String[] expectedOrderedDataObjectsId = {"EVN194", "EVN193", "EVN24",
            "EVN23", "EVN25", "EVN20", "EVN21"};
        assertEquals(expectedOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId.length; i++) {
            assertEquals(expectedOrderedDataObjectsId[i], dataObjects.get(i));
        }
    }

    public void testLoadFutureEvents_3() throws ApsSystemException {
        Date today = DateConverter.parseDate("2005-01-01", "yyyy-MM-dd");
        List<String> groups = new ArrayList<String>();
        groups.add("coach");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, today, null);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        String[] expectedOrderedDataObjectsId = {"EVN194", "EVN193", "EVN24",
            "EVN23", "EVN41", "EVN25", "EVN20", "EVN21"};
        assertEquals(expectedOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId.length; i++) {
            assertEquals(expectedOrderedDataObjectsId[i], dataObjects.get(i));
        }
    }

    public void testLoadPastEvents_1() throws ApsSystemException {
        Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");

        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter};

        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);
        String[] expectedOrderedDataObjectsId = {"EVN191", "EVN192",
            "EVN21", "EVN20", "EVN25", "EVN23"};
        assertEquals(expectedOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId.length; i++) {
            assertEquals(expectedOrderedDataObjectsId[i], dataObjects.get(i));
        }
    }

    public void testLoadPastEvents_2() throws ApsSystemException {
        Date today = DateConverter.parseDate("2008-10-01", "yyyy-MM-dd");

        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.DESC_ORDER);
        EntitySearchFilter[] filters = {filter};

        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, null);
        String[] expectedOrderedDataObjectsId = {"EVN23", "EVN25",
            "EVN20", "EVN21", "EVN192", "EVN191"};
        assertEquals(expectedOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId.length; i++) {
            assertEquals(expectedOrderedDataObjectsId[i], dataObjects.get(i));
        }
    }

    public void testLoadPastEvents_3() throws ApsSystemException {
        Date today = DateConverter.parseDate("2008-02-13", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter};

        List<String> groups = new ArrayList<String>();
        groups.add("coach");
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, filters, groups);
        String[] expectedOrderedDataObjectsId = {"EVN191", "EVN192", "EVN103",
            "EVN21", "EVN20", "EVN25", "EVN41", "EVN23"};
        assertEquals(expectedOrderedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedOrderedDataObjectsId.length; i++) {
            assertEquals(expectedOrderedDataObjectsId[i], dataObjects.get(i));
        }
    }

    public void testLoadDataObjectsForCategory_1() throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        String[] categories1 = {"general_cat1"};
        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(categories1, null, groups);
        assertEquals(2, dataObjects.size());
//        assertTrue(dataObjects.contains("ART179"));
//        assertTrue(dataObjects.contains("ART180"));
        assertTrue(dataObjects.contains("ART102"));
        assertTrue(dataObjects.contains("ART111"));
//        assertTrue(dataObjects.contains("EVN192"));

        String[] categories2 = {"general_cat1", "general_cat2"};

        dataObjects = this._dataObjectManager.loadDataObjectsId(categories2, null, groups);
        assertEquals(1, dataObjects.size());
        assertTrue(dataObjects.contains("ART111"));
//        assertTrue(dataObjects.contains("ART179"));
    }

    public void testLoadDataObjectsForCategory_2() throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        String[] categories1 = {"general_cat1"};
        EntitySearchFilter filter1 = new EntitySearchFilter(IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", false);
        EntitySearchFilter[] filters = {filter1};
        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(categories1, filters, groups);
        assertEquals(2, dataObjects.size());
        assertTrue(dataObjects.contains("ART102"));
        assertTrue(dataObjects.contains("ART111"));
//        assertTrue(dataObjects.contains("ART180"));
//        assertTrue(dataObjects.contains("ART179"));

        String[] categories2 = {"general_cat2"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories2, filters, groups);
        assertEquals(2, dataObjects.size());
        assertTrue(dataObjects.contains("ART111"));
//        assertTrue(dataObjects.contains("ART112"));
        assertTrue(dataObjects.contains("ART120"));
//        assertTrue(dataObjects.contains("ART179"));

        String[] categories12 = {"general_cat1", "general_cat2"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories12, false, filters, groups);
        assertEquals(1, dataObjects.size());
        assertTrue(dataObjects.contains("ART111"));
//        assertTrue(dataObjects.contains("ART179"));
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories12, true, filters, groups);
        assertEquals(3, dataObjects.size());
        assertTrue(dataObjects.contains("ART102"));
        assertTrue(dataObjects.contains("ART111"));
//        assertTrue(dataObjects.contains("ART112"));
        assertTrue(dataObjects.contains("ART120"));
//        assertTrue(dataObjects.contains("ART180"));

        String[] categories3 = {"general_cat3"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories3, filters, groups);
        assertEquals(2, dataObjects.size());
        assertTrue(dataObjects.contains("ART120"));
//        assertTrue(dataObjects.contains("ART121"));
        assertTrue(dataObjects.contains("ART122"));

        String[] categories23 = {"general_cat2", "general_cat3"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories23, false, filters, groups);
        assertEquals(1, dataObjects.size());
        assertTrue(dataObjects.contains("ART120"));
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories23, true, filters, groups);
        assertEquals(3, dataObjects.size());
        assertTrue(dataObjects.contains("ART111"));
//        assertTrue(dataObjects.contains("ART112"));
        assertTrue(dataObjects.contains("ART120"));
//        assertTrue(dataObjects.contains("ART179"));
//        assertTrue(dataObjects.contains("ART121"));
        assertTrue(dataObjects.contains("ART122"));

        String[] categories123 = {"general_cat1", "general_cat2", "general_cat3"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories123, false, filters, groups);
        assertEquals(0, dataObjects.size());
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories123, true, filters, groups);
        assertEquals(4, dataObjects.size());
        assertTrue(dataObjects.contains("ART102"));
        assertTrue(dataObjects.contains("ART111"));
//        assertTrue(dataObjects.contains("ART112"));
        assertTrue(dataObjects.contains("ART120"));
//        assertTrue(dataObjects.contains("ART121"));
        assertTrue(dataObjects.contains("ART122"));
//        assertTrue(dataObjects.contains("ART180"));
//        assertTrue(dataObjects.contains("ART179"));
    }

    public void testLoadDataObjectsForCategory() throws ApsSystemException {
        String[] categories1 = {"evento"};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId(categories1, null, null);
        assertEquals(2, dataObjects.size());
        assertTrue(dataObjects.contains("EVN192"));
        assertTrue(dataObjects.contains("EVN193"));

        String[] categories2 = {"cat1"};
        dataObjects = _dataObjectManager.loadDataObjectsId(categories2, null, null);
        assertEquals(1, dataObjects.size());
        assertTrue(dataObjects.contains("ART180"));
    }

    public void testLoadEventsForCategory_1() throws ApsSystemException {
        String[] categories = {"evento"};
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", categories, null, null);
        assertEquals(2, dataObjects.size());
        assertTrue(dataObjects.contains("EVN192"));
        assertTrue(dataObjects.contains("EVN193"));

        Date today = DateConverter.parseDate("2005-02-13", "yyyy-MM-dd");
        EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true, null, today);
        filter.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter};
        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", categories, filters, null);
        assertEquals(1, dataObjects.size());
        assertTrue(dataObjects.contains("EVN192"));
    }

    public void testLoadEventsForCategory_2() throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        String[] categories1 = {"general_cat1"};
        EntitySearchFilter filter1 = new EntitySearchFilter(IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", false);
        EntitySearchFilter[] filters = {filter1};
        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(categories1, filters, groups);
        assertEquals(2, dataObjects.size());
        assertTrue(dataObjects.contains("ART102"));
        assertTrue(dataObjects.contains("ART111"));

        String[] categories2 = {"general_cat2"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories2, filters, groups);
        assertEquals(2, dataObjects.size());
        assertTrue(dataObjects.contains("ART111"));
        assertTrue(dataObjects.contains("ART120"));

        String[] categories12 = {"general_cat1", "general_cat2"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories12, false, filters, groups);
        assertEquals(1, dataObjects.size());
        assertTrue(dataObjects.contains("ART111"));
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories12, true, filters, groups);
        assertEquals(3, dataObjects.size());
        assertTrue(dataObjects.contains("ART102"));
        assertTrue(dataObjects.contains("ART111"));
        assertTrue(dataObjects.contains("ART120"));

        String[] categories3 = {"general_cat3"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories3, filters, groups);
        assertEquals(2, dataObjects.size());
        assertTrue(dataObjects.contains("ART120"));
        assertTrue(dataObjects.contains("ART122"));

        String[] categories23 = {"general_cat2", "general_cat3"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories23, false, filters, groups);
        assertEquals(1, dataObjects.size());
        assertTrue(dataObjects.contains("ART120"));
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories23, true, filters, groups);
        assertEquals(3, dataObjects.size());
        assertTrue(dataObjects.contains("ART111"));
        assertTrue(dataObjects.contains("ART120"));
        assertTrue(dataObjects.contains("ART122"));

        String[] categories123 = {"general_cat1", "general_cat2", "general_cat3"};
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories123, false, filters, groups);
        assertEquals(0, dataObjects.size());
        dataObjects = this._dataObjectManager.loadDataObjectsId(categories123, true, filters, groups);
        assertEquals(4, dataObjects.size());
        assertTrue(dataObjects.contains("ART102"));
        assertTrue(dataObjects.contains("ART111"));
        assertTrue(dataObjects.contains("ART120"));
        assertTrue(dataObjects.contains("ART122"));
    }

    public void testLoadEventsForGroup() throws ApsSystemException {
        List<String> dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, null, null);
        String[] expectedFreeDataObjectsId = {"EVN191", "EVN192", "EVN193", "EVN194",
            "EVN20", "EVN23", "EVN21", "EVN24", "EVN25"};
        assertEquals(expectedFreeDataObjectsId.length, dataObjects.size());

        for (int i = 0; i < expectedFreeDataObjectsId.length; i++) {
            assertTrue(dataObjects.contains(expectedFreeDataObjectsId[i]));
        }

        Collection<String> allowedGroup = new HashSet<String>();
        allowedGroup.add(Group.FREE_GROUP_NAME);
        allowedGroup.add("customers");

        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, null, allowedGroup);
        assertEquals(expectedFreeDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedFreeDataObjectsId.length; i++) {
            assertTrue(dataObjects.contains(expectedFreeDataObjectsId[i]));
        }
        assertFalse(dataObjects.contains("EVN103"));//evento coach

        allowedGroup.remove("customers");
        allowedGroup.remove(Group.FREE_GROUP_NAME);
        allowedGroup.add(Group.ADMINS_GROUP_NAME);

        dataObjects = _dataObjectManager.loadDataObjectsId("EVN", null, null, allowedGroup);
        assertEquals(11, dataObjects.size());
        for (int i = 0; i < expectedFreeDataObjectsId.length; i++) {
            assertTrue(dataObjects.contains(expectedFreeDataObjectsId[i]));
        }
        assertTrue(dataObjects.contains("EVN103"));
        assertTrue(dataObjects.contains("EVN41"));
    }

    public void testLoadDataObjectsByAttribute_1() throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);

        EntitySearchFilter filter0 = new EntitySearchFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY, false);
        filter0.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter filter1 = new EntitySearchFilter(IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "ART", true);
        EntitySearchFilter filter2 = new EntitySearchFilter("Numero", true);
        filter2.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter0, filter1, filter2};
        String[] expectedDataObjectsId = {"ART120", "ART121"};

        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters, groups);
        assertEquals(expectedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedDataObjectsId.length; i++) {
            assertEquals(expectedDataObjectsId[i], dataObjects.get(i));
        }

        filter2.setNullOption(true);
        EntitySearchFilter[] filters2 = {filter0, filter1, filter2};
        String[] expectedDataObjectsId2 = {"ART1", "ART102", "ART104",
            "ART111", "ART112", "ART122", "ART180", "ART187"};

        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters2, groups);
        assertEquals(expectedDataObjectsId2.length, dataObjects.size());
        for (int i = 0; i < expectedDataObjectsId2.length; i++) {
            assertEquals(expectedDataObjectsId2[i], dataObjects.get(i));
        }
    }

    public void testLoadDataObjectsByAttribute_2() throws ApsSystemException {
        List<String> groups = new ArrayList<String>();
        groups.add(Group.ADMINS_GROUP_NAME);
        EntitySearchFilter filter0 = new EntitySearchFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY, false);
        filter0.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter filter1 = new EntitySearchFilter(IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN", true);
        EntitySearchFilter filter2 = new EntitySearchFilter("Titolo", true);
        filter2.setOrder(EntitySearchFilter.ASC_ORDER);
        EntitySearchFilter[] filters = {filter0, filter1, filter2};
        String[] expectedDataObjectsId = {"EVN103", "EVN191", "EVN192",
            "EVN193", "EVN194", "EVN20", "EVN21", "EVN23", "EVN24", "EVN25", "EVN41"};

        List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters, groups);
        assertEquals(expectedDataObjectsId.length, dataObjects.size());
        for (int i = 0; i < expectedDataObjectsId.length; i++) {
            assertEquals(expectedDataObjectsId[i], dataObjects.get(i));
        }

        filter2.setNullOption(true);
        EntitySearchFilter[] filters2 = {filter0, filter1, filter2};

        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters2, groups);
        assertEquals(0, dataObjects.size());

        filter2.setLangCode("it");
        EntitySearchFilter[] filters3 = {filter0, filter1, filter2};
        dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters3, groups);
        assertEquals(0, dataObjects.size());
    }

    public void testLoadDataObjectsByAttribute_3() throws Throwable {
        List<String> groups = new ArrayList<String>();
        String[] masterDataObjectIds = {"EVN193", "EVN191", "EVN192", "EVN194", "EVN23", "EVN24"};
        String[] newDataObjectIds = null;
        try {
            newDataObjectIds = this.addDraftDataObjectsForTest(masterDataObjectIds, false);
            for (int i = 0; i < newDataObjectIds.length; i++) {
                DataObject dataObject = this._dataObjectManager.loadDataObject(newDataObjectIds[i], false);
                TextAttribute titleAttribute = (TextAttribute) dataObject.getAttribute("Titolo");
                if (i % 2 == 1 && i < 4) {
                    titleAttribute.setText(null, "en");
                }
                titleAttribute.setText(null, "it");
                this._dataObjectManager.saveDataObject(dataObject);
            }
            groups.add(Group.ADMINS_GROUP_NAME);
            EntitySearchFilter filter0 = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false);
            filter0.setOrder(EntitySearchFilter.ASC_ORDER);
            EntitySearchFilter filter1 = new EntitySearchFilter(IDataObjectManager.ENTITY_TYPE_CODE_FILTER_KEY, false, "EVN", false);
            EntitySearchFilter filter2 = new EntitySearchFilter(IDataObjectManager.ENTITY_ID_FILTER_KEY, false);
            filter2.setOrder(EntitySearchFilter.ASC_ORDER);
            EntitySearchFilter[] filters = {filter0, filter1, filter2};
            String[] expectedDataObjectsId = {"EVN191", "EVN192", "EVN193", "EVN194",
                "EVN103", "EVN20", "EVN23", "EVN24", "EVN25", "EVN41", "EVN21",
                newDataObjectIds[0], newDataObjectIds[1], newDataObjectIds[2], newDataObjectIds[3], newDataObjectIds[4], newDataObjectIds[5]};

            List<String> dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters, groups);
            assertEquals(expectedDataObjectsId.length, dataObjects.size());
            for (int i = 0; i < expectedDataObjectsId.length; i++) {
                assertEquals(expectedDataObjectsId[i], dataObjects.get(i));
            }

            EntitySearchFilter filter3 = new EntitySearchFilter("Titolo", true);
            filter3.setLangCode("en");
            filter3.setOrder(EntitySearchFilter.ASC_ORDER);
            EntitySearchFilter[] filters1 = {filter0, filter1, filter2, filter3};
            String[] expectedDataObjectsId1 = {"EVN191", "EVN192", "EVN193", "EVN194",
                "EVN103", "EVN20", "EVN23", "EVN24", "EVN25", "EVN41", "EVN21",
                newDataObjectIds[0], newDataObjectIds[2]};

            dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters1, groups);
            assertEquals(expectedDataObjectsId1.length, dataObjects.size());
            for (int i = 0; i < expectedDataObjectsId1.length; i++) {
                assertEquals(expectedDataObjectsId1[i], dataObjects.get(i));
            }

            filter3.setNullOption(true);
            filter3.setLangCode("it");
            EntitySearchFilter[] filters2 = {filter0, filter1, filter2, filter3};
            String[] expectedDataObjectsId2 = {newDataObjectIds[0], newDataObjectIds[1],
                newDataObjectIds[2], newDataObjectIds[3], newDataObjectIds[4], newDataObjectIds[5]};

            dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters2, groups);
            assertEquals(expectedDataObjectsId2.length, dataObjects.size());
            for (int i = 0; i < expectedDataObjectsId2.length; i++) {
                assertEquals(expectedDataObjectsId2[i], dataObjects.get(i));
            }

            filter3.setNullOption(true);
            filter3.setLangCode("en");
            EntitySearchFilter[] filters3 = {filter0, filter1, filter2, filter3};
            String[] expectedDataObjectsId3 = {newDataObjectIds[1], newDataObjectIds[3], newDataObjectIds[4], newDataObjectIds[5]};

            dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters3, groups);
            assertEquals(expectedDataObjectsId3.length, dataObjects.size());
            for (int i = 0; i < expectedDataObjectsId3.length; i++) {
                assertEquals(expectedDataObjectsId3[i], dataObjects.get(i));
            }

            filter2.setNullOption(true);
            EntitySearchFilter[] filters4 = {filter0, filter1, filter2};

            dataObjects = this._dataObjectManager.loadDataObjectsId(null, filters4, groups);
            assertEquals(0, dataObjects.size());

        } catch (Throwable t) {
            throw t;
        } finally {
            this.deleteDataObjects(newDataObjectIds);
        }
    }

    protected String[] addDraftDataObjectsForTest(String[] masterDataObjectIds, boolean publish) throws Throwable {
        String[] newDataObjectIds = new String[masterDataObjectIds.length];
        for (int i = 0; i < masterDataObjectIds.length; i++) {
            DataObject dataObject = this._dataObjectManager.loadDataObject(masterDataObjectIds[i], false);
            dataObject.setId(null);
            this._dataObjectManager.saveDataObject(dataObject);
            newDataObjectIds[i] = dataObject.getId();
            if (publish) {
                this._dataObjectManager.insertDataObject(dataObject);
            }
        }
        for (int i = 0; i < newDataObjectIds.length; i++) {
            DataObject dataObject = this._dataObjectManager.loadDataObject(newDataObjectIds[i], false);
            assertNotNull(dataObject);
        }
        return newDataObjectIds;
    }

    private void deleteDataObjects(String[] dataObjectIds) throws Throwable {
        for (int i = 0; i < dataObjectIds.length; i++) {
            DataObject dataObject = this._dataObjectManager.loadDataObject(dataObjectIds[i], false);
            if (null != dataObject) {
                this._dataObjectManager.removeDataObject(dataObject);
                this._dataObjectManager.deleteDataObject(dataObject);
            }
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
