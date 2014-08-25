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
