/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.apsadmin.portal;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;

/**
 * Main action class for the pages configuration.
 * @author E.Santoboni
 */
public class PageConfigAction extends AbstractPortalAction implements IPageConfigAction {

	private static final Logger _logger = LoggerFactory.getLogger(PageConfigAction.class);
	
	@Override
	public String configure() {
		String pageCode = (this.getSelectedNode() != null ? this.getSelectedNode() : this.getPageCode());
		this.setPageCode(pageCode);
		String check = this.checkSelectedNode(pageCode);
		if (null != check) return check;
		return SUCCESS;
	}
	
	@Override
	public String editFrame() {
		try {
			String result = this.checkBaseParams();
			if (null != result) {
				return result;
			}
			Widget widget = this.getCurrentPage().getWidgets()[this.getFrame()];// can be null
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
			//ApsSystemUtils.logThrowable(e, this, "editFrame");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * @deprecated Use {@link #joinWidget()} instead
	 */
	@Override
	public String joinShowlet() {
		return this.joinWidget();
	}

	@Override
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
			//ApsSystemUtils.logThrowable(e, this, "joinWidget");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	@Deprecated
	public String removeShowlet() {
		return this.trashWidget();
	}
	
	/**
	 * @deprecated Use {@link #trashWidget()} instead
	 */
	@Override
	public String trashShowlet() {
		return trashWidget();
	}

	@Override
	public String trashWidget() {
		try {
			String result = this.checkBaseParams();
			if (null != result) return result;
		} catch (Exception e) {
			_logger.error("error in trashWidget", e);
			//ApsSystemUtils.logThrowable(e, this, "trashShowlet");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * @deprecated Use {@link #deleteWidget()} instead
	 */
	@Override
	public String deleteShowlet() {
		return deleteWidget();
	}

	@Override
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
			//ApsSystemUtils.logThrowable(e, this, "deleteWidget");
			return FAILURE;
		}
		return SUCCESS;
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
		if (this.getFrame() == -1 || this.getFrame() >= page.getWidgets().length) {
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
	
	public Widget getShowlet() {
		return _showlet;
	}
	public void setShowlet(Widget widget) {
		this._showlet = widget;
	}
	
	private String _pageCode;
	private int _frame = -1;
	private String _widgetAction;
	
	private String _widgetTypeCode;
	
	private Widget _showlet;
	
}
