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

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.util.ApsWebApplicationUtils;

/**
 * Returns the requested information held by the current page bean.
 * It is possible to ask for the title in the current language (param value: "title") or the code 
 * (param: "code"). The title in the current language is returned if "param" is not specified
 * @author E.Santoboni
 */
public class CurrentPageTag extends PageInfoTag {

	private static final Logger _logger = LoggerFactory.getLogger(CurrentPageTag.class);
	
	@Override
	public int doEndTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		RequestContext reqCtx = (RequestContext) request.getAttribute(RequestContext.REQCTX);
		Lang currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
		try {
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			if (this.getParam() == null || this.getParam().equals(TITLE_INFO)) {
				this.extractPageTitle(page, currentLang, reqCtx);
			} else if (this.getParam().equals(CODE_INFO)) {
				this.setValue(page.getCode());
			} else if (this.getParam().equals(OWNER_INFO)) {
				this.extractPageOwner(page, reqCtx);
			} else if (this.getInfo().equals(CHILD_OF_INFO)) {
				this.extractIsChildOfTarget(page);
			} else if (this.getInfo().equals(HAS_CHILD)) {
				boolean hasChild = (page.getChildren() != null && page.getChildren().length > 0);
				this.setValue(new Boolean(hasChild).toString());
			}
			this.evalValue();
		} catch (Throwable t) {
			_logger.error("error in doStartTag", t);
			//ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization ", t);
		}
		return EVAL_PAGE;
	}
	
	protected void extractPageTitle(IPage page, Lang currentLang, RequestContext reqCtx) {
		if (!page.isUseExtraTitles()) {
			super.extractPageTitle(page, currentLang);
			return;
		}
		Object extraTitleObject = reqCtx.getExtraParam(SystemConstants.EXTRAPAR_EXTRA_PAGE_TITLES);
		if (null == extraTitleObject) {
			super.extractPageTitle(page, currentLang);
			return;
		}
		if (extraTitleObject instanceof String) {
			String extraTitleString = (String) extraTitleObject;
			if (extraTitleString.trim().length() > 0) {
				this.setValue(extraTitleString);
			} else {
				super.extractPageTitle(page, currentLang);
			}
		} else if (extraTitleObject instanceof Map<?, ?>) {
			this.extractPageTitleFromExtraTitles(page, currentLang, (Map) extraTitleObject);
			if (null == this.getValue()) {
				super.extractPageTitle(page, currentLang);
			}
		} else {
			super.extractPageTitle(page, currentLang);
		}
	}
	
	private void extractPageTitleFromExtraTitles(IPage page, Lang currentLang, Map extraTitlesMap) {
		Object value = null;
		if ((this.getLangCode() == null) || (this.getLangCode().equals(""))
				|| (currentLang.getCode().equalsIgnoreCase(this.getLangCode()))) {
			value = extraTitlesMap.get(currentLang.getCode());
		} else {
			value = extraTitlesMap.get(this.getLangCode());
		}
		if (value == null || value.toString().trim().equals("")) {
			ILangManager langManager = 
				(ILangManager) ApsWebApplicationUtils.getBean(SystemConstants.LANGUAGE_MANAGER, this.pageContext);
			value = extraTitlesMap.get(langManager.getDefaultLang().getCode());
		}
		if (null != value && value.toString().trim().length() > 0) {
			this.setValue(value.toString());
		}
	}

	protected String getParam() {
		return super.getInfo();
	}
	public void setParam(String param) {
		super.setInfo(param);
	}
	
}
