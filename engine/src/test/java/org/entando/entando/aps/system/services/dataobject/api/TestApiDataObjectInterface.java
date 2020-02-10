/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.dataobject.api;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.util.DateConverter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import org.entando.entando.aps.system.services.api.ApiBaseTestCase;
import org.entando.entando.aps.system.services.api.UnmarshalUtils;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobject.api.model.JAXBDataObject;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * @author E.Santoboni
 */
public class TestApiDataObjectInterface extends ApiBaseTestCase {

    private IDataObjectManager dataObjectManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetXmlDataObject() throws Throwable {
        MediaType mediaType = MediaType.APPLICATION_XML_TYPE;
        this.testGetDataObject(mediaType, "admin", "ALL4", "it");
    }
    /*
    public void testGetJsonDataObject() throws Throwable {
        MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
        this.testGetDataObject(mediaType, "admin", "ALL4", "en");
    }
    */
    public void testCreateNewDataObjectFromXml() throws Throwable {
        MediaType mediaType = MediaType.APPLICATION_XML_TYPE;
        this.testCreateNewDataObject(mediaType, "ALL4");
    }
    /*
    public void testCreateNewDataObjectFromJson() throws Throwable {
        MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
        this.testCreateNewDataObject(mediaType, "ALL4");
    }
    */
    protected void testCreateNewDataObject(MediaType mediaType, String dataObjectId) throws Throwable {
        String dateNow = DateConverter.getFormattedDate(new Date(), SystemConstants.DATA_TYPE_METADATA_DATE_FORMAT);
        EntitySearchFilter filter = new EntitySearchFilter(IDataObjectManager.DATA_OBJECT_CREATION_DATE_FILTER_KEY, false, dateNow, null);
        EntitySearchFilter[] filters = {filter};
        List<String> ids = this.dataObjectManager.searchId(filters);
        assertTrue(ids.isEmpty());
        JAXBDataObject jaxbDataObject = this.testGetDataObject(mediaType, "admin", dataObjectId, "it");
        ApiResource dataTypeResource = this.getApiCatalogManager().getResource("core", "dataObject");
        ApiMethod postMethod = dataTypeResource.getPostMethod();
        Properties properties = super.createApiProperties("admin", "it", mediaType);
        try {
            jaxbDataObject.setId(null);
            Object response = this.getResponseBuilder().createResponse(postMethod, jaxbDataObject, properties);
            assertNotNull(response);
            assertTrue(response instanceof StringApiResponse);
            assertEquals(IResponseBuilder.SUCCESS, ((StringApiResponse) response).getResult());
            ids = this.dataObjectManager.searchId(filters);
            assertEquals(1, ids.size());
            String newDataObjectId = ids.get(0);
            DataObject newDataType = this.dataObjectManager.loadDataObject(newDataObjectId, false);
            DataObject masterDataType = this.dataObjectManager.loadDataObject(dataObjectId, true);
            List<AttributeInterface> attributes = masterDataType.getAttributeList();
            for (int i = 0; i < attributes.size(); i++) {
                AttributeInterface attribute = attributes.get(i);
                AttributeInterface newAttribute = (AttributeInterface) newDataType.getAttribute(attribute.getName());
                this.checkAttributes(attribute, newAttribute);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            ids = this.dataObjectManager.searchId(filters);
            if (!ids.isEmpty()) {
                for (int i = 0; i < ids.size(); i++) {
                    String id = ids.get(i);
                    DataObject dataObject = this.dataObjectManager.loadDataObject(id, false);
                    this.dataObjectManager.deleteDataObject(dataObject);
                }
            }
        }
    }

    private void checkAttributes(AttributeInterface oldAttribute, AttributeInterface newAttribute) {
        if (null == newAttribute) {
            fail();
        }
        assertEquals(oldAttribute.getName(), newAttribute.getName());
        assertEquals(oldAttribute.getType(), newAttribute.getType());
        if (!oldAttribute.isSimple()) {
            if (oldAttribute instanceof AbstractListAttribute) {
                List<AttributeInterface> oldListAttributes = ((AbstractComplexAttribute) oldAttribute).getAttributes();
                List<AttributeInterface> newListAttributes = ((AbstractComplexAttribute) newAttribute).getAttributes();
                assertEquals(oldListAttributes.size(), newListAttributes.size());
                for (int i = 0; i < oldListAttributes.size(); i++) {
                    AttributeInterface oldElement = oldListAttributes.get(i);
                    AttributeInterface newElement = newListAttributes.get(i);
                    this.checkAttributes(oldElement, newElement);
                }
            } else if (oldAttribute instanceof CompositeAttribute) {
                Map<String, AttributeInterface> oldAttributeMap = ((CompositeAttribute) oldAttribute).getAttributeMap();
                Map<String, AttributeInterface> newAttributeMap = ((CompositeAttribute) newAttribute).getAttributeMap();
                assertEquals(oldAttributeMap.size(), newAttributeMap.size());
                Iterator<String> iterator = oldAttributeMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    AttributeInterface oldElement = oldAttributeMap.get(key);
                    AttributeInterface newElement = newAttributeMap.get(key);
                    this.checkAttributes(oldElement, newElement);
                }
            }
        } else {
            assertEquals(oldAttribute.getValue(), newAttribute.getValue());
        }
    }

    protected JAXBDataObject testGetDataObject(MediaType mediaType, String username, String dataId, String langCode) throws Throwable {
        ApiResource dataResource = this.getApiCatalogManager().getResource("core", "dataObject");
        ApiMethod getMethod = dataResource.getGetMethod();
        Properties properties = super.createApiProperties(username, langCode, mediaType);
        properties.put("id", dataId);
        Object result = this.getResponseBuilder().createResponse(getMethod, properties);
        assertNotNull(result);
        ApiDataObjectInterface apiDataObjectInterface = (ApiDataObjectInterface) this.getApplicationContext().getBean("ApiDataObjectInterface");
        Object singleResult = apiDataObjectInterface.getDataObject(properties);
        assertNotNull(singleResult);
        String toString = this.marshall(singleResult, mediaType);
        JAXBDataObject jaxbData = (JAXBDataObject) UnmarshalUtils.unmarshal(JAXBDataObject.class, toString, mediaType);
        assertNotNull(jaxbData);
        return jaxbData;
    }

    private void init() throws Exception {
        try {
            this.dataObjectManager = (IDataObjectManager) this.getApplicationContext().getBean(SystemConstants.DATA_OBJECT_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

}
