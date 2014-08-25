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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.resource.mock.MockResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AttachResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * @author W.Ambu - E.Santoboni
 */
public class TestResourceManager extends BaseTestCase {
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
    
    public void testLoadResource() throws Throwable {
    	try {
			ResourceInterface resource = this._resourceManager.loadResource("44");
			assertTrue(resource instanceof ImageResource);
			assertTrue(resource.isMultiInstance());
			assertEquals(resource.getDescr(), "logo");
			assertEquals(resource.getCategories().size(), 1);
    		resource = this._resourceManager.loadResource("7");
			assertTrue(resource instanceof AttachResource);
			assertFalse(resource.isMultiInstance());
			assertEquals(resource.getDescr(), "configurazione");
			assertEquals(resource.getCategories().size(), 0);
		} catch (Throwable t) {
			throw t;
		}
    }
    
    public void testUpdateResource() throws Throwable {
    	String oldDescr = null;
    	List<Category> oldCategories = null;
    	try {
			ResourceInterface resource = this._resourceManager.loadResource("44");
			assertTrue(resource instanceof ImageResource);
			assertEquals(resource.getDescr(), "logo");
			assertEquals(resource.getCategories().size(), 1);
			assertTrue(resource.isMultiInstance());
			oldCategories = resource.getCategories();
			oldDescr = resource.getDescr();
			String newDescr = "New Description";
			resource.setDescr(newDescr);
			resource.setCategories(new ArrayList<Category>());
			this._resourceManager.updateResource(resource);
			resource = this._resourceManager.loadResource("44");
			assertEquals(resource.getDescr(), newDescr);
			assertEquals(resource.getCategories().size(), 0);
		} catch (Throwable t) {
			throw t;
		} finally {
			if (oldCategories != null && oldDescr != null) {
				ResourceInterface resource = this._resourceManager.loadResource("44");
				resource.setCategories(oldCategories);
				resource.setDescr(oldDescr);
				this._resourceManager.updateResource(resource);
			}
		}
    }
    
    public void testSearchResources_1() throws Throwable {
    	List<String> resourceIds = this._resourceManager.searchResourcesId("Image", "", null, this.getAllGroupCodes());
		assertEquals(3, resourceIds.size());
		
		resourceIds = _resourceManager.searchResourcesId("Image", "Wrong descr", null, this.getAllGroupCodes());
		assertEquals(0, resourceIds.size());
		
		List<String> allowedGroups = new ArrayList<String>();
		allowedGroups.add("customers");
		resourceIds = _resourceManager.searchResourcesId("Image", "", null, allowedGroups);
		assertEquals(1, resourceIds.size());
    }
    
    public void testSearchResources_2() throws Throwable {
    	List<String> resourceIds = this._resourceManager.searchResourcesId("Image", "", "jpg", null, this.getAllGroupCodes());
    	assertEquals(3, resourceIds.size());
    	
    	resourceIds = this._resourceManager.searchResourcesId("Image", "", "aps", null, this.getAllGroupCodes());
    	assertEquals(2, resourceIds.size());
    	
    	resourceIds = this._resourceManager.searchResourcesId("Image", "", "aps.JPG", null, this.getAllGroupCodes());
    	assertEquals(1, resourceIds.size());
    }
    
    public void testSearchResourcesForCategory() throws Throwable {
    	List<String> resourceIds = _resourceManager.searchResourcesId("Image", null, "resCat1", this.getAllGroupCodes());
		assertEquals(1, resourceIds.size());
		
		resourceIds = _resourceManager.searchResourcesId("Image", null, "wrongCat", this.getAllGroupCodes());
		assertEquals(0, resourceIds.size());
		
		List<String> allowedGroups = new ArrayList<String>();
		allowedGroups.add("customers");
		resourceIds = _resourceManager.searchResourcesId("Image", "", "resCat1", allowedGroups);
		assertEquals(0, resourceIds.size());
    }
    
    public void testAddRemoveImageResource() throws Throwable {
    	this.testAddRemoveImageResource(Group.FREE_GROUP_NAME);
    	this.testAddRemoveImageResource(Group.ADMINS_GROUP_NAME);
    }
    
    private void testAddRemoveImageResource(String mainGroup) throws Throwable {
    	List<String> allowedGroups = this.getAllGroupCodes();
    	ResourceInterface res = null;
    	String resDescrToAdd = "Entando Logo";
    	String resourceType = "Image";
    	String categoryCodeToAdd = "resCat1";
    	ResourceDataBean bean = this.getMockResource(resourceType, mainGroup, resDescrToAdd, categoryCodeToAdd);
    	try {
    		List<String> resourcesId = _resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
			assertEquals(1, resourcesId.size());
    		
			this._resourceManager.addResource(bean);
			resourcesId = _resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
			assertEquals(resourcesId.size(), 1);
			resourcesId = _resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, allowedGroups);
			assertEquals(resourcesId.size(), 1);
			res = this._resourceManager.loadResource(resourcesId.get(0));
			assertTrue(res instanceof ImageResource);
			assertEquals(res.getCategories().size(), 1);
			assertEquals(res.getDescr(), resDescrToAdd);
			
			ResourceInstance instance0 = ((ImageResource) res).getInstance(0, null);
			assertEquals("entando_logo_d0.jpg", instance0.getFileName());
			assertEquals("image/jpeg", instance0.getMimeType());
			
			resourcesId = _resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
			assertEquals(resourcesId.size(), 2);
		} catch (Throwable t) {
			throw t;
		} finally {
			if (res != null) {
				this._resourceManager.deleteResource(res);
				List<String> resources = _resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
				assertEquals(resources.size(), 0);
				
				resources = _resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
				assertEquals(resources.size(), 1);
			}
		}
    }
    
    private ResourceDataBean getMockResource(String resourceType, 
    		String mainGroup, String resDescrToAdd, String categoryCodeToAdd) {
    	File file = new File("target/test/entando_logo.jpg");
    	MockResourceDataBean bean = new MockResourceDataBean();
    	bean.setFile(file);
    	bean.setDescr(resDescrToAdd);
    	bean.setMainGroup(mainGroup);
    	bean.setResourceType(resourceType);
    	bean.setMimeType("image/jpeg");
    	List<Category> categories = new ArrayList<Category>();
    	ICategoryManager catManager = 
    		(ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
    	Category cat = catManager.getCategory(categoryCodeToAdd);
    	categories.add(cat);
    	bean.setCategories(categories);
    	return bean;
    }
    
    public void testAddNullResource() throws Throwable {
    	List<String> allowedGroups = this.getAllGroupCodes();
    	String resDescrToAdd = "Null Entando resource";
    	String resourceType = "Attach";
    	List<String> resourcesId = _resourceManager.searchResourcesId(resourceType, null, null, allowedGroups);
		int initsize = resourcesId.size();
    	ResourceDataBean bean = this.getNullMockResource(resourceType, resDescrToAdd);
    	try {
    		this._resourceManager.addResource(bean);
    		fail();
		} catch (Throwable t) {
			//nothing to do
		} finally {
			this.verifyTestAddNullResource(resDescrToAdd, resourceType, initsize);
		}
    }
    
	private void verifyTestAddNullResource(String resDescrToAdd, String resourceType, int initsize) throws Throwable {
		List<String> allowedGroups = this.getAllGroupCodes();
		List<String> resourcesId = null;
		try {
			resourcesId = this._resourceManager.searchResourcesId(resourceType, null, null, allowedGroups);
			assertEquals(initsize, resourcesId.size());
			resourcesId = this._resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
			assertEquals(0, resourcesId.size());
		} catch (Throwable t) {
			resourcesId = this._resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
			for (int i = 0; i < resourcesId.size(); i++) {
				ResourceInterface res = this._resourceManager.loadResource(resourcesId.get(i));
				this._resourceManager.deleteResource(res);
			}
			throw t;
		}
	}
    
	private ResourceDataBean getNullMockResource(String resourceType, String resDescrToAdd) {
		MockResourceDataBean bean = new MockResourceDataBean();
		bean.setDescr(resDescrToAdd);
		bean.setMainGroup(Group.FREE_GROUP_NAME);
		bean.setResourceType(resourceType);
		bean.setMimeType("text/plain");
		return bean;
	}
    
	public void testGetResourceType() {
		ResourceInterface imageResource = this._resourceManager.createResourceType("Image");
		assertEquals("", imageResource.getDescr());
		assertEquals("", imageResource.getId());
		assertEquals("Image", imageResource.getType());
	}
    
	public void testCreateResourceType() {
		ResourceInterface imageResource = this._resourceManager.createResourceType("Image");
		assertNotNull(imageResource);
		assertEquals("", imageResource.getDescr());
		assertEquals("", imageResource.getId());
		assertEquals("Image", imageResource.getType());
		assertNotSame("", imageResource.getXML());
	}
    
    public void testGetGroupUtilizers() throws Throwable {
    	assertTrue(this._resourceManager instanceof GroupUtilizer);
    	List utilizers = ((GroupUtilizer) this._resourceManager).getGroupUtilizers(Group.FREE_GROUP_NAME);
    	assertEquals(3, utilizers.size());
    	
    	utilizers = ((GroupUtilizer) this._resourceManager).getGroupUtilizers("customers");
    	assertEquals(1, utilizers.size());
    	String resourceId = (String) utilizers.get(0);
    	assertEquals("82", resourceId);
    }
    
    private List<String> getAllGroupCodes() {
    	List<String> groupCodes = new ArrayList<String>();
    	List<Group> groups = this._groupManager.getGroups();
    	for (int i = 0; i < groups.size(); i++) {
			groupCodes.add(groups.get(i).getName());
		}
    	return groupCodes;
    }
    
    private void init() throws Exception {
    	try {
    		this._resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
    		this._groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IResourceManager _resourceManager;
    private IGroupManager _groupManager;
    
}