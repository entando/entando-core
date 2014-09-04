/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
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
public class SimpleWidgetConfigAction extends AbstractPortalAction implements ISimpleWidgetConfigAction {

	private static final Logger _logger = LoggerFactory.getLogger(SimpleWidgetConfigAction.class);
	
	@Override
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
		this.setShowlet(widget);
		return SUCCESS;
	}
	
	protected Widget createNewShowlet() throws Exception {
		if (this.getWidgetTypeCode() == null || this.getShowletType(this.getWidgetTypeCode()) == null) {
			throw new Exception("Widget Code missin or invalid : " + this.getWidgetTypeCode());
		}
		Widget widget = new Widget();
		WidgetType type = this.getShowletType(this.getWidgetTypeCode());
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
		clone.setPublishedContent(widget.getPublishedContent());
		return clone;
	}
	
	@Override
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
			//ApsSystemUtils.logThrowable(t, this, "save");
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
				if ("contentId".equals(paramName)) {
					widget.setPublishedContent(value);
				}
			}
		}
		this.setShowlet(widget);
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
	
	public WidgetType getShowletType(String typeCode) {
		return this.getWidgetTypeManager().getWidgetType(typeCode);
	}
	
	/**
	 * @deprecated Use {@link #getWidget()} instead
	 */
	@Override
	public Widget getShowlet() {
		return getWidget();
	}
	
	@Override
	public Widget getWidget() {
		return _showlet;
	}
	public void setShowlet(Widget widget) {
		this._showlet = widget;
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
	
	private Widget _showlet;
	
}
