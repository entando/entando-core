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
package com.agiletec.aps.system.services.page.cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import java.util.*;

/**
 * @author E.Santoboni
 */
public class PageManagerCacheWrapper extends AbstractCacheWrapper implements IPageManagerCacheWrapper {

    private static final Logger _logger = LoggerFactory.getLogger(PageManagerCacheWrapper.class);

    private static final String ONLINE_WITH_CHANGES = "ONLINE_WITH_CHANGES";
    private static final String ONLINE = "ONLINE";
    private static final String UNPUBLISHED = "UNPUBLISHED";

    @Override
    public void initCache(IPageDAO pageDao) throws ApsSystemException {
        PagesStatus status = new PagesStatus();
        IPage newDraftRoot = null;
        IPage newOnLineRoot = null;
        try {
            Cache cache = this.getCache();
            this.releaseCachedObjects(cache);
            List<PageRecord> pageRecordList = pageDao.loadPageRecords();
            Map<String, IPage> newFullMap = new HashMap<String, IPage>(pageRecordList.size());
            Map<String, IPage> newOnlineMap = new HashMap<String, IPage>();
            List<IPage> pageListO = new ArrayList<>();
            List<IPage> pageListD = new ArrayList<>();
            for (int i = 0; i < pageRecordList.size(); i++) {
                PageRecord pageRecord = pageRecordList.get(i);
                IPage pageD = pageRecord.createDraftPage();
                IPage pageO = pageRecord.createOnlinePage();
                pageListD.add(pageD);
                newFullMap.put(pageD.getCode(), pageD);
                if (pageD.getCode().equals(pageD.getParentCode())) {
                    newDraftRoot = pageD;
                    newOnLineRoot = pageO;
                }
                this.buildPagesStatus(status, pageD);
                if (pageD.isOnline()) {
                    newOnlineMap.put(pageO.getCode(), pageO);
                    pageListO.add(pageO);
                }
            }
            for (int i = 0; i < pageListD.size(); i++) {
                this.buildTreeHierarchy(newDraftRoot, newFullMap, pageListD.get(i));
            }
            for (int i = 0; i < pageListO.size(); i++) {
                this.buildTreeHierarchy(newOnLineRoot, newOnlineMap, pageListO.get(i));
            }
            if (newDraftRoot == null) {
                throw new ApsSystemException("Error in the page tree: root page undefined");
            }
            this.insertObjectsOnCache(cache, status, newDraftRoot, newOnLineRoot, pageListD, pageListO);
        } catch (ApsSystemException e) {
            throw e;
        } catch (Throwable t) {
            _logger.error("Error while building the tree of pages", t);
            throw new ApsSystemException("Error while building the tree of pages", t);
        }
    }

    protected void releaseCachedObjects(Cache cache) {
        List<String> codes = (List<String>) this.get(cache, PAGE_CODES_CACHE_NAME, List.class);
        if (null != codes) {
            for (int i = 0; i < codes.size(); i++) {
                String code = codes.get(i);
                cache.evict(DRAFT_PAGE_CACHE_NAME_PREFIX + code);
                cache.evict(ONLINE_PAGE_CACHE_NAME_PREFIX + code);
            }
            cache.evict(PAGE_CODES_CACHE_NAME);
        }
    }

    protected void insertObjectsOnCache(Cache cache, PagesStatus status,
            IPage newDraftRoot, IPage newOnLineRoot, List<IPage> pageListD, List<IPage> pageListO) {
        cache.put(DRAFT_ROOT_CACHE_NAME, newDraftRoot);
        cache.put(ONLINE_ROOT_CACHE_NAME, newOnLineRoot);
        cache.put(PAGE_STATUS_CACHE_NAME, status);
        for (int i = 0; i < pageListD.size(); i++) {
            IPage draftPage = pageListD.get(i);
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + draftPage.getCode(), draftPage);
        }
        for (int i = 0; i < pageListO.size(); i++) {
            IPage onLinePage = pageListO.get(i);
            cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + onLinePage.getCode(), onLinePage);
        }
    }

    @Override
    public PagesStatus getPagesStatus() {
        return this.get(PAGE_STATUS_CACHE_NAME, PagesStatus.class);
    }

    @Override
    public IPage getOnlinePage(String pageCode) {
        return this.get(ONLINE_PAGE_CACHE_NAME_PREFIX + pageCode, IPage.class);
    }

    @Override
    public IPage getDraftPage(String pageCode) {
        return this.get(DRAFT_PAGE_CACHE_NAME_PREFIX + pageCode, IPage.class);
    }

    @Override
    public void deleteOnlinePage(String pageCode) {
        IPage page = this.getDraftPage(pageCode);
        if(page.isOnline()) {
            PagesStatus currentStatus = this.getPagesStatus();
            currentStatus.setOnline(currentStatus.getOnline() - 1);
            this.getCache().put(PAGE_STATUS_CACHE_NAME, currentStatus);
        }

        this.getCache().evict(ONLINE_PAGE_CACHE_NAME_PREFIX + pageCode);
    }

    @Override
    public void deleteDraftPage(String pageCode) {

        IPage page = this.getDraftPage(pageCode);
        if(!page.isOnline()) {
            PagesStatus currentStatus = this.getPagesStatus();
            currentStatus.setUnpublished(currentStatus.getUnpublished() - 1);
            this.getCache().put(PAGE_STATUS_CACHE_NAME, currentStatus);
        }else if(page.isOnline() && page.isChanged()) {
            PagesStatus currentStatus = this.getPagesStatus();
            currentStatus.setOnlineWithChanges(currentStatus.getOnlineWithChanges() -1 );
            this.getCache().put(PAGE_STATUS_CACHE_NAME, currentStatus);
        }
        this.getCache().evict(DRAFT_PAGE_CACHE_NAME_PREFIX + pageCode);
    }

    @Override
    public IPage getOnlineRoot() {
        return this.get(ONLINE_ROOT_CACHE_NAME, IPage.class);
    }

    @Override
    public IPage getDraftRoot() {
        return this.get(DRAFT_ROOT_CACHE_NAME, IPage.class);
    }

    protected void buildTreeHierarchy(IPage root, Map<String, IPage> pagesMap, IPage page) {
        Page parent = (Page) pagesMap.get(page.getParentCode());
        ((Page) page).setParent(parent);
        if (!page.getCode().equals(root.getCode())) {
            parent.addChildCode(page.getCode());
        }
    }

    protected void buildPagesStatus(PagesStatus status, IPage pageD) {
        Date currentDate = pageD.getMetadata().getUpdatedAt();
        if (pageD.isOnline()) {
            if (pageD.isChanged()) {
                status.setOnlineWithChanges(status.getOnlineWithChanges() + 1);
            } else {
                status.setOnline(status.getOnline() + 1);
            }
        } else {
            status.setUnpublished(status.getUnpublished() + 1);
        }
        if (null != currentDate) {
            if (null == status.getLastUpdate() || status.getLastUpdate().before(currentDate)) {
                status.setLastUpdate(currentDate);
            }
        }
    }

    public void addPageToCache(IPage page) {

        if(page.isOnlineInstance()) {
            if(page.isOnline()) {

//                IPage currentPage = this.getOnlinePage(page.getCode());
//
//                String currentStatus = "";
//                if(currentPage!=null) {
//                    currentStatus = getCurrentPageStatus(currentPage);
//                }
//
//                String newStatus = getCurrentPageStatus(page);
//                updatePageStatusForSinglePageChange(currentStatus, newStatus);

                if(page.getCode().equals(page.getParentCode())){
                    this.getCache().put(ONLINE_ROOT_CACHE_NAME, page);
                }
                this.getCache().put(ONLINE_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
            }
        } else {

            IPage currentPage = this.getDraftPage(page.getCode());

            String currentStatus = "";
            if(currentPage!=null) {
                currentStatus = getCurrentPageStatus(currentPage);
            }

            String newStatus = getCurrentPageStatus(page);
            updatePageStatusForSinglePageChange(currentStatus, newStatus);

            if(page.getCode().equals(page.getParentCode())){
                this.getCache().put(DRAFT_ROOT_CACHE_NAME, page);
            }
            this.getCache().put(DRAFT_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
        }
    }

    @Override
    protected String getCacheName() {
        return PAGE_MANAGER_CACHE_NAME;
    }

    private void updatePageStatusForSinglePageChange(String oldStatus, String newStatus){

        if(oldStatus.equals(newStatus)){
            return;
        }

        PagesStatus status = this.getPagesStatus();
        if(oldStatus.equals(ONLINE_WITH_CHANGES)){
            status.setOnlineWithChanges(status.getOnlineWithChanges() - 1);
        }else if(oldStatus.equals(ONLINE)){
            status.setOnline(status.getOnline() - 1);
        }else if(oldStatus.equals(UNPUBLISHED)){
            status.setUnpublished(status.getUnpublished() - 1 );
        }

        if(newStatus.equals(ONLINE_WITH_CHANGES)){
            status.setOnlineWithChanges(status.getOnlineWithChanges() + 1);
        }else if(newStatus.equals(ONLINE)){
            status.setOnline(status.getOnline() + 1);
        }else if(newStatus.equals(UNPUBLISHED)){
            status.setUnpublished(status.getUnpublished() + 1 );
        }

        this.getCache().put(PAGE_STATUS_CACHE_NAME, status);
    }

    private String getCurrentPageStatus(IPage pageD) {
        Date currentDate = pageD.getMetadata().getUpdatedAt();
        String result = "";

        if (pageD.isOnline()) {
            if (pageD.isChanged()) {
                result = ONLINE_WITH_CHANGES;
            } else {
                result = ONLINE;
            }
        } else {
            result = UNPUBLISHED;
        }

        return result;
    }
}