/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.apsadmin.portal.specialwidget;

import java.util.List;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.AbstractPortalAction;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

/**
 * This action class handles the configuration of the widgets with parameters.
 * @author E.Santoboni
 */
public class SimpleWidgetConfigAction extends AbstractPortalAction {

	private static final Logger _logger = LoggerFactory.getLogger(SimpleWidgetConfigAction.class);
	
	public String init() {
		this.checkBaseParams();
		return this.extractInitConfig();
	}
	
	protected String extractInitConfig() {
		if (null != this.getWidget()) return SUCCESS;
		Widget widget = this.getCurrentPage().getWidgets()[this.getFrame()];
		if (null == widget) {
			try {
				widget = this.createNewShowlet();
			} catch (Exception e) {
				_logger.error("error in extractInitConfig", e);
				//TODO METTI MESSAGGIO DI ERRORE NON PREVISTO... Vai in pageTree con messaggio di errore Azione non prevista o cosa del genere
				this.addActionError(this.getText("Message.userNotAllowed"));
				return "pageTree";
			}
			_logger.info("Configurating new Widget {} - Page {} - Frame {}", this.getWidgetTypeCode(), this.getPageCode(), this.getFrame());
		} else {
			_logger.info("Edit Widget config {} - Page {} - Frame {}", this.getWidgetTypeCode(), this.getPageCode(), this.getFrame());
			widget = this.createCloneFrom(widget);
		}
		this.setWidget(widget);
		return SUCCESS;
	}
	
	protected Widget createNewShowlet() throws Exception {
		if (this.getWidgetTypeCode() == null || this.getWidgetType(this.getWidgetTypeCode()) == null) {
			throw new Exception("Widget Code missin or invalid : " + this.getWidgetTypeCode());
		}
		Widget widget = new Widget();
		WidgetType type = this.getWidgetType(this.getWidgetTypeCode());
		widget.setType(type);
		widget.setConfig(new ApsProperties());
		return widget;
	}
	
	protected Widget createCloneFrom(Widget widget) {
		Widget clone = new Widget();
		clone.setType(widget.getType());
		if (null != widget.getConfig()) {
			clone.setConfig((ApsProperties) widget.getConfig().clone());
		}
		return clone;
	}
	
	public String save() {
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
			IPage page = this.getPage(this.getPageCode());
			int strutsAction = (null != page.getWidgets()[this.getFrame()]) ? ApsAdminSystemConstants.ADD : ApsAdminSystemConstants.EDIT;
			this.getPageManager().joinWidget(this.getPageCode(), this.getWidget(), this.getFrame());
			_logger.debug("Saving Widget - code: {}, pageCode: {}, frame: {}", this.getWidget().getType().getCode(), this.getPageCode(), this.getFrame());
			this.addActivityStreamInfo(strutsAction, true);
		} catch (Throwable t) {
			_logger.error("error in save", t);
			return FAILURE;
		}
		return "configure";
	}
	
	protected void addActivityStreamInfo(int strutsAction, boolean addLink) {
		IPage page = this.getPage(this.getPageCode());
		ActivityStreamInfo asi = super.getPageActionHelper()
				.createConfigFrameActivityStreamInfo(page, this.getFrame(), strutsAction, true);
		super.addActivityStreamInfo(asi);
	}
	
	protected void createValuedShowlet() throws Exception {
		Widget widget = this.createNewShowlet();
		List<WidgetTypeParameter> parameters = widget.getType().getTypeParameters();
		for (int i=0; i<parameters.size(); i++) {
			WidgetTypeParameter param = parameters.get(i);
			String paramName = param.getName();
			String value = this.getRequest().getParameter(paramName);
			if (value != null && value.trim().length()>0) {
				widget.getConfig().setProperty(paramName, value);
			}
		}
		this.setWidget(widget);
	}
	
	protected String checkBaseParams() {
		IPage page = this.getPage(this.getPageCode());
		if (null== page || !this.isUserAllowed(page)) {
			_logger.info("User not allowed");
			this.addActionError(this.getText("error.page.userNotAllowed"));
			return "pageTree";
		}
		if (this.getFrame() == -1 || this.getFrame()>= page.getWidgets().length) {
			_logger.info("invalid frame '{}'", this.getFrame());
			this.addActionError(this.getText("error.page.invalidPageFrame"));
			return "pageTree";
		}
		return null;
	}
	
	public IPage getCurrentPage() {
		return this.getPage(this.getPageCode());
	}
	
	public String getPageCode() {
		return _pageCode;
	}
	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}
	
	public int getFrame() {
		return _frame;
	}
	public void setFrame(int frame) {
		this._frame = frame;
	}
	
	@Deprecated
	public WidgetType getShowletType(String typeCode) {
		return this.getWidgetType(typeCode);
	}
	public WidgetType getWidgetType(String typeCode) {
		return this.getWidgetTypeManager().getWidgetType(typeCode);
	}
	
	@Deprecated
	public Widget getShowlet() {
		return this.getWidget();
	}
	public Widget getWidget() {
		return _widget;
	}
	
	@Deprecated
	public void setShowlet(Widget widget) {
		this.setWidget(widget);
	}
	public void setWidget(Widget widget) {
		this._widget = widget;
	}
	
	@Deprecated
	public String getShowletTypeCode() {
		return this.getWidgetTypeCode();
	}
	@Deprecated
	public void setShowletTypeCode(String showletTypeCode) {
		this.setWidgetTypeCode(showletTypeCode);
	}
	
	public String getWidgetTypeCode() {
		return _widgetTypeCode;
	}
	public void setWidgetTypeCode(String widgetTypeCode) {
		this._widgetTypeCode = widgetTypeCode;
	}
	
	private String _pageCode;
	private int _frame = -1;
	
	private String _widgetTypeCode;
	
	private Widget _widget;
	
}
