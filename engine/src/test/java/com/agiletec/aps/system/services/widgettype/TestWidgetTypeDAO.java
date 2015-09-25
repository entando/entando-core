/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.aps.system.services.widgettype;

import java.util.Map;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeDAO;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;

/**
 * @author M.Diana
 */
public class TestWidgetTypeDAO extends BaseTestCase {
	
    public void testLoadWidgetTypes() throws Throwable {
    	DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
    	WidgetTypeDAO widgetTypeDao = new WidgetTypeDAO();
    	widgetTypeDao.setDataSource(dataSource);
    	ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
    	widgetTypeDao.setLangManager(langManager);
    	Map<String, WidgetType> types = null;
		try {
			types = widgetTypeDao.loadWidgetTypes();
		} catch (Throwable t) {
            throw t;
        }
		WidgetType showletType = (WidgetType) types.get("content_viewer");
		assertNotNull(showletType);
		showletType = (WidgetType) types.get("content_viewer_list");
		assertNotNull(showletType);
	}    
	
}
