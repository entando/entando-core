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
package com.agiletec.plugins.jacms.apsadmin.portal;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPageManager;
import java.util.List;
import java.util.Map;

import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.portal.PageAction;
import static junit.framework.Assert.assertEquals;

public class TestTrashReferencedPage extends ApsAdminBaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testTrashReferencedPage() throws Throwable {
        try {
            this._pageManager.setPageOffline("pagina_11");
            String result = this.executeTrashPage("pagina_11", "admin");
            assertEquals("references", result);
            PageAction action = (PageAction) this.getAction();
            Map utilizers = action.getReferences();
            assertEquals(1, utilizers.size());
            List contentUtilizers = (List) utilizers.get("jacmsContentManagerUtilizers");
            assertEquals(2, contentUtilizers.size());
        } catch (Exception e) {
            throw e;
        } finally {
            this._pageManager.setPageOnline("pagina_11");
        }
    }

    private String executeTrashPage(String selectedPageCode, String userName) throws Throwable {
        this.setUserOnSession(userName);
        this.initAction("/do/Page", "trash");
        this.addParameter("selectedNode", selectedPageCode);
        String result = this.executeAction();
        return result;
    }

    private void init() throws Exception {
        try {
            this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
        } catch (Throwable t) {
            throw new Exception(t);
        }
    }

    private IPageManager _pageManager = null;

}
