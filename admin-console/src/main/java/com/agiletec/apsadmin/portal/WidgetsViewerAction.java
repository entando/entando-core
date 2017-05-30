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

import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import java.util.ArrayList;

/**
 * @author E.Santoboni
 */
public class WidgetsViewerAction extends AbstractPortalAction {

	private static final Logger _logger = LoggerFactory.getLogger(WidgetsViewerAction.class);

	@Deprecated
	public String viewShowlets() {
		return viewWidgets();
	}

	public String viewWidgets() {
		return SUCCESS;
	}

	@Deprecated
	public List<IPage> getShowletUtilizers(String widgetTypeCode) {
		return this.getWidgetUtilizers(widgetTypeCode);
	}

	public List<IPage> getWidgetUtilizers(String widgetTypeCode) {
		List<IPage> utilizers = new ArrayList<>();
		try {
			List<IPage> draftUtilizers = this.getPageManager().getDraftWidgetUtilizers(widgetTypeCode);
			if (null != draftUtilizers && draftUtilizers.size() > 0) {
				utilizers.addAll(draftUtilizers);
			}
			List<IPage> onlineUtilizers = this.getPageManager().getOnlineWidgetUtilizers(widgetTypeCode);
			if (null != onlineUtilizers && onlineUtilizers.size() > 0) {
				utilizers.addAll(onlineUtilizers);
			}
		} catch (Throwable t) {
			_logger.error("Error on extracting widgetUtilizers : widget type code {}", t);
			throw new RuntimeException("Error on extracting widgetUtilizers : widget type code " + widgetTypeCode, t);
		}
		return utilizers;
	}

	public Group getGroup(String groupCode) {
		Group group = super.getGroupManager().getGroup(groupCode);
		if (null == group) {
			group = super.getGroupManager().getGroup(Group.FREE_GROUP_NAME);
		}
		return group;
	}

	public String listJson() {
		return SUCCESS;
	}

	@Deprecated
	public String viewShowletUtilizers() {
		return viewWidgetUtilizers();
	}

	public String viewWidgetUtilizers() {
		return SUCCESS;
	}

	public List<IPage> getWidgetUtilizers() {
		return this.getWidgetUtilizers(this.getWidgetTypeCode());
	}

	@Deprecated
	public List<IPage> getShowletUtilizers() {
		return this.getWidgetUtilizers();
	}

	public WidgetType getWidgetType(String typeCode) {
		return this.getWidgetTypeManager().getWidgetType(typeCode);
	}

	@Deprecated
	public WidgetType getShowletType(String typeCode) {
		return this.getWidgetType(typeCode);
	}

	@Deprecated
	public String getShowletTypeCode() {
		return this.getWidgetTypeCode();
	}

	@Deprecated
	public void setShowletTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}

	public String getWidgetTypeCode() {
		return _widgetTypeCode;
	}

	public void setWidgetTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}

	private String _widgetTypeCode;

}
