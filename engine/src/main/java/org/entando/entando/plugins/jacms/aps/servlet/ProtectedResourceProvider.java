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
package org.entando.entando.plugins.jacms.aps.servlet;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMonoInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractMultiInstanceResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInstance;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.entando.entando.aps.servlet.IProtectedResourceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider bean of protected cms resources.
 * @author E.Santoboni
 */
public class ProtectedResourceProvider implements IProtectedResourceProvider {
	
	private static final Logger _logger = LoggerFactory.getLogger(ProtectedResourceProvider.class);
	
	@Override
	public boolean provideProtectedResource(HttpServletRequest request, HttpServletResponse response) throws ApsSystemException {
		try {
			String[] uriSegments = request.getRequestURI().split("/");
			int segments = uriSegments.length;
			//CONTROLLO ASSOCIAZIONE RISORSA A CONTENUTO
			int indexGuardian = 0;
			String checkContentAssociation = uriSegments[segments-2];
			if (checkContentAssociation.equals(AbstractResourceAttribute.REFERENCED_RESOURCE_INDICATOR)) {
				// LA Sintassi /<RES_ID>/<SIZE>/<LANG_CODE>/<REFERENCED_RESOURCE_INDICATOR>/<CONTENT_ID>
				indexGuardian = 2;
			}
			String resId = uriSegments[segments-3-indexGuardian];
			UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (currentUser == null) {
				currentUser = this.getUserManager().getGuestUser();
			}
			boolean isAuthForProtectedRes = false;
			if (indexGuardian != 0) {
				if (this.isAuthOnProtectedRes(currentUser, resId, uriSegments[segments-1])) {
					isAuthForProtectedRes = true;
				} else {
					this.executeLoginRedirect(request, response);
					return true;
				}
			}
			ResourceInterface resource = this.getResourceManager().loadResource(resId);
			if (resource == null) {
				return false;
			}
			IAuthorizationManager authManager = this.getAuthorizationManager();
			if (isAuthForProtectedRes 
					|| authManager.isAuthOnGroup(currentUser, resource.getMainGroup()) 
					|| authManager.isAuthOnGroup(currentUser, Group.ADMINS_GROUP_NAME)) {
				ResourceInstance instance = null;
				if (resource.isMultiInstance()) {
					String sizeStr = uriSegments[segments-2-indexGuardian];
					if (!this.isValidNumericString(sizeStr)) {
						return false;
					}
					int size = Integer.parseInt(sizeStr);
					String langCode = uriSegments[segments-1-indexGuardian];
					instance = ((AbstractMultiInstanceResource) resource).getInstance(size, langCode);
				} else {
					instance = ((AbstractMonoInstanceResource) resource).getInstance();
				}
				this.createResponse(response, resource, instance);
				return true;
			}
		} catch (Throwable t) {
			_logger.error("Error extracting protected resource", t);
			throw new ApsSystemException("Error extracting protected resource", t);
		}
		return false;
	}
	
	private boolean isAuthOnProtectedRes(UserDetails currentUser, String resourceId, String contentId) {
		PublicContentAuthorizationInfo authInfo = this.getContentAuthorizationHelper().getAuthorizationInfo(contentId);
		IAuthorizationManager authManager = this.getAuthorizationManager();
		return (authInfo.isProtectedResourceReference(resourceId) && authInfo.isUserAllowed(authManager.getUserGroups(currentUser)));
	}
	
	private void createResponse(HttpServletResponse resp, ResourceInterface resource, 
			ResourceInstance instance) throws IOException, ServletException {
		resp.setContentType(instance.getMimeType());
		resp.setHeader("Content-Disposition","inline; filename=" + instance.getFileName());
		ServletOutputStream out = resp.getOutputStream();
		try {
			InputStream is = resource.getResourceStream(instance);
			if (null != is) {
				byte[] buffer = new byte[2048];
				int length = -1;
			    // Transfer the data
			    while ((length = is.read(buffer)) != -1) {
			    	out.write(buffer, 0, length);
			    	out.flush();
			    }
				is.close();
			}
		} catch (Throwable t) {
			_logger.error("Error extracting protected resource", t);
			throw new ServletException("Error extracting protected resource", t);
		} finally {
			out.close();
		}
	}
	
	private void executeLoginRedirect(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			StringBuilder targetUrl = new StringBuilder(request.getRequestURL());
			Map<String, String> params = new HashMap<String, String>();
			params.put("returnUrl", URLEncoder.encode(targetUrl.toString(), "UTF-8"));
			String loginPageCode = this.getConfigManager().getParam(SystemConstants.CONFIG_PARAM_LOGIN_PAGE_CODE);
			IPage page = this.getPageManager().getPage(loginPageCode);
			Lang defaultLang = this.getLangManager().getDefaultLang();
			String url = this.getUrlManager().createUrl(page, defaultLang, params);
			response.sendRedirect(url);
		} catch (Throwable t) {
			_logger.error("Error executing redirect login page", t);
			throw new ServletException("Error executing redirect login page", t);
		}
	}
	
	private boolean isValidNumericString(String integerNumber) {
		return (integerNumber.trim().length() > 0 && integerNumber.matches("\\d+"));
	}
	
	protected IResourceManager getResourceManager() {
		return _resourceManager;
	}
	public void setResourceManager(IResourceManager resourceManager) {
		this._resourceManager = resourceManager;
	}
	
	protected IContentAuthorizationHelper getContentAuthorizationHelper() {
		return _contentAuthorizationHelper;
	}
	public void setContentAuthorizationHelper(IContentAuthorizationHelper contentAuthorizationHelper) {
		this._contentAuthorizationHelper = contentAuthorizationHelper;
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
	
	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	private IResourceManager _resourceManager;
	private IContentAuthorizationHelper _contentAuthorizationHelper;
	
	private IUserManager _userManager;
	private IAuthorizationManager _authorizationManager;
	private IURLManager _urlManager;
	private IPageManager _pageManager;
	private ILangManager _langManager;
	private ConfigInterface _configManager;
	
}
