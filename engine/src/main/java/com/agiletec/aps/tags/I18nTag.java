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

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Tag for string localisation
 * @author S.Didaci - E.Santoboni
 */
public class I18nTag extends OutSupport {

	private static final Logger _logger = LoggerFactory.getLogger(I18nTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
		try {
			Lang currentLang = null;;
			if (reqCtx != null) {
				currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			} else {
				ILangManager langManager = (ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
				currentLang = langManager.getDefaultLang();
			}
			String label = this.extractLabel(currentLang);
			if (this._varName != null) {
				this.pageContext.setAttribute(this._varName, label);
			} else {
				if (this.getEscapeXml()) {
					out(this.pageContext, this.getEscapeXml(), label);
				} else {
					this.pageContext.getOut().print(label);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error during tag initialization", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization", t);
		}
		return super.doStartTag();
	}

	private String extractLabel(Lang currentLang) throws ApsSystemException {
		String label = null;
		II18nManager i18nManager = (II18nManager) ApsWebApplicationUtils.getBean(SystemConstants.I18N_MANAGER, this.pageContext);
		try {
			if (this._lang == null 
					|| this._lang.equals("") 
					|| currentLang.getCode().equalsIgnoreCase(this._lang)) {
				label = i18nManager.getLabel(this._key, currentLang.getCode());
				if (label == null) {
					ILangManager langManager = 
						(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
					Lang defaultLang = langManager.getDefaultLang();
					label = i18nManager.getLabel(this._key, defaultLang.getCode());
				}
			} else {
				label = i18nManager.getLabel(this._key, this._lang);
			}
			if (label == null) {
				label = this._key;
			}
		} catch (Throwable t) {
			_logger.error("Error getting label", t);
			//ApsSystemUtils.logThrowable(t, this, "extractLabel");
			throw new ApsSystemException("Error getting label", t);
		}
		return label;
	}
	
	@Override
	public void release() {
		this._key = null;
		this._lang = null;
		this._varName = null;
		super.escapeXml = true;
	}

	/**
	 * Return the lang code.
	 * @return The code.
	 */
	public String getLang() {
		return _lang;
	}

	/**
	 * Set the lang code.
	 * @param lang The code
	 */
	public void setLang(String lang) {
		this._lang = lang;
	}

	/**
	 * Set the name of the variable where the requested label will be placed.
	 * @param var The name.
	 */
	public void setVar(String var) {
		this._varName = var;
	}

	/**
	 * Set the name of the variable where the requested label will be placed.
	 * @return The name of the variable.
	 */
	public String getVar() {
		return _varName;
	}

	/**
	 * Return the key of the label to return.
	 * @return The key of the label
	 */
	public String getKey() {
		return _key;
	}

	/**
	 * Set the key of the label to return.
	 * @param key The key of the requested label
	 */
	public void setKey(String _key) {
		this._key = _key;
	}
	
	/**
	 * Checks whether to perform the escaping of the special characters.
	 * @return TTrue to escape special characters, false otherwise.
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	/**
	 * Toggles the escape of the special characters of the label to return.
	 * @param escapeXml True to escape special characters
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	
	private String _varName;
	private String _key;
	private String _lang;
	
}