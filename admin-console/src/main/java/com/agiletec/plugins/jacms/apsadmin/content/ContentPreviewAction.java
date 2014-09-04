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
package com.agiletec.plugins.jacms.apsadmin.content;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

import java.io.IOException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Classe action delegate alla gestione della funzione di preview contenuti.
 * @author E.Santoboni
 */
public class ContentPreviewAction extends AbstractContentAction implements IContentPreviewAction, ServletResponseAware {

	private static final Logger _logger = LoggerFactory.getLogger(ContentFinderAction.class);
	
	@Override
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
	
	@Override
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
			if (null == pageDestCode || null == pageManager.getPage(pageDestCode)) {
				String[] args = {pageDestCode};
				this.addFieldError("previewPageCode", this.getText("error.content.preview.pageNotValid", args));
				return null;
			}
		}
		if (null == pageManager.getPage(pageDestCode)) {
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
		IPage pageDest = pageManager.getPage(pageDestCode);
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("contentOnSessionMarker", this.getContentOnSessionMarker());
		String redirectUrl = this.getUrlManager().createUrl(pageDest, currentLang, parameters, false);
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
	
}