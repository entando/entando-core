/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.linkresolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.contentpagemapper.IContentPageMapperManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

/**
 * Servizio gestore della risoluzione dei link sinbolici.
 * Scopo di questa classe è l'individuazione in un testo di stringhe rappresentanti
 * link simbolici, e la loro traduzione e sostituzione nel testo con i 
 * corrispondenti URL.
 * @author M.Diana - E.Santoboni
 */
public class LinkResolverManager extends AbstractService implements ILinkResolverManager {

	private static final Logger _logger = LoggerFactory.getLogger(LinkResolverManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready.", this.getClass().getName());
	}
	
	@Override
	public String resolveLinks(String text, RequestContext reqCtx) {
		return this.resolveLinks(text, null, reqCtx);
	}
	
	/**
	 * Sotituisce nel testo i link simbolici con URL reali.
	 * @param text Il testo che può contenere link simbolici.
	 * @param contentId The id of content that contains the link to resolve
	 * @param reqCtx Il contesto di richiesta
	 * @return Il testo in cui i link simbolici sono sostituiti con URL reali.
	 */
	@Override
	public String resolveLinks(String text, String contentId, RequestContext reqCtx) {
		StringBuilder resolvedText = new StringBuilder();
		int postfixLen = SymbolicLink.SYMBOLIC_DEST_POSTFIX.length();
		int end = 0;
		int parsed = 0;
		int start = text.indexOf(SymbolicLink.SYMBOLIC_DEST_PREFIX);
		while(start >= 0){
			end = text.indexOf(SymbolicLink.SYMBOLIC_DEST_POSTFIX, start);
			if(end >= 0) {
				end = end + postfixLen;
				String symbolicString = text.substring(start, end);
				String url = this.convertToURL(symbolicString, contentId, reqCtx);
				if(url != null){
					String invariantText = text.substring(parsed, start);
					resolvedText.append(invariantText);
					resolvedText.append(url);
					parsed = end;
				} else {
					end = start + 1;
				}
				start = text.indexOf(SymbolicLink.SYMBOLIC_DEST_PREFIX, end);
			} else {
				start = -1; //uscita
			}
		}
		String residualText = text.substring(parsed);
		resolvedText.append(residualText);
		return resolvedText.toString();
	}
	
	protected String convertToURL(String symbolicString, String contentId, RequestContext reqCtx) {
		String url = null;
		SymbolicLink symbolicLink = new SymbolicLink();
		if (symbolicLink.setSymbolicDestination(symbolicString)) {
			url = this.resolveLink(symbolicLink, contentId, reqCtx);
		}
		return url;
	}
	
	@Override
	public String resolveLink(SymbolicLink symbolicLink, RequestContext reqCtx) {
		return this.resolveLink(symbolicLink, null, reqCtx);
	}
	
	@Override
	public String resolveLink(SymbolicLink symbolicLink, String contentId, RequestContext reqCtx) {
		if (null == symbolicLink) {
			_logger.error("Null Symbolic Link");
			return "";
		}
		String url = null;
		try {
			if (symbolicLink.getDestType() == SymbolicLink.URL_TYPE) {
				url = symbolicLink.getUrlDest();
			} else if (symbolicLink.getDestType() == SymbolicLink.PAGE_TYPE) {
				PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
				pageUrl.setPageCode(symbolicLink.getPageDest());
				url = pageUrl.getURL();
			} else if (symbolicLink.getDestType() == SymbolicLink.CONTENT_ON_PAGE_TYPE) {
				PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
				pageUrl.setPageCode(symbolicLink.getPageDest());
				pageUrl.addParam(SystemConstants.K_CONTENT_ID_PARAM, symbolicLink.getContentDest());
				url = pageUrl.getURL();
			} else if (symbolicLink.getDestType() == SymbolicLink.CONTENT_TYPE) {
				PageURL pageUrl = this.getUrlManager().createURL(reqCtx);
				String contentIdDest = symbolicLink.getContentDest();
				String pageCode = this.getContentPageMapperManager().getPageCode(contentIdDest);
				boolean forwardToDefaultPage = !this.isPageAllowed(reqCtx, pageCode);
				if (forwardToDefaultPage) {
					String viewPageCode = this.getContentManager().getViewPage(contentIdDest);
					pageUrl.setPageCode(viewPageCode);
					pageUrl.addParam(SystemConstants.K_CONTENT_ID_PARAM, contentIdDest);
				} else {
					pageUrl.setPageCode(pageCode);
				}
				url = pageUrl.getURL();
			} else if (symbolicLink.getDestType() == SymbolicLink.RESOURCE_TYPE) {
				ResourceInterface resource = this.getResourceManager().loadResource(symbolicLink.getResourceDest());
				if (null != resource) {
					url = resource.getDefaultUrlPath();
					if (!resource.getMainGroup().equals(Group.FREE_GROUP_NAME)) {
						if (!url.endsWith("/")) {
							url += "/";
						}
						url += AbstractResourceAttribute.REFERENCED_RESOURCE_INDICATOR + "/" + contentId + "/";
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error resolve link from SymbolicLink", t);
			throw new RuntimeException("Error resolve link from SymbolicLink", t);
		}
		return url;
	}
	
	/**
	 * Verifica se l'utente corrente è autorizzato 
	 * all'accesso alla pagina specificata.
	 * @param reqCtx Il contesto della richiesta.
	 * @param pageCode Il codice della pagina.
	 * @return true se l'utente corrente è abilitato all'accesso 
	 * alla pagina specificata, false in caso contrario.
	 * @deprecated Old Wrong method name. Use isPageAllowed
	 */
	protected boolean isCurrentUserAllowed(RequestContext reqCtx, String pageCode) {
		return this.isPageAllowed(reqCtx, pageCode);
	}
	
	protected boolean isPageAllowed(RequestContext reqCtx, String pageCode) {
		UserDetails user = null;
		if (null != reqCtx) {
			user = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		}
		return this.isPageAllowed(user, pageCode);
	}
	
	protected boolean isPageAllowed(UserDetails user, String pageCode) {
		if (null == user) {
			user = this.getUserManager().getGuestUser();
		}
		boolean isPageAllowed = false;
		if (pageCode != null) {
			IPage page = this.getPageManager().getPage(pageCode);
			if (page != null && this.getAuthorizationManager().isAuth(user, page)) {
				isPageAllowed = true;
			}
		}
		return isPageAllowed;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	
	protected IContentPageMapperManager getContentPageMapperManager() {
		return _contentPageMapperManager;
	}
	public void setContentPageMapperManager(IContentPageMapperManager contentPageMapperManager) {
		this._contentPageMapperManager = contentPageMapperManager;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	private IPageManager _pageManager;
	private IContentManager _contentManager;
	private IResourceManager _resourceManager;
	private IContentPageMapperManager _contentPageMapperManager;
	private IURLManager _urlManager;
	
	private IUserManager _userManager;
	private IAuthorizationManager _authorizationManager;
	
}
