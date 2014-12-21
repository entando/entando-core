/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.IContentPageMapperManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.ILinkResolverManager;
import com.agiletec.plugins.jacms.aps.system.services.renderer.IContentRenderer;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;

/**
 * @author W.Ambu
 */
public class TestApplicationContext extends BaseTestCase {
	
    public void testGetServices() {
    	IResourceManager resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
        assertNotNull(resourceManager);
        IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
        assertNotNull(contentManager);
        IContentModelManager contentModelManager = (IContentModelManager) this.getService(JacmsSystemConstants.CONTENT_MODEL_MANAGER);
        assertNotNull(contentModelManager);
        IContentRenderer contentRenderer = (IContentRenderer) this.getService(JacmsSystemConstants.CONTENT_RENDERER_MANAGER);
        assertNotNull(contentRenderer);
        IContentDispenser contentDispenser = (IContentDispenser) this.getService(JacmsSystemConstants.CONTENT_DISPENSER_MANAGER);
        assertNotNull(contentDispenser);
        ICmsSearchEngineManager searchEngineManager = (ICmsSearchEngineManager) this.getService(JacmsSystemConstants.SEARCH_ENGINE_MANAGER);
        assertNotNull(searchEngineManager);
        ILinkResolverManager linkResolver = (ILinkResolverManager) this.getService(JacmsSystemConstants.LINK_RESOLVER_MANAGER);
        assertNotNull(linkResolver);
        IContentPageMapperManager contentPageMapper = (IContentPageMapperManager) this.getService(JacmsSystemConstants.CONTENT_PAGE_MAPPER_MANAGER);
        assertNotNull(contentPageMapper);
        
    }
    
}
