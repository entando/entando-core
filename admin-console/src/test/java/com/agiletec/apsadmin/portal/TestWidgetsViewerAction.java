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
package com.agiletec.apsadmin.portal;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestWidgetsViewerAction extends ApsAdminBaseTestCase {

	public void testViewWidgets() throws Throwable {
		String result = this.executeViewWidgets("admin");
		assertEquals(Action.SUCCESS, result);

		result = this.executeViewWidgets("pageManagerCustomers");
		assertEquals(Action.SUCCESS, result);

		result = this.executeViewWidgets("editorCustomers");
		assertEquals("userNotAllowed", result);

		result = this.executeViewWidgets(null);
		assertEquals("apslogin", result);
	}

	public void testGetWidgetFlavours() throws Throwable {
		String result = this.executeViewWidgets("admin");
		assertEquals(Action.SUCCESS, result);
		AbstractPortalAction action = (AbstractPortalAction) this.getAction();
		List<List<SelectItem>> widgetFlavours = action.getWidgetFlavours();
		assertNotNull(widgetFlavours);
		assertTrue(widgetFlavours.size() >= 3);
		Lang currentLang = action.getCurrentLang();

		List<SelectItem> userWidgets = widgetFlavours.get(0);
		assertEquals(2, userWidgets.size());
		SelectItem userType = userWidgets.get(1);
		assertEquals(AbstractPortalAction.USER_WIDGETS_CODE, userType.getOptgroup());
		if (currentLang.getCode().equals("it")) {
			assertEquals("logic_type", userType.getKey());
			assertEquals("Tipo logico per test", userType.getValue());
		} else {
			assertEquals("logic_type", userType.getKey());
			assertEquals("Logic type for test", userType.getValue());
		}

		List<SelectItem> customWidgets = widgetFlavours.get(1);
		assertEquals(1, customWidgets.size());
		SelectItem customType = customWidgets.get(0);
		assertEquals(AbstractPortalAction.CUSTOM_WIDGETS_CODE, customType.getOptgroup());
		if (currentLang.getCode().equals("it")) {
			assertEquals("leftmenu", customType.getKey());
			assertEquals("Menu di navigazione verticale", customType.getValue());
		} else {
			assertEquals("leftmenu", customType.getKey());
			assertEquals("Vertical Navigation Menu", customType.getValue());
		}

		List<SelectItem> jacmsWidgets = widgetFlavours.get(2);
		assertEquals(3, jacmsWidgets.size());
		SelectItem jacmsWidgetTypes = jacmsWidgets.get(1);
		assertEquals("jacms", jacmsWidgetTypes.getOptgroup());
		if (currentLang.getCode().equals("it")) {
			assertEquals("content_viewer_list", jacmsWidgetTypes.getKey());
			assertEquals("Contenuti - Pubblica una Lista di Contenuti", jacmsWidgetTypes.getValue());
		}

		List<SelectItem> stockWidgets = widgetFlavours.get(widgetFlavours.size() - 1);
		assertEquals(4, stockWidgets.size());
		SelectItem stockType = stockWidgets.get(3);
		assertEquals(AbstractPortalAction.STOCK_WIDGETS_CODE, stockType.getOptgroup());
		if (currentLang.getCode().equals("it")) {
			assertEquals("login_form", stockType.getKey());
			assertEquals("Widget di Login", stockType.getValue());
		} else {
			assertEquals("messages_system", stockType.getKey());
			assertEquals("System Messages", stockType.getValue());
		}
	}

	public void testGetWidgetUtilizers_1() throws Throwable {
		String result = this.executeViewWidgetUtilizers("admin", null);
		assertEquals(Action.INPUT, result);
		Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("showletTypeCode").size());

		result = this.executeViewWidgetUtilizers("admin", "");
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("showletTypeCode").size());

		result = this.executeViewWidgetUtilizers("admin", "invalidShowletCode");
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(1, fieldErrors.size());
		assertEquals(1, fieldErrors.get("showletTypeCode").size());
	}

	public void testGetWidgetUtilizers_2() throws Throwable {
		String result = this.executeViewWidgetUtilizers("admin", "logic_type");
		assertEquals(Action.SUCCESS, result);
		WidgetsViewerAction action = (WidgetsViewerAction) this.getAction();
		List<IPage> pageUtilizers = action.getWidgetUtilizers();
		assertEquals(0, pageUtilizers.size());

		result = this.executeViewWidgetUtilizers("admin", "leftmenu");
		assertEquals(Action.SUCCESS, result);
		action = (WidgetsViewerAction) this.getAction();
		pageUtilizers = action.getWidgetUtilizers();
		assertEquals(4, pageUtilizers.size());
		this.checkUtilizers(pageUtilizers, 3, 1);
		assertEquals("pagina_1", pageUtilizers.get(0).getCode());

		result = this.executeViewWidgetUtilizers("admin", "content_viewer");
		assertEquals(Action.SUCCESS, result);
		action = (WidgetsViewerAction) this.getAction();
		pageUtilizers = action.getWidgetUtilizers();
		assertEquals(14, pageUtilizers.size());
		this.checkUtilizers(pageUtilizers, 7, 7);
		assertEquals("homepage", pageUtilizers.get(0).getCode());
		assertEquals("pagina_2", pageUtilizers.get(3).getCode());
		assertEquals("customer_subpage_2", pageUtilizers.get(6).getCode());
	}

	private void checkUtilizers(List<IPage> pageUtilizers, int expectedDraft, int expectedOnline) {
		int online = 0;
		int draft = 0;
		for (int i = 0; i < pageUtilizers.size(); i++) {
			IPage page = pageUtilizers.get(i);
			if (page.isOnlineInstance()) {
				online++;
			} else {
				draft++;
			}
		}
		assertEquals(expectedOnline, online);
		assertEquals(expectedDraft, draft);
	}

	private String executeViewWidgets(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Portal/WidgetType", "viewWidgets");
		String result = this.executeAction();
		return result;
	}

	private String executeViewWidgetUtilizers(String username, String widgetTypeCode) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/Portal/WidgetType", "viewWidgetUtilizers");
		if (null != widgetTypeCode) {
			this.addParameter("widgetTypeCode", widgetTypeCode);
		}
		String result = this.executeAction();
		return result;
	}

}
