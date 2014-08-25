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
package com.agiletec.aps.system.services.group;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class TestGroupManager extends BaseTestCase {
	
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testGetGroups() {
    	List<Group> groups = this._groupManager.getGroups();
    	assertTrue(groups.size()>=6);
    }
    
    public void testAddDeleteGroup() throws Throwable {
    	int initSize = this._groupManager.getGroups().size();
    	Group group = new Group();
    	group.setName("Gruppo_Prova");
    	group.setDescr("descr_gruppo_prova");
    	try {
    		assertNull(_groupManager.getGroup("Gruppo_Prova"));
    		_groupManager.addGroup(group);
    		List<Group> groups = _groupManager.getGroups();
    		assertEquals(initSize+1, groups.size());
    		assertNotNull(_groupManager.getGroup("Gruppo_Prova"));
    		_groupManager.removeGroup(group);
    		groups = _groupManager.getGroups();
    		assertEquals(initSize, groups.size());
    		assertNull(_groupManager.getGroup("Gruppo_Prova"));
    	} catch (Throwable t) {
			throw t;
		} finally {
			_groupManager.removeGroup(group);
		}
    }
    
    public void testUpdateGroup() throws Throwable {
    	int initSize = this._groupManager.getGroups().size();
    	Group group = new Group();
    	group.setName("Gruppo_Prova");
    	group.setDescr("descr_gruppo_prova");
    	try {
    		assertNull(_groupManager.getGroup("Gruppo_Prova"));
    		_groupManager.addGroup(group);
    		List<Group> groups = _groupManager.getGroups();
    		assertEquals(initSize+1, groups.size());
    		
    		Group groupNew = new Group();
			groupNew.setName(group.getName());
			groupNew.setDescr("Nuova_descr");
    		_groupManager.updateGroup(groupNew);
    		Group extracted = _groupManager.getGroup("Gruppo_Prova");
    		assertEquals(group.getDescr(), extracted.getDescr());
    		
    		_groupManager.removeGroup(group);
    		groups = _groupManager.getGroups();
    		assertEquals(initSize, groups.size());
    		assertNull(_groupManager.getGroup("Gruppo_Prova"));
    	} catch (Throwable t) {
			throw t;
		} finally {
			_groupManager.removeGroup(group);
		}
    }
    
    private void init() throws Exception {
    	try {
    		_groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
		} catch (Exception e) {
			throw e;
		}
    }
	
	private IGroupManager _groupManager = null;
	
}
