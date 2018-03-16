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
package com.agiletec.plugins.jacms.aps.system.services.dispenser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.entando.entando.aps.system.services.cache.CacheableInfo;
import org.entando.entando.aps.system.services.cache.ICacheInfoManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeRole;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.IContentAuthorizationHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.helper.PublicContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.ILinkResolverManager;
import com.agiletec.plugins.jacms.aps.system.services.renderer.IContentRenderer;
import org.springframework.cache.annotation.CachePut;

/**
 * Fornisce i contenuti formattati. Il compito del servizio, in fase di
 * richiesta di un contenuto formattato, è quello di controllare preliminarmente
 * le autorizzazzioni dell'utente corrente all'accesso al contenuto;
 * successivamente (in caso di autorizzazioni valide) restituisce il contenuto
 * formattato.
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
    @Deprecated
    public String getRenderedContent(String contentId, long modelId, String langCode, RequestContext reqCtx) {
        ContentRenderizationInfo renderInfo = this.getRenderizationInfo(contentId, modelId, langCode, reqCtx);
        if (null == renderInfo) {
            return "";
        }
        return renderInfo.getCachedRenderedContent();
    }

    @Override
    @CachePut(value = ICacheInfoManager.DEFAULT_CACHE_NAME,
            key = "T(com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser).getRenderizationInfoCacheKey(#contentId, #modelId, #langCode, #reqCtx)")
    @CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser).getRenderizationInfoCacheGroupsCsv(#contentId, #modelId)")
    public ContentRenderizationInfo getRenderizationInfo(String contentId, long modelId, String langCode, RequestContext reqCtx) {
        PublicContentAuthorizationInfo authInfo = this.getContentAuthorizationHelper().getAuthorizationInfo(contentId, true);
        if (null == authInfo) {
            return null;
        }
        return this.getRenderizationInfo(authInfo, contentId, modelId, langCode, reqCtx);
    }

    @Override
    @CachePut(value = ICacheInfoManager.DEFAULT_CACHE_NAME, condition = "#cacheable",
            key = "T(com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser).getRenderizationInfoCacheKey(#contentId, #modelId, #langCode, #reqCtx)")
    @CacheableInfo(groups = "T(com.agiletec.plugins.jacms.aps.system.services.dispenser.BaseContentDispenser).getRenderizationInfoCacheGroupsCsv(#contentId, #modelId)")
    public ContentRenderizationInfo getRenderizationInfo(String contentId,
            long modelId, String langCode, RequestContext reqCtx, boolean cacheable) {
        PublicContentAuthorizationInfo authInfo = this.getContentAuthorizationHelper().getAuthorizationInfo(contentId, cacheable);
        if (null == authInfo) {
            return null;
        }
        return this.getRenderizationInfo(authInfo, contentId, modelId, langCode, reqCtx, cacheable);
    }

    protected ContentRenderizationInfo getRenderizationInfo(PublicContentAuthorizationInfo authInfo,
            String contentId, long modelId, String langCode, RequestContext reqCtx) {
        return this.getRenderizationInfo(authInfo, contentId, modelId, langCode, reqCtx, true);
    }

    protected ContentRenderizationInfo getRenderizationInfo(PublicContentAuthorizationInfo authInfo,
            String contentId, long modelId, String langCode, RequestContext reqCtx, boolean cacheable) {
        ContentRenderizationInfo renderInfo = null;
        try {
            UserDetails currentUser = (null != reqCtx) ? (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) : null;
            List<Group> userGroups = (null != currentUser) ? this.getAuthorizationManager().getUserGroups(currentUser) : new ArrayList<Group>();
            if (authInfo.isUserAllowed(userGroups)) {
                renderInfo = this.getBaseRenderizationInfo(authInfo, contentId, modelId, langCode, currentUser, reqCtx, cacheable);
                if (null == renderInfo) {
                    return null;
                }
            } else {
                String renderedContent = "Current user '" + currentUser.getUsername() + "' can't view this content";
                Content contentToRender = this.getContentManager().loadContent(contentId, true, cacheable);
                renderInfo = new ContentRenderizationInfo(contentToRender, renderedContent, modelId, langCode, null);
                renderInfo.setRenderedContent(renderedContent);
                return renderInfo;
            }
        } catch (Throwable t) {
            _logger.error("Error while rendering content {}", contentId, t);
            return null;
        }
        return renderInfo;
    }

    @Override
    public void resolveLinks(ContentRenderizationInfo renderizationInfo, RequestContext reqCtx) {
        if (null == renderizationInfo) {
            return;
        }
        try {
            String finalRenderedContent = this.getLinkResolverManager().resolveLinks(renderizationInfo.getCachedRenderedContent(), renderizationInfo.getContentId(), reqCtx);
            renderizationInfo.setRenderedContent(finalRenderedContent);
        } catch (Throwable t) {
            _logger.error("Error while resolve links for content {}", renderizationInfo.getContentId(), t);
        }
    }

    public ContentRenderizationInfo getBaseRenderizationInfo(PublicContentAuthorizationInfo authInfo,
            String contentId, long modelId, String langCode, UserDetails currentUser, RequestContext reqCtx) {
        return this.getBaseRenderizationInfo(authInfo, contentId, modelId, langCode, currentUser, reqCtx, true);
    }

    public ContentRenderizationInfo getBaseRenderizationInfo(PublicContentAuthorizationInfo authInfo,
            String contentId, long modelId, String langCode, UserDetails currentUser, RequestContext reqCtx, boolean cacheable) {
        ContentRenderizationInfo renderInfo = null;
        try {
            List<Group> userGroups = (null != currentUser) ? this.getAuthorizationManager().getUserGroups(currentUser) : new ArrayList<Group>();
            if (authInfo.isUserAllowed(userGroups)) {
                Content contentToRender = this.getContentManager().loadContent(contentId, true, cacheable);
                String renderedContent = this.buildRenderedContent(contentToRender, modelId, langCode, reqCtx);
                if (null != renderedContent && renderedContent.trim().length() > 0) {
                    List<AttributeRole> roles = this.getContentManager().getAttributeRoles();
                    renderInfo = new ContentRenderizationInfo(contentToRender, renderedContent, modelId, langCode, roles);
                }
            }
        } catch (Throwable t) {
            _logger.error("Error while rendering content {}", contentId, t);
            return null;
        }
        return renderInfo;
    }

    protected synchronized String buildRenderedContent(Content content, long modelId, String langCode, RequestContext reqCtx) {
        if (null == content) {
            _logger.warn("Null The content can't be rendered");
            return null;
        }
        String renderedContent = null;
        boolean ok = false;
        try {
            renderedContent = this.getContentRender().render(content, modelId, langCode, reqCtx);
            ok = true;
        } catch (Throwable t) {
            _logger.error("error in buildRenderedContent", t);
        }
        if (!ok) {
            _logger.warn("The content {} can't be rendered", content.getId());
        }
        return renderedContent;
    }

    public static String getRenderizationInfoCacheKey(String contentId, long modelId, String langCode, RequestContext reqCtx) {
        UserDetails currentUser = (null != reqCtx) ? (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) : null;
        StringBuffer key = new StringBuffer();
        key.append(contentId).append("_").append(modelId).append("_").append(langCode).append("_RENDER_INFO_CacheKey");
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

    private static void appendAuthCodes(List<String> codes, StringBuffer key) {
        Collections.sort(codes);
        for (int i = 0; i < codes.size(); i++) {
            if (i > 0) {
                key.append("-");
            }
            key.append(codes.get(i));
        }
    }

    public static String getRenderizationInfoCacheGroupsCsv(String contentId, long modelId) {
        StringBuilder builder = new StringBuilder();
        String typeCode = contentId.substring(0, 3);
        String contentCacheGroupId = JacmsSystemConstants.CONTENT_CACHE_GROUP_PREFIX + contentId;
        String modelCacheGroupId = JacmsSystemConstants.CONTENT_MODEL_CACHE_GROUP_PREFIX + modelId;
        String typeCacheGroupId = JacmsSystemConstants.CONTENT_TYPE_CACHE_GROUP_PREFIX + typeCode;
        builder.append(contentCacheGroupId).append(",").append(modelCacheGroupId).append(",").append(typeCacheGroupId);
        return builder.toString();
    }

    protected IContentAuthorizationHelper getContentAuthorizationHelper() {
        return _contentAuthorizationHelper;
    }

    public void setContentAuthorizationHelper(IContentAuthorizationHelper contentAuthorizationHelper) {
        this._contentAuthorizationHelper = contentAuthorizationHelper;
    }

    protected IContentManager getContentManager() {
        return _contentManager;
    }

    public void setContentManager(IContentManager manager) {
        this._contentManager = manager;
    }

    protected IContentRenderer getContentRender() {
        return _contentRenderer;
    }

    public void setContentRenderer(IContentRenderer renderer) {
        this._contentRenderer = renderer;
    }

    protected ILinkResolverManager getLinkResolverManager() {
        return _linkResolver;
    }

    public void setLinkResolver(ILinkResolverManager resolver) {
        this._linkResolver = resolver;
    }

    protected IAuthorizationManager getAuthorizationManager() {
        return _authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this._authorizationManager = authorizationManager;
    }

    private IContentAuthorizationHelper _contentAuthorizationHelper;

    private IContentRenderer _contentRenderer;
    private IContentManager _contentManager;
    private ILinkResolverManager _linkResolver;
    private IAuthorizationManager _authorizationManager;

}
