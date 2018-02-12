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
package org.entando.entando.aps.system.services.dataobjectmodel.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.AbstractGenericCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

public class DataObjectModelCacheWrapper extends AbstractGenericCacheWrapper<DataObjectModel> implements IDataObjectModelCacheWrapper {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void initCache(IDataObjectModelDAO dataObjectModelDAO) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, DataObjectModel> modelsMap = this.getModelsMap(dataObjectModelDAO);
			super.insertObjectsOnCache(cache, modelsMap);
		} catch (Throwable t) {
			logger.error("Error bootstrapping data object models map cache", t);
			throw new ApsSystemException("Error bootstrapping data object models map cache", t);
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

	private Map<String, DataObjectModel> getModelsMap(IDataObjectModelDAO dataObjectModelDAO) {
		Map<Long, DataObjectModel> models = dataObjectModelDAO.loadDataModels();
		Map<String, DataObjectModel> modelsMap = new HashMap<>();
		for (Map.Entry<Long, DataObjectModel> entry : models.entrySet()) {
			modelsMap.put(entry.getKey().toString(), entry.getValue());
		}
		return modelsMap;
	}

	@Override
	public List<DataObjectModel> getModels() {
		Map<String, DataObjectModel> map = super.getObjectMap();
		return new ArrayList<>(map.values());
	}

	@Override
	public DataObjectModel getModel(String code) {
		return this.get(this.getCache(), CACHE_NAME_PREFIX + code, DataObjectModel.class);
	}

	@Override
	public void addModel(DataObjectModel model) {
		this.manage(String.valueOf(model.getId()), model, Action.ADD);
	}

	@Override
	public void updateModel(DataObjectModel model) {
		this.manage(String.valueOf(model.getId()), model, Action.UPDATE);
	}

	@Override
	public void removeModel(DataObjectModel model) {
		this.manage(String.valueOf(model.getId()), model, Action.DELETE);
	}

}
