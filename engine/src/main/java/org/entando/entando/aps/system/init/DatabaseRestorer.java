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

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.util.TableDataUtils;
import org.entando.entando.aps.system.init.util.TableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;

/**
 * @author E.Santoboni
 */
public class DatabaseRestorer extends AbstractDatabaseUtils {

	private static final Logger _logger = LoggerFactory.getLogger(DatabaseRestorer.class);
	
	protected void initOracleSchema(DataSource dataSource) throws Throwable {
		IDatabaseManager.DatabaseType type = this.getType(dataSource);
		try {
			if (!type.equals(IDatabaseManager.DatabaseType.ORACLE)) {
				return;
			}
			String[] queryTimestampFormat = new String[]{"ALTER SESSION SET NLS_TIMESTAMP_FORMAT = 'YYYY-MM-DD HH24:MI:SS.FF'"};
			TableDataUtils.executeQueries(dataSource, queryTimestampFormat, false);
		} catch (Throwable t) {
			_logger.error("Error initializing oracle schema ", t);
			//ApsSystemUtils.getLogger().info("Error initializing oracle schema - " + t.getMessage());
			throw new ApsSystemException("Error initializing oracle schema", t);
		}
	}
	
	protected void initDerbySchema(DataSource dataSource) throws Throwable {
		String username = this.invokeGetMethod("getUsername", dataSource);
		try {
			String[] queryCreateSchema = new String[]{"CREATE SCHEMA " + username.toUpperCase()};
			TableDataUtils.executeQueries(dataSource, queryCreateSchema, false);
		} catch (Throwable t) {
			_logger.info("Error creating derby schema" + t);
			throw new ApsSystemException("Error creating derby schema", t);
		}
		try {
			String[] initSchemaQuery = new String[]{"SET SCHEMA \"" + username.toUpperCase() + "\""};
			TableDataUtils.executeQueries(dataSource, initSchemaQuery, true);
		} catch (Throwable t) {
			_logger.error("Error initializating Derby Schema", t);
			//ApsSystemUtils.logThrowable(t, this, "initDerbySchema", "Error initializating Derby Schema");
			throw new ApsSystemException("Error initializating Derby Schema", t);
		}
	}
	
	protected void dropAndRestoreBackup(String backupSubFolder) throws ApsSystemException {
		try {
			List<Component> components = this.getComponents();
			int size = components.size();
			for (int i = 0; i < components.size(); i++) {
				Component componentConfiguration = components.get(size - i - 1);
				this.dropTables(componentConfiguration.getTableMapping());
			}
			this.dropTables(this.getEntandoTableMapping());
			this.restoreBackup(backupSubFolder);
		} catch (Throwable t) {
			_logger.error("Error while restoring backup: {}", backupSubFolder, t);
			//ApsSystemUtils.logThrowable(t, this, "dropAndRestoreBackup");
			throw new ApsSystemException("Error while restoring backup", t);
		}
	}
	
	private void dropTables(Map<String, List<String>> tableMapping) throws ApsSystemException {
		if (null == tableMapping) {
			return;
		}
		try {
			String[] dataSourceNames = this.extractBeanNames(DataSource.class);
			for (int i = 0; i < dataSourceNames.length; i++) {
				String dataSourceName = dataSourceNames[i];
				List<String> tableClasses = tableMapping.get(dataSourceName);
				if (null == tableClasses || tableClasses.isEmpty()) continue;
				DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
				int size = tableClasses.size();
				for (int j = 0; j < tableClasses.size(); j++) {
					String tableClassName = tableClasses.get(size - j - 1);
					Class tableClass = Class.forName(tableClassName);
					String tableName = TableFactory.getTableName(tableClass);
					String[] queries = {"DELETE FROM " + tableName};
					TableDataUtils.executeQueries(dataSource, queries, true);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while dropping tables", t);
			//ApsSystemUtils.logThrowable(t, this, "dropTables");
			throw new RuntimeException("Error while dropping tables", t);
		}
	}
	
	protected void restoreBackup(String backupSubFolder) throws ApsSystemException {
		try {
			this.restoreLocalDump(this.getEntandoTableMapping(), backupSubFolder);
			List<Component> components = this.getComponents();
			for (int i = 0; i < components.size(); i++) {
				Component componentConfiguration = components.get(i);
				this.restoreLocalDump(componentConfiguration.getTableMapping(), backupSubFolder);
			}
		} catch (Throwable t) {
			_logger.error("Error while restoring local backup", t);
			//ApsSystemUtils.logThrowable(t, this, "restoreBackup");
			throw new ApsSystemException("Error while restoring local backup", t);
		}
	}
	
	private void restoreLocalDump(Map<String, List<String>> tableMapping, String backupSubFolder) throws ApsSystemException {
		if (null == tableMapping) {
			return;
		}
		try {
			StringBuilder folder = new StringBuilder(this.getLocalBackupsFolder())
					.append(backupSubFolder).append(File.separator);
			String[] dataSourceNames = this.extractBeanNames(DataSource.class);
			for (int i = 0; i < dataSourceNames.length; i++) {
				String dataSourceName = dataSourceNames[i];
				List<String> tableClasses = tableMapping.get(dataSourceName);
				if (null == tableClasses || tableClasses.isEmpty()) {
					continue;
				}
				DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
				this.initOracleSchema(dataSource);
				for (int j = 0; j < tableClasses.size(); j++) {
					String tableClassName = tableClasses.get(j);
					Class tableClass = Class.forName(tableClassName);
					String tableName = TableFactory.getTableName(tableClass);
					String fileName = folder.toString() + dataSourceName + File.separator + tableName + ".sql";
					InputStream is = this.getStorageManager().getStream(fileName, true);
					if (null != is) {
						String sqlDump = FileTextReader.getText(is);
						TableDataUtils.valueDatabase(sqlDump, tableName, dataSource, null);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while restoring local dump", t);
			//ApsSystemUtils.logThrowable(t, this, "restoreLocalDump");
			throw new RuntimeException("Error while restoring local dump", t);
		}
	}
	
}
