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
	}

	@Override
	public SystemInstallationReport getReport() {
		return this.get(this.getCache(), INITIALIZER_REPORT_CACHE_NAME, SystemInstallationReport.class);
	}

	@Override
	public void setCurrentReport(SystemInstallationReport report) {
		this.getCache().put(INITIALIZER_REPORT_CACHE_NAME, report);

	}

}
