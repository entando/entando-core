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
package org.entando.entando.apsadmin.admin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.ComponentInstallationReport;
import org.entando.entando.aps.system.init.model.DataSourceDumpReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.j256.ormlite.table.DatabaseTable;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author E.Santoboni
 */
public class DatabaseAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DatabaseAction.class);
	
	public String executeBackup() {
		try {
			this.getDatabaseManager().createBackup();
			//TODO MESSAGE
		} catch (Throwable t) {
			_logger.error("error in executeBackup", t);
			//ApsSystemUtils.logThrowable(t, this, "executeBackup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<DataSourceDumpReport> getDumpReports() {
		try {
			return this.getDatabaseManager().getBackupReports();
		} catch (Throwable t) {
			_logger.error("Error extracting dump reports", t);
			//ApsSystemUtils.logThrowable(t, this, "getDumpReports");
			throw new RuntimeException("Error extracting dump reports", t);
		}
	}
	
	public DataSourceDumpReport getDumpReport(String subFolderName) {
		try {
			return this.getDatabaseManager().getBackupReport(subFolderName);
		} catch (Throwable t) {
			_logger.error("Error extracting report of subfolder {}", subFolderName, t);
			//ApsSystemUtils.logThrowable(t, this, "getDumpReport");
			throw new RuntimeException("Error extracting report of subfolder " + subFolderName, t);
		}
	}
	
	public String entryBackupDetails() {
		String check = this.checkBackupCode(this.getSubFolderName());
		if (null != check) {
			return check;
		}
		return SUCCESS;
	}
	
	public String redirectRestoreIntro() {
		try {
			String check = this.checkBackupCode(this.getSubFolderName());
			if (null != check) {
				return check;
			}
		} catch (Throwable t) {
			_logger.error("error in redirectRestoreIntro", t);
			//ApsSystemUtils.logThrowable(t, this, "redirectRestoreIntro");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String restoreBackup() {
		try {
			String check = this.checkBackupCode(this.getSubFolderName());
			if (null != check) {
				return check;
			}
			this.getDatabaseManager().dropAndRestoreBackup(this.getSubFolderName());
			this.addActionMessage(this.getText("message.restore.done"));
		} catch (Throwable t) {
			_logger.error("error in restoreBackup", t);
			//ApsSystemUtils.logThrowable(t, this, "restoreBackup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String extractTableDump() {
		try {
			String check = this.checkBackupCode(this.getSubFolderName());
			if (null != check) {
				return check;
			}
			InputStream stream = this.getDatabaseManager().getTableDump(this.getTableName(), this.getDataSourceName(), this.getSubFolderName());
			this.setInputStream(stream);
		} catch (Throwable t) {
			_logger.error("error in extractLastTableDump", t);
			//ApsSystemUtils.logThrowable(t, this, "extractLastTableDump");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String trashBackup() {
		try {
			String check = this.checkBackupCode(this.getSubFolderName());
			if (null != check) {
				return check;
			}
		} catch (Throwable t) {
			_logger.error("error in trashBackup", t);
			//ApsSystemUtils.logThrowable(t, this, "trashBackup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String deleteBackup() {
		try {
			String check = this.checkBackupCode(this.getSubFolderName());
			if (null != check) {
				return check;
			}
			this.getDatabaseManager().deleteBackup(this.getSubFolderName());
			String[] args = {this.getSubFolderName()};
			this.addActionMessage(this.getText("message.backup.deleteDone", args));
		} catch (Throwable t) {
			_logger.error("error in deleteBackup", t);
			//ApsSystemUtils.logThrowable(t, this, "deleteBackup");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected String checkBackupCode(String backupCode) {
		if (null == backupCode) {
			this.addFieldError("subFolderName", this.getText("error.backup.nullCode"));
			return INPUT;
		} else if (null == this.getDumpReport(backupCode)) {
			String[] args = {backupCode};
			this.addFieldError("subFolderName", this.getText("error.backup.invalidCode", args));
			return INPUT;
		}
		return null;
	}
	
	public int getManagerStatus() {
		return this.getDatabaseManager().getStatus();
	}
	
	public Map<String, List<String>> getEntandoTableMapping() {
		return this.getDatabaseManager().getEntandoTableMapping();
	}
	
	public List<Component> getCurrentComponents() {
		List<Component> components = null;
		try {
			components = this.getComponentManager().getCurrentComponents();
		} catch (Throwable t) {
			_logger.error("Error extracting current components", t);
			//ApsSystemUtils.logThrowable(t, this, "getCurrentComponents");
			throw new RuntimeException("Error extracting current components", t);
		}
		return components;
	}
	
	public List<String> getTableNames(List<String> tableClassNames) {
		if (null == tableClassNames || tableClassNames.isEmpty()) {
			return null;
		}
		List<String> tableNames = new ArrayList<String>();
		try {
			for (int i = 0; i < tableClassNames.size(); i++) {
				String tableClassName = tableClassNames.get(i);
				Class tableClass = Class.forName(tableClassName);
				DatabaseTable tableAnnotation = (DatabaseTable) tableClass.getAnnotation(DatabaseTable.class);
				tableNames.add(tableAnnotation.tableName());
			}
		} catch (Throwable t) {
			_logger.error("Error extracting table names", t);
			//ApsSystemUtils.logThrowable(t, this, "getTableNames");
			throw new RuntimeException("Error extracting table names", t);
		}
		return tableNames;
	}
	
	public boolean checkRestore(List<Component> currentComponents, DataSourceDumpReport report) {
		List<String> codes = new ArrayList<String>();
		codes.add("entandoCore");
		for (int i = 0; i < currentComponents.size(); i++) {
			Component component = currentComponents.get(i);
			codes.add(component.getCode());
		}
		List<ComponentInstallationReport> reports = report.getComponentsHistory();
		if (reports.size() != codes.size()) {
			return false;
		}
		for (int i = 0; i < reports.size(); i++) {
			ComponentInstallationReport componentReport = reports.get(i);
			if (!codes.contains(componentReport.getComponentCode())) {
				System.out.println(componentReport.getComponentCode() + " missing");
				return false;
			}
		}
		return true;
	}
	
	public String getSubFolderName() {
		return _subFolderName;
	}
	public void setSubFolderName(String subFolderName) {
		this._subFolderName = subFolderName;
	}
	
	public String getDataSourceName() {
		return _dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this._dataSourceName = dataSourceName;
	}
	
	public String getTableName() {
		return _tableName;
	}
	public void setTableName(String tableName) {
		this._tableName = tableName;
	}
	
	public InputStream getInputStream() {
		return _inputStream;
	}
	protected void setInputStream(InputStream inputStream) {
		this._inputStream = inputStream;
	}
	
	protected IDatabaseManager getDatabaseManager() {
		return _databaseManager;
	}
	public void setDatabaseManager(IDatabaseManager databaseManager) {
		this._databaseManager = databaseManager;
	}
	
	protected IComponentManager getComponentManager() {
		return _componentManager;
	}
	public void setComponentManager(IComponentManager componentManager) {
		this._componentManager = componentManager;
	}
	
	private String _subFolderName;
	
	private String _tableName;
	private String _dataSourceName;
	private InputStream _inputStream;
	private IDatabaseManager _databaseManager;
	private IComponentManager _componentManager;
	
}
