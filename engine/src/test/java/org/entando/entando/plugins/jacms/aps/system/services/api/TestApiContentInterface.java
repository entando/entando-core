/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.core.MediaType;
import org.entando.entando.aps.system.services.api.ApiBaseTestCase;
import org.entando.entando.aps.system.services.api.UnmarshalUtils;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
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
		this.testGetContent(mediaType, "ALL4");
	}
	
	public void testGetJsonContent() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
		this.testGetContent(mediaType, "ALL4");
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
		
		JAXBContent jaxbContent = this.testGetContent(mediaType, contentId);
		ApiResource contentResource = this.getApiCatalogManager().getResource("jacms", "content");
		ApiMethod postMethod = contentResource.getPostMethod();
		Properties properties = super.createApiProperties("admin", "en", mediaType);
		try {
			jaxbContent.setId(null);
			Object response = this.getResponseBuilder().createResponse(postMethod, jaxbContent, properties);
			assertNotNull(response);
			//System.out.println("------------------------");
			//String toString = this.marshall(response, mediaType);
			//System.out.println(toString);
			//System.out.println("------------------------");
			
			ids = this._contentManager.searchId(filters);
			assertEquals(1, ids.size());
			String newContentId = ids.get(0);
			//System.out.println("newContentId ------------------------ " + newContentId);
			Content newContent = this._contentManager.loadContent(newContentId, false);
			Content masterContent = this._contentManager.loadContent(contentId, true);
			List<AttributeInterface> attributes = masterContent.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				if (!attribute.isSimple() || attribute instanceof AbstractResourceAttribute) continue;
				AttributeInterface newAttribute = (AttributeInterface) newContent.getAttribute(attribute.getName());
				//System.out.println("attribute ------------------------ " + attribute.getName());
				//System.out.println("old --- " + attribute.getValue());
				//System.out.println("new --- " + newAttribute.getValue());
				assertEquals(attribute.getValue(), newAttribute.getValue());
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
	
	protected JAXBContent testGetContent(MediaType mediaType, String contentId) throws Throwable {
		ApiResource contentResource = 
				this.getApiCatalogManager().getResource("jacms", "content");
		ApiMethod getMethod = contentResource.getGetMethod();
		
		Properties properties = super.createApiProperties("admin", "en", mediaType);
		properties.put("id", contentId);
		
		Object result = this.getResponseBuilder().createResponse(getMethod, properties);
		assertNotNull(result);
		//System.out.println("------------------------");
		//System.out.println(this.marshall(result, mediaType));
		//System.out.println("------------------------");
		
		ApiContentInterface apiContentInterface = (ApiContentInterface) this.getApplicationContext().getBean("jacmsApiContentInterface");
		Object singleResult = apiContentInterface.getContent(properties);
		assertNotNull(singleResult);
		//System.out.println("------------------------");
		String toString = this.marshall(singleResult, mediaType);
		//System.out.println(toString);
		//System.out.println("------------------------");
		
		JAXBContent jaxbContent = (JAXBContent) UnmarshalUtils.unmarshal(JAXBContent.class, toString, mediaType);
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
