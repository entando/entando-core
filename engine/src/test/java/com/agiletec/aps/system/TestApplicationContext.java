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
package com.agiletec.aps.system;

import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.IUserManager;

/**
 * @author W.Ambu
 */
public class TestApplicationContext extends BaseTestCase {
	
    public void testGetServices() {
    	ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
        assertNotNull(configManager);
        ICacheInfoManager cacheInfoManager = (ICacheInfoManager) this.getService(SystemConstants.CACHE_INFO_MANAGER);
        assertNotNull(cacheInfoManager);
        ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
        assertNotNull(langManager);
        IWidgetTypeManager showletTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
        assertNotNull(showletTypeManager);
        IPageModelManager pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
        assertNotNull(pageModelManager);
        IPageManager pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        assertNotNull(pageManager);
        IRoleManager roleManager = (IRoleManager) this.getService(SystemConstants.ROLE_MANAGER);
        assertNotNull(roleManager);
        IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
        assertNotNull(userManager);
        IURLManager urlManager = (IURLManager) this.getService(SystemConstants.URL_MANAGER);
        assertNotNull(urlManager);
        II18nManager i18nManager = (II18nManager) this.getService(SystemConstants.I18N_MANAGER);
        assertNotNull(i18nManager);
        ControllerManager controller = (ControllerManager) this.getService(SystemConstants.CONTROLLER_MANAGER);
        assertNotNull(controller);
        IKeyGeneratorManager keyGeneratorManager = (IKeyGeneratorManager) this.getService(SystemConstants.KEY_GENERATOR_MANAGER);
        assertNotNull(keyGeneratorManager);
        ICategoryManager categoryManager = (ICategoryManager) this.getService(SystemConstants.CATEGORY_MANAGER);
        assertNotNull(categoryManager);
    }
    
}
