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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageDAO;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageRecord;
import com.agiletec.aps.system.services.page.PagesStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author E.Santoboni
 */
public class PageManagerCacheWrapper implements IPageManagerCacheWrapper {
	
	private static final Logger _logger = LoggerFactory.getLogger(PageManagerCacheWrapper.class);
	
	private CacheManager _springCacheManager;
	
	@Override
	public void loadPageTree(IPageDAO pageDao) throws ApsSystemException {
		PagesStatus status = new PagesStatus();
		IPage newRoot = null;
		IPage newRootOnline = null;
		try {
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
					newRoot = pageD;
					newRootOnline = pageO;
				}
				this.buildPagesStatus(status, pageD);
				if (pageD.isOnline()) {
					newOnlineMap.put(pageO.getCode(), pageO);
					pageListO.add(pageO);
				}
			}
			for (int i = 0; i < pageListD.size(); i++) {
				this.buildTreeHierarchy(newRoot, newFullMap, pageListD.get(i));
			}
			for (int i = 0; i < pageListO.size(); i++) {
				this.buildTreeHierarchy(newRootOnline, newOnlineMap, pageListO.get(i));
			}
			if (newRoot == null) {
				throw new ApsSystemException("Error in the page tree: root page undefined");
			}
			Cache cache = this.getCache();
			cache.put("PageManager_draftRoot", newRoot);
			cache.put("PageManager_onLineRoot", newRootOnline);
			cache.put("PageManager_pagesStatus", status);
			for (int i = 0; i < pageListD.size(); i++) {
				IPage draftPage = pageListD.get(i);
				cache.put("PageManager_draft_" + draftPage.getCode(), draftPage);
			}
			for (int i = 0; i < pageListO.size(); i++) {
				IPage onLinePage = pageListO.get(i);
				cache.put("PageManager_onLine_" + onLinePage.getCode(), onLinePage);
			}
		} catch (ApsSystemException e) {
			throw e;
		} catch (Throwable t) {
			_logger.error("Error while building the tree of pages", t);
			throw new ApsSystemException("Error while building the tree of pages", t);
		}
	}

	@Override
	public PagesStatus getPagesStatus() {
		return this.getCache().get("PageManager_pagesStatus", PagesStatus.class);
	}

	@Override
	public IPage getOnlinePage(String pageCode) {
		return this.getCache().get("PageManager_onLine_" + pageCode, IPage.class);
	}

	@Override
	public IPage getDraftPage(String pageCode) {
		return this.getCache().get("PageManager_draft_" + pageCode, IPage.class);
	}

	@Override
	public void deleteDraftPage(String pageCode) {
		this.getCache().evict("PageManager_draft_" + pageCode);
	}

	@Override
	public void deleteOnlinePage(String pageCode) {
		this.getCache().evict("PageManager_onLine_" + pageCode);
	}
	
	@Override
	public IPage getOnlineRoot() {
		return this.getCache().get("PageManager_onLineRoot", IPage.class);
	}

	@Override
	public IPage getDraftRoot() {
		return this.getCache().get("PageManager_draftRoot", IPage.class);
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
			status.setDraft(status.getDraft() + 1);
		}
		if (null != currentDate) {
			if (null == status.getLastUpdate() || status.getLastUpdate().before(currentDate)) {
				status.setLastUpdate(currentDate);
			}
		}
	}
	
	protected Cache getCache() {
		return this.getSpringCacheManager().getCache(getCacheName());
	}

	public static String getCacheName() {
		return "Entando_PageManager";
	}
	
	protected CacheManager getSpringCacheManager() {
		return _springCacheManager;
	}
	public void setSpringCacheManager(CacheManager springCacheManager) {
		this._springCacheManager = springCacheManager;
	}
	
}
