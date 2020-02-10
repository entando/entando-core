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
package org.entando.entando.aps.system.services.i18n;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.util.ApsProperties;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.entando.entando.aps.system.services.api.ApiBaseTestCase;
import org.entando.entando.aps.system.services.api.UnmarshalUtils;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.StringApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.aps.system.services.i18n.model.JAXBI18nLabel;

/**
 * @author E.Santoboni
 */
public class TestApiI18nLabelInterface extends ApiBaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
    	super.setUp();
    	this.init();
    }
	
	public void testGetXmlLabel() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_XML_TYPE;
		this.testGetLabel(mediaType, "admin", "PAGE_TITLE");
	}
	
	public void testGetJsonLabel() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
		this.testGetLabel(mediaType, "admin", "PAGE_TITLE");
	}
	
	public void testCreateNewLabelFromXml() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_XML_TYPE;
		this.testCreateNewLabel(mediaType);
	}
	
	public void testCreateNewContentFromJson() throws Throwable {
		MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;
		this.testCreateNewLabel(mediaType);
	}
	
	protected void testCreateNewLabel(MediaType mediaType) throws Throwable {
		String key = "TEST_LABEL_KEY";
		String label = this._i18nManager.getLabel(key, "it");
		assertNull(label);
		ApsProperties labels = new ApsProperties();
		labels.put("en", "Test label");
		labels.put("it", "Label di Test");
		JAXBI18nLabel jaxbLabel = new JAXBI18nLabel(key, labels);
		ApiResource labelResource = this.getApiCatalogManager().getResource("core", "i18nlabel");
		ApiMethod postMethod = labelResource.getPostMethod();
		Properties properties = super.createApiProperties("admin", "it", mediaType);
		try {
			Object response = this.getResponseBuilder().createResponse(postMethod, jaxbLabel, properties);
			assertNotNull(response);
			assertTrue(response instanceof StringApiResponse);
			assertEquals(IResponseBuilder.SUCCESS, ((StringApiResponse) response).getResult());
			label = this._i18nManager.getLabel(key, "it");
			assertEquals("Label di Test", label);
		} catch (Exception e) {
			throw e;
		} finally {
			this._i18nManager.deleteLabelGroup(key);
		}
	}
	
	protected JAXBI18nLabel testGetLabel(MediaType mediaType, String username, String key) throws Throwable {
		ApiResource contentResource = 
				this.getApiCatalogManager().getResource("core", "i18nlabel");
		ApiMethod getMethod = contentResource.getGetMethod();
		
		Properties properties = super.createApiProperties(username, "en", mediaType);
		properties.put("key", key);
		Object result = this.getResponseBuilder().createResponse(getMethod, properties);
		assertNotNull(result);
		ApiI18nLabelInterface apiLabelInterface = (ApiI18nLabelInterface) this.getApplicationContext().getBean("ApiI18nLabelInterface");
		Object singleResult = apiLabelInterface.getLabel(properties);
		assertNotNull(singleResult);
		String toString = this.marshall(singleResult, mediaType);
		JAXBI18nLabel jaxbLabel = (JAXBI18nLabel) UnmarshalUtils.unmarshal(JAXBI18nLabel.class, toString, mediaType);
		assertNotNull(jaxbLabel);
		return jaxbLabel;
	}
	
	private void init() throws Exception {
    	try {
    		this._i18nManager = (II18nManager) this.getApplicationContext().getBean(SystemConstants.I18N_MANAGER);
    	} catch (Throwable t) {
    		throw new Exception(t);
        }
    }
	
	private II18nManager _i18nManager;
	
}
