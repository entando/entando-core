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
package com.agiletec.aps.system.services.url;

import java.util.Map;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;

/**
 * @version 1.0
 * @author M.Casari
 */
public class TestURLManager extends BaseTestCase {
	
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetURLString_1() throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		PageURL pageURL = _urlManager.createURL(reqCtx);
		pageURL.setLangCode("it");
		pageURL.setPageCode("homepage");
		try {
			String url = this._urlManager.getURLString(pageURL, reqCtx);
			assertEquals("/Entando/it/homepage.page", url);
			this.changeUrlStyle(SystemConstants.CONFIG_PARAM_URL_STYLE_BREADCRUMBS);
			url = this._urlManager.getURLString(pageURL, reqCtx);
			assertEquals("/Entando/pages/it/homepage/", url);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.changeUrlStyle(SystemConstants.CONFIG_PARAM_URL_STYLE_CLASSIC);
		}
	}
    
    public void testGetURLString_2() throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		PageURL pageURL = _urlManager.createURL(reqCtx);
		pageURL.setLangCode("en");
		pageURL.setPageCode("pagina_11");
		try {
			String url = this._urlManager.getURLString(pageURL, reqCtx);
			assertEquals("/Entando/en/pagina_11.page", url);
			this.changeUrlStyle(SystemConstants.CONFIG_PARAM_URL_STYLE_BREADCRUMBS);
			url = this._urlManager.getURLString(pageURL, reqCtx);
			assertEquals("/Entando/pages/en/homepage/pagina_1/pagina_11/", url);
		} catch (Throwable t) {
			throw t;
		} finally {
			this.changeUrlStyle(SystemConstants.CONFIG_PARAM_URL_STYLE_CLASSIC);
		}
	}
    
    private void changeUrlStyle(String styleType) throws Throwable {
    	try {
			String xmlParams = this._configManager.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
			Map<String, String> systemParams = SystemParamsUtils.getParams(xmlParams);
			systemParams.put(SystemConstants.CONFIG_PARAM_URL_STYLE, styleType);
			String newXmlParams = SystemParamsUtils.getNewXmlParams(xmlParams, systemParams);
			this._configManager.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, newXmlParams);
		} catch (Throwable t) {
			throw t;
		}
    }
    
    private void init() throws Exception {
		try {
    		this._urlManager = (IURLManager) this.getService(SystemConstants.URL_MANAGER);
    		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		} catch (Exception e) {
			throw e;
		}
	}
    
    private ConfigInterface _configManager = null;
    private IURLManager _urlManager = null;
    
    
}
