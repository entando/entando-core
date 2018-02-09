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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.action.hypertext;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.apsadmin.portal.PageTreeAction;
import com.agiletec.apsadmin.portal.helper.IPageActionHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.apsadmin.content.ContentActionConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe action delegata alla gestione dei jAPSLinks (link interni al testo degli attributi Hypertext) su pagina.
 *
 * @author E.Santoboni
 */
public class PageLinkAttributeAction extends PageTreeAction {

    private static final Logger _logger = LoggerFactory.getLogger(PageLinkAttributeAction.class);

    @Override
    protected Collection<String> getNodeGroupCodes() {
        Set<String> groupCodes = new HashSet<String>();
        groupCodes.add(Group.FREE_GROUP_NAME);
        Content currentContent = this.getContent();
        if (null != currentContent.getMainGroup()) {
            groupCodes.add(currentContent.getMainGroup());
        }
        return groupCodes;
    }

    public Content getContent() {
        return (Content) this.getRequest().getSession()
                .getAttribute(ContentActionConstants.SESSION_PARAM_NAME_CURRENT_CONTENT_PREXIX + this.getContentOnSessionMarker());
    }

    public String getContentOnSessionMarker() {
        return _contentOnSessionMarker;
    }

    public void setContentOnSessionMarker(String contentOnSessionMarker) {
        this._contentOnSessionMarker = contentOnSessionMarker;
    }

    @Override
    public ITreeNode getAllowedTreeRootNode() {
        ITreeNode node = null;
        try {
            node = ((IPageActionHelper) this.getTreeHelper()).getAllowedTreeRoot(this.getNodeGroupCodes(), true);
        } catch (Throwable t) {
            _logger.error("error in getAllowedTreeRootNode", t);
        }
        return node;
    }

    private String _contentOnSessionMarker;
}