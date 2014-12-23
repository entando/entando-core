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
package com.agiletec.aps.system.services.baseconfig;

import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @author M.Diana
 */
public class TestConfigItemDAO extends BaseTestCase {
	
    public void testLoadVersionItems() throws Throwable {
    	DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		ConfigItemDAO configItemDAO = new ConfigItemDAO();
		configItemDAO.setDataSource(dataSource);
		Map<String, String> items = null;
        try {
            items = configItemDAO.loadVersionItems("test");
        } catch (Throwable e) {
        	throw e;
        }
		assertTrue(items.containsKey(SystemConstants.CONFIG_ITEM_PARAMS));
		String config = items.get(SystemConstants.CONFIG_ITEM_PARAMS);
		int index = config.indexOf("<SpecialPages>");
		assertTrue(index != -1);
    }
	
	public void testLoadVersionItem() throws Throwable {
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		ConfigItemDAO configItemDAO = new ConfigItemDAO();
		configItemDAO.setDataSource(dataSource);
		String config = null;
        try {
        	config = configItemDAO.loadVersionItem("test", SystemConstants.CONFIG_ITEM_LANGS);
        } catch (Throwable e) {
        	throw e;
        }
		int index = config.indexOf("<code>it</code>");
		assertTrue(index != -1);
	} 	
    
}