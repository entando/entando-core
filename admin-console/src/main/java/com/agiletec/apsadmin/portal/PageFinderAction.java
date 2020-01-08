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
package com.agiletec.apsadmin.portal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.entando.entando.apsadmin.portal.rs.model.PageJO;
import org.entando.entando.apsadmin.portal.rs.model.PagesStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.PagesStatus;
import com.agiletec.aps.system.services.user.UserDetails;
import org.apache.commons.lang3.StringUtils;

/**
 * This action class contains all the elements currently use to perform searches across the portal pages.
 *
 * @author M.E. Minnai
 */
public class PageFinderAction extends AbstractPortalAction {

    private static final Logger _logger = LoggerFactory.getLogger(PageFinderAction.class);
    private static final int DEFAULT_LASTUPDATE_RESPONSE_SIZE = 5;

    private String _pageCodeToken;

    private int _lastUpdateResponseSize;

    public PagesStatusResponse getPagesStatusResponse() {
        PagesStatusResponse response = null;
        try {
            PagesStatus pagesStatus = this.getPageManager().getPagesStatus();
            response = new PagesStatusResponse(pagesStatus);
        } catch (Throwable t) {
            _logger.error("Error loading pagesStatus", t);
            throw new RuntimeException("Error loading pagesStatus", t);
        }
        return response;
    }

    public String getLastUpdated() {
        if (this.getLastUpdateResponseSize() < 1) {
            this.setLastUpdateResponseSize(DEFAULT_LASTUPDATE_RESPONSE_SIZE);
        }
        return SUCCESS;
    }

    public List<PageJO> getLastUpdatePagesResponse() {
        List<PageJO> response = null;
        try {
            List<IPage> pages = this.getPageManager().loadLastUpdatedPages(this.getLastUpdateResponseSize());
            if (null != pages && !pages.isEmpty()) {
                response = new ArrayList<>();
                Iterator<IPage> it = pages.iterator();
                while (it.hasNext()) {
                    IPage page = it.next();
                    if (null != page) {
                        PageJO pageJO = this.buildLastUpdateResponseItem(page);
                        response.add(pageJO);
                    }
                }
            }
        } catch (Throwable t) {
            _logger.error("Error loading pagesStatus", t);
            throw new RuntimeException("Error loading pagesStatus", t);
        }
        return response;
    }

    protected PageJO buildLastUpdateResponseItem(IPage page) {
        PageJO pageJO = new PageJO();
        pageJO.setCode(page.getCode());
        pageJO.setRoot(page.isRoot());
        pageJO.setOnline(page.isOnline());
        pageJO.setChanged(page.isChanged());
        pageJO.setParentCode(page.getParentCode());
        pageJO.setGroup(page.getGroup());
        pageJO.setDraftMetadata(page.getMetadata());
        return pageJO;
    }

    public String search() {
        if (StringUtils.isBlank(this.getPageCodeToken())) {
            return "pageTree";
        }
        return SUCCESS;
    }

    public List<IPage> searchPages() {
        List<IPage> result = null;
        try {
            List<String> allowedGroupCodes = this.getAllowedGroupCodes();
            result = this.getPageManager().searchPages(this.getPageCodeToken(), allowedGroupCodes);
        } catch (Throwable t) {
            _logger.error("Error on searching pages", t);
            throw new RuntimeException("Error on searching pages", t);
        }
        return result;
    }

    @Deprecated
    public List<IPage> getPagesFound() {
        return this.searchPages();
    }

    private List<String> getAllowedGroupCodes() {
        List<String> allowedGroups = new ArrayList<>();
        UserDetails currentUser = this.getCurrentUser();
        List<Group> userGroups = this.getAuthorizationManager().getUserGroups(currentUser);
        Iterator<Group> iter = userGroups.iterator();
        while (iter.hasNext()) {
            Group group = iter.next();
            allowedGroups.add(group.getName());
        }
        return allowedGroups;
    }
    
    public String getFullTitle(IPage page, String langCode) {
        if (null != page) {
            return page.getFullTitle(langCode, this.getPageManager());
        }
        return "undefined";
    }

    /**
     * Set the token to be searched among the pages
     *
     * @param pageCodeToken The token to be searched among the pages
     */
    public void setPageCodeToken(String pageCodeToken) {
        this._pageCodeToken = pageCodeToken;
    }

    /**
     * Return the token to be searched among the pages
     *
     * @return The token to be searched among the pages
     */
    public String getPageCodeToken() {
        return _pageCodeToken;
    }

    public int getLastUpdateResponseSize() {
        return _lastUpdateResponseSize;
    }

    public void setLastUpdateResponseSize(int lastUpdateResponseSize) {
        this._lastUpdateResponseSize = lastUpdateResponseSize;
    }

}
