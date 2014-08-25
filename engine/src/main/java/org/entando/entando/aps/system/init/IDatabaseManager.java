/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.init;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.init.model.DataSourceDumpReport;

import com.agiletec.aps.system.exception.ApsSystemException;

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
	
	public enum DatabaseType {DERBY, POSTGRESQL, MYSQL, ORACLE, SQLSERVER, UNKNOWN}
	
	public static final String DUMP_REPORT_FILE_NAME = "dumpReport.xml";
	
}
