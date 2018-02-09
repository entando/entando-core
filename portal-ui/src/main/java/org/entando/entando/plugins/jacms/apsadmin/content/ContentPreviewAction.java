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
package org.entando.entando.plugins.jacms.apsadmin.content;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.AbstractContentAction;

/**
 * Classe action delegate alla gestione della funzione di preview contenuti.
 * @author E.Santoboni
 */
public class ContentPreviewAction extends AbstractContentAction implements ServletResponseAware {

	private static final Logger _logger = LoggerFactory.getLogger(ContentPreviewAction.class);
	
	public String preview() {
		Content content = this.getContent();
		this.getContentActionHelper().updateEntity(content, this.getRequest());
		try {
			String previewLangCode = this.extractPreviewLangCode();
			this.setPreviewLangCode(previewLangCode);
			String previewPageCode = this.getRequest().getParameter(PAGE_CODE_PARAM_PREFIX + "_" + previewLangCode);
			if (null == previewPageCode) {
				previewPageCode = this.getRequest().getParameter(PAGE_CODE_PARAM_PREFIX);
			}
			this.setPreviewPageCode(previewPageCode);
		} catch (Throwable t) {
			_logger.error("error in preview", t);
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private String extractPreviewLangCode() {
		String previewLangCode = null;
		Enumeration<String> attributeEnum = this.getRequest().getAttributeNames();
		if (null != attributeEnum) {
			while (attributeEnum.hasMoreElements()) {
				String attributeName = attributeEnum.nextElement();
				if (attributeName.startsWith(LANG_CODE_PARAM_PREFIX + "_")) {
					previewLangCode = (String) this.getRequest().getAttribute(attributeName);
					break;
				}
			}
		}
		if (null == previewLangCode || previewLangCode.trim().length() == 0) {
			previewLangCode = this.getLangManager().getDefaultLang().getCode();
		}
		return previewLangCode;
	}
	
	public String executePreview() {
		try {
			String pageDestCode = this.getCheckPageDestinationCode();
			if (null == pageDestCode) return INPUT;
			this.prepareForward(pageDestCode);
			this.getRequest().setCharacterEncoding("UTF-8");
		} catch (Throwable t) {
			_logger.error("error in executePreview", t);
			throw new RuntimeException("error in executePreview", t);
		}
		return null;
	}
	
	protected String getCheckPageDestinationCode() {
		IPageManager pageManager = this.getPageManager();
		String pageDestCode = this.getPreviewPageCode();
		if (null == pageDestCode || pageDestCode.trim().length() == 0) {
			pageDestCode = this.getContent().getViewPage();
			if (null == pageDestCode || null == pageManager.getOnlinePage(pageDestCode)) {
				String[] args = {pageDestCode};
				this.addFieldError("previewPageCode", this.getText("error.content.preview.pageNotValid", args));
				return null;
			}
		}
		if (null == pageManager.getOnlinePage(pageDestCode)) {
			String[] args = {pageDestCode};
			this.addFieldError("previewPageCode", this.getText("error.content.preview.pageNotFound", args));
			return null;
		}
		return pageDestCode;
	}
	
	private void prepareForward(String pageDestCode) throws IOException {
		Lang currentLang = this.getLangManager().getLang(this.getPreviewLangCode());
		if (null == currentLang) {
			currentLang = this.getLangManager().getDefaultLang();
		}
		IPageManager pageManager = this.getPageManager();
		IPage pageDest = pageManager.getOnlinePage(pageDestCode);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("contentOnSessionMarker", this.getContentOnSessionMarker());
		String redirectUrl = this.getUrlManager().createURL(pageDest, currentLang, parameters, false);
		this.getServletResponse().sendRedirect(redirectUrl);
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}
	public HttpServletResponse getServletResponse() {
		return _response;
	}
	
	public String getPreviewPageCode() {
		return _previewPageCode;
	}
	public void setPreviewPageCode(String previewPageCode) {
		this._previewPageCode = previewPageCode;
	}
	
	public String getPreviewLangCode() {
		return _previewLangCode;
	}
	public void setPreviewLangCode(String previewLangCode) {
		this._previewLangCode = previewLangCode;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	public IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	private HttpServletResponse _response;
	
	private String _previewPageCode;
	private String _previewLangCode;
	
	private IPageManager _pageManager;
	private IURLManager _urlManager;
	
	public static final String PAGE_CODE_PARAM_PREFIX = "jacmsPreviewActionPageCode";
	
	public static final String LANG_CODE_PARAM_PREFIX = "jacmsPreviewActionLangCode";
	
}