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

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;
import com.agiletec.apsadmin.portal.model.DeleteWidgetResponse;
import com.agiletec.apsadmin.portal.model.JoinWidgetResponse;
import com.agiletec.apsadmin.portal.model.SwapWidgetRequest;
import com.agiletec.apsadmin.portal.model.SwapWidgetRequestValidator;
import com.agiletec.apsadmin.portal.model.SwapWidgetResponse;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

/**
 * Main action class for the pages configuration.
 * @author E.Santoboni
 */
public class PageConfigAction extends AbstractPortalAction implements ServletResponseAware {

	private static final Logger _logger = LoggerFactory.getLogger(PageConfigAction.class);
	private HttpServletResponse response;

	@Autowired
	private SwapWidgetRequestValidator validator;

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}


	public String configure() {
		String pageCode = (this.getSelectedNode() != null ? this.getSelectedNode() : this.getPageCode());
		this.setPageCode(pageCode);
		String check = this.checkSelectedNode(pageCode);
		if (null != check) return check;
		return SUCCESS;
	}

	public String editFrame() {
		try {
			String result = this.checkBaseParams();
			if (null != result) {
				return result;
			}
			Widget widget = this.getCurrentPage().getDraftWidgets()[this.getFrame()];// can be null
			this.setShowlet(widget);
			if (widget != null) {
				WidgetType widgetType = widget.getType();
				_logger.debug("pageCode: {}, frame: {}, widgetType: {}", this.getPageCode(), this.getFrame(), widgetType.getCode());
				this.setWidgetAction(widgetType.getAction());
				if (null == widgetType.getConfig() && null != this.getWidgetAction()) {
					return "configureSpecialWidget";
				}
			} else {
				_logger.debug("pageCode: {} frame: {}, empty widhet to config", this.getPageCode(), this.getFrame());
			}
		} catch (Exception e) {
			_logger.error("error in edit frame", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Deprecated
	public String joinShowlet() {
		return this.joinWidget();
	}

	public String joinWidget() {
		try {
			String result = this.checkBaseParams();
			if (null != result) return result;
			if (null != this.getWidgetTypeCode() && this.getWidgetTypeCode().length() == 0) {
				this.addActionError(this.getText("error.page.widgetTypeCodeUnknown"));
				return INPUT;
			}
			_logger.debug("code={}, pageCode={}, frame={}" + this.getWidgetTypeCode(),this.getPageCode() ,this.getFrame());
			WidgetType widgetType = this.getShowletType(this.getWidgetTypeCode());
			if (null == widgetType) {
				this.addActionError(this.getText("error.page.widgetTypeCodeUnknown"));
				return INPUT;
			}
			if (null == widgetType.getConfig() && null != widgetType.getAction()) {
				this.setWidgetAction(widgetType.getAction());
				//continue to widget configuration
				return "configureSpecialWidget";
			}
			Widget widget = new Widget();
			widget.setType(widgetType);
			this.getPageManager().joinWidget(this.getPageCode(), widget, this.getFrame());
			this.addActivityStreamInfo(ApsAdminSystemConstants.ADD, true);
		} catch (Exception e) {
			_logger.error("error in joinWidget", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String joinWidgetJson() {
		try {
			String result = this.checkBaseParams();
			if (null != result) return result;
			if (null != this.getWidgetTypeCode() && this.getWidgetTypeCode().length() == 0) {
				this.addActionError(this.getText("error.page.widgetTypeCodeUnknown"));
				return INPUT;
			}
			_logger.debug("code={}, pageCode={}, frame={}" + this.getWidgetTypeCode(),this.getPageCode() ,this.getFrame());
			WidgetType widgetType = this.getShowletType(this.getWidgetTypeCode());
			if (null == widgetType) {
				this.addActionError(this.getText("error.page.widgetTypeCodeUnknown"));
				return INPUT;
			}
			if (null == widgetType.getConfig() && null != widgetType.getAction()) {
				this.setWidgetAction(widgetType.getAction());
				//continue to widget configuration
				return "configureSpecialWidget";
			}
			Widget widget = new Widget();
			widget.setType(widgetType);
			this.getPageManager().joinWidget(this.getPageCode(), widget, this.getFrame());
			this.addActivityStreamInfo(ApsAdminSystemConstants.ADD, true);
		} catch (Exception e) {
			_logger.error("error in joinWidget", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	public JoinWidgetResponse getJoinWidgetResponse() {
		JoinWidgetResponse response = new JoinWidgetResponse(this);
		if (StringUtils.isNotBlank(this.getPageCode())) {
			IPage page = this.getPage(this.getPageCode());
			response.setPage(page);
			if (StringUtils.isNotBlank(this.getWidgetAction())) {
				String url = "/do/Page/SpecialWidget/" + this.getWidgetAction() + "?";
				String[] params = new String[]{"pageCode="+ this.getPageCode(), "widgetTypeCode="+ this.getWidgetTypeCode(), "frame="+ this.getFrame() };
				url = url + StringUtils.join(params, "&");
				response.setRedirectLocation(url);
			}
		}
		return response;
	}

	@Deprecated
	public String removeShowlet() {
		return this.trashWidget();
	}

	@Deprecated
	public String trashShowlet() {
		return trashWidget();
	}

	public String trashWidget() {
		try {
			String result = this.checkBaseParams();
			if (null != result) return result;
		} catch (Exception e) {
			_logger.error("error in trashWidget", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Deprecated
	public String deleteShowlet() {
		return deleteWidget();
	}

	public String deleteWidget() {
		try {
			String result = this.checkBaseParams();
			if (null != result) {
				return result;
			}
			this.getPageManager().removeWidget(this.getPageCode(), this.getFrame());
			this.addActivityStreamInfo(ApsAdminSystemConstants.DELETE, true);
		} catch (Exception e) {
			_logger.error("error in deleteWidget", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String deleteWidgetJson() {
		try {
			String result = this.checkBaseParams();
			if (null != result) {
				return result;
			}
			this.getPageManager().removeWidget(this.getPageCode(), this.getFrame());
			this.addActivityStreamInfo(ApsAdminSystemConstants.DELETE, true);
		} catch (Exception e) {
			_logger.error("error in deleteWidget", e);
			return FAILURE;
		}
		return SUCCESS;
	}

	public DeleteWidgetResponse getDeleteWidgetResponse() {
		DeleteWidgetResponse response = new DeleteWidgetResponse(this);
		if (StringUtils.isNotBlank(this.getPageCode())) {
			IPage page = this.getPage(this.getPageCode());
			response.setPage(page);
		}
		return response;		
	}


	public String move() {
		try {
			SwapWidgetRequest ajaxRequest = this.getSwapWidgetRequest();
			this.validator.validateRequest(ajaxRequest, this);
			if (this.hasActionErrors() || this.hasFieldErrors()) {
				this.response.setStatus(Status.BAD_REQUEST.getStatusCode());
				return INPUT;
			}

			this.getPageManager().moveWidget(ajaxRequest.getPageCode(), ajaxRequest.getSrc(), ajaxRequest.getDest());
		} catch (Throwable t) {
			this.response.setStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode());
			_logger.error("error in move", t);
			return FAILURE;
		}
		this.response.setStatus(Status.OK.getStatusCode());
		this.setPageCode(swapWidgetRequest.getPageCode());
		return SUCCESS;
	}

	public SwapWidgetResponse getMoveWidgetResponse() {
		SwapWidgetResponse response = new SwapWidgetResponse(this);
		if (StringUtils.isNotBlank(this.getPageCode())) {
			IPage page = this.getPage(this.getPageCode());
			response.setPage(page);
		}
		return response;
	}


	//TODO METODO COMUNE ALLA CONFIG SPECIAL WIDGET
	protected String checkBaseParams() {
		IPage page = this.getPage(this.getPageCode());
		if (!this.isUserAllowed(page)) {
			_logger.info("Curent user not allowed");
			this.addActionError(this.getText("error.page.userNotAllowed"));
			return "pageTree";
		}
		if (null == page) {
			_logger.info("Null page code");
			this.addActionError(this.getText("error.page.invalidPageCode"));
			return "pageTree";
		}
		if (this.getFrame() == -1 || this.getFrame() >= page.getDraftWidgets().length) {
			_logger.info("Mandatory frame id or invalid - '{}'", this.getFrame());
			this.addActionError(this.getText("error.page.invalidPageFrame"));
			return "pageTree";
		}
		return null;
	}

	protected void addActivityStreamInfo(int strutsAction, boolean addLink) {
		IPage page = this.getPage(this.getPageCode());
		ActivityStreamInfo asi = this.getPageActionHelper()
				.createActivityStreamInfo(page, strutsAction, addLink, "configure");
		super.addActivityStreamInfo(asi);
	}

	public WidgetType getShowletType(String typeCode) {
		return this.getWidgetTypeManager().getWidgetType(typeCode);
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
	public String getShowletAction() {
		return this.getWidgetAction();
	}
	@Deprecated
	public void setShowletAction(String showletAction) {
		this.setWidgetAction(showletAction);
	}

	public String getWidgetAction() {
		return _widgetAction;
	}
	public void setWidgetAction(String widgetAction) {
		this._widgetAction = widgetAction;
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

	@Deprecated
	public Widget getShowlet() {
		return this.getWidget();
	}
	@Deprecated
	public void setShowlet(Widget widget) {
		this.setWidget(widget);
	}

	public Widget getWidget() {
		return _widget;
	}
	public void setWidget(Widget widget) {
		this._widget = widget;
	}

	public SwapWidgetRequest getSwapWidgetRequest() {
		return swapWidgetRequest;
	}
	public void setSwapWidgetRequest(SwapWidgetRequest swapWidgetRequest) {
		this.swapWidgetRequest = swapWidgetRequest;
	}

	protected IPageActionHelper getPageActionHelper() {
		return _pageActionHelper;
	}
	public void setPageActionHelper(IPageActionHelper pageActionHelper) {
		this._pageActionHelper = pageActionHelper;
	}

	private String _pageCode;
	private int _frame = -1;
	private String _widgetAction;
	private String _widgetTypeCode;	
	private Widget _widget;
	private SwapWidgetRequest swapWidgetRequest;

	private IPageActionHelper _pageActionHelper;

}
