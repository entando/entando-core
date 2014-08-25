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
package com.agiletec.plugins.jacms.aps.system.services;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.IContentPageMapperManager;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.ILinkResolverManager;
import com.agiletec.plugins.jacms.aps.system.services.renderer.IContentRenderer;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.searchengine.ICmsSearchEngineManager;

public class TestCmsApplicationContext extends BaseTestCase{

	public void testGetCmsServices() throws Throwable {
		try {
	        IResourceManager resourceManager = (IResourceManager) this.getService(JacmsSystemConstants.RESOURCE_MANAGER);
	        assertNotNull(resourceManager);
	        IContentManager contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
	        assertNotNull(contentManager);
	        ICategoryManager categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
	        assertNotNull(categoryManager);
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
		} catch (Throwable t) {
			throw t;
		}
	}
}
