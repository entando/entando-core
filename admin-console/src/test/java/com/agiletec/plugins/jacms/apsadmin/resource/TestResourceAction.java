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
package com.agiletec.plugins.jacms.apsadmin.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestResourceAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testTrashResource() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Resource", "trash");
		this.addParameter("resourceId", "7");
		String result = this.executeAction();
		assertEquals("references", result);
		ResourceAction action = (ResourceAction) this.getAction();
		Map utilizersGroup = action.getReferences();
		assertNotNull(utilizersGroup);
		assertEquals(1, utilizersGroup.size());
		// test with no resourceId 
		this.initAction("/do/jacms/Resource", "trash");
		result = this.executeAction();
		assertEquals(Action.INPUT, result);
	}
	
	public void testDeleteResourceReferencedFromEditor() throws Throwable {
		this.setUserOnSession("mainEditor");
		this.initAction("/do/jacms/Resource", "delete");
		this.addParameter("resourceId", "7");
		String result = this.executeAction();
		assertEquals("references", result);
	}
	
	public void testDeleteUnknownResource() throws Throwable {
		String result = null;
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jacms/Resource", "delete");
			result = this.executeAction();
			assertEquals(Action.INPUT, result);			
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testSaveNewResource_1() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Resource", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> actionFieldErrors = action.getFieldErrors();
		assertEquals(4, actionFieldErrors.size());
		assertEquals(1, actionFieldErrors.get("resourceTypeCode").size());
		assertEquals(1, actionFieldErrors.get("descr").size());
		assertEquals(1, actionFieldErrors.get("mainGroup").size());
		assertEquals(1, actionFieldErrors.get("upload").size());
	}
	
	public void testSaveNewResource_2() throws Throwable {
		String insertedDescr = "Description ";
		while (insertedDescr.length() < 300) insertedDescr += insertedDescr;
		this.setUserOnSession("admin");
		this.initAction("/do/jacms/Resource", "save");
		this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
		this.addParameter("descr", insertedDescr);
		this.addParameter("mainGroup", Group.FREE_GROUP_NAME);
		
		String result = this.executeAction();
		assertEquals(Action.INPUT, result);
		ActionSupport action = this.getAction();
		Map<String, List<String>> actionFieldErrors = action.getFieldErrors();
		assertEquals(3, actionFieldErrors.size());
		assertEquals(1, actionFieldErrors.get("resourceTypeCode").size());
		assertEquals(1, actionFieldErrors.get("descr").size());
	}
	
	public void testSaveEditedResource() throws Throwable {
		String resourceId = "44";
		this.setUserOnSession("admin");
		ResourceInterface resource = this._resourceManager.loadResource(resourceId);
		try {
			this.initAction("/do/jacms/Resource", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
			this.addParameter("descr", "Descrizione di test");
			this.addParameter("mainGroup", resource.getMainGroup());
			this.addParameter("resourceId", String.valueOf(resourceId));
			this.addParameter("resourceTypeCode", resource.getType());
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			ResourceInterface modifiedResource = this._resourceManager.loadResource(resourceId);
			assertEquals("Descrizione di test", modifiedResource.getDescr());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._resourceManager.updateResource(resource);
		}
	}
	
	public void testPerformRemoveJoinCategory() throws Throwable {
		this.entryEditResource("44", "admin");
		
		ResourceAction action = (ResourceAction) this.getAction();
		Map<String, String> params = new HashMap<String, String>();
		params.put("resourceId", action.getResourceId());
		params.put("strutsAction", String.valueOf(action.getStrutsAction()));
		params.put("descr", action.getDescr());
		params.put("mainGroup", action.getMainGroup());
		Iterator<String> iter = action.getCategoryCodes().iterator();
		while (iter.hasNext()) {
			String categoryCode = iter.next();
			params.put("categoryCodes", categoryCode);
		}
		params.put("resourceTypeCode", action.getResourceTypeCode());
		
		assertEquals(1, action.getCategoryCodes().size());
		assertTrue(action.getCategoryCodes().contains("resCat1"));
		
		this.initAction("/do/jacms/Resource", "removeCategory");
		this.setUserOnSession("admin");
		this.addParameter("categoryCode", "resCat1");
		this.addParameters(params);
		
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		action = (ResourceAction) this.getAction();
		assertEquals(0, action.getCategoryCodes().size(), 0);
		
		this.initAction("/do/jacms/Resource", "joinCategory");
		params.put("categoryCode", "resCat1");
		Iterator<String> iter2 = action.getCategoryCodes().iterator();
		while (iter2.hasNext()) {
			String categoryCode = iter2.next();
			params.put("categoryCodes", categoryCode);
		}
		this.addParameters(params);
		this.setUserOnSession("admin");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		action = (ResourceAction) this.getAction();
		assertEquals(1, action.getCategoryCodes().size());
		assertTrue(action.getCategoryCodes().contains("resCat1"));
		
		this.initAction("/do/jacms/Resource", "joinCategory");
		params.put("categoryCode", "resCat2");
		Iterator<String> iter3 = action.getCategoryCodes().iterator();
		while (iter3.hasNext()) {
			String categoryCode = iter3.next();
			params.put("categoryCodes", categoryCode);
		}
		this.addParameters(params);
		this.setUserOnSession("admin");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		
		action = (ResourceAction) this.getAction();
		assertEquals(2, action.getCategoryCodes().size());
		assertTrue(action.getCategoryCodes().contains("resCat1"));
		assertTrue(action.getCategoryCodes().contains("resCat2"));
	}
	
	public void testJoinCategoryWithInvalidCategory() throws Throwable {
		String result = null;
		try {
			this.setUserOnSession("admin");			
			this.initAction("/do/jacms/Resource", "joinCategory");
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result); // null category does a no-op		
		} catch (Throwable t) {
			throw t;
		}
	}
	
	/*
	 * NOTE: we create a fake resource using the manager rather than the most obvious 'save' action.
	 *
	 */
	public void testDelete() throws Throwable {
		ResourceInterface resource = this._resourceManager.createResourceType("Image");
		String resourceId = null;
		String result = null;
		ResourceAction action = null;
		assertNotNull(resource);
		try {			
			this.setUserOnSession("admin");
			
			resource.setMainGroup(Group.FREE_GROUP_NAME);
			resource.setDescr("Levò la bocca dal fero pasto quel peccator");
			resource.setCategories(new ArrayList<Category>());
			this._resourceManager.addResource(resource);
			resourceId = resource.getId();
			
			ResourceInterface verify  = this._resourceManager.loadResource(resourceId);
			assertNotNull(verify);
			
			// test with invalid ID 
			this.initAction("/do/jacms/Resource", "delete");
			result = this.executeAction();
			assertEquals(Action.INPUT, result);
			action = (ResourceAction) this.getAction();
			assertEquals(1, action.getActionErrors().size());
						
			// test with a valid ID
			this.initAction("/do/jacms/Resource", "delete");
			this.addParameter("resourceId", resourceId);			
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			verify = this._resourceManager.loadResource(resourceId);
			assertNull(verify);
			
		} catch (Throwable t) {
			throw t;
		} finally {
			this._resourceManager.deleteResource(resource);			
		}
	}
	
	private void entryEditResource(String resourceId, String userName) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/jacms/Resource", "edit");
		this.addParameter("resourceId", resourceId);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
    
    private void init() throws Exception {
    	try {
    		_resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IResourceManager _resourceManager = null;
	
}
