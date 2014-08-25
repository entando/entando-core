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

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestResourceFinderAction extends ApsAdminBaseTestCase {
	
	public void testViewImageResources() throws Throwable {
		String result = this.executeShowList("admin", "Image");
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		String resourceTypeCode = action.getResourceTypeCode();
		assertNotNull(resourceTypeCode);
		assertEquals("Image", resourceTypeCode);
		assertEquals(3, action.getResources().size());
		Category root = ((ResourceFinderAction) action).getCategoryRoot();
		assertNotNull(root);
		assertEquals("Home", root.getTitle());
	}
	
	public void testViewAttachResources() throws Throwable {
		String result = this.executeShowList("admin", "Attach");
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		String resourceTypeCode = action.getResourceTypeCode();
		assertNotNull(resourceTypeCode);
		assertEquals("Attach", resourceTypeCode);
		assertEquals(1, action.getResources().size());
		Category root = ((ResourceFinderAction) action).getCategoryRoot();
		assertNotNull(root);
		assertEquals("Home", root.getTitle());
	}
	
	public void testViewImageResourcesByCustomerUser() throws Throwable {
		String result = this.executeShowList("editorCustomers", "Image");
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		String resourceTypeCode = action.getResourceTypeCode();
		assertNotNull(resourceTypeCode);
		assertEquals("Image", resourceTypeCode);
		assertEquals(1, action.getResources().size());
		Category root = ((ResourceFinderAction) action).getCategoryRoot();
		assertNotNull(root);
		assertEquals("Home", root.getTitle());
	}
	
	public void testViewImagesWithUserNotAllowed() throws Throwable {
		String result = this.executeShowList("pageManagerCustomers", "Image");
		assertEquals("userNotAllowed", result);
	}
	
	private String executeShowList(String userName, String resourceTypeCode) throws Throwable {
		this.setUserOnSession(userName);
		this.initAction("/do/jacms/Resource", "list");
		this.addParameter("resourceTypeCode", resourceTypeCode);
		return this.executeAction();
	}
	
	public void testSearchResources_1() throws Throwable {
		String result = this.executeSearchResource("admin", "Attach", "WrongDescription", null, null, null);
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		assertTrue(action.getResources().isEmpty());
		assertEquals("WrongDescription", action.getText());
	}
	
	public void testSearchResources_2() throws Throwable {
		String result = this.executeSearchResource("admin", "Attach", "", null, null, null);
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		assertEquals(1, action.getResources().size());
		assertEquals("", action.getText());
	}
	
	public void testSearchResources_3() throws Throwable {
		String result = this.executeSearchResource("admin", "Image", null, null, "jpg", null);
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		assertEquals(3, action.getResources().size());
		assertEquals("jpg", action.getFileName());
		
		result = this.executeSearchResource("admin", "Image", null, null, "aps", null);
		assertEquals(Action.SUCCESS, result);
		action = (ResourceFinderAction) this.getAction();
		assertEquals(2, action.getResources().size());
		assertEquals("aps", action.getFileName());
		
		result = this.executeSearchResource("admin", "Image", null, null, "aps.JPG", null);
		assertEquals(Action.SUCCESS, result);
		action = (ResourceFinderAction) this.getAction();
		assertEquals(1, action.getResources().size());
		assertEquals("aps.JPG", action.getFileName());
    }
	
	public void testSearchByCategory() throws Throwable {
		String result = this.executeSearchResource("admin", "Image", "", null, null, "resCat1");
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		assertEquals(1, action.getResources().size());
		assertEquals("", action.getText());
		assertEquals("resCat1", action.getCategoryCode());
		
		result = this.executeSearchResource("admin", "Image", "log", null, null, "resCat1");
		assertEquals(Action.SUCCESS, result);
		action = (ResourceFinderAction) this.getAction();
		assertEquals(1, action.getResources().size());
		
		result = this.executeSearchResource("admin", "Image", "japs", null, null, "resCat1");
		assertEquals(Action.SUCCESS, result);
		action = (ResourceFinderAction) this.getAction();
		assertTrue(action.getResources().isEmpty());
	}

	public void testSearchByGroup_1() throws Throwable {
		String result = this.executeSearchResource("admin", "Image", null, Group.FREE_GROUP_NAME, null, null);
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		assertEquals(2, action.getResources().size());
		
		result = this.executeSearchResource("admin", "Image", null, Group.ADMINS_GROUP_NAME, null, null);
		assertEquals(Action.SUCCESS, result);
		action = (ResourceFinderAction) this.getAction();
		assertEquals(0, action.getResources().size());
		
		result = this.executeSearchResource("admin", "Image", null, "customers", null, null);
		assertEquals(Action.SUCCESS, result);
		action = (ResourceFinderAction) this.getAction();
		assertEquals(1, action.getResources().size());
	}
	
	public void testSearchByGroup_2() throws Throwable {
		String result = this.executeSearchResource("editorCoach", "Image", null, Group.FREE_GROUP_NAME, null, null);
		assertEquals(Action.SUCCESS, result);
		ResourceFinderAction action = (ResourceFinderAction) this.getAction();
		assertEquals(0, action.getResources().size());
		
		result = this.executeSearchResource("editorCoach", "Image", null, Group.ADMINS_GROUP_NAME, null, null);
		assertEquals(Action.SUCCESS, result);
		action = (ResourceFinderAction) this.getAction();
		assertEquals(0, action.getResources().size());
		
		result = this.executeSearchResource("editorCoach", "Image", null, "customers", null, null);
		assertEquals(Action.SUCCESS, result);
		action = (ResourceFinderAction) this.getAction();
		assertEquals(1, action.getResources().size());
	}
	
	private String executeSearchResource(String username, String resourceTypeCode, 
			String text, String ownerGroupName, String fileName, String categoryCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jacms/Resource", "search");
		this.addParameter("resourceTypeCode", resourceTypeCode);
		this.addParameter("text", text);
		this.addParameter("fileName", fileName);
		this.addParameter("ownerGroupName", ownerGroupName);
		this.addParameter("categoryCode", categoryCode);
		return this.executeAction();
	}
	
}