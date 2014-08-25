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
package com.agiletec.plugins.jacms.aps.system.services.resource;

import javax.sql.DataSource;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.services.mock.MockResourcesDAO;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceRecordVO;

/**
 * @version 1.0
 * @author M.Diana
 */
public class TestResourceDAO extends BaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testAddDeleteResource() throws Throwable {  
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
    	MockResourcesDAO mockResourcesDao = new MockResourcesDAO();
    	mockResourcesDao.setDataSource(dataSource);
    	
    	ResourceInterface resource = new ImageResource();
    	resource.setId("temp");
    	resource.setDescr("temp");
    	resource.setMainGroup(Group.FREE_GROUP_NAME);
    	resource.setType("Image");
    	resource.setFolder("/temp");
    	//resource.setBaseURL("temp");
    	ResourceRecordVO resourceRecordVO = null;  
        try {
        	mockResourcesDao.deleteResource("temp");
        } catch (Throwable t) {
        	throw t;
        }
    	_resourceDao.addResource(resource); 
    	resourceRecordVO = _resourceDao.loadResourceVo(resource.getId());
    	assertEquals(resourceRecordVO.getDescr().equals("temp"), true);
    	_resourceDao.deleteResource(resource.getId()); 
    	resourceRecordVO = _resourceDao.loadResourceVo(resource.getId());
    	assertNull(resourceRecordVO);
    }
    
    private void init() throws Exception {
		try {
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			ResourceDAO resourceDaoImpl = new ResourceDAO();
			resourceDaoImpl.setDataSource(dataSource);
			this._resourceDao = resourceDaoImpl;
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
    
	private IResourceDAO _resourceDao;
	
}
