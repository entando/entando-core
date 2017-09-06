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
package org.entando.entando.aps.system.services.dataobject.helper;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import org.entando.entando.aps.system.services.dataobject.IContentManager;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;

/**
 * Return informations of dataobject authorization
 *
 * @author E.Santoboni
 */
public class DataAuthorizationHelper implements IDataAuthorizationHelper {

	private static final Logger _logger = LoggerFactory.getLogger(DataAuthorizationHelper.class);

	@Override
	public boolean isAuth(UserDetails user, DataObject dataObject) throws ApsSystemException {
		if (null == dataObject) {
			_logger.error("Null content");
			return false;
		} else if (DataObject.STATUS_NEW.equals(dataObject.getStatus()) && null == dataObject.getMainGroup()) {
			return true;
		}
		return this.getAuthorizationManager().isAuth(user, dataObject);
	}

	@Override
	public boolean isAuth(UserDetails user, PublicDataTypeAuthorizationInfo info) throws ApsSystemException {
		List<Group> userGroups = this.getAuthorizationManager().getUserGroups(user);
		return info.isUserAllowed(userGroups);
	}

	@Override
	public boolean isAuth(UserDetails user, String dataObjectId, boolean publicVersion) throws ApsSystemException {
		if (publicVersion) {
			PublicDataTypeAuthorizationInfo authorizationInfo = this.getAuthorizationInfo(dataObjectId);
			return this.isAuth(user, authorizationInfo);
		}
		DataObject content = this.getDataObjectManager().loadContent(dataObjectId, publicVersion);
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
	public boolean isAuthToEdit(UserDetails user, DataObject dataObject) throws ApsSystemException {
		if (null == dataObject) {
			_logger.error("Null content");
			return false;
		} else if (DataObject.STATUS_NEW.equals(dataObject.getStatus()) && null == dataObject.getMainGroup()) {
			return true;
		}
		String mainGroupName = dataObject.getMainGroup();
		return this.isAuthToEdit(user, mainGroupName);
	}

	@Override
	public boolean isAuthToEdit(UserDetails user, PublicDataTypeAuthorizationInfo info) throws ApsSystemException {
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
	public boolean isAuthToEdit(UserDetails user, String dataObjectId, boolean publicVersion) throws ApsSystemException {
		DataObject content = this.getDataObjectManager().loadContent(dataObjectId, publicVersion);
		return this.isAuth(user, content);
	}

	@Override
	//@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_AUTH_INFO_CACHE_PREFIX.concat(#contentId)")
	//@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsCsv(#contentId)")
	public PublicDataTypeAuthorizationInfo getAuthorizationInfo(String dataObjectId) {
		PublicDataTypeAuthorizationInfo authInfo = null;
		try {
			DataObject dataObject = this.getDataObjectManager().loadContent(dataObjectId, true);
			if (null == dataObject) {
				_logger.debug("public content {} doesn't exist", dataObjectId);
				return null;
			}
			authInfo = new PublicDataTypeAuthorizationInfo(dataObject, this.getLangManager().getLangs());
		} catch (Throwable t) {
			_logger.error("error in getAuthorizationInfo for dataObject {}", dataObjectId, t);
		}
		return authInfo;
	}

	@Override
	//@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, condition = "#cacheable",
	//		key = "T(com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants).CONTENT_AUTH_INFO_CACHE_PREFIX.concat(#contentId)")
	//@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.cache.CmsCacheWrapperManager).getContentCacheGroupsCsv(#contentId)")
	public PublicDataTypeAuthorizationInfo getAuthorizationInfo(String dataObjectId, boolean cacheable) {
		PublicDataTypeAuthorizationInfo authInfo = null;
		try {
			DataObject dataObject = this.getDataObjectManager().loadContent(dataObjectId, true, cacheable);
			if (null == dataObject) {
				_logger.debug("public dataObject {} doesn't exist", dataObjectId);
				return null;
			}
			authInfo = new PublicDataTypeAuthorizationInfo(dataObject, this.getLangManager().getLangs());
		} catch (Throwable t) {
			_logger.error("error in getAuthorizationInfo for dataObject {}", dataObjectId, t);
		}
		return authInfo;
	}

	public IContentManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IContentManager dataObjectManager) {
		this._dataObjectManager = dataObjectManager;
	}

	protected ILangManager getLangManager() {
		return _langManager;
	}

	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}

	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}

	private IContentManager _dataObjectManager;
	private ILangManager _langManager;
	private IAuthorizationManager _authorizationManager;

}
