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
package com.agiletec.plugins.jacms.apsadmin.content;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.SearchEngineManager;
import com.agiletec.plugins.jacms.apsadmin.content.util.AbstractBaseTestContentAction;
import java.util.List;
import java.util.Map;

/**
 * @author E.Mezzano
 */
public class TestContentAdminAction extends AbstractBaseTestContentAction {

    private IResourceManager resourceManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testOpenIndexProspect() throws Throwable {
        String result = this.executeOpenIndexProspect("admin");
        assertEquals(BaseAction.SUCCESS, result);
        ContentAdminAction contentAdminAction = (ContentAdminAction) this.getAction();
        assertEquals(IContentManager.STATUS_READY, contentAdminAction.getContentManagerStatus());
        assertEquals(ICmsSearchEngineManager.STATUS_READY, contentAdminAction.getSearcherManagerStatus());
        assertNull(contentAdminAction.getLastReloadInfo());
        assertEquals("metadataKey1,metadataKey2,metadatakey3,xxx,yyy", contentAdminAction.getResourceAltMapping());
        assertEquals("metadataKeyA,metadataKeyB,JPEG Comment", contentAdminAction.getResourceDescriptionMapping());
        assertEquals("metadataKeyX,metadataKeyY,metadatakeyX,YYYY,Detected File Type Long Name,WWWWW", contentAdminAction.getResourceLegendMapping());
        assertEquals("metadataKeyG,metadataKeyK,metadatakeyF", contentAdminAction.getResourceTitleMapping());
    }

    public void testReloadContentsIndex() throws Throwable {
        String result = this.executeReloadContentsIndex("admin");
        assertEquals(BaseAction.SUCCESS, result);
        this.waitReloadThreads();
        ContentAdminAction contentAdminAction = (ContentAdminAction) this.getAction();
        assertEquals(IContentManager.STATUS_READY, contentAdminAction.getContentManagerStatus());
        assertEquals(ICmsSearchEngineManager.STATUS_READY, contentAdminAction.getSearcherManagerStatus());
        assertNotNull(contentAdminAction.getLastReloadInfo());
    }

    public void testReloadContentsReference() throws Throwable {
        String result = this.executeReloadContentsReference("admin");
        assertEquals(BaseAction.SUCCESS, result);
        this.waitReloadThreads();
        ContentAdminAction contentAdminAction = (ContentAdminAction) this.getAction();
        assertEquals(IContentManager.STATUS_READY, contentAdminAction.getContentManagerStatus());
        assertEquals(ICmsSearchEngineManager.STATUS_READY, contentAdminAction.getSearcherManagerStatus());
    }

    public void testUpdateResourceMapping() throws Throwable {
        Map<String, List<String>> defaultMapping = this.resourceManager.getMetadataMapping();
        try {
            this.initAction("/do/jacms/Content/Admin", "updateSystemParams");
            this.setUserOnSession("admin");
            super.addParameter("resourceAltMapping", "metadataKey1,metadataKey2,metadatakey3,metadatakey4,metadatakey5");
            super.addParameter("resourceDescriptionMapping", "newMetadataKeyA,newMetadataKeyB,JPEG Comment,Component 2");
            super.addParameter("resourceLegendMapping", "");
            super.addParameter("resourceTitleMapping", "metadataKeyG,metadataKeyK,metadatakeyF,metadatakeyZ");
            String result = this.executeAction();
            assertEquals(BaseAction.SUCCESS, result);
            Map<String, List<String>> newMapping = this.resourceManager.getMetadataMapping();
            assertEquals(3, newMapping.size());
            assertEquals(5, newMapping.get(IResourceManager.ALT_METADATA_MAPPING_KEY).size());
            assertEquals("metadatakey4", newMapping.get(IResourceManager.ALT_METADATA_MAPPING_KEY).get(3));
            assertEquals(4, newMapping.get(IResourceManager.DESCRIPTION_METADATA_MAPPING_KEY).size());
            assertEquals("Component 2", newMapping.get(IResourceManager.DESCRIPTION_METADATA_MAPPING_KEY).get(3));
            assertEquals(4, newMapping.get(IResourceManager.TITLE_METADATA_MAPPING_KEY).size());
        } catch (Throwable e) {
            throw e;
        } finally {
            this.resourceManager.updateMetadataMapping(defaultMapping);
        }
    }

    private String executeOpenIndexProspect(String currentUserName) throws Throwable {
        this.initAction("/do/jacms/Content/Admin", "openIndexProspect");
        this.setUserOnSession(currentUserName);
        return this.executeAction();
    }

    private String executeReloadContentsIndex(String currentUserName) throws Throwable {
        this.initAction("/do/jacms/Content/Admin", "reloadContentsIndex");
        this.setUserOnSession(currentUserName);
        return this.executeAction();
    }

    private String executeReloadContentsReference(String currentUserName) throws Throwable {
        this.initAction("/do/jacms/Content/Admin", "reloadContentsReference");
        this.setUserOnSession(currentUserName);
        return this.executeAction();
    }

    private void waitReloadThreads() throws InterruptedException {
        Thread[] threads = new Thread[50];
        Thread.enumerate(threads);
        for (int i = 0; i < threads.length; i++) {
            Thread currentThread = threads[i];
            if (currentThread != null
                    && (currentThread.getName().startsWith(SearchEngineManager.RELOAD_THREAD_NAME_PREFIX)
                    || currentThread.getName().startsWith(ApsEntityManager.RELOAD_REFERENCES_THREAD_NAME_PREFIX))) {
                currentThread.join();
            }
        }
    }

    protected void init() throws Exception {
        try {
            this.resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

}
