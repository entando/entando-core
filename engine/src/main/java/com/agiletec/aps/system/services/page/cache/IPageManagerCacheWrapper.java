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
import com.agiletec.aps.system.services.page.PagesStatus;

/**
 * @author E.Santoboni
 */
public interface IPageManagerCacheWrapper {

	public static final String PAGE_MANAGER_CACHE_NAME = "Entando_PageManager";
	public static final String ONLINE_PAGE_CACHE_NAME_PREFIX = "PageManager_onLine_";
	public static final String DRAFT_PAGE_CACHE_NAME_PREFIX = "PageManager_draft_";
	public static final String ONLINE_ROOT_CACHE_NAME = "PageManager_onLineRoot";
	public static final String DRAFT_ROOT_CACHE_NAME = "PageManager_draftRoot";
	public static final String PAGE_STATUS_CACHE_NAME = "PageManager_pagesStatus";
	public static final String PAGE_CODES_CACHE_NAME = "PageManager_codes";

	public void initCache(IPageDAO pageDao) throws ApsSystemException;

	public PagesStatus getPagesStatus();

	public IPage getOnlinePage(String pageCode);

	public IPage getDraftPage(String pageCode);

	public IPage getOnlineRoot();

	public IPage getDraftRoot();

	public void deleteDraftPage(String pageCode);

	public void deleteOnlinePage(String pageCode);

}
