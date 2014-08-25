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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.aps.tags.util.IParameterParentTag;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Returns the information of the specified page.
 * This tag can use the sub-tag "ParameterTag" to add url parameters 
 * if the info attribute is set to 'url'.
 * @author E.Santoboni
 */
public class PageInfoTag extends OutSupport implements IParameterParentTag {

	private static final Logger _logger = LoggerFactory.getLogger(PageInfoTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		try {
			IPageManager pageManager = 
				(IPageManager) ApsWebApplicationUtils.getBean(SystemConstants.PAGE_MANAGER, this.pageContext);
			IPage page = pageManager.getPage(this.getPageCode());
			if (null == page) {
				_logger.error("Required info for null page : inserted code '{}'", this.getPageCode());
			}
			if (this.getInfo() == null || this.getInfo().equals(CODE_INFO)) {
				this.setValue(page.getCode());
			} else if (this.getInfo().equals(TITLE_INFO)) {
				this.extractPageTitle(page, currentLang);
			} else if (this.getInfo().equals(URL_INFO)) {
				this.extractPageUrl(page, currentLang, reqCtx);
			} else if (this.getInfo().equals(OWNER_INFO)) {
				this.extractPageOwner(page, reqCtx);
			} else if (this.getInfo().equals(CHILD_OF_INFO)) {
				this.extractIsChildOfTarget(page);
			} else if (this.getInfo().equals(HAS_CHILD)) {
				boolean hasChild = (page.getChildren() != null && page.getChildren().length > 0);
				this._value = new Boolean(hasChild).toString();
			}
			this.evalValue();
		} catch (Throwable t) {
			_logger.error("Error during tag initialization", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization ", t);
		}
		this.release();
		return EVAL_PAGE;
	}
	
	protected void extractIsChildOfTarget(IPage page) {
		if (null != this.getTargetPage()) {
			boolean isChild = (page.getCode().equals(this.getTargetPage()) || page.isChildOf(this.getTargetPage()));
			this._value = new Boolean(isChild).toString();
		}
	}
	
	protected void extractPageTitle(IPage page, Lang currentLang) {
		ApsProperties titles = page.getTitles();
		String value = null;
		if ((this.getLangCode() == null) || (this.getLangCode().equals(""))
				|| (currentLang.getCode().equalsIgnoreCase(this.getLangCode()))) {
			value = titles.getProperty(currentLang.getCode());
		} else {
			value = titles.getProperty(this.getLangCode());
		}
		if (value == null || value.trim().equals("")) {
			ILangManager langManager = 
				(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			value = titles.getProperty(langManager.getDefaultLang().getCode());
		}
		this.setValue(value);
	}
	
	protected void extractPageUrl(IPage page, Lang currentLang, RequestContext reqCtx) {
		IURLManager urlManager = 
			(IURLManager) ApsWebApplicationUtils.getBean(SystemConstants.URL_MANAGER, this.pageContext);
		PageURL pageUrl = urlManager.createURL(reqCtx);
		pageUrl.setPageCode(page.getCode());
		if (this.getLangCode() != null) {
			pageUrl.setLangCode(this.getLangCode());
		} else {
			pageUrl.setLangCode(currentLang.getCode());
		}
		if (null != this.getParameters()) {
			Iterator<String> iter = this.getParameters().keySet().iterator();
			while (iter.hasNext()) {
				String name = (String) iter.next();
				pageUrl.addParam(name, this.getParameters().get(name));
			}
		}
		this.setValue(pageUrl.getURL());
	}
	
	protected void extractPageOwner(IPage page, RequestContext reqCtx) {
		String value = page.getGroup();
		this.setValue(value);
	}	
	
	protected void evalValue() throws JspException {
		if (this.getVar() != null) {
			this.pageContext.setAttribute(this.getVar(), this.getValue());
		} else {
			try {
				if (this.getEscapeXml()) {
					out(this.pageContext, this.getEscapeXml(), this.getValue());
				} else {
					this.pageContext.getOut().print(this.getValue());
				}
			} catch (IOException e) {
				_logger.error("Error closing tag", e);
				//ApsSystemUtils.logThrowable(e, this, "doEndTag");
				throw new JspException("Error closing tag ", e);
			}
		}
	}
	
	@Override
	public void release() {
		this._pageCode = null;
		this._info = null;
		this._langCode = null;
		this._var = null;
		this._value = null;
		this._parameters = null;
		super.escapeXml = true;
	}
	
	public String getPageCode() {
		return _pageCode;
	}
	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}
	
	public String getInfo() {
		return _info;
	}
	public void setInfo(String info) {
		this._info = info;
	}
	
	public String getTargetPage() {
		return _targetPage;
	}
	public void setTargetPage(String targetPage) {
		this._targetPage = targetPage;
	}
	
	public String getLangCode() {
		return _langCode;
	}
	public void setLangCode(String langCode) {
		this._langCode = langCode;
	}
	
	public void setVar(String var) {
		this._var = var;
	}
	protected String getVar() {
		return _var;
	}
	
	public String getValue() {
		return _value;
	}
	public void setValue(String value) {
		this._value = value;
	}
	
	/**
	 * Returns True if the system escape the special characters. 
	 * @return True if the system escape the special characters.
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	/**
	 * Set if the system has to escape the special characters. 
	 * @param escapeXml True if the system has to escape the special characters, else false.
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
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
	
	private String _pageCode;
	private String _info;
	private String _targetPage;

	private String _langCode;
	
	private String _var;
	private String _value;
	
	private Map<String, String> _parameters;
	
	public static final String CODE_INFO = "code";
	public static final String URL_INFO = "url";
	public static final String TITLE_INFO = "title";
	public static final String OWNER_INFO = "owner";
	public static final String CHILD_OF_INFO = "childOf";
	public static final String HAS_CHILD = "hasChild";

}