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

import java.util.Collection;
import java.util.Iterator;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author E.Santoboni
 */
public class TestPageAction extends ApsAdminBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testSavePage() throws Throwable {
		String pageCode = "customer_subpage_2";
		IPage page = this._pageManager.getDraftPage(pageCode);
		assertNotNull(page);
		try {
			PageMetadata metadata = page.getDraftMetadata();
			this.setUserOnSession("admin");
			this.initAction("/do/Page", "save");
			this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
			this.addParameter("langit", "");
			this.addParameter("langen", metadata.getTitle("en"));
			this.addParameter("model", metadata.getModel().getCode());
			this.addParameter("group", page.getGroup());
			this.addParameter("pageCode", pageCode);
			Collection<String> extraGroups = metadata.getExtraGroups();
			if (null != extraGroups) {
				Iterator<String> extraGroupIter = extraGroups.iterator();
				while (extraGroupIter.hasNext()) {
					String extraGroup = (String) extraGroupIter.next();
					this.addParameter("extraGroups", extraGroup);
				}
			}
			this.addParameter("extraGroups", "management");
			String result = this.executeAction();
			assertEquals(Action.INPUT, result);
			ActionSupport action = this.getAction();
			assertEquals(2, action.getFieldErrors().size());
			assertEquals(1, action.getFieldErrors().get("langit").size());
			assertEquals(1, action.getFieldErrors().get("extraGroups").size());
		} catch (Throwable t) {
			this._pageManager.updatePage(page);
			throw t;
		}
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
