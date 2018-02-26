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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.tags.util.IParameterParentTag;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.aps.tags.ExtendedTagSupport;

/**
 * Tag for string localisation
 * @author S.Didaci - E.Santoboni
 */
public class I18nTag extends ExtendedTagSupport implements IParameterParentTag {

	private static final Logger _logger = LoggerFactory.getLogger(I18nTag.class);
	
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}
	
	@Override
	public int doEndTag() throws JspException {
		RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
		try {
			Lang currentLang = null;
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
			throw new JspException("Error during tag initialization", t);
		}
		return super.doStartTag();
	}

	private String extractLabel(Lang currentLang) throws ApsSystemException {
		String label = null;
		II18nManager i18nManager = (II18nManager) ApsWebApplicationUtils.getBean(SystemConstants.I18N_MANAGER, this.pageContext);
		try {
			String key = this.getKey();
			String langCode = this.getLang();
			Map<String, String> params = this.getParameters();
			if (StringUtils.isNotEmpty(langCode) && !currentLang.getCode().equalsIgnoreCase(langCode)) {
				label = i18nManager.renderLabel(key, langCode, false, params);
			}
			if (label == null) {
				label = i18nManager.renderLabel(key, currentLang.getCode(), true, params);
			}
		} catch (Throwable t) {
			_logger.error("Error getting label", t);
			throw new ApsSystemException("Error getting label", t);
		}
		return label;
	}
	
	@Override
	public void release() {
		super.release();
		this._key = null;
		this._lang = null;
		this._varName = null;
		this._parameters = null;
	}
	
	/**
	 * Return the lang code.
	 * @return The lang code.
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
	public void setKey(String key) {
		this._key = key;
	}
	
	public Map<String, String> getParameters() {
		return this._parameters;
	}
	
	@Override
	public void addParameter(String name, String value) {
		if (null == this._parameters) {
			this._parameters = new HashMap<String, String>();
		}
		this._parameters.put(name, value);
	}
	
	private String _varName;
	private String _key;
	private String _lang;
	private Map<String, String> _parameters;
	
}
