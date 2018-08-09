/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import com.agiletec.plugins.jacms.aps.system.services.resource.model.BaseResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDataBean;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import java.util.HashMap;
import java.util.Map;

/**
 * @author W.Ambu - E.Santoboni
 */
public class ResourceManagerIntegrationTest extends BaseTestCase {

    private IResourceManager resourceManager;
    private IGroupManager groupManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testLoadResource() throws Throwable {
        try {
            ResourceInterface resource = this.resourceManager.loadResource("44");
            assertTrue(resource instanceof ImageResource);
            assertTrue(resource.isMultiInstance());
            assertEquals(resource.getDescription(), "logo");
            assertEquals(resource.getCategories().size(), 1);
            resource = this.resourceManager.loadResource("7");
            assertTrue(resource instanceof AttachResource);
            assertFalse(resource.isMultiInstance());
            assertEquals(resource.getDescription(), "configurazione");
            assertEquals(resource.getCategories().size(), 0);
        } catch (Throwable t) {
            throw t;
        }
    }

    public void testUpdateResource() throws Throwable {
        String oldDescr = null;
        List<Category> oldCategories = null;
        try {
            ResourceInterface resource = this.resourceManager.loadResource("44");
            assertTrue(resource instanceof ImageResource);
            assertEquals(resource.getDescription(), "logo");
            assertEquals(resource.getCategories().size(), 1);
            assertTrue(resource.isMultiInstance());
            oldCategories = resource.getCategories();
            oldDescr = resource.getDescription();
            String newDescr = "New Description";
            resource.setDescription(newDescr);
            resource.setCategories(new ArrayList<Category>());
            this.resourceManager.updateResource(resource);
            resource = this.resourceManager.loadResource("44");
            assertEquals(resource.getDescription(), newDescr);
            assertEquals(resource.getCategories().size(), 0);
        } catch (Throwable t) {
            throw t;
        } finally {
            if (oldCategories != null && oldDescr != null) {
                ResourceInterface resource = this.resourceManager.loadResource("44");
                resource.setCategories(oldCategories);
                resource.setDescription(oldDescr);
                this.resourceManager.updateResource(resource);
            }
        }
    }

    public void testSearchResources_1() throws Throwable {
        List<String> resourceIds = this.resourceManager.searchResourcesId("Image", "", null, this.getAllGroupCodes());
        assertEquals(3, resourceIds.size());

        resourceIds = resourceManager.searchResourcesId("Image", "Wrong descr", null, this.getAllGroupCodes());
        assertEquals(0, resourceIds.size());

        List<String> allowedGroups = new ArrayList<String>();
        allowedGroups.add("customers");
        resourceIds = resourceManager.searchResourcesId("Image", "", null, allowedGroups);
        assertEquals(1, resourceIds.size());
    }

    public void testSearchResources_2() throws Throwable {
        List<String> resourceIds = this.resourceManager.searchResourcesId("Image", "", "jpg", null, this.getAllGroupCodes());
        assertEquals(3, resourceIds.size());

        resourceIds = this.resourceManager.searchResourcesId("Image", "", "aps", null, this.getAllGroupCodes());
        assertEquals(2, resourceIds.size());

        resourceIds = this.resourceManager.searchResourcesId("Image", "", "aps.JPG", null, this.getAllGroupCodes());
        assertEquals(1, resourceIds.size());
    }

    public void testSearchResourcesForCategory() throws Throwable {
        List<String> resourceIds = resourceManager.searchResourcesId("Image", null, "resCat1", this.getAllGroupCodes());
        assertEquals(1, resourceIds.size());

        resourceIds = resourceManager.searchResourcesId("Image", null, "wrongCat", this.getAllGroupCodes());
        assertEquals(0, resourceIds.size());

        List<String> allowedGroups = new ArrayList<String>();
        allowedGroups.add("customers");
        resourceIds = resourceManager.searchResourcesId("Image", "", "resCat1", allowedGroups);
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
            List<String> resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(1, resourcesId.size());

            this.resourceManager.addResource(bean);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            
            res = this.resourceManager.loadResource(resourcesId.get(0));
            assertTrue(res instanceof ImageResource);
            assertEquals(res.getCategories().size(), 1);
            assertEquals(res.getDescription(), resDescrToAdd);

            ResourceInstance instance0 = ((ImageResource) res).getInstance(0, null);
            assertEquals("entando_logo.jpg", res.getMasterFileName());
            assertEquals("image/jpeg", instance0.getMimeType());

            resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 2);
        } catch (Throwable t) {
            throw t;
        } finally {
            if (res != null) {
                this.resourceManager.deleteResource(res);
                List<String> resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
                assertEquals(resources.size(), 0);

                resources = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
                assertEquals(resources.size(), 1);
            }
        }
    }

    private void testAddRemoveImageResources(String mainGroup) throws Throwable {
        List<String> allowedGroups = this.getAllGroupCodes();
        ResourceInterface res = null;
        String resDescrToAdd1 = "Entando Logo 1";
        String resDescrToAdd2 = "Entando Logo 2";
        String resDescrToAdd3 = "Entando Logo 3";
        String resourceType = "Image";
        String categoryCodeToAdd = "resCat1";

        BaseResourceDataBean bean1 = this.getMockBaseResource(resourceType, mainGroup, resDescrToAdd1, categoryCodeToAdd, "entando_logo_1.jpg");
        BaseResourceDataBean bean2 = this.getMockBaseResource(resourceType, mainGroup, resDescrToAdd2, categoryCodeToAdd, "entando_logo_2.jpg");
        BaseResourceDataBean bean3 = this.getMockBaseResource(resourceType, mainGroup, resDescrToAdd3, categoryCodeToAdd, "entando_logo_3.jpg");

        List<BaseResourceDataBean> resourceList = new ArrayList<BaseResourceDataBean>();
        resourceList.add(bean1);
        resourceList.add(bean2);
        resourceList.add(bean3);
        List<ResourceInterface> resourceListAdded = null;
        try {
            List<String> resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(1, resourcesId.size());

            resourceListAdded = this.resourceManager.addResources(resourceList);

            // Image 1
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd1, null, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd1, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            res = this.resourceManager.loadResource(resourcesId.get(0));
            assertTrue(res instanceof ImageResource);
            assertEquals(res.getCategories().size(), 1);
            assertEquals(res.getDescription(), resDescrToAdd1);
            assertEquals(2,res.getMetadata().size());
            assertEquals("value1", res.getMetadata().get("metadata1"));
            assertEquals("value2", res.getMetadata().get("metadata2"));
            ResourceInstance instance0 = ((ImageResource) res).getInstance(0, null);
            assertEquals("entando_logo.jpg", res.getMasterFileName());
            assertEquals("image/jpeg", instance0.getMimeType());

            resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 2);

            // Image 2
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd2, null, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd2, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            res = this.resourceManager.loadResource(resourcesId.get(0));
            assertTrue(res instanceof ImageResource);
            assertEquals(res.getCategories().size(), 1);
            assertEquals(res.getDescription(), resDescrToAdd2);

            instance0 = ((ImageResource) res).getInstance(0, null);
            assertEquals("entando_logo.jpg", res.getMasterFileName());
            assertEquals("image/jpeg", instance0.getMimeType());

            resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 2);

            // Image 3
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd3, null, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            resourcesId = resourceManager.searchResourcesId(resourceType, resDescrToAdd3, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 1);
            res = this.resourceManager.loadResource(resourcesId.get(0));
            assertTrue(res instanceof ImageResource);
            assertEquals(res.getCategories().size(), 1);
            assertEquals(res.getDescription(), resDescrToAdd3);

            instance0 = ((ImageResource) res).getInstance(0, null);
            assertEquals("entando_logo.jpg", res.getMasterFileName());
            assertEquals("image/jpeg", instance0.getMimeType());

            resourcesId = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
            assertEquals(resourcesId.size(), 2);

        } catch (Throwable t) {
            throw t;
        } finally {
            if (res != null) {
                this.resourceManager.deleteResources(resourceListAdded);
                List<String> resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd1, null, allowedGroups);
                assertEquals(resources.size(), 0);
                resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd2, null, allowedGroups);
                assertEquals(resources.size(), 0);
                resources = resourceManager.searchResourcesId(resourceType, resDescrToAdd3, null, allowedGroups);
                assertEquals(resources.size(), 0);
                resources = resourceManager.searchResourcesId(resourceType, null, categoryCodeToAdd, allowedGroups);
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
        Map<String, String> metadata = new HashMap<String, String>();
        metadata.put("metadata1", "value1");
        metadata.put("metadata2", "value2");
        bean.setMetadata(metadata);
        bean.setMimeType("image/jpeg");
        List<Category> categories = new ArrayList<Category>();
        ICategoryManager catManager
                = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        Category cat = catManager.getCategory(categoryCodeToAdd);
        categories.add(cat);
        bean.setCategories(categories);
        return bean;
    }

    private BaseResourceDataBean getMockBaseResource(String resourceType,
            String mainGroup, String resDescrToAdd, String categoryCodeToAdd,
            String fileName) {
        File file = new File("target/test/" + fileName);
        BaseResourceDataBean bean = new BaseResourceDataBean();
        bean.setFile(file);
        bean.setDescr(resDescrToAdd);
        bean.setMainGroup(mainGroup);
        bean.setResourceType(resourceType);
        bean.setMimeType("image/jpeg");
        Map<String, String> metadata = new HashMap<String, String>();
        metadata.put("metadata1", "value1");
        metadata.put("metadata2", "value2");
        metadata.put("Blue TRC", "0000");
        bean.setMetadata(metadata);
        List<Category> categories = new ArrayList<Category>();
        ICategoryManager catManager
                = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        Category cat = catManager.getCategory(categoryCodeToAdd);
        categories.add(cat);
        bean.setCategories(categories);
        return bean;
    }

    public void testAddNullResource() throws Throwable {
        List<String> allowedGroups = this.getAllGroupCodes();
        String resDescrToAdd = "Null Entando resource";
        String resourceType = "Attach";
        List<String> resourcesId = resourceManager.searchResourcesId(resourceType, null, null, allowedGroups);
        int initsize = resourcesId.size();
        ResourceDataBean bean = this.getNullMockResource(resourceType, resDescrToAdd);
        try {
            this.resourceManager.addResource(bean);
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
            resourcesId = this.resourceManager.searchResourcesId(resourceType, null, null, allowedGroups);
            assertEquals(initsize, resourcesId.size());
            resourcesId = this.resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
            assertEquals(0, resourcesId.size());
        } catch (Throwable t) {
            resourcesId = this.resourceManager.searchResourcesId(resourceType, resDescrToAdd, null, allowedGroups);
            for (int i = 0; i < resourcesId.size(); i++) {
                ResourceInterface res = this.resourceManager.loadResource(resourcesId.get(i));
                this.resourceManager.deleteResource(res);
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
        ResourceInterface imageResource = this.resourceManager.createResourceType("Image");
        assertEquals("", imageResource.getDescription());
        assertEquals("", imageResource.getId());
        assertEquals("Image", imageResource.getType());
    }

    public void testCreateResourceType() {
        ResourceInterface imageResource = this.resourceManager.createResourceType("Image");
        assertNotNull(imageResource);
        assertEquals("", imageResource.getDescription());
        assertEquals("", imageResource.getId());
        assertEquals("Image", imageResource.getType());
        assertNotSame("", imageResource.getXML());
    }

    public void testGetGroupUtilizers() throws Throwable {
        assertTrue(this.resourceManager instanceof GroupUtilizer);
        List utilizers = ((GroupUtilizer) this.resourceManager).getGroupUtilizers(Group.FREE_GROUP_NAME);
        assertEquals(3, utilizers.size());

        utilizers = ((GroupUtilizer) this.resourceManager).getGroupUtilizers("customers");
        assertEquals(1, utilizers.size());
        String resourceId = (String) utilizers.get(0);
        assertEquals("82", resourceId);
    }

    private List<String> getAllGroupCodes() {
        List<String> groupCodes = new ArrayList<String>();
        List<Group> groups = this.groupManager.getGroups();
        for (int i = 0; i < groups.size(); i++) {
            groupCodes.add(groups.get(i).getName());
        }
        return groupCodes;
    }

    private void init() throws Exception {
        try {
            this.resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
            this.groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }


}
