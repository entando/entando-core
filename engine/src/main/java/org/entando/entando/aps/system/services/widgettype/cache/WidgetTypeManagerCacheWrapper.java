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
package org.entando.entando.aps.system.services.widgettype.cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeDAO;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class WidgetTypeManagerCacheWrapper extends AbstractCacheWrapper implements IWidgetTypeManagerCacheWrapper {

	private static final Logger _logger = LoggerFactory.getLogger(WidgetTypeManagerCacheWrapper.class);

	@Override
	public void initCache(IWidgetTypeDAO widgetTypeDAO) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			Map<String, WidgetType> widgetTypes = widgetTypeDAO.loadWidgetTypes();
			Iterator<WidgetType> iter = widgetTypes.values().iterator();
			while (iter.hasNext()) {
				WidgetType type = iter.next();
				String mainTypeCode = type.getParentTypeCode();
				if (null != mainTypeCode) {
					type.setParentType(widgetTypes.get(mainTypeCode));
				}
			}
			this.insertObjectsOnCache(cache, widgetTypes);
		} catch (Throwable t) {
			_logger.error("Error loading widgets types", t);
			throw new ApsSystemException("Error loading widgets types", t);
		}
	}

	protected void releaseCachedObjects(Cache cache) {
		List<String> codes = (List<String>) this.get(cache, WIDGET_TYPE_CODES_CACHE_NAME, List.class);
		if (null != codes) {
			for (int i = 0; i < codes.size(); i++) {
				String code = codes.get(i);
				cache.evict(WIDGET_TYPE_CACHE_NAME_PREFIX + code);
			}
			cache.evict(WIDGET_TYPE_CODES_CACHE_NAME);
		}
	}

	protected void insertObjectsOnCache(Cache cache, Map<String, WidgetType> widgetTypes) {
		List<String> widgetCodes = new ArrayList<String>();
		Iterator<WidgetType> iter = widgetTypes.values().iterator();
		while (iter.hasNext()) {
			WidgetType type = iter.next();
			cache.put(WIDGET_TYPE_CACHE_NAME_PREFIX + type.getCode(), type);
			widgetCodes.add(type.getCode());
		}
		cache.put(WIDGET_TYPE_CODES_CACHE_NAME, widgetCodes);
	}

	@Override
	public WidgetType getWidgetType(String code) {
		return this.get(WIDGET_TYPE_CACHE_NAME_PREFIX + code, WidgetType.class);
	}

	@Override
	public Collection<WidgetType> getWidgetTypes() {
		List<WidgetType> types = new ArrayList<WidgetType>();
		Cache cache = this.getCache();
		List<String> codes = (List<String>) this.get(cache, WIDGET_TYPE_CODES_CACHE_NAME, List.class);
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i);
			types.add(this.get(cache, WIDGET_TYPE_CACHE_NAME_PREFIX + code, WidgetType.class));
		}
		return types;
	}

	@Override
	public void addWidgetType(WidgetType type) {
		if (null == type) {
			_logger.debug("Null widget type can be add");
			return;
		}
		Cache cache = this.getCache();
		cache.put(WIDGET_TYPE_CACHE_NAME_PREFIX + type.getCode(), type);
		List<String> codes = (List<String>) this.get(cache, WIDGET_TYPE_CODES_CACHE_NAME, List.class);
		codes.add(type.getCode());
		cache.put(WIDGET_TYPE_CODES_CACHE_NAME, codes);
	}

	@Override
	public void updateWidgetType(WidgetType type) {
		if (null == type) {
			_logger.debug("Null widget type can be update");
			return;
		}
		this.getCache().put(WIDGET_TYPE_CACHE_NAME_PREFIX + type.getCode(), type);
	}

	@Override
	public void deleteWidgetType(String code) {
		Cache cache = this.getCache();
		cache.evict(WIDGET_TYPE_CACHE_NAME_PREFIX + code);
		List<String> codes = (List<String>) this.get(cache, WIDGET_TYPE_CODES_CACHE_NAME, List.class);
		codes.remove(code);
		cache.put(WIDGET_TYPE_CODES_CACHE_NAME, codes);
	}

	@Override
	protected String getCacheName() {
		return WIDGET_TYPE_MANAGER_CACHE_NAME;
	}

}
