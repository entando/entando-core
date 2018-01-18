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
package com.agiletec.aps.system.services.pagemodel.cache;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelDAO;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * @author E.Santoboni
 */
public class PageModelManagerCacheWrapper implements IPageModelManagerCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(PageModelManagerCacheWrapper.class);

	private CacheManager _springCacheManager;

	@Override
	public void loadPageModels(IPageModelDAO pageModelDAO) throws ApsSystemException {
		try {
			Map<String, PageModel> models = pageModelDAO.loadModels();
			List<String> modelCodes = new ArrayList<String>();
			Iterator<PageModel> iterator = models.values().iterator();
			while (iterator.hasNext()) {
				PageModel pageModel = iterator.next();
				this.getCache().put(PAGE_MODEL_CACHE_NAME_PREFIX + pageModel.getCode(), pageModel);
				modelCodes.add(pageModel.getCode());
			}
			this.getCache().put(PAGE_MODEL_CODES_CACHE_NAME, modelCodes);
		} catch (Throwable t) {
			_logger.error("Error loading page models", t);
			throw new ApsSystemException("Error loading page models", t);
		}
	}

	@Override
	public PageModel getPageModel(String name) {
		return this.getCache().get(PAGE_MODEL_CACHE_NAME_PREFIX + name, PageModel.class);
	}

	@Override
	public Collection<PageModel> getPageModels() {
		List<PageModel> models = new ArrayList<PageModel>();
		Cache cache = this.getCache();
		List<String> codes = cache.get(PAGE_MODEL_CODES_CACHE_NAME, List.class);
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i);
			models.add(cache.get(PAGE_MODEL_CACHE_NAME_PREFIX + code, PageModel.class));
		}
		return models;
	}

	@Override
	public void addPageModel(PageModel pageModel) {
		if (null == pageModel) {
			_logger.debug("Null page model can be add");
			return;
		}
		Cache cache = this.getCache();
		cache.put(PAGE_MODEL_CACHE_NAME_PREFIX + pageModel.getCode(), pageModel);
		List<String> codes = cache.get(PAGE_MODEL_CODES_CACHE_NAME, List.class);
		codes.add(pageModel.getCode());
		cache.put(PAGE_MODEL_CODES_CACHE_NAME, codes);
	}

	@Override
	public void updatePageModel(PageModel pageModel) {
		if (null == pageModel) {
			_logger.debug("Null page model can be update");
			return;
		}
		this.getCache().put(PAGE_MODEL_CACHE_NAME_PREFIX + pageModel.getCode(), pageModel);
	}

	@Override
	public void deletePageModel(String code) {
		Cache cache = this.getCache();
		cache.evict(PAGE_MODEL_CACHE_NAME_PREFIX + code);
		List<String> codes = cache.get(PAGE_MODEL_CODES_CACHE_NAME, List.class);
		codes.remove(code);
		cache.put(PAGE_MODEL_CODES_CACHE_NAME, codes);
	}

	protected Cache getCache() {
		return this.getSpringCacheManager().getCache(PAGE_MODEL_MANAGER_CACHE_NAME);
	}

	protected CacheManager getSpringCacheManager() {
		return _springCacheManager;
	}

	public void setSpringCacheManager(CacheManager springCacheManager) {
		this._springCacheManager = springCacheManager;
	}

}
