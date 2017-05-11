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

import java.util.List;

import org.entando.entando.apsadmin.portal.rs.model.PageJO;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.portal.PageFinderAction;
import com.opensymphony.xwork2.Action;


public class TestPageFinderAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetLastUpdated() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/rs/Page", "lastUpdated");
		this.addParameter("lastUpdateResponseSize", 1);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		PageFinderAction action = (PageFinderAction) this.getAction();
		List<PageJO> list = action.getLastUpdatePagesResponse();
		assertEquals(1, list.size());
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

