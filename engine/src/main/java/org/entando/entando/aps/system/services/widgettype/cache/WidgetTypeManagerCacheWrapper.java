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

import com.agiletec.aps.system.common.AbstractGenericCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeDAO;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author E.Santoboni
 */
public class WidgetTypeManagerCacheWrapper extends AbstractGenericCacheWrapper<WidgetType> implements IWidgetTypeManagerCacheWrapper {

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

	@Override
	public WidgetType getWidgetType(String code) {
		return this.get(WIDGET_TYPE_CACHE_NAME_PREFIX + code, WidgetType.class);
	}

	@Override
	public Collection<WidgetType> getWidgetTypes() {
		Map<String, WidgetType> map = super.getObjectMap();
		return map.values();
	}

	@Override
	public void addWidgetType(WidgetType type) {
		this.manage(type.getCode(), type, Action.ADD);
	}

	@Override
	public void updateWidgetType(WidgetType type) {
		this.manage(type.getCode(), type, Action.UPDATE);
	}

	@Override
	public void deleteWidgetType(String code) {
		this.manage(code, new WidgetType(), Action.DELETE);
	}

	@Override
	protected String getCodesCacheKey() {
		return WIDGET_TYPE_CODES_CACHE_NAME;
	}

	@Override
	protected String getCacheKeyPrefix() {
		return WIDGET_TYPE_CACHE_NAME_PREFIX;
	}

	@Override
	protected String getCacheName() {
		return WIDGET_TYPE_MANAGER_CACHE_NAME;
	}

}
