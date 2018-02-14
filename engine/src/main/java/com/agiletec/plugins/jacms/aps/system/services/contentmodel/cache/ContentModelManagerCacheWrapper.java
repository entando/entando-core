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
package com.agiletec.plugins.jacms.aps.system.services.contentmodel.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.AbstractGenericCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

public class ContentModelManagerCacheWrapper extends AbstractGenericCacheWrapper<ContentModel> implements IContentModelManagerCacheWrapper {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void initCache(IContentModelDAO contentModelDao) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, ContentModel> modelsMap = this.getModelsMap(contentModelDao);
			super.insertObjectsOnCache(cache, modelsMap);
		} catch (Throwable t) {
			logger.error("Error bootstrapping models map cache", t);
			throw new ApsSystemException("Error bootstrapping models map cache", t);
		}
	}

	@Override
	protected String getCacheName() {
		return CACHE_NAME;
	}

	@Override
	protected String getCodesCacheKey() {
		return CODES_CACHE_NAME;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_NAME_PREFIX;
	}

	private Map<String, ContentModel> getModelsMap(IContentModelDAO contentModelDao) {
		Map<Long, ContentModel> models = contentModelDao.loadContentModels();
		Map<String, ContentModel> modelsMap = new HashMap<>();
		for (Map.Entry<Long, ContentModel> entry : models.entrySet()) {
			modelsMap.put(entry.getKey().toString(), entry.getValue());
		}
		return modelsMap;
	}

	@Override
	public List<ContentModel> getContentModels() {
		Map<String, ContentModel> map = super.getObjectMap();
		return new ArrayList<>(map.values());
	}

	@Override
	public ContentModel getContentModel(String code) {
		return this.get(this.getCache(), CACHE_NAME_PREFIX + code, ContentModel.class);
	}

	@Override
	public void addContentModel(ContentModel contentModel) {
		this.manage(String.valueOf(contentModel.getId()), contentModel, Action.ADD);
	}

	@Override
	public void updateContentModel(ContentModel contentModel) {
		this.manage(String.valueOf(contentModel.getId()), contentModel, Action.UPDATE);
	}

	@Override
	public void removeContentModel(ContentModel contentModel) {
		this.manage(String.valueOf(contentModel.getId()), contentModel, Action.DELETE);
	}

}
