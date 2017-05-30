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
package com.agiletec.apsadmin.admin;

import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestBaseAdminAction extends ApsAdminBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testReloadConfig() throws Throwable {
		this.setUserOnSession("supervisorCoach");
		this.initAction("/do/BaseAdmin", "reloadConfig");
		String result = this.executeAction();
		assertEquals("userNotAllowed", result);

		this.setUserOnSession("admin");
		this.initAction("/do/BaseAdmin", "reloadConfig");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		assertEquals(BaseAdminAction.SUCCESS_RELOADING_RESULT_CODE, ((BaseAdminAction) this.getAction()).getReloadingResult());
	}

	public void testReloadEntitiesReferences() throws Throwable {
		this.setUserOnSession("supervisorCoach");
		this.initAction("/do/BaseAdmin", "reloadEntitiesReferences");
		String result = this.executeAction();
		assertEquals("userNotAllowed", result);

		this.setUserOnSession("admin");
		this.initAction("/do/BaseAdmin", "reloadEntitiesReferences");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		synchronized (this) {
			this.wait(3000);
		}
		super.waitNotifyingThread();
	}

	public void testConfigSystemParams() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/BaseAdmin", "configSystemParams");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		BaseAdminAction action = (BaseAdminAction) this.getAction();
		Map<String, String> params = action.getSystemParams();
		assertTrue(params.size() >= 6);
		assertEquals("homepage", params.get(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE));
	}

	public void testUpdateConfigParams_1() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/BaseAdmin", "updateSystemParams");
		this.addParameter(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE, "newErrorPageCode");
		this.addParameter(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE, "newHomepageCode");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		assertEquals("newHomepageCode", this._configManager.getParam(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE));
		assertEquals("newErrorPageCode", this._configManager.getParam(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE));
	}

	public void testUpdateConfigParams_2() throws Throwable {
		assertEquals("homepage", this._configManager.getParam(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE));
		assertEquals("errorpage", this._configManager.getParam(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE));

		this.setUserOnSession("admin");
		this.initAction("/do/BaseAdmin", "updateSystemParams");
		this.addParameter("newCustomParameter", "parameterValue");
		this.addParameter(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE, "newErrorPageCode");
		this.addParameter(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE, "newHomepageCode");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		assertEquals("newHomepageCode", this._configManager.getParam(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE));
		assertEquals("newErrorPageCode", this._configManager.getParam(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE));
		assertNull(this._configManager.getParam("newCustomParameter"));

		this.initAction("/do/BaseAdmin", "updateSystemParams");
		this.addParameter("newCustomParameter", "parameterValue");
		this.addParameter("newCustomParameter_newParamMarker", "true");
		this.addParameter(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE, "newErrorPageCode");
		this.addParameter(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE, "newHomepageCode");
		result = this.executeAction();
		assertEquals(Action.SUCCESS, result);

		assertEquals("newHomepageCode", this._configManager.getParam(SystemConstants.CONFIG_PARAM_HOMEPAGE_PAGE_CODE));
		assertEquals("newErrorPageCode", this._configManager.getParam(SystemConstants.CONFIG_PARAM_ERROR_PAGE_CODE));
		assertNotNull(this._configManager.getParam("newCustomParameter"));
		assertEquals("parameterValue", this._configManager.getParam("newCustomParameter"));
	}

	@Override
	protected void tearDown() throws Exception {
		this._configManager.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, this._oldConfigParam);
		super.tearDown();
	}

	private void init() {
		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._oldConfigParam = this._configManager.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
	}

	private ConfigInterface _configManager;
	private String _oldConfigParam;

}
