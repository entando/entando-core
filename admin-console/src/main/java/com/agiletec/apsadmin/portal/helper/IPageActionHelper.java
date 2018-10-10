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
package com.agiletec.apsadmin.portal.helper;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.entando.entando.aps.system.services.actionlog.model.ActivityStreamInfo;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.apsadmin.system.ITreeNodeBaseActionHelper;
import org.entando.entando.apsadmin.portal.node.PageTreeNodeWrapper;

/**
 * Interface for the helper classes handling the portal pages.
 *
 * @author E.Santoboni
 */
public interface IPageActionHelper extends ITreeNodeBaseActionHelper {

    public boolean checkPageGroup(IPage page, BaseAction currentAction);

    public Map getReferencingObjects(IPage page, HttpServletRequest request) throws ApsSystemException;

    public PageTreeNodeWrapper getVirtualRoot();

    /**
     * Return the root node of the page tree respecting the given permissions.
     *
     * @param groupCodes The groups list used when building the page tree.
     * @param alsoFreeViewPages Indicate if include also only free view pages
     * @return The root of the page tree
     * @throws ApsSystemException In case of error
     */
    public ITreeNode getAllowedTreeRoot(Collection<String> groupCodes, boolean alsoFreeViewPages) throws ApsSystemException;

    public ActivityStreamInfo createActivityStreamInfo(IPage page, int strutsAction, boolean addLink, String entryAction);

    public ActivityStreamInfo createConfigFrameActivityStreamInfo(IPage page, int framePos, int strutsAction, boolean addLink);

}
