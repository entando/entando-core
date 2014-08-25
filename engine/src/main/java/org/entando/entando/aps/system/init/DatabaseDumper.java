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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.DataSourceDumpReport;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.init.model.TableDumpResult;
import org.entando.entando.aps.system.init.util.TableDataUtils;
import org.entando.entando.aps.system.init.util.TableFactory;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;

/**
 * @author E.Santoboni
 */
public class DatabaseDumper extends AbstractDatabaseUtils {

	private static final Logger _logger = LoggerFactory.getLogger(DatabaseDumper.class);
	
	protected void createBackup(AbstractInitializerManager.Environment environment, SystemInstallationReport installationReport) throws ApsSystemException {
		try {
			DataSourceDumpReport report = new DataSourceDumpReport(installationReport);
			long start = System.currentTimeMillis();
			String backupSubFolder = (AbstractInitializerManager.Environment.develop.equals(environment)) ?
					environment.toString() : DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
					//this.setBackupSubFolder(subFolder);
					report.setSubFolderName(backupSubFolder);
					List<Component> components = this.getComponents();
					for (int i = 0; i < components.size(); i++) {
						Component componentConfiguration = components.get(i);
						this.createBackup(componentConfiguration.getTableMapping(), report, backupSubFolder);
					}
					this.createBackup(this.getEntandoTableMapping(), report, backupSubFolder);
					long time = System.currentTimeMillis() - start;
					report.setRequiredTime(time);
					report.setDate(new Date());
					StringBuilder reportFolder = new StringBuilder(this.getLocalBackupsFolder());
					if (null != backupSubFolder) {
						reportFolder.append(backupSubFolder).append(File.separator);
					}
					this.save(DatabaseManager.DUMP_REPORT_FILE_NAME,
							reportFolder.toString(), report.toXml());
		} catch (Throwable t) {
			_logger.error("error in ", t);
			//ApsSystemUtils.logThrowable(t, this, "Error while creating backup");
			throw new ApsSystemException("Error while creating backup", t);
		}
	}

	private void createBackup(Map<String, List<String>> tableMapping, DataSourceDumpReport report, String backupSubFolder) throws ApsSystemException {
		if (null == tableMapping || tableMapping.isEmpty()) {
			return;
		}
		try {
			String[] dataSourceNames = this.extractBeanNames(DataSource.class);
			for (int j = 0; j < dataSourceNames.length; j++) {
				String dataSourceName = dataSourceNames[j];
				List<String> tableClassNames = tableMapping.get(dataSourceName);
				if (null == tableClassNames || tableClassNames.isEmpty()) continue;
				DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
				for (int k = 0; k < tableClassNames.size(); k++) {
					String tableClassName = tableClassNames.get(k);
					Class tableClass = Class.forName(tableClassName);
					String tableName = TableFactory.getTableName(tableClass);
					this.dumpTableData(tableName, dataSourceName, dataSource, report, backupSubFolder);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while creating backup", t);
			//ApsSystemUtils.logThrowable(t, this, "createBackup");
			throw new ApsSystemException("Error while creating backup", t);
		}
	}

	protected void dumpTableData(String tableName, String dataSourceName,
			DataSource dataSource, DataSourceDumpReport report, String backupSubFolder) throws ApsSystemException {
		try {
			TableDumpResult tableDumpResult = TableDataUtils.dumpTable(dataSource, tableName);
			report.addTableReport(dataSourceName, tableDumpResult);
			StringBuilder dirName = new StringBuilder(this.getLocalBackupsFolder());
			if (null != backupSubFolder) {
				dirName.append(backupSubFolder).append(File.separator);
			}
			dirName.append(dataSourceName).append(File.separator);
			this.save(tableName + ".sql", dirName.toString(), tableDumpResult.getSqlDump());
		} catch (Throwable t) {
			_logger.error("Error dumping table '{}' - datasource '{}'", tableName, dataSourceName, t);
			//ApsSystemUtils.logThrowable(t, this, "dumpTableData");
			throw new ApsSystemException("Error dumping table '" + tableName + "' - datasource '" + dataSourceName + "'", t);
		}
	}

	protected void save(String filename, String folder, String content) throws ApsSystemException {
		try {
			IStorageManager storageManager = this.getStorageManager();
			String path = folder + filename;
			ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
			storageManager.saveFile(path, true, bais);
		} catch (Throwable t) {
			_logger.error("Error  save backup '{}'", filename, t);
			//ApsSystemUtils.logThrowable(t, this, "save");
			throw new ApsSystemException("Error  save backup '" + filename , t);
		}
	}

}