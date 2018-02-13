/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init.cache;

import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.agiletec.aps.system.common.AbstractCacheWrapper;
import com.agiletec.aps.system.exception.ApsSystemException;

public class InitializerManagerCacheWrapper extends AbstractCacheWrapper implements IInitializerManagerCacheWrapper {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected String getCacheName() {
		return INITIALIZER_MANAGER_CACHE_NAME;
	}

	@Override
	public void initCache(SystemInstallationReport report) throws ApsSystemException {
		try {
			Cache cache = this.getCache();
			this.releaseCachedObjects(cache);
			this.insertObjectsOnCache(cache, report);
		} catch (Throwable t) {
			logger.error("Error bootstrapping InitializerManager cache", t);
			throw new ApsSystemException("Error bootstrapping InitializerManager cache", t);
		}
	}

	private void insertObjectsOnCache(Cache cache, SystemInstallationReport report) {
		cache.put(INITIALIZER_REPORT_CACHE_NAME, report);
	}

	protected void releaseCachedObjects(Cache cache) {
		cache.evict(INITIALIZER_REPORT_CACHE_NAME);
		logger.trace("report entry evicted");
	}

	@Override
	public SystemInstallationReport getReport() {
		return this.get(this.getCache(), INITIALIZER_REPORT_CACHE_NAME, SystemInstallationReport.class);
	}

	@Override
	public void setCurrentReport(SystemInstallationReport report) {
		this.getCache().put(INITIALIZER_REPORT_CACHE_NAME, report);
		logger.trace("report entry updated");
	}

}
