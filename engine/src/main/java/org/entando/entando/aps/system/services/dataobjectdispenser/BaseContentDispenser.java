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
package org.entando.entando.aps.system.services.dataobjectdispenser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.dataobject.IContentManager;
import org.entando.entando.aps.system.services.dataobject.helper.IDataAuthorizationHelper;
import org.entando.entando.aps.system.services.dataobject.helper.PublicDataTypeAuthorizationInfo;
import org.entando.entando.aps.system.services.dataobject.model.DataObject;
import org.entando.entando.aps.system.services.dataobjectrenderer.IDataObjectRenderer;

/**
 * Fornisce i DataObject formattati. Il compito del servizio, in fase di
 * richiesta di un DataObject formattato, è quello di controllare
 * preliminarmente le autorizzazzioni dell'utente corrente all'accesso al
 * DataObject; successivamente (in caso di autorizzazioni valide) restituisce il
 * DataObject formattato.
 *
 * @author M.Diana - E.Santoboni
 */
public class BaseContentDispenser extends AbstractService implements IContentDispenser {

	private static final Logger _logger = LoggerFactory.getLogger(BaseContentDispenser.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	//@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
	//		key = "T(com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser).getRenderizationInfoCacheKey(#contentId, #modelId, #langCode, #reqCtx)")
	//@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser).getRenderizationInfoCacheGroupsCsv(#contentId, #modelId)")
	public ContentRenderizationInfo getRenderizationInfo(String dataObjectId, long modelId, String langCode, RequestContext reqCtx) {
		PublicDataTypeAuthorizationInfo authInfo = this.getDataAuthorizationHelper().getAuthorizationInfo(dataObjectId, true);
		if (null == authInfo) {
			return null;
		}
		return this.getRenderizationInfo(authInfo, dataObjectId, modelId, langCode, reqCtx);
	}

	@Override
	//@Cacheable(value = ICacheInfoManager.DEFAULT_CACHE_NAME, condition = "#cacheable",
	//		key = "T(com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser).getRenderizationInfoCacheKey(#contentId, #modelId, #langCode, #reqCtx)")
	//@CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser).getRenderizationInfoCacheGroupsCsv(#contentId, #modelId)")
	public ContentRenderizationInfo getRenderizationInfo(String dataObjectId,
			long modelId, String langCode, RequestContext reqCtx, boolean cacheable) {
		PublicDataTypeAuthorizationInfo authInfo = this.getDataAuthorizationHelper().getAuthorizationInfo(dataObjectId, cacheable);
		if (null == authInfo) {
			return null;
		}
		return this.getRenderizationInfo(authInfo, dataObjectId, modelId, langCode, reqCtx, cacheable);
	}

	protected ContentRenderizationInfo getRenderizationInfo(PublicDataTypeAuthorizationInfo authInfo,
			String dataObjectId, long modelId, String langCode, RequestContext reqCtx) {
		return this.getRenderizationInfo(authInfo, dataObjectId, modelId, langCode, reqCtx, true);
	}

	protected ContentRenderizationInfo getRenderizationInfo(PublicDataTypeAuthorizationInfo authInfo,
			String dataObjectId, long modelId, String langCode, RequestContext reqCtx, boolean cacheable) {
		ContentRenderizationInfo renderInfo = null;
		try {
			UserDetails currentUser = (null != reqCtx) ? (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) : null;
			List<Group> userGroups = (null != currentUser) ? this.getAuthorizationManager().getUserGroups(currentUser) : new ArrayList<Group>();
			if (authInfo.isUserAllowed(userGroups)) {
				renderInfo = this.getBaseRenderizationInfo(authInfo, dataObjectId, modelId, langCode, currentUser, reqCtx, cacheable);
				if (null == renderInfo) {
					return null;
				}
			} else {
				String renderedDataObject = "Current user '" + currentUser.getUsername() + "' can't view this DataObject";
				DataObject contentToRender = this.getDataObjectManager().loadContent(dataObjectId, true, cacheable);
				renderInfo = new ContentRenderizationInfo(contentToRender, renderedDataObject, modelId, langCode, null);
				renderInfo.setRenderedDataobject(renderedDataObject);
				return renderInfo;
			}
		} catch (Throwable t) {
			_logger.error("Error while rendering dataObject {}", dataObjectId, t);
			return null;
		}
		return renderInfo;
	}

	public ContentRenderizationInfo getBaseRenderizationInfo(PublicDataTypeAuthorizationInfo authInfo,
			String dataObjectId, long modelId, String langCode, UserDetails currentUser, RequestContext reqCtx) {
		return this.getBaseRenderizationInfo(authInfo, dataObjectId, modelId, langCode, currentUser, reqCtx, true);
	}

	public ContentRenderizationInfo getBaseRenderizationInfo(PublicDataTypeAuthorizationInfo authInfo,
			String dataObjectId, long modelId, String langCode, UserDetails currentUser, RequestContext reqCtx, boolean cacheable) {
		ContentRenderizationInfo renderInfo = null;
		try {
			List<Group> userGroups = (null != currentUser) ? this.getAuthorizationManager().getUserGroups(currentUser) : new ArrayList<Group>();
			if (authInfo.isUserAllowed(userGroups)) {
				DataObject contentToRender = this.getDataObjectManager().loadContent(dataObjectId, true, cacheable);
				String renderedContent = this.buildRenderedDataObject(contentToRender, modelId, langCode, reqCtx);
				if (null != renderedContent && renderedContent.trim().length() > 0) {
					List<AttributeRole> roles = this.getDataObjectManager().getAttributeRoles();
					renderInfo = new ContentRenderizationInfo(contentToRender, renderedContent, modelId, langCode, roles);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while rendering DataObject {}", dataObjectId, t);
			return null;
		}
		return renderInfo;
	}

	protected synchronized String buildRenderedDataObject(DataObject dataObject, long modelId, String langCode, RequestContext reqCtx) {
		if (null == dataObject) {
			_logger.warn("Null The dataObject can't be rendered");
			return null;
		}
		String renderedDataObject = null;
		boolean ok = false;
		try {
			renderedDataObject = this.getDataObjectRenderer().render(dataObject, modelId, langCode, reqCtx);
			ok = true;
		} catch (Throwable t) {
			_logger.error("error in buildRenderedDataObject", t);
		}
		if (!ok) {
			_logger.warn("The DataObject {} can't be rendered", dataObject.getId());
		}
		return renderedDataObject;
	}

	/*
	public static String getRenderizationInfoCacheKey(String dataObjectId, long modelId, String langCode, RequestContext reqCtx) {
		UserDetails currentUser = (null != reqCtx) ? (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) : null;
		StringBuffer key = new StringBuffer();
		key.append(dataObjectId).append("_").append(modelId).append("_").append(langCode).append("_RENDER_INFO_CacheKey");
		if (null != currentUser && !currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
			List<String> codes = new ArrayList<String>();
			if (null != currentUser.getAuthorizations()) {
				for (int i = 0; i < currentUser.getAuthorizations().size(); i++) {
					Authorization auth = currentUser.getAuthorizations().get(i);
					if (null != auth && null != auth.getGroup()) {
						String code = auth.getGroup().getAuthority() + "_";
						if (null != auth.getRole()) {
							code += auth.getRole().getAuthority();
						} else {
							code += "null";
						}
						codes.add(code);
					}
				}
			}
			if (!codes.isEmpty()) {
				key.append("_AUTHS:");
				appendAuthCodes(codes, key);
			}
		}
		return key.toString();
	}
	 */
	private static void appendAuthCodes(List<String> codes, StringBuffer key) {
		Collections.sort(codes);
		for (int i = 0; i < codes.size(); i++) {
			if (i > 0) {
				key.append("-");
			}
			key.append(codes.get(i));
		}
	}

	/*
	public static String getRenderizationInfoCacheGroupsCsv(String contentId, long modelId) {
		StringBuilder builder = new StringBuilder();
		String typeCode = contentId.substring(0, 3);
		String contentCacheGroupId = JacmsSystemConstants.CONTENT_CACHE_GROUP_PREFIX + contentId;
		String modelCacheGroupId = JacmsSystemConstants.CONTENT_MODEL_CACHE_GROUP_PREFIX + modelId;
		String typeCacheGroupId = JacmsSystemConstants.CONTENT_TYPE_CACHE_GROUP_PREFIX + typeCode;
		builder.append(contentCacheGroupId).append(",").append(modelCacheGroupId).append(",").append(typeCacheGroupId);
		return builder.toString();
	}
	 */
	public IDataAuthorizationHelper getDataAuthorizationHelper() {
		return _dataAuthorizationHelper;
	}

	public void setDataAuthorizationHelper(IDataAuthorizationHelper dataAuthorizationHelper) {
		this._dataAuthorizationHelper = dataAuthorizationHelper;
	}

	public IContentManager getDataObjectManager() {
		return _dataObjectManager;
	}

	public void setDataObjectManager(IContentManager dataObjectManager) {
		this._dataObjectManager = dataObjectManager;
	}

	protected IDataObjectRenderer getDataObjectRenderer() {
		return _dataObjectRenderer;
	}

	public void setDataObjectRenderer(IDataObjectRenderer renderer) {
		this._dataObjectRenderer = renderer;
	}

	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}

	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}

	private IDataAuthorizationHelper _dataAuthorizationHelper;

	private IDataObjectRenderer _dataObjectRenderer;
	private IContentManager _dataObjectManager;
	private IAuthorizationManager _authorizationManager;

}
