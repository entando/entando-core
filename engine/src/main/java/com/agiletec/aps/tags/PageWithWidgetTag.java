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
package com.agiletec.aps.tags;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Search and return the page (or the list of pages) with the given widget type.
 */
public class PageWithWidgetTag extends TagSupport {

	private static final Logger _logger = LoggerFactory.getLogger(PageWithWidgetTag.class);

	@Override
	public int doStartTag() throws JspException {
		IPageManager pageManager = (IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
		try {
			List<IPage> pages = pageManager.getOnlineWidgetUtilizers(this.getWidgetTypeCode());
			if (StringUtils.isNotBlank(this.getFilterParamName()) && StringUtils.isNotBlank(this.getFilterParamValue())) {
				pages = this.filterByConfigParamValue(pages);
			}
			if (this.isListResult()) {
				this.pageContext.setAttribute(this.getVar(), pages);
			} else if (null != pages && pages.size() > 0) {
				this.pageContext.setAttribute(this.getVar(), pages.get(0));
			}
		} catch (Throwable t) {
			_logger.error("Error in doStartTag", t);
			// ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error in doStartTag", t);
		}
		return super.doStartTag();
	}

	protected List<IPage> filterByConfigParamValue(List<IPage> pages) {
		List<IPage> filteredPages = new ArrayList<IPage>();
		Iterator<IPage> it = pages.iterator();
		while (it.hasNext()) {
			IPage currentPage = it.next();
			Widget[] widgets = currentPage.getWidgets();
			if (widgets != null) {
				for (Widget currentWidget : widgets) {
					if (null != currentWidget && currentWidget.getType().getCode().equals(this.getWidgetTypeCode())) {
						ApsProperties config = currentWidget.getConfig();
						if (null != config) {
							String value = config.getProperty(this.getFilterParamName());
							if (StringUtils.isNotBlank(value) && value.equals(this.getFilterParamValue())) {
								filteredPages.add(currentPage);
							}
						}
					}
				}
			}
		}
		return filteredPages;
	}

	@Override
	public void release() {
		super.release();
		this.setWidgetTypeCode(null);
		this.setVar(null);
		this.setListResult(false);
		this.setFilterParamValue(null);
		this.setFilterParamName(null);
	}

	public String getVar() {
		return _var;
	}

	public void setVar(String var) {
		this._var = var;
	}

	public boolean isListResult() {
		return _listResult;
	}

	public void setListResult(boolean listResult) {
		this._listResult = listResult;
	}

	public String getFilterParamName() {
		return _filterParamName;
	}

	public void setFilterParamName(String filterParamName) {
		this._filterParamName = filterParamName;
	}

	public String getFilterParamValue() {
		return _filterParamValue;
	}

	public void setFilterParamValue(String filterParamValue) {
		this._filterParamValue = filterParamValue;
	}

	public String getWidgetTypeCode() {
		return _widgetTypeCode;
	}

	public void setWidgetTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}

	private String _widgetTypeCode;
	private String _var;
	private boolean _listResult;
	private String _filterParamName;
	private String _filterParamValue;

}
