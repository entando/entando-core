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
package com.agiletec.plugins.jacms.aps.system.services.page;

import java.util.List;

import javax.sql.DataSource;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;

/**
 * @author M.Diana
 */
public class TestCmsPageDAO extends BaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
    public void testGetContentUtilizers() throws Throwable {
    	List<String> utilizers = this._pageDao.getContentUtilizers("ART187");
    	assertEquals(3, utilizers.size());
    	assertTrue(utilizers.contains("coach_page"));
    	assertTrue(utilizers.contains("pagina_11"));
    	assertTrue(utilizers.contains("pagina_2"));
    	
    	utilizers = this._pageDao.getContentUtilizers("ART111");
     	assertEquals(1, utilizers.size());
     	assertTrue(utilizers.contains("customers_page"));
    }
    
	private void init() throws Exception {
    	try {
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
    		_pageDao = new CmsPageDAO();
    		_pageDao.setDataSource(dataSource);
    		IPageModelManager pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
    		IWidgetTypeManager showletTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
    		this._pageDao.setPageModelManager(pageModelManager);
    		this._pageDao.setWidgetTypeManager(showletTypeManager);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
    private CmsPageDAO _pageDao;
	
}
