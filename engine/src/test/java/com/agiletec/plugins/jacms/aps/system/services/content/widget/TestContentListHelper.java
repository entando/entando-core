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
package com.agiletec.plugins.jacms.aps.system.services.content.widget;

import java.util.List;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;

/**
 * @author E.Santoboni
 */
public class TestContentListHelper extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetFilters() throws Throwable {
		String filtersShowletParam = "(key=DataInizio;attributeFilter=true;start=21/10/2007;order=DESC)+(key=Titolo;attributeFilter=true;order=ASC)";
		EntitySearchFilter[] filters = this._helper.getFilters("EVN", filtersShowletParam, this.getRequestContext());
		assertEquals(2, filters.length);
		EntitySearchFilter filter = filters[0];
		assertEquals("DataInizio", filter.getKey());
		assertEquals(DateConverter.parseDate("21/10/2007", "dd/MM/yyyy"), filter.getStart());
		assertNull(filter.getEnd());
		assertNull(filter.getValue());
		assertEquals("DESC", filter.getOrder().toString());
	}

	public void testGetFilters_OneDefinition() {
		RequestContext reqCtx = this.getRequestContext();
		String contentType = "ART";
		String showletParam = "(key=Titolo;attributeFilter=TRUE;start=START;end=END;like=FALSE;order=ASC)";
		EntitySearchFilter[] filters = this._helper.getFilters(contentType, showletParam, reqCtx);

		assertNotNull(filters);
		assertEquals(1, filters.length);

		EntitySearchFilter entitySearchFilter = filters[0];
		assertNotNull(entitySearchFilter);

		assertEquals("Titolo", entitySearchFilter.getKey());
		assertEquals("START", entitySearchFilter.getStart());
		assertEquals("END", entitySearchFilter.getEnd());
		assertEquals("ASC", entitySearchFilter.getOrder().toString());

		contentType = "ART";
		showletParam = "(key=Titolo;attributeFilter=TRUE;start=START;end=END;like=FALSE;order=DESC)";
		filters = this._helper.getFilters(contentType, showletParam, reqCtx);

		assertNotNull(filters);
		assertEquals(1, filters.length);

		entitySearchFilter = filters[0];
		assertNotNull(entitySearchFilter);

		assertEquals("Titolo", entitySearchFilter.getKey());
		assertEquals("START", entitySearchFilter.getStart());
		assertEquals("END", entitySearchFilter.getEnd());
		assertEquals("DESC", entitySearchFilter.getOrder().toString());

		contentType = "ART";
		showletParam = "(key=descr;value=VALUE;attributeFilter=FALSE;order=ASC)";
		filters = this._helper.getFilters(contentType, showletParam, reqCtx);

		assertNotNull(filters);
		assertEquals(1, filters.length);

		entitySearchFilter = filters[0];
		assertNotNull(entitySearchFilter);

		assertEquals("descr", entitySearchFilter.getKey());
		assertEquals(null, entitySearchFilter.getStart());
		assertEquals(null, entitySearchFilter.getEnd());
		assertEquals("ASC", entitySearchFilter.getOrder().toString());
	}

	public void testGetFilters_TwoDefinition() {
		RequestContext reqCtx = this.getRequestContext();
		String contentType = "ART";
		String showletParam = "(key=Titolo;attributeFilter=TRUE;start=START;end=END;like=FALSE;order=ASC)+(key=descr;value=VALUE;attributeFilter=FALSE;order=ASC)";
		EntitySearchFilter[] filters = this._helper.getFilters(contentType, showletParam, reqCtx);

		assertNotNull(filters);
		assertEquals(2, filters.length);

		EntitySearchFilter entitySearchFilter = filters[0];
		assertNotNull(entitySearchFilter);

		assertEquals("Titolo", entitySearchFilter.getKey());
		assertEquals("START", entitySearchFilter.getStart());
		assertEquals("END", entitySearchFilter.getEnd());
		assertEquals("ASC", entitySearchFilter.getOrder().toString());
		assertEquals(null, entitySearchFilter.getValue());
		assertTrue(entitySearchFilter.isAttributeFilter());

		entitySearchFilter = filters[1];
		assertNotNull(entitySearchFilter);

		assertEquals("descr", entitySearchFilter.getKey());
		assertEquals(null, entitySearchFilter.getStart());
		assertEquals(null, entitySearchFilter.getEnd());
		assertEquals("ASC", entitySearchFilter.getOrder().toString());
		assertFalse(entitySearchFilter.isAttributeFilter());
		Object obj = entitySearchFilter.getValue();
		assertNotNull(obj);
		assertEquals(String.class, obj.getClass());
		assertEquals("VALUE", (String) obj);
	}

	public void testGetContents_1() throws Throwable {
		String pageCode = "pagina_1";
		int frame = 1;
		try {
			this.setUserOnSession("guest");
			RequestContext reqCtx = this.valueRequestContext(pageCode, frame);
			MockContentListTagBean bean = new MockContentListTagBean();
			bean.setContentType("EVN");
			EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true);
			filter.setOrder(EntitySearchFilter.DESC_ORDER);
			bean.addFilter(filter);
			List<String> contents = this._helper.getContentsId(bean, reqCtx);
			String[] expected = { "EVN194", "EVN193", "EVN24", "EVN23", "EVN25", "EVN20", "EVN21", "EVN192", "EVN191" };
			assertEquals(expected.length, contents.size());
			for (int i = 0; i < expected.length; i++) {
				assertEquals(expected[i], contents.get(i));
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			this.setPageWidgets(pageCode, frame, null);
		}
	}

	public void testGetContents_2() throws Throwable {
		String pageCode = "pagina_1";
		int frame = 1;
		try {
			this.setUserOnSession("admin");
			RequestContext reqCtx = this.valueRequestContext(pageCode, frame);
			MockContentListTagBean bean = new MockContentListTagBean();
			bean.setContentType("EVN");
			bean.addCategory("evento");
			EntitySearchFilter filter = new EntitySearchFilter("DataInizio", true);
			filter.setOrder(EntitySearchFilter.DESC_ORDER);
			bean.addFilter(filter);
			List<String> contents = this._helper.getContentsId(bean, reqCtx);
			String[] expected = { "EVN193", "EVN192" };
			assertEquals(expected.length, contents.size());
			for (int i = 0; i < expected.length; i++) {
				assertEquals(expected[i], contents.get(i));
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			this.setPageWidgets(pageCode, frame, null);
		}
	}

	private RequestContext valueRequestContext(String pageCode, int frame) throws Throwable {
		RequestContext reqCtx = this.getRequestContext();
		try {
			Widget widgetToAdd = this.getShowletForTest("content_viewer_list", null);
			this.setPageWidgets(pageCode, frame, widgetToAdd);

			IPage page = this._pageManager.getOnlinePage(pageCode);
			reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE, page);
			Widget widget = page.getWidgets()[frame];
			reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_WIDGET, widget);
			reqCtx.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME, new Integer(frame));
		} catch (Throwable t) {
			this.setPageWidgets(pageCode, frame, null);
			throw t;
		}
		return reqCtx;
	}

	private void setPageWidgets(String pageCode, int frame, Widget widget) throws ApsSystemException {
		IPage page = this._pageManager.getDraftPage(pageCode);
		page.getWidgets()[frame] = widget;
		page.getWidgets()[frame] = widget;
		this._pageManager.updatePage(page);
	}

	private Widget getShowletForTest(String showletTypeCode, ApsProperties config) throws Throwable {
		WidgetType type = this._showletTypeManager.getWidgetType(showletTypeCode);
		Widget widget = new Widget();
		widget.setType(type);
		if (null != config) {
			widget.setConfig(config);
		}
		return widget;
	}

	private void init() throws Exception {
		try {
			this._pageManager = (IPageManager) this.getService(SystemConstants.PAGE_MANAGER);
			this._showletTypeManager = (IWidgetTypeManager) this.getService(SystemConstants.WIDGET_TYPE_MANAGER);
			this._helper = (IContentListWidgetHelper) this.getApplicationContext().getBean(JacmsSystemConstants.CONTENT_LIST_HELPER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IPageManager _pageManager = null;
	private IWidgetTypeManager _showletTypeManager;
	private IContentListWidgetHelper _helper;

}
