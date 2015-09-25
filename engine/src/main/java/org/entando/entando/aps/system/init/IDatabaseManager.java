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
package org.entando.entando.aps.system.init;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.init.model.DataSourceDumpReport;

import com.agiletec.aps.system.exception.ApsSystemException;
import javax.sql.DataSource;

/**
 * @author E.Santoboni
 */
public interface IDatabaseManager {
	
	public void createBackup() throws ApsSystemException;
	
	public void deleteBackup(String subFolderName) throws ApsSystemException;
	
	public int getStatus();
	
	public InputStream getTableDump(String tableName, String dataSourceName, String subFolderName) throws ApsSystemException;
	
	public boolean dropAndRestoreBackup(String subFolderName) throws ApsSystemException;
	
	public DataSourceDumpReport getBackupReport(String subFolderName) throws ApsSystemException;
	
	public List<DataSourceDumpReport> getBackupReports() throws ApsSystemException;
	
	public Map<String, List<String>> getEntandoTableMapping();
	
	public DatabaseType getDatabaseType(DataSource dataSource) throws ApsSystemException;
	
	public enum DatabaseType {DERBY, POSTGRESQL, MYSQL, ORACLE, SQLSERVER, UNKNOWN}
	
	public static final String DUMP_REPORT_FILE_NAME = "dumpReport.xml";
	
}
