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
package org.entando.entando.plugins.jacms.aps.system.services.api;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractComplexAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractListAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.LinkAttribute;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
import org.entando.entando.plugins.jacms.aps.system.services.api.model.JAXBContent;

/**
 * @author E.Santoboni
 */
public class TestApiContentInterface extends ApiBaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
    	super.setUp();
    	this.init();
    }
	
	public void testGetXmlContent() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_XML_TYPE;
		this.testGetContent(mediaType, "admin", "ALL4", "it");
	}
	
	public void testGetJsonContent() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
		this.testGetContent(mediaType, "admin", "ALL4", "en");
	}
	
	public void testCreateNewContentFromXml() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_XML_TYPE;
		this.testCreateNewContent(mediaType, "ALL4");
	}
	
	public void testCreateNewContentFromJson() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
		this.testCreateNewContent(mediaType, "ALL4");
	}
	
	protected void testCreateNewContent(MediaType mediaType, String contentId) throws Throwable {
		String dateNow = DateConverter.getFormattedDate(new Date(), JacmsSystemConstants.CONTENT_METADATA_DATE_FORMAT);
		EntitySearchFilter filter = new EntitySearchFilter(IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, false, dateNow, null);
		EntitySearchFilter[] filters = {filter};
		List<String> ids = this._contentManager.searchId(filters);
		assertTrue(ids.isEmpty());
		JAXBContent jaxbContent = this.testGetContent(mediaType, "admin", contentId, "it");
		ApiResource contentResource = this.getApiCatalogManager().getResource("jacms", "content");
		ApiMethod postMethod = contentResource.getPostMethod();
		Properties properties = super.createApiProperties("admin", "it", mediaType);
		try {
			jaxbContent.setId(null);
			Object response = this.getResponseBuilder().createResponse(postMethod, jaxbContent, properties);
			assertNotNull(response);
			assertTrue(response instanceof StringApiResponse);
			assertEquals(IResponseBuilder.SUCCESS, ((StringApiResponse) response).getResult());
			ids = this._contentManager.searchId(filters);
			assertEquals(1, ids.size());
			String newContentId = ids.get(0);
			Content newContent = this._contentManager.loadContent(newContentId, false);
			Content masterContent = this._contentManager.loadContent(contentId, true);
			List<AttributeInterface> attributes = masterContent.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				AttributeInterface newAttribute = (AttributeInterface) newContent.getAttribute(attribute.getName());
				this.checkAttributes(attribute, newAttribute);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			ids = this._contentManager.searchId(filters);
			if (!ids.isEmpty()) {
				for (int i = 0; i < ids.size(); i++) {
					String id = ids.get(i);
					Content content = this._contentManager.loadContent(id, false);
					this._contentManager.deleteContent(content);
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
			if (oldAttribute instanceof AbstractResourceAttribute || oldAttribute instanceof LinkAttribute) {
				return;
			}
			assertEquals(oldAttribute.getValue(), newAttribute.getValue());
		}
	}
	
	protected JAXBContent testGetContent(MediaType mediaType, String username, String contentId, String langCode) throws Throwable {
		ApiResource contentResource = 
				this.getApiCatalogManager().getResource("jacms", "content");
		ApiMethod getMethod = contentResource.getGetMethod();
		Properties properties = super.createApiProperties(username, langCode, mediaType);
		properties.put("id", contentId);
		Object result = this.getResponseBuilder().createResponse(getMethod, properties);
		assertNotNull(result);
		ApiContentInterface apiContentInterface = (ApiContentInterface) this.getApplicationContext().getBean("jacmsApiContentInterface");
		Object singleResult = apiContentInterface.getContent(properties);
		assertNotNull(singleResult);
		String toString = this.marshall(singleResult, mediaType);
		InputStream stream = new ByteArrayInputStream(toString.getBytes());
		JAXBContent jaxbContent = (JAXBContent) UnmarshalUtils.unmarshal(super.getApplicationContext(), JAXBContent.class, stream, mediaType);
		assertNotNull(jaxbContent);
		return jaxbContent;
	}
	
	private void init() throws Exception {
    	try {
    		this._contentManager = (IContentManager) this.getApplicationContext().getBean(JacmsSystemConstants.CONTENT_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
	
	private IContentManager _contentManager;
	
}
