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
package com.agiletec.aps.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Returns informations about the showlet where the tag resides.
 * The "param" attribute acceptes the following values:
 * - "code" returns the code of the associated showlet type (empty if none associated)<br/>
 * - "title" returns the name of the associated showlet type (empty if none associated)<br/>
 * - "config" returns the value of the configuration parameter declared in the "configParam" attribute<br/>
 * To obtain information about a showlet placed in a frame other than the current, use the "frame" attribute.
 * @author E.Santoboni - E.Mezzano
 */
@SuppressWarnings("serial")
public class CurrentWidgetTag extends OutSupport {
	
	private static final Logger _logger = LoggerFactory.getLogger(CurrentWidgetTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		try {
			Widget widget = this.extractShowlet();
			if (null == widget) return super.doStartTag();
			String value = null;
			if ("code".equals(this.getParam())) {
				value = widget.getType().getCode();
			} else if ("title".equals(this.getParam())) {
				value = this.extractTitle(widget);
			} else if ("config".equals(this.getParam())) {
				ApsProperties config = widget.getConfig();
				if (null != config) {
					value = config.getProperty(this.getConfigParam());
				}
			}
			if (null != value) {
				String var = this.getVar();
				if (null == var || "".equals(var)) {
					if (this.getEscapeXml()) {
						out(this.pageContext, this.getEscapeXml(), value);
					} else {
						this.pageContext.getOut().print(value);
					}
				} else {
					this.pageContext.setAttribute(this.getVar(), value);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error detected during showlet preprocessing", t);
			//ApsSystemUtils.logThrowable(t, this, "doEndTag", msg);
			String msg = "Error detected during showlet preprocessing";
			throw new JspException(msg, t);
		}
		return super.doStartTag();
	}
	
	private String extractTitle(Widget widget) {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		WidgetType type = widget.getType();
		String value = type.getTitles().getProperty(currentLang.getCode());
		if (null == value || value.trim().length() == 0) {
			ILangManager langManager = 
				(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			Lang defaultLang = langManager.getDefaultLang();
			value = type.getTitles().getProperty(defaultLang.getCode());
		}
		return value;
	}
	
	private Widget extractShowlet() {
		ServletRequest req =  this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) req.getAttribute(RequestContext.REQCTX);
		Widget widget = null;
		if (this.getFrame() < 0) {
			widget = (Widget) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_WIDGET));
		} else {
			IPage currentPage = (IPage) reqCtx.getExtraParam((SystemConstants.EXTRAPAR_CURRENT_PAGE));
			Widget[] showlets = currentPage.getWidgets();
			if (showlets.length > this.getFrame()) {
				widget = showlets[this.getFrame()];
			}
		}
		return widget;
	}
	
	@Override
	public void release() {
		super.release();
		this._param = null;
		this._configParam = null;
		this._var = null;
		this._frame = -1;
		super.escapeXml = true;
	}
	
	public String getParam() {
		return _param;
	}
	public void setParam(String param) {
		this._param = param;
	}
	
	public String getConfigParam() {
		return _configParam;
	}
	public void setConfigParam(String configParam) {
		this._configParam = configParam;
	}
	
	public String getVar() {
		return _var;
	}
	public void setVar(String var) {
		this._var = var;
	}
	
	public int getFrame() {
		return _frame;
	}
	public void setFrame(int frame) {
		this._frame = frame;
	}
	
	/**
	 * Checks if the special characters must be escaped
	 * @return True if the special characters must be escaped
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	/**
	 * Toggles the escape of the special characters of the result.
	 * @param escapeXml True to perform the escaping, false otherwise.
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	
	private String _param;
	private String _configParam;
	private String _var;
	
	private int _frame = -1;
	
}