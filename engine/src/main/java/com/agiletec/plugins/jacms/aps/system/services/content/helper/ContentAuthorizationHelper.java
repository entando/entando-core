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
package com.agiletec.plugins.jacms.aps.system.services.content.helper;

import java.util.List;
import java.util.Set;

import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Return informations of content authorization
 * @author E.Santoboni
 */
public class ContentAuthorizationHelper implements IContentAuthorizationHelper {

	private static final Logger _logger = LoggerFactory.getLogger(ContentAuthorizationHelper.class);
	
	@Override
	public boolean isAuth(UserDetails user, Content content) throws ApsSystemException {
		if (null == content) {
			_logger.error("Null content");
			return false;
		}
		return this.getAuthorizationManager().isAuth(user, content); 
	}
	
	@Override
	public boolean isAuth(UserDetails user, PublicContentAuthorizationInfo info) throws ApsSystemException {
		List<Group> userGroups = this.getAuthorizationManager().getUserGroups(user);
		return info.isUserAllowed(userGroups);
	}
	
	@Override
	public boolean isAuth(UserDetails user, String contentId, boolean publicVersion) throws ApsSystemException {
		if (publicVersion) {
			PublicContentAuthorizationInfo authorizationInfo = this.getAuthorizationInfo(contentId);
			return this.isAuth(user, authorizationInfo);
		}
		Content content = this.getContentManager().loadContent(contentId, publicVersion);
		return this.isAuth(user, content);
	}
	
	protected boolean isAuth(UserDetails user, Set<String> groupCodes) throws ApsSystemException {
		if (null == user) {
			_logger.error("Null user");
			return false;
		}
		return this.getAuthorizationManager().isAuth(user, groupCodes);
	}
	
	@Override
	public boolean isAuthToEdit(UserDetails user, Content content) throws ApsSystemException {
		if (null == content) {
			_logger.error("Null content");
			return false;
		}
		String mainGroupName = content.getMainGroup();
		return this.isAuthToEdit(user, mainGroupName);
	}
	
	@Override
	public boolean isAuthToEdit(UserDetails user, PublicContentAuthorizationInfo info) throws ApsSystemException {
		String mainGroupName = info.getMainGroup();
		return this.isAuthToEdit(user, mainGroupName);
	}
	
	private boolean isAuthToEdit(UserDetails user, String mainGroupName) throws ApsSystemException {
		if (null == user) {
			_logger.error("Null user");
			return false;
		}
		return (this.getAuthorizationManager().isAuthOnPermission(user, JacmsSystemConstants.PERMISSION_EDIT_CONTENTS) 
				&& this.getAuthorizationManager().isAuthOnGroup(user, mainGroupName));
	}
	
	@Override
	public boolean isAuthToEdit(UserDetails user, String contentId, boolean publicVersion) throws ApsSystemException {
		Content content = this.getContentManager().loadContent(contentId, publicVersion);
		return this.isAuth(user, content);
	}
	
	@Override
	@Cacheable(value = ICacheInfoManager.CACHE_NAME, 
			key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_AUTH_INFO_CACHE_PREFIX.concat(#contentId)")
	@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsCsv(#contentId)")
	public PublicContentAuthorizationInfo getAuthorizationInfo(String contentId) {
		PublicContentAuthorizationInfo authInfo = null;
		try {
			Content content = this.getContentManager().loadContent(contentId, true);
			authInfo = new PublicContentAuthorizationInfo(content);
		} catch (Throwable t) {
			_logger.error("error in getAuthorizationInfo for content {}", contentId, t);
		}
		return authInfo;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	private IContentManager _contentManager;
	private IAuthorizationManager _authorizationManager;
	
}
