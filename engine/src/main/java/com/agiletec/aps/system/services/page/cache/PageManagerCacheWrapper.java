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
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageDAO;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.system.services.page.PageRecord;
import com.agiletec.aps.system.services.page.PagesStatus;
import com.agiletec.aps.system.services.page.Widget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class PageManagerCacheWrapper extends AbstractCacheWrapper implements IPageManagerCacheWrapper {

    private static final Logger _logger = LoggerFactory.getLogger(PageManagerCacheWrapper.class);

    private List<String> localObject = new CopyOnWriteArrayList<>();

    @Override
    public void initCache(IPageDAO pageDao) throws ApsSystemException {
        PagesStatus status = new PagesStatus();
        IPage newDraftRoot = null;
        IPage newOnLineRoot = null;
        try {
            List<PageRecord> pageRecordList = pageDao.loadPageRecords();
            Map<String, IPage> newFullMap = new HashMap<>(pageRecordList.size());
            Map<String, IPage> newOnlineMap = new HashMap<>();
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
            Cache cache = this.getCache();
            //this.releaseCachedObjects(cache);
            this.cleanLocalCache(cache);
            List<String> pageCodes = pageListD.stream().map(p -> p.getCode()).collect(Collectors.toList());
            cache.put(PAGE_CODES_CACHE_NAME, pageCodes);
            this.insertObjectsOnCache(cache, status, newDraftRoot, newOnLineRoot, pageListD, pageListO);
        } catch (ApsSystemException e) {
            throw e;
        } catch (Throwable t) {
            _logger.error("Error while building the tree of pages", t);
            throw new ApsSystemException("Error while building the tree of pages", t);
        }
    }

    private void cleanLocalCache(Cache cache) {
        for (String key : this.localObject) {
            if (null != key) {
                cache.evict(key);
            }
        }
        this.localObject.clear();
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
    public void deleteDraftPage(String pageCode) {
        Cache cache = this.getCache();
        IPage page = this.getDraftPage(pageCode);
        if (null == page) {
            return;
        }
        IPage parent = this.getDraftPage(page.getParentCode());
        if (null != parent.getChildrenCodes()) {
            List<String> childrenCodes = new ArrayList<>(Arrays.asList(parent.getChildrenCodes()));
            int index = -1;
            for (int i = 0; i < childrenCodes.size(); i++) {
                String childCode = childrenCodes.get(i);
                if (childCode.equals(pageCode)) {
                    index = i;
                }
                if (index > 0 && i > index) {
                    this.upgradePositionForSisterDeletion(cache, childCode, true);
                }
            }
            boolean executedRemove = childrenCodes.remove(pageCode);
            if (executedRemove) {
                ((Page) parent).setChildrenCodes(childrenCodes.toArray(new String[childrenCodes.size()]));
                cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + parent.getCode(), parent);
            }
            if (index > 0 && index<childrenCodes.size()-2) {
                for (int i = index; i < childrenCodes.size(); i++) {
                    String code = childrenCodes.get(i);
                    IPage sister = this.getDraftPage(code);
                    if (null != sister) {
                        sister.setPosition(sister.getPosition()-1);
                        cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + sister.getCode(), sister);
                    }
                    IPage onlineSister = this.getOnlinePage(code);
                    if (null != onlineSister) {
                        onlineSister.setPosition(onlineSister.getPosition()-1);
                        cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + onlineSister.getCode(), onlineSister);
                    }
                }
            }
        }
        List<String> codes = (List<String>) this.get(cache, PAGE_CODES_CACHE_NAME, List.class);
        if (null != codes) {
            codes.remove(pageCode);
            cache.put(PAGE_CODES_CACHE_NAME, codes);
        }
        cache.evict(DRAFT_PAGE_CACHE_NAME_PREFIX + pageCode);
        cache.evict(ONLINE_PAGE_CACHE_NAME_PREFIX + pageCode);
        this.cleanLocalCache(cache);
        PagesStatus status = this.getPagesStatus();
        status.setLastUpdate(new Date());
        status.setUnpublished(status.getUnpublished() - 1);
        cache.put(PAGE_STATUS_CACHE_NAME, status);
    }

    private void upgradePositionForSisterDeletion(Cache cache, String code, boolean online) {
        IPage page = (online) ? this.getOnlinePage(code) : this.getDraftPage(code);
        if (null == page) {
            return;
        }
        ((Page) page).setPosition(page.getPosition() - 1);
        if (online) {
            cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
        } else {
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
        }
    }
    
    @Override
    public void addDraftPage(IPage page) {
        Cache cache = this.getCache();
        List<String> codes = (List<String>) this.get(cache, PAGE_CODES_CACHE_NAME, List.class);
        codes.add(page.getCode());
        cache.put(PAGE_CODES_CACHE_NAME, codes);
        IPage parent = this.getDraftPage(page.getParentCode());
        String[] childCodes = parent.getChildrenCodes();
        childCodes = ArrayUtils.add(childCodes, page.getCode());
        ((Page) parent).setChildrenCodes(childCodes);
        cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + parent.getCode(), parent);
        cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
        this.cleanLocalCache(cache);
        PagesStatus status = this.getPagesStatus();
        status.setLastUpdate(new Date());
        status.setUnpublished(status.getUnpublished()+1);
        cache.put(PAGE_STATUS_CACHE_NAME, status);
    }
    
    @Override
    public void updateDraftPage(IPage page) {
        IPage onlinepage = this.getOnlinePage(page.getCode());
        PageMetadata onlineMeta = (null != onlinepage) ? onlinepage.getMetadata() : null;
        Widget[] widgetsOnline = (null != onlinepage) ? onlinepage.getWidgets() : new Widget[0];
        ((Page) page).setOnline(null != onlineMeta);
        boolean isChanged = (null != onlinepage) && this.isChanged(page.getMetadata(), onlineMeta, page.getWidgets(), widgetsOnline);
        ((Page) page).setChanged(isChanged);
        Cache cache = this.getCache();
        cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
        this.cleanLocalCache(cache);
        if (isChanged) {
            PagesStatus status = this.getPagesStatus();
            status.setLastUpdate(new Date());
            status.setOnlineWithChanges(status.getOnlineWithChanges() + 1);
            status.setOnline(status.getOnline()-1);
            cache.put(PAGE_STATUS_CACHE_NAME, status);
        }
    }

    @Override
    public void setPageOnline(String pageCode) {
        Cache cache = this.getCache();
        IPage page = this.getDraftPage(pageCode);
        if (null != page) {
            IPage onlinepage = this.getOnlinePage(page.getCode());
            boolean alreadyOnline = null != onlinepage;
            boolean changed = (alreadyOnline 
                    && this.isChanged(page.getMetadata(), onlinepage.getMetadata(), page.getWidgets(), onlinepage.getWidgets()));
            ((Page) page).setOnline(true);
            ((Page) page).setChanged(false);
            IPage newOnlinePage = page.clone();
            cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + newOnlinePage.getCode(), newOnlinePage);
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
            if (!alreadyOnline || changed) {
                PagesStatus status = this.getPagesStatus();
                status.setLastUpdate(new Date());
                if (!alreadyOnline) {
                    status.setOnline(status.getOnline()+1);
                    status.setUnpublished(status.getUnpublished()-1);
                } else if (changed) {
                    status.setOnlineWithChanges(status.getOnlineWithChanges()+1);
                    status.setOnline(status.getOnline()-1);
                }
                cache.put(PAGE_STATUS_CACHE_NAME, status);
            }
        }
        this.cleanLocalCache(cache);
    }

    @Override
    public void setPageOffline(String pageCode) {
        Cache cache = this.getCache();
        IPage page = this.getDraftPage(pageCode);
        IPage onlinepage = this.getOnlinePage(pageCode);
        if (null != onlinepage) {
            cache.evict(ONLINE_PAGE_CACHE_NAME_PREFIX + pageCode);
            PagesStatus status = this.getPagesStatus();
            status.setLastUpdate(new Date());
            if (page.isChanged()) {
                status.setOnlineWithChanges(status.getOnlineWithChanges()-1);
            } else {
                status.setOnline(status.getOnline()-1);
            }
            status.setUnpublished(status.getUnpublished()+1);
            cache.put(PAGE_STATUS_CACHE_NAME, status);
        }
        if (null != page) {
            ((Page) page).setOnline(false);
            ((Page) page).setChanged(false);
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
        }
        this.cleanLocalCache(cache);
    }

    protected boolean isChanged(PageMetadata draftMeta, PageMetadata onlineMeta, Widget[] widgetsDraft, Widget[] widgetsOnline) {
        boolean changed = false;
        if (onlineMeta != null) {
            if (draftMeta != null) {
                boolean widgetEquals = true;
                widgetsDraft = (null == widgetsDraft) ? new Widget[0] : widgetsDraft;
                widgetsOnline = (null == widgetsOnline) ? new Widget[0] : widgetsOnline;
                for (int i = 0; i < widgetsDraft.length; i++) {
                    Widget widgetDraft = widgetsDraft[i];
                    if (widgetsOnline.length < i) {
                        widgetEquals = false;
                        break;
                    }
                    Widget widgetOnline = widgetsOnline[i];
                    if (null == widgetOnline && null == widgetDraft) {
                        continue;
                    }
                    if ((null != widgetOnline && null == widgetDraft) || (null == widgetOnline && null != widgetDraft)) {
                        widgetEquals = false;
                        break;
                    }
                    if (!widgetOnline.getType().getCode().equals(widgetDraft.getType().getCode())) {
                        widgetEquals = false;
                    }
                    if (null == widgetOnline.getConfig() && null == widgetDraft.getConfig()) {
                        continue;
                    }
                    if ((null != widgetOnline.getConfig() && null == widgetDraft.getConfig())
                            || (null == widgetOnline.getConfig() && null != widgetDraft.getConfig())) {
                        widgetEquals = false;
                        break;
                    }
                    if (!widgetOnline.getConfig().equals(widgetDraft.getConfig())) {
                        widgetEquals = false;
                        break;
                    }
                }
                boolean metaEquals = onlineMeta.hasEqualConfiguration(draftMeta);
                return !(widgetEquals && metaEquals);
            } else {
                changed = true;
            }
        }
        return changed;
    }
    
    @Override
    public void moveUpDown(String pageDown, String pageUp) {
        IPage draftToMoveUp = this.getDraftPage(pageUp);
        IPage draftToMoveDown = this.getDraftPage(pageDown);
        if (null != draftToMoveDown && null != draftToMoveUp
                && draftToMoveDown.getParentCode().equals(draftToMoveUp.getParentCode())) {
            Cache cache = this.getCache();
            draftToMoveUp.setPosition(draftToMoveUp.getPosition()-1);
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + draftToMoveUp.getCode(), draftToMoveUp);
            draftToMoveDown.setPosition(draftToMoveDown.getPosition()+1);
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + draftToMoveDown.getCode(), draftToMoveDown);
            IPage onlineToMoveUp = this.getOnlinePage(pageUp);
            if (null != onlineToMoveUp) {
                onlineToMoveUp.setPosition(draftToMoveUp.getPosition());
                cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + draftToMoveUp.getCode(), draftToMoveUp);
            }
            IPage onlineToMoveDown = this.getOnlinePage(pageDown);
            if (null != onlineToMoveDown) {
                onlineToMoveDown.setPosition(draftToMoveUp.getPosition());
                cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + onlineToMoveDown.getCode(), onlineToMoveDown);
            }
            if (draftToMoveUp.getPosition()<draftToMoveDown.getPosition()) {
                this.switchSister(draftToMoveUp.getParentCode(), pageUp, pageDown);
            }
        } else {
            _logger.error("Movement impossible - page to move up {} - page to move down {}", pageUp, pageDown);
        }
    }

    private void switchSister(String parentCode, String pageUp, String pageDown) {
        Cache cache = this.getCache();
        IPage onlineParent = this.getOnlinePage(parentCode);
        if (null != onlineParent) {
            List<String> children = new ArrayList(Arrays.asList(onlineParent.getChildrenCodes()));
            int pos1 = children.indexOf(pageUp);
            int pos2 = children.indexOf(pageDown);
            if (pos1 > 0 && pos2 > 0) {
                Collections.swap(children, pos1, pos2);
                ((Page) onlineParent).setChildrenCodes(children.toArray(new String[children.size()]));
                cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + onlineParent.getCode(), onlineParent);
            }
        }
        IPage draftParent = this.getDraftPage(parentCode);
        if (null != draftParent) {
            List<String> children = new ArrayList(Arrays.asList(draftParent.getChildrenCodes()));
            int pos1 = children.indexOf(pageUp);
            int pos2 = children.indexOf(pageDown);
            if (pos1 > 0 && pos2 > 0) {
                Collections.swap(children, pos1, pos2);
                ((Page) draftParent).setChildrenCodes(children.toArray(new String[children.size()]));
                cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + draftParent.getCode(), draftParent);
            }
        }
    }
    
    @Override
    public PagesStatus getPagesStatus() {
        return this.get(PAGE_STATUS_CACHE_NAME, PagesStatus.class);
    }
    
    @Override
    public IPage getOnlinePage(String pageCode) {
        IPage page = this.get(ONLINE_PAGE_CACHE_NAME_PREFIX + pageCode, IPage.class);
        if (null != page) {
            return page.clone();
        }
        return null;
    }

    @Override
    public IPage getDraftPage(String pageCode) {
        IPage page = this.get(DRAFT_PAGE_CACHE_NAME_PREFIX + pageCode, IPage.class);
        if (null != page) {
            return page.clone();
        }
        return null;
    }

    @Override
    public IPage getOnlineRoot() {
        return this.get(ONLINE_ROOT_CACHE_NAME, IPage.class).clone();
    }

    @Override
    public IPage getDraftRoot() {
        return this.get(DRAFT_ROOT_CACHE_NAME, IPage.class).clone();
    }

    protected void buildTreeHierarchy(IPage root, Map<String, IPage> pagesMap, IPage page) {
        Page parent = (Page) pagesMap.get(page.getParentCode());
        page.setParentCode(parent.getCode());
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

    @Override
    public List<String> getOnlineWidgetUtilizers(String widgetTypeCode) throws ApsSystemException {
        return this.getWidgetUtilizers(widgetTypeCode, false);
    }

    @Override
    public List<String> getDraftWidgetUtilizers(String widgetTypeCode) throws ApsSystemException {
        return this.getWidgetUtilizers(widgetTypeCode, true);
    }

    private List<String> getWidgetUtilizers(String widgetTypeCode, boolean draft) throws ApsSystemException {
        if (null == widgetTypeCode) {
            return null;
        }
        Cache cache = super.getCache();
        String key = this.getWidgetUtilizerCacheName(widgetTypeCode, draft);
        List<String> pageCodes = this.get(cache, key, List.class);
        if (null == pageCodes) {
            Map<String, List> utilizersMap = new HashMap<>();
            try {
                IPage root = (draft) ? this.getDraftRoot() : this.getOnlineRoot();
                this.getWidgetUtilizers(root, utilizersMap, draft);
            } catch (Throwable t) {
                String message = "Error during searching draft page utilizers";
                _logger.error(message, t);
                throw new ApsSystemException(message, t);
            }
            utilizersMap.keySet().stream().forEach(cacheKey -> {
                cache.put(cacheKey, utilizersMap.get(cacheKey));
                this.localObject.add(cacheKey);
            });
            pageCodes = utilizersMap.get(key);
        }
        if (null == pageCodes) {
            pageCodes = new ArrayList<>();
        }
        return pageCodes;
    }

    private void getWidgetUtilizers(IPage page, Map<String, List> utilizersMap, boolean draft) {
        Widget[] widgets = page.getWidgets();
        for (Widget widget : widgets) {
            if (null != widget && null != widget.getType()) {
                String cacheCode = this.getWidgetUtilizerCacheName(widget.getType().getCode(), draft);
                List<String> widgetUtilizers = utilizersMap.get(cacheCode);
                if (null == widgetUtilizers) {
                    widgetUtilizers = new ArrayList<>();
                    utilizersMap.put(cacheCode, widgetUtilizers);
                }
                widgetUtilizers.add(page.getCode());
            }
        }
        String[] childrenCodes = page.getChildrenCodes();
        for (String childrenCode : childrenCodes) {
            IPage child = (draft) ? this.getDraftPage(childrenCode) : this.getOnlinePage(childrenCode);
            if (null != child) {
                this.getWidgetUtilizers(child, utilizersMap, draft);
            }
        }
    }

    private String getWidgetUtilizerCacheName(String widgetTypeCode, boolean draft) {
        return ((draft) ? DRAFT_WIDGET_UTILIZER_CACHE_NAME_PREFIX : ONLINE_WIDGET_UTILIZER_CACHE_NAME_PREFIX) + widgetTypeCode;
    }

    @Override
    protected String getCacheName() {
        return PAGE_MANAGER_CACHE_NAME;
    }

}
