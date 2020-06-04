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

import java.util.*;
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
            for (PageRecord pageRecord : pageRecordList) {
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
            for (IPage iPage : pageListD) {
                this.buildTreeHierarchy(newDraftRoot, newFullMap, iPage);
            }
            for (IPage iPage : pageListO) {
                this.buildTreeHierarchy(newOnLineRoot, newOnlineMap, iPage);
            }
            if (newDraftRoot == null) {
                throw new ApsSystemException("Error in the page tree: root page undefined");
            }
            Cache cache = this.getCache();
            //this.releaseCachedObjects(cache);
            this.cleanLocalCache(cache);
            List<String> draftPageCodes = pageListD.stream().map(p -> p.getCode()).collect(Collectors.toList());
            cache.put(DRAFT_PAGE_CODES_CACHE_NAME, draftPageCodes);
            List<String> onlinePageCodes = pageListO.stream().map(p -> p.getCode()).collect(Collectors.toList());
            cache.put(ONLINE_PAGE_CODES_CACHE_NAME, onlinePageCodes);
            this.insertObjectsOnCache(cache, status, newDraftRoot, newOnLineRoot, pageListD, pageListO);
        } catch (ApsSystemException e) {
            throw e;
        } catch (Throwable t) {
            _logger.error("Error while building the tree of pages", t);
            throw new ApsSystemException("Error while building the tree of pages", t);
        }
    }

    private void cleanLocalCache(Cache cache) {
        for (Iterator<String> iterator = this.localObject.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            if (null != key) {
                cache.evict(key);
            }
        }
        this.localObject.clear();
    }

    protected void releaseCachedObjects(Cache cache) {
        List<String> codes = (List<String>) this.get(cache, DRAFT_PAGE_CODES_CACHE_NAME, List.class);
        if (null != codes) {
            for (String code : codes) {
                cache.evict(DRAFT_PAGE_CACHE_NAME_PREFIX + code);
                cache.evict(ONLINE_PAGE_CACHE_NAME_PREFIX + code);
            }
            cache.evict(DRAFT_PAGE_CODES_CACHE_NAME);
            cache.evict(ONLINE_PAGE_CODES_CACHE_NAME);
        }
    }

    protected void insertObjectsOnCache(Cache cache, PagesStatus status,
            IPage newDraftRoot, IPage newOnLineRoot, List<IPage> pageListD, List<IPage> pageListO) {
        cache.put(DRAFT_ROOT_CACHE_NAME, newDraftRoot);
        cache.put(ONLINE_ROOT_CACHE_NAME, newOnLineRoot);
        cache.put(PAGE_STATUS_CACHE_NAME, status);
        for (IPage draftPage : pageListD) {
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + draftPage.getCode(), draftPage);
        }
        for (IPage onLinePage : pageListO) {
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
            int i = 0;
            while (i < childrenCodes.size()) {
                String childCode = childrenCodes.get(i);
                if (childCode.equals(pageCode)) {
                    index = i;
                }
                if (index > 0 && i > index) {
                    this.upgradePositionForSisterDeletion(cache, childCode, true);
                    this.upgradePositionForSisterDeletion(cache, childCode, false);
                }
                i++;
            }
            boolean executedRemove = childrenCodes.remove(pageCode);
            if (executedRemove) {
                ((Page) parent).setChildrenCodes(childrenCodes.toArray(new String[childrenCodes.size()]));
                cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + parent.getCode(), parent);
                this.checkRootModification(parent, false, cache);
            }
            IPage parentOnLine = this.getOnlinePage(page.getParentCode());
            if (null != parentOnLine) {
                List<String> onlineChildrenCodes = new ArrayList<>(Arrays.asList(parentOnLine.getChildrenCodes()));
                boolean executedRemoveOnOnLine = onlineChildrenCodes.remove(pageCode);
                if (executedRemoveOnOnLine) {
                    ((Page) parentOnLine).setChildrenCodes(onlineChildrenCodes.toArray(new String[childrenCodes.size()]));
                    cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + parentOnLine.getCode(), parentOnLine);
                    this.checkRootModification(parentOnLine, true, cache);
                }
            }
        }
        this.removeCodeFromCachedList(cache, DRAFT_PAGE_CODES_CACHE_NAME, pageCode);
        this.removeCodeFromCachedList(cache, ONLINE_PAGE_CODES_CACHE_NAME, pageCode);
        boolean isPublic = page.isOnline();
        boolean isChanged = page.isChanged();
        cache.evict(DRAFT_PAGE_CACHE_NAME_PREFIX + pageCode);
        cache.evict(ONLINE_PAGE_CACHE_NAME_PREFIX + pageCode);
        this.cleanLocalCache(cache);
        PagesStatus status = this.getPagesStatus();
        status.setLastUpdate(new Date());
        if (isPublic && isChanged) {
            status.setOnlineWithChanges(status.getOnlineWithChanges() - 1);
        } else if (isPublic && !isChanged) {
            status.setOnline(status.getOnline() - 1);
        } else {
            status.setUnpublished(status.getUnpublished() - 1);
        }
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
        this.addCodeFromCachedList(cache, DRAFT_PAGE_CODES_CACHE_NAME, page.getCode());
        ((Page) page).setChildrenCodes(new String[0]);
        IPage parent = this.getDraftPage(page.getParentCode());
        String[] childCodes = parent.getChildrenCodes();
        boolean containsChild = Arrays.stream(childCodes).anyMatch(page.getCode()::equals);
        if (!containsChild) {
            childCodes = ArrayUtils.add(childCodes, page.getCode());
            ((Page) parent).setChildrenCodes(childCodes);
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + parent.getCode(), parent);
        }
        cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
        this.checkRootModification(page, false, cache);
        this.checkRootModification(parent, false, cache);
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
        this.checkRootModification(page, false, cache);
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
            this.addCodeFromCachedList(cache, ONLINE_PAGE_CODES_CACHE_NAME, page.getCode());
            IPage onlinepage = this.getOnlinePage(page.getCode());
            boolean alreadyOnline = null != onlinepage;
            boolean changed = (alreadyOnline
                    && this.isChanged(page.getMetadata(), onlinepage.getMetadata(), page.getWidgets(), onlinepage.getWidgets()));
            ((Page) page).setOnline(true);
            ((Page) page).setChanged(false);
            IPage newOnlinePage = page.clone();
            ((Page) newOnlinePage).setOnlineInstance(true);
            List<String> totalOnlineCodes = (List<String>) this.get(cache, ONLINE_PAGE_CODES_CACHE_NAME, List.class);
            List<String> onLineCodes = Arrays.asList(newOnlinePage.getChildrenCodes())
                    .stream().filter(code -> null != this.getOnlinePage(code) && totalOnlineCodes.contains(code))
                    .collect(Collectors.toList());
            ((Page) newOnlinePage).setChildrenCodes(onLineCodes.toArray(new String[onLineCodes.size()]));
            cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + newOnlinePage.getCode(), newOnlinePage);
            this.checkRootModification(newOnlinePage, true, cache);
            cache.put(DRAFT_PAGE_CACHE_NAME_PREFIX + page.getCode(), page);
            this.checkRootModification(page, false, cache);
            if (!alreadyOnline) {
                IPage parentOnLine = this.getOnlinePage(newOnlinePage.getParentCode());
                if (null != parentOnLine && !parentOnLine.getCode().equals(pageCode)) {
                    IPage parentDraft = this.getDraftPage(newOnlinePage.getParentCode());
                    List<String> draftChildrenCodes = Arrays.asList(parentDraft.getChildrenCodes());
                    List<String> newOnLineCodesForParent = draftChildrenCodes.stream()
                            .filter(code -> null != this.getOnlinePage(code) && totalOnlineCodes.contains(code))
                            .collect(Collectors.toList());
                    ((Page) parentOnLine).setChildrenCodes(newOnLineCodesForParent.toArray(new String[newOnLineCodesForParent.size()]));
                    cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + parentOnLine.getCode(), parentOnLine);
                    this.checkRootModification(parentOnLine, true, cache);
                }
            }
            if (!alreadyOnline || changed) {
                PagesStatus status = this.getPagesStatus();
                status.setLastUpdate(new Date());
                if (!alreadyOnline) {
                    status.setOnline(status.getOnline() + 1);
                    status.setUnpublished(status.getUnpublished() - 1);
                } else if (changed) {
                    status.setOnlineWithChanges(status.getOnlineWithChanges() + 1);
                    status.setOnline(status.getOnline() - 1);
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
        this.removeCodeFromCachedList(cache, ONLINE_PAGE_CODES_CACHE_NAME, pageCode);
        IPage onlinepage = this.getOnlinePage(pageCode);
        if (null != onlinepage) {
            cache.evict(ONLINE_PAGE_CACHE_NAME_PREFIX + pageCode);
            PagesStatus status = this.getPagesStatus();
            status.setLastUpdate(new Date());
            if (page.isChanged()) {
                status.setOnlineWithChanges(status.getOnlineWithChanges() - 1);
            } else {
                status.setOnline(status.getOnline() - 1);
            }
            status.setUnpublished(status.getUnpublished() + 1);
            cache.put(PAGE_STATUS_CACHE_NAME, status);
            IPage parentOnLine = this.getOnlinePage(onlinepage.getParentCode());
            if (null != parentOnLine) {
                List<String> onlineChildrenCodes = new ArrayList<>(Arrays.asList(parentOnLine.getChildrenCodes()));
                boolean executedRemoveOnOnLine = onlineChildrenCodes.remove(pageCode);
                if (executedRemoveOnOnLine) {
                    ((Page) parentOnLine).setChildrenCodes(onlineChildrenCodes.toArray(new String[onlineChildrenCodes.size()]));
                    cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + parentOnLine.getCode(), parentOnLine);
                    this.checkRootModification(parentOnLine, true, cache);
                }
            }
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
                int i = 0;
                while (i < widgetsDraft.length) {
                    Widget widgetDraft = widgetsDraft[i];
                    if (widgetsOnline.length <= i) {
                        widgetEquals = false;
                        break;
                    }
                    Widget widgetOnline = widgetsOnline[i];
                    if (null == widgetOnline && null == widgetDraft) {
                        i++;
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
                        i++;
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
                    i++;
                }
                boolean metaEquals = onlineMeta.hasEqualConfiguration(draftMeta);
                return !(widgetEquals && metaEquals);
            } else {
                changed = true;
            }
        }
        return changed;
    }
    
    private void addCodeFromCachedList(Cache cache, String listKey, String codeToAdd) {
        List<String> codes = (List<String>) this.get(cache, listKey, List.class);
        if (null != codes && !codes.contains(codeToAdd)) {
            codes.add(codeToAdd);
            cache.put(listKey, codes);
        }
    }
    
    private void removeCodeFromCachedList(Cache cache, String listKey, String codeToRemove) {
        List<String> codes = (List<String>) this.get(cache, listKey, List.class);
        if (null != codes) {
            codes.remove(codeToRemove);
            cache.put(listKey, codes);
        }
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
            this.upgradePositionOnOnlineVersion(pageUp, draftToMoveUp.getPosition(), cache);
            this.upgradePositionOnOnlineVersion(pageDown, draftToMoveDown.getPosition(), cache);
            if (draftToMoveUp.getPosition()<draftToMoveDown.getPosition()) {
                this.switchSisterOnParent(draftToMoveUp.getParentCode(), pageUp, pageDown, cache, false);
                this.switchSisterOnParent(draftToMoveUp.getParentCode(), pageUp, pageDown, cache, true);
            }
        } else {
            _logger.error("Movement impossible - page to move up {} - page to move down {}", pageUp, pageDown);
        }
    }
    
    private void upgradePositionOnOnlineVersion(String pageCode, int position, Cache cache) {
        IPage onlinePage = this.getOnlinePage(pageCode);
        if (null != onlinePage) {
            onlinePage.setPosition(position);
            cache.put(ONLINE_PAGE_CACHE_NAME_PREFIX + onlinePage.getCode(), onlinePage);
        }
    }
    
    private void switchSisterOnParent(String parentCode, String pageUp, String pageDown, Cache cache, boolean online) {
        IPage parent = (online) ? this.getOnlinePage(parentCode) : this.getDraftPage(parentCode);
        if (null != parent) {
            List<String> children = new ArrayList(Arrays.asList(parent.getChildrenCodes()));
            int pos1 = children.indexOf(pageUp);
            int pos2 = children.indexOf(pageDown);
            if (pos1 >= 0 && pos2 >= 0) {
                Collections.swap(children, pos1, pos2);
                ((Page) parent).setChildrenCodes(children.toArray(new String[children.size()]));
                String cacheKey = (online) ? 
                        (ONLINE_PAGE_CACHE_NAME_PREFIX + parent.getCode()) :
                        (DRAFT_PAGE_CACHE_NAME_PREFIX + parent.getCode());
                cache.put(cacheKey, parent);
            }
            this.checkRootModification(parent, online, cache);
        }
    }
    
    private void checkRootModification(IPage page, boolean online, Cache cache) {
        if (page.isRoot()) {
            if (!online) {
                cache.put(DRAFT_ROOT_CACHE_NAME, page);
            } else {
                cache.put(ONLINE_ROOT_CACHE_NAME, page);
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
        return this.returnClone(page);
    }

    @Override
    public IPage getDraftPage(String pageCode) {
        IPage page = this.get(DRAFT_PAGE_CACHE_NAME_PREFIX + pageCode, IPage.class);
        return this.returnClone(page);
    }

    @Override
    public IPage getOnlineRoot() {
        IPage page = this.get(ONLINE_ROOT_CACHE_NAME, IPage.class).clone();
        return this.returnClone(page);
    }

    @Override
    public IPage getDraftRoot() {
        IPage page = this.get(DRAFT_ROOT_CACHE_NAME, IPage.class).clone();
        return this.returnClone(page);
    }
    
    private IPage returnClone(IPage page) {
        if (null != page) {
            return page.clone();
        }
        return null;
    }

    protected void buildTreeHierarchy(IPage root, Map<String, IPage> pagesMap, IPage page) {
        try {
            Page parent = (Page) pagesMap.get(page.getParentCode());
            page.setParentCode(parent.getCode());
            if (!page.getCode().equals(root.getCode())) {
                parent.addChildCode(page.getCode());
            }
        } catch (Exception e) {
            _logger.error("Error extracting parent of page {} - parent {}", page.getCode(), page.getParentCode(), e);
            throw e;
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
            return new ArrayList<>();
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
                if (!this.localObject.contains(cacheKey)) {
                    this.localObject.add(cacheKey);
                }
            });
            pageCodes = utilizersMap.get(key);
        }
        if (null == pageCodes) {
            pageCodes = new ArrayList<>();
            cache.put(key, pageCodes);
            this.localObject.add(key);
        }
        return pageCodes;
    }

    private void getWidgetUtilizers(IPage page, Map<String, List> utilizersMap, boolean draft) {
        Widget[] widgets = page.getWidgets();

        if(widgets!=null) {
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
        }
        String[] childrenCodes = page.getChildrenCodes();

        if(childrenCodes !=null) {
            for (String childrenCode : childrenCodes) {
                IPage child = (draft) ? this.getDraftPage(childrenCode) : this.getOnlinePage(childrenCode);
                if (null != child) {
                    this.getWidgetUtilizers(child, utilizersMap, draft);
                }
            }
        }
    }

    @Override
    public void movePage(String pageCode, String newParentCode) {
        Cache cache = super.getCache();
        IPage pageToMove = this.getDraftPage(pageCode);
        IPage newParent = this.getDraftPage(newParentCode);
        
        IPage oldParentDraft = this.updateOldParent(pageToMove, true, cache);
        this.updateOldParent(pageToMove, false, cache);
        String[] newChildrenDraft = oldParentDraft.getChildrenCodes();
        int i = 0;
        while (i < newChildrenDraft.length) {
            this.updatePositionAndParent(newChildrenDraft[i], i+1, null, false, cache);
            this.updatePositionAndParent(newChildrenDraft[i], i+1, null, true, cache);
            i++;
        }

        String[] oldChildDest = newParent.getChildrenCodes();
        Integer lastPos = (null == oldChildDest || oldChildDest.length == 0) ? 1
                : Arrays.stream(oldChildDest).map(f -> this.getDraftPage(f).getPosition()).max(Integer::compareTo).get() + 1;
        this.updatePositionAndParent(pageCode, lastPos, newParentCode, false, cache);
        this.updatePositionAndParent(pageCode, lastPos, newParentCode, true, cache);
        
        this.updateNewParent(pageCode, newParentCode, true, cache);
        if (null != this.getOnlinePage(pageCode)) {
            this.updateNewParent(pageCode, newParentCode, false, cache);
        }
    }
    
    private IPage updateOldParent(IPage pageToMove, boolean draft, Cache cache) {
        IPage oldParent = (draft) ? this.getDraftPage(pageToMove.getParentCode()) : this.getOnlinePage(pageToMove.getParentCode());
        if (null == oldParent) {
            return null;
        }
        int index = Arrays.asList(oldParent.getChildrenCodes()).indexOf(pageToMove.getCode());
        if (index > -1) {
            //rimuovo l'elemento dal vecchio parent
            String[] newChildren = ArrayUtils.remove(oldParent.getChildrenCodes(), index);
            ((Page) oldParent).setChildrenCodes(newChildren);
            cache.put(((draft) ? DRAFT_PAGE_CACHE_NAME_PREFIX : ONLINE_PAGE_CACHE_NAME_PREFIX) + oldParent.getCode(), oldParent);
        }
        return oldParent;
    }
    
    private void updatePositionAndParent(String pageCode, int newPosition, String newParent, boolean draft, Cache cache) {
        IPage page = (draft) ? this.getDraftPage(pageCode) : this.getOnlinePage(pageCode);
        if (null != page) {
            page.setPosition(newPosition);
            if (null != newParent) {
                page.setParentCode(newParent);
            }
            cache.put(((draft) ? DRAFT_PAGE_CACHE_NAME_PREFIX : ONLINE_PAGE_CACHE_NAME_PREFIX) + page.getCode(), page);
        }
    }
    
    private IPage updateNewParent(String pageToMoveCode, String newParentCode, boolean draft, Cache cache) {
        IPage newParent = (draft) ? this.getDraftPage(newParentCode) : this.getOnlinePage(newParentCode);
        if (null == newParent) {
            return null;
        }
        String[] newChildren = ArrayUtils.add(newParent.getChildrenCodes(), pageToMoveCode);
        ((Page) newParent).setChildrenCodes(newChildren);
        cache.put(((draft) ? DRAFT_PAGE_CACHE_NAME_PREFIX : ONLINE_PAGE_CACHE_NAME_PREFIX) + newParent.getCode(), newParent);
        return newParent;
    }
    
    private String getWidgetUtilizerCacheName(String widgetTypeCode, boolean draft) {
        return ((draft) ? DRAFT_WIDGET_UTILIZER_CACHE_NAME_PREFIX : ONLINE_WIDGET_UTILIZER_CACHE_NAME_PREFIX) + widgetTypeCode;
    }

    @Override
    protected String getCacheName() {
        return PAGE_MANAGER_CACHE_NAME;
    }

}
