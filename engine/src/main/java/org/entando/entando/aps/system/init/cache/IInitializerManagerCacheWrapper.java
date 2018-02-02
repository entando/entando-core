package org.entando.entando.aps.system.init.cache;

import org.entando.entando.aps.system.init.model.SystemInstallationReport;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IInitializerManagerCacheWrapper {

	public static final String INITIALIZER_MANAGER_CACHE_NAME = "Entando_InitializerManager";

	public static final String INITIALIZER_REPORT_CACHE_NAME = "I18nManager_report";

	public void initCache(SystemInstallationReport report) throws ApsSystemException;

	public SystemInstallationReport getReport();

	public void setCurrentReport(SystemInstallationReport report);

}
