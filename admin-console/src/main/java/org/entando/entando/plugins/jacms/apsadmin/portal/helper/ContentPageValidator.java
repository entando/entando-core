/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jacms.apsadmin.portal.helper;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.portal.helper.IExternalPageValidator;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.jacms.aps.util.CmsPageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author E.Santoboni
 */
public class ContentPageValidator implements IExternalPageValidator, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ContentPageValidator.class);

    private IContentManager contentManager;
    private ApplicationContext applicationContext;

    @Override
    public void checkPageGroup(IPage page, boolean draftPageHepler, BaseAction action) {
        if (null == page) {
            return;
        }
        Set<String> pageGroups = new HashSet<>();
        pageGroups.add(page.getGroup());
        if (null != page.getExtraGroups()) {
            pageGroups.addAll(page.getExtraGroups());
        }
        try {
            this.checkReferencingContents(page, pageGroups, action);
            this.checkPublishedContents(page, pageGroups, action);
        } catch (Exception e) {
            logger.error("Error checking page {}", page.getCode(), e);
            throw new RuntimeException("Error checking page " + page.getCode(), e);
        }
    }

    private void checkReferencingContents(IPage page, Set<String> pageGroups, BaseAction action) throws ApsSystemException {
        if (pageGroups.contains(Group.FREE_GROUP_NAME)) {
            return;
        }
        List<String> referencingContent = ((PageUtilizer) this.getContentManager()).getPageUtilizers(page.getCode());
        if (null != referencingContent) {
            for (String contentId : referencingContent) {
                Content content = this.getContentManager().loadContent(contentId, true, false);
                if (null == content) {
                    continue;
                }
                Set<String> contentGroups = this.getContentGroups(content);
                /*
                containsAll(Collection<?> coll1, Collection<?> coll2)
                Returns true iff all elements of coll2 are also contained in coll1.
                 */
                boolean check = CollectionUtils.containsAll(pageGroups, contentGroups);
                if (!check) {
                    action.addFieldError("extraGroups", action.getText("error.page.contentRef.incompatibleGroups", new String[]{contentId, content.getDescription(), contentGroups.toString()}));
                }
            }
        }
    }

    private void checkPublishedContents(IPage page, Set<String> pageGroups, BaseAction action) throws ApsSystemException {
        if (pageGroups.contains(Group.ADMINS_GROUP_NAME)) {
            return;
        }
        Collection<Content> contents = CmsPageUtil.getPublishedContents(page.getCode(), true, this.getApplicationContext());
        if (null == contents) {
            return;
        }
        for (Content content : contents) {
            this.checkPublishedContent(content, pageGroups, action);
        }
    }

    private void checkPublishedContent(Content content, Set<String> pageGroups, BaseAction action) throws ApsSystemException {
        if (null == content) {
            return;
        }
        Set<String> contentGroups = this.getContentGroups(content);
        if (contentGroups.contains(Group.FREE_GROUP_NAME)) {
            return;
        }
        /*
        containsAll(Collection<?> coll1, Collection<?> coll2)
        Returns true iff all elements of coll2 are also contained in coll1.
         */
        boolean check = CollectionUtils.containsAll(contentGroups, pageGroups);
        if (!check) {
            action.addFieldError("extraGroups", action.getText("error.page.publishedContents.incompatibleGroups", new String[]{content.getId(), content.getDescription(), contentGroups.toString()}));
        }
    }

    private Set<String> getContentGroups(Content content) {
        Set<String> contentGroups = new HashSet<>();
        contentGroups.add(content.getMainGroup());
        if (null != content.getGroups()) {
            contentGroups.addAll(content.getGroups());
        }
        return contentGroups;
    }

    @Override
    public boolean checkForSetOnline(IPage page, BaseAction action) {
        try {
            for (Widget widget : page.getWidgets()) {
                if (null != widget) {
                    ApsProperties config = widget.getConfig();
                    String contentId = (null != config) ? config.getProperty("contentId") : null;
                    this.checkContent(action, contentId);
                }
            }
        } catch (ApsSystemException e) {
            logger.error("error checking draft page - content references", e);
            return false;
        }
        return true;
    }

    protected void checkContent(ActionSupport action, String contentId) throws ApsSystemException {
        if (StringUtils.isNotBlank(contentId)) {
            Content content = this.getContentManager().loadContent(contentId, true);
            if (null == content || !content.isOnLine()) {
                List<String> args = new ArrayList<>();
                args.add(null == content ? contentId : content.getDescription());
                action.addFieldError("extraGroups", action.getText("error.page.setOnlineContent.ref.offline", args));
            }
        }
    }

    protected IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    protected ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
