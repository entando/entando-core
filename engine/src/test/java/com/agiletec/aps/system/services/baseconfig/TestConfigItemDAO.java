/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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