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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Returns the information of the desired system parameter.
 * Key of the desired system parameter, admitted values are:<br/>
 * "startLang" returns the code of start language of web browsing<br/>
 * "defaultLang" returns the code of default language<br/>
 * "currentLang" returns the code of current language<br/>
 * "langs" returns the list of the languages defined in the system<br/> 
 * "systemParam" returns the value of the system parameter specified in the "paramName" attribute.
 * 
 * @author Wiz of Id <wiz@apritisoftware.it>
 */
public class InfoTag extends OutSupport {

	private static final Logger _logger = LoggerFactory.getLogger(InfoTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			if ("startLang".equals(this._key)) {
				Lang startLang = this.extractStartLang();
				this._info = startLang.getCode();
			} else if ("defaultLang".equals(this._key)) {
				ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
				this._info = langManager.getDefaultLang().getCode();
			} else if ("currentLang".equals(this._key)) {
				RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
				Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
				this._info = currentLang.getCode();
			} else if ("langs".equals(this._key)) {
				ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
				this._info = langManager.getLangs();
			} else if ("systemParam".equals(this._key)) {
				if (SystemConstants.PAR_APPL_BASE_URL.equals(this.getParamName())) {
					IURLManager urlManager = (IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this.pageContext);
					this._info = urlManager.getApplicationBaseURL((HttpServletRequest) request);
				} else {
					ConfigInterface confManager = (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, this.pageContext);
					this._info = confManager.getParam(this.getParamName());
				}
			}
		} catch (Throwable t) {
			_logger.error("Error during tag initialization", t);
			throw new JspException("Error during tag initialization", t);
		}
		return EVAL_BODY_INCLUDE;
	}
	
	private Lang extractStartLang() {
		Lang startLang = null;
		ConfigInterface baseConfigManager = (ConfigInterface) ApsWebApplicationUtils.getBean(SystemConstants.BASE_CONFIG_MANAGER, this.pageContext);
		ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
		try {
			String startLangFromBrowser = baseConfigManager.getParam(SystemConstants.CONFIG_PARAM_START_LANG_FROM_BROWSER);
			if (null != startLangFromBrowser && startLangFromBrowser.equalsIgnoreCase("true")) {
				ServletRequest request = this.pageContext.getRequest();
				if (request instanceof HttpServletRequest) {
					String headerLang = ((HttpServletRequest) request).getHeader("Accept-Language");
					if (null != headerLang && headerLang.length() >= 2) {
						String langCode = headerLang.substring(0, 2);
						startLang = langManager.getLang(langCode);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error extracting start lang", t);
		} finally {
			if (null == startLang) {
				startLang = langManager.getDefaultLang();
			}
		}
		return startLang;
	}
	
	/**
	 * Performs the generation of the label and make it available for immediate output or places it
	 * in a variable
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		if (null == this._info) {
			_logger.debug("Null information - key '{}' - parameter '{}'", this.getKey(), this.getParamName());
			return super.doEndTag();
		}
		if (this._varName != null) {
			this.pageContext.setAttribute(this._varName, this._info);
		} else {
			try {
				if (this.getEscapeXml()) {
					out(this.pageContext, this.getEscapeXml(), this._info);
				} else {
					this.pageContext.getOut().print(this._info);
				}
			} catch (Throwable t) {
				_logger.error("Error closing tag", t);
				throw new JspException("Error closing tag", t);
			}
		}
		return EVAL_PAGE;
	}
	
	@Override
	public void release() {
		this._key = null;
		this._varName = null;
		this._info = null;
		this._paramName = null;
		super.escapeXml = true;
	}

	/**
	 * Return the value of the key attribute
	 * @return The key of the requested information
	 */
	public String getKey() {
		return _key;
	}

	/**
	 * Set the value of the key attribute.
	 * @param key The key of the requested information
	 */
	public void setKey(String key) {
		this._key = key;
	}
	/**
	 * Set the name of the variable used to store the result.
	 * @param var The name of the variable.
	 */
	public void setVar(String var) {
		this._varName = var;
	}
	/**
	 * Return the name of the variable used to store the result.
	 * @return The name of the variable.
	 */
	public String getVar() {
		return _varName;
	}

	/** 
	 * Return the name of the requested system parameter.
	 * This value is ignored when the value of the "key" attribute is other than "systemParam"
	 * @return The name of the system parameter.
	 */
	public String getParamName() {
		return _paramName;
	}

	/**
	 * Set the name of the requested system parameter.
	 * This value is ignored when the value of the "key" attribute is other than "systemParam"
	 * @param paramName The name of the system parameter.
	 */
	public void setParamName(String paramName) {
		this._paramName = paramName;
	}
	
	/**
	 * Enable the special characters escaping in the result.
	 * @return True if the result needs to be escaped, false otherwise.
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	/**
	 * Toggles the escaping of the special characters in the result.
	 * @param escapeXml True to escape the result, false otherwise.
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	
	private String _key;
	private String _varName;
	private String _paramName;
	private Object _info;
	
}
