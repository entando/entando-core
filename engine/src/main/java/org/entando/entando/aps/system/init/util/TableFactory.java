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
package org.entando.entando.aps.system.init.util;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.DataSourceInstallationReport;
import org.entando.entando.aps.system.init.model.ExtendedColumnDefinition;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.db.MysqlDatabaseType;
import com.j256.ormlite.db.PostgresDatabaseType;
import com.j256.ormlite.db.SqlServerDatabaseType;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

/**
 * @author E.Santoboni
 */
public class TableFactory {

	private static final Logger _logger = LoggerFactory.getLogger(TableFactory.class);
	
	public TableFactory(String databaseName, DataSource dataSource, IDatabaseManager.DatabaseType type) {
		this.setDataSource(dataSource);
		this.setDatabaseName(databaseName);
		this.setType(type);
	}
	
	public void createTables(List<String> tableClassNames, DataSourceInstallationReport schemaReport) throws ApsSystemException {
		ConnectionSource connectionSource = null;
		try {
			connectionSource = this.createConnectionSource();
			this.createTables(tableClassNames, connectionSource, schemaReport);
		} catch (Throwable t) {
			_logger.error("Error creating tables to db {}", this.getDatabaseName(), t);
			//ApsSystemUtils.logThrowable(t, this, "createTables", "Error creating tables into db " + this.getDatabaseName());
			throw new ApsSystemException("Error creating tables to db " + this.getDatabaseName(), t);
		} finally {
			if (connectionSource != null) {
				try {
					connectionSource.close();
				} catch (SQLException ex) {}
			}
		}
	}

	private ConnectionSource createConnectionSource() throws ApsSystemException {
		ConnectionSource connectionSource = null;
		try {
			DataSource dataSource = this.getDataSource();
			IDatabaseManager.DatabaseType type = this.getType();
			String url = this.invokeGetMethod("getUrl", dataSource);
			String username = this.invokeGetMethod("getUsername", dataSource);
			String password = this.invokeGetMethod("getPassword", dataSource);
			com.j256.ormlite.db.DatabaseType dataType = null;
			if (type.equals(IDatabaseManager.DatabaseType.DERBY)) {
				dataType = new ApsDerbyEmbeddedDatabaseType();
				url = url + ";user=" + username + ";password=" + password;
				connectionSource = new JdbcConnectionSource(url, dataType);
			} else {
				if (type.equals(IDatabaseManager.DatabaseType.POSTGRESQL)) {
					dataType = new PostgresDatabaseType();
				} else if (type.equals(IDatabaseManager.DatabaseType.MYSQL)) {
					dataType = new MysqlDatabaseType();
				} else if (type.equals(IDatabaseManager.DatabaseType.ORACLE)) {
					dataType = new ApsOracleDatabaseType();
				} else if (type.equals(IDatabaseManager.DatabaseType.SQLSERVER)) {
					dataType = new SqlServerDatabaseType();
				}
				connectionSource = new JdbcConnectionSource(url, username, password, dataType);
			}
		} catch (Throwable t) {
			_logger.error("Error creating connectionSource to db {}", this.getDatabaseName(), t);
			//ApsSystemUtils.logThrowable(t, this, "createConnectionSource", "Error creating connectionSource to db " + this.getDatabaseName());
			throw new ApsSystemException("Error creating connectionSource to db " + this.getDatabaseName(), t);
		}
		return connectionSource;
	}

	private String invokeGetMethod(String methodName, DataSource dataSource) throws Throwable {
		Method method = dataSource.getClass().getDeclaredMethod(methodName);
		return (String) method.invoke(dataSource);
	}

	private void createTables(List<String> tableClassNames,
			ConnectionSource connectionSource, DataSourceInstallationReport schemaReport) throws ApsSystemException {
		try {
			List<String> tables = schemaReport.getDataSourceTables().get(this.getDatabaseName());
			if (null == tables) {
				tables = new ArrayList<String>();
				schemaReport.getDataSourceTables().put(this.getDatabaseName(), tables);
			}
			for (int i = 0; i < tableClassNames.size(); i++) {
				String tableClassName = tableClassNames.get(i);
				Class tableClass = Class.forName(tableClassName);
				String tableName = getTableName(tableClass);
				if (tables.contains(tableName)) {
					continue;
				}
				try {
					System.out.println("|   ( ok )  " + this.getDatabaseName() + "." + tableName);
					this.createTable(tableClass, connectionSource);
					//if (!tables.contains(tableName)) {
					tables.add(tableName);
					//System.out.println("DONE!!!");
					//}
				} catch (Throwable t) {
					schemaReport.getDatabaseStatus().put(this.getDatabaseName(), SystemInstallationReport.Status.INCOMPLETE);
					String message = "Error creating table " + this.getDatabaseName() + "/" + tableClassName + " - " + t.getMessage();
					_logger.error("Error creating table {}/{}",this.getDatabaseName(), tableClassName, t);
					//ApsSystemUtils.logThrowable(t, this, "createTables", message);
					throw new ApsSystemException(message, t);
				}
			}
		} catch (Throwable t) {
			schemaReport.getDatabaseStatus().put(this.getDatabaseName(), SystemInstallationReport.Status.INCOMPLETE);
			_logger.error("Error on setup Database - {}", this.getDatabaseName(), t);
			//ApsSystemUtils.logThrowable(t, this, "setupDatabase", "Error on setup Database - " + this.getDatabaseName());
			throw new ApsSystemException("Error on setup Database", t);
		}
	}

	private void createTable(Class tableClass, ConnectionSource connectionSource) throws Throwable {
		int result = 0;
		String logTableName = this.getDatabaseName() + "/" + getTableName(tableClass);
		try {
			result = ApsTableUtils.createTable(connectionSource, tableClass);
			if (result > 0) {
				_logger.info("Created table - {}", logTableName);
				Object tableModel = tableClass.newInstance();
				if (tableModel instanceof ExtendedColumnDefinition) {
					String[] extensions = ((ExtendedColumnDefinition) tableModel).extensions(this.getType());
					if (null != extensions && extensions.length > 0) {
						Dao dao = DaoManager.createDao(connectionSource, tableClass);
						for (int i = 0; i < extensions.length; i++) {
							String query = extensions[i];
							dao.executeRaw(query);
						}
					}
				}
			} else {
				throw new RuntimeException("Error creating table from class " + logTableName);
			}
		} catch (Throwable t) {
			_logger.error("Error creating table {}", logTableName, t);
			//ApsSystemUtils.logThrowable(t, this, "setupDatabase", "Error creating table " + logTableName + " - " + t.getMessage());
			if (result > 0) {
				TableUtils.dropTable(connectionSource, tableClass, true);
			}
			throw new ApsSystemException("Error creating table " + logTableName, t);
		}
	}

	public static String getTableName(Class tableClass) {
		DatabaseTable tableAnnotation = (DatabaseTable) tableClass.getAnnotation(DatabaseTable.class);
		return tableAnnotation.tableName();
	}

	protected DataSource getDataSource() {
		return _dataSource;
	}
	protected void setDataSource(DataSource dataSource) {
		this._dataSource = dataSource;
	}

	protected String getDatabaseName() {
		return _databaseName;
	}
	protected void setDatabaseName(String databaseName) {
		this._databaseName = databaseName;
	}

	protected IDatabaseManager.DatabaseType getType() {
		return _type;
	}
	protected void setType(IDatabaseManager.DatabaseType type) {
		this._type = type;
	}
	
	private String _databaseName;
	private DataSource _dataSource;
	private IDatabaseManager.DatabaseType _type;
	
}
