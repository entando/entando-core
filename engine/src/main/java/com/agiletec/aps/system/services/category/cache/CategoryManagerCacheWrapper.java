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
package com.agiletec.aps.system.services.category.cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryDAO;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class CategoryManagerCacheWrapper extends AbstractCacheWrapper implements ICategoryManagerCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(CategoryManagerCacheWrapper.class);

	@Override
	public void initCache(ICategoryDAO categoryDAO, ILangManager langManager) throws ApsSystemException {
		List<Category> categories = null;
		try {
			categories = categoryDAO.loadCategories(langManager);
			if (categories.isEmpty()) {
				Category root = this.createRoot(langManager);
				categoryDAO.addCategory(root);
				categories.add(root);
			}
			this.initCache(categories);
		} catch (Throwable t) {
			_logger.error("Error loading the category tree", t);
			throw new ApsSystemException("Error loading the category tree.", t);
		}
	}
	
	protected Category createRoot(ILangManager langManager) {
		Category root = new Category();
		root.setCode("home");
		root.setParentCode("home");
		List<Lang> langs = langManager.getLangs();
		ApsProperties titles = new ApsProperties();
		for (int i = 0; i < langs.size(); i++) {
			Lang lang = (Lang) langs.get(i);
			titles.setProperty(lang.getCode(), "Home");
		}
		root.setTitles(titles);
		return root;
	}
	
	private void initCache(List<Category> categories) throws ApsSystemException {
		Cache cache = this.getCache();
		this.releaseCachedObjects(cache);
		Category root = null;
		Map<String, Category> categoryMap = new HashMap<String, Category>();
		for (int i = 0; i < categories.size(); i++) {
			Category cat = (Category) categories.get(i);
			categoryMap.put(cat.getCode(), cat);
			if (cat.getCode().equals(cat.getParentCode())) {
				root = cat;
			}
		}
		for (int i = 0; i < categories.size(); i++) {
			Category cat = categories.get(i);
			Category parent = categoryMap.get(cat.getParentCode());
			if (cat != root) {
				parent.addChildCode(cat.getCode());
			}
			cat.setParent(parent);
		}
		if (root == null) {
			throw new ApsSystemException("Error found in the category tree: undefined root");
		}
		this.insertObjectsOnCache(cache, root, categoryMap);
	}
	
	protected void releaseCachedObjects(Cache cache) {
		List<String> codes = (List<String>) this.get(cache, CATEGORY_CODES_CACHE_NAME, List.class);
		if (null != codes) {
			for (int i = 0; i < codes.size(); i++) {
				String code = codes.get(i);
				cache.evict(CATEGORY_CACHE_NAME_PREFIX + code);
			}
			cache.evict(CATEGORY_CODES_CACHE_NAME);
		}
	}
	
	protected void insertObjectsOnCache(Cache cache, Category root, Map<String, Category> categoryMap) {
		cache.put(CATEGORY_ROOT_CACHE_NAME, root);
		Iterator<Category> iter = categoryMap.values().iterator();
		while (iter.hasNext()) {
			Category category = iter.next();
			cache.put(CATEGORY_CACHE_NAME_PREFIX + category.getCode(), category);
		}
	}

	@Override
	public Category getCategory(String code) {
		return this.get(CATEGORY_CACHE_NAME_PREFIX + code, Category.class);
	}
	
	@Override
	public void deleteCategory(String code) {
		this.getCache().evict(CATEGORY_CACHE_NAME_PREFIX + code);
	}

	@Override
	public Category getRoot() {
		return this.get(CATEGORY_ROOT_CACHE_NAME, Category.class);
	}

	@Override
	protected String getCacheName() {
		return CATEGORY_MANAGER_CACHE_NAME;
	}
	
}
