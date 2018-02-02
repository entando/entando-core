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

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IInitializerManagerCacheWrapper {

	public static final String INITIALIZER_MANAGER_CACHE_NAME = "Entando_InitializerManager";

	public static final String INITIALIZER_REPORT_CACHE_NAME = "I18nManager_report";

	public void initCache(SystemInstallationReport report) throws ApsSystemException;

	public SystemInstallationReport getReport();

	public void setCurrentReport(SystemInstallationReport report);

}
