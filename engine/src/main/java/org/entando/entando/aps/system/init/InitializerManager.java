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

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.ComponentEnvironment;
import org.entando.entando.aps.system.init.model.ComponentInstallationReport;
import org.entando.entando.aps.system.init.model.IPostProcess;
import org.entando.entando.aps.system.init.model.InvalidPostProcessResultException;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class InitializerManager extends AbstractInitializerManager {

	private static final Logger _logger = LoggerFactory.getLogger(InitializerManager.class);
	
	public void init() throws Exception {
		SystemInstallationReport report = null;
		try {
			report = this.extractReport();
			report = ((IDatabaseInstallerManager) this.getDatabaseManager()).installDatabase(report, this.isCheckOnStartup());
		} catch (Throwable t) {
			_logger.error("Error while initializating Db Installer", t);
			//ApsSystemUtils.logThrowable(t, this, "init", "Error while initializating Db Installer");
			throw new Exception("Error while initializating Db Installer", t);
		} finally {
			if (null != report && report.isUpdated()) {
				this.saveReport(report);
			}
		}
		_logger.debug("{}: initializated - Check on startup {}", this.getClass().getName(), this.isCheckOnStartup());
	}
	
	public void executePostInitProcesses() throws BeansException {
		SystemInstallationReport report = null;
		try {
			report = this.extractReport();
			List<Component> components = this.getComponentManager().getCurrentComponents();
			for (int i = 0; i < components.size(); i++) {
				Component component = components.get(i);
				ComponentInstallationReport componentReport = report.getComponentReport(component.getCode(), false);
				SystemInstallationReport.Status postProcessStatus = componentReport.getPostProcessStatus();
				if (!postProcessStatus.equals(SystemInstallationReport.Status.INIT)) {
					continue;
				}
				String compEnvKey = (AbstractInitializerManager.Environment.test.equals(this.getEnvironment())) 
						? AbstractInitializerManager.Environment.test.toString() : AbstractInitializerManager.Environment.production.toString();
				ComponentEnvironment componentEnvironment = (null != component.getEnvironments()) ? 
						component.getEnvironments().get(compEnvKey) :
						null;
				List<IPostProcess> postProcesses = (null != componentEnvironment) ? componentEnvironment.getPostProcesses() : null;
				if (null == postProcesses || postProcesses.isEmpty()) {
					postProcessStatus = SystemInstallationReport.Status.NOT_AVAILABLE;
				} else if (!this.isCheckOnStartup()) {
					postProcessStatus = SystemInstallationReport.Status.SKIPPED;
				} else if (!componentReport.isPostProcessExecutionRequired()) {
					//Porting or restore
					postProcessStatus = SystemInstallationReport.Status.NOT_AVAILABLE;
				} else {
					postProcessStatus = this.executePostProcesses(postProcesses);
				}
				componentReport.setPostProcessStatus(postProcessStatus);
				report.setUpdated();
			}
		} catch (Throwable t) {
			_logger.error("Error while executing post processes", t);
			//ApsSystemUtils.logThrowable(t, this, "executePostInitProcesses", "Error while executing post processes");
			throw new FatalBeanException("Error while executing post processes", t);
		} finally {
			if (null != report && report.isUpdated()) {
				this.saveReport(report);
			}
		}
	}
	
	protected SystemInstallationReport.Status executePostProcesses(List<IPostProcess> postProcesses) throws ApsSystemException {
		if (null == postProcesses || postProcesses.isEmpty()) {
			return SystemInstallationReport.Status.NOT_AVAILABLE;
		}
		for (int i = 0; i < postProcesses.size(); i++) {
			IPostProcess postProcess = postProcesses.get(i);
			try {
				IPostProcessor postProcessor = this.getPostProcessors().get(postProcess.getCode());
				if (null != postProcessor) {
					postProcessor.executePostProcess(postProcess);
				} else {
					_logger.error("Missing Post Processor for process '{}'", postProcess.getCode());
					//ApsSystemUtils.getLogger().error("Missing Post Processor for process '" + postProcess.getCode() + "'");
				}
			} catch (InvalidPostProcessResultException t) {
				_logger.error("Error while executing post process of index {}",i, t);
				//ApsSystemUtils.logThrowable(t, this, "executePostProcess", "Error while executing post process of index " + i + " - " + t.getMessage());
				return SystemInstallationReport.Status.INCOMPLETE;
			} catch (Throwable t) {
				_logger.error("Error while executing post process - index {}", i, t);
				//ApsSystemUtils.logThrowable(t, this, "executePostProcesses", "Error while executing post process - index " + i);
				return SystemInstallationReport.Status.INCOMPLETE;
			}
		}
		return SystemInstallationReport.Status.OK;
	}
	
	//-------------------- REPORT -------- START
	
	private void saveReport(SystemInstallationReport report) throws BeansException {
		if (null == report || report.getReports().isEmpty()) {
			return;
		}
		try {
			InstallationReportDAO dao = new InstallationReportDAO();
			DataSource dataSource = (DataSource) this.getBeanFactory().getBean("portDataSource");
			dao.setDataSource(dataSource);
			dao.saveConfigItem(report.toXml(), this.getConfigVersion());
		} catch (Throwable t) {
			_logger.error("Error saving report", t);
			//ApsSystemUtils.logThrowable(t, this, "saveReport");
			throw new FatalBeanException("Error saving report", t);
		}
	}
	
	protected boolean isCheckOnStartup() {
		return _checkOnStartup;
	}
	public void setCheckOnStartup(boolean checkOnStartup) {
		this._checkOnStartup = checkOnStartup;
	}
	
	public Map<String, IPostProcessor> getPostProcessors() {
		return _postProcessors;
	}
	public void setPostProcessors(Map<String, IPostProcessor> postProcessors) {
		this._postProcessors = postProcessors;
	}
	
	protected IComponentManager getComponentManager() {
		return _componentManager;
	}
	public void setComponentManager(IComponentManager componentManager) {
		this._componentManager = componentManager;
	}
	
	protected IDatabaseManager getDatabaseManager() {
		return _databaseManager;
	}
	public void setDatabaseManager(IDatabaseManager databaseManager) {
		this._databaseManager = databaseManager;
	}
	
	private boolean _checkOnStartup;
	
	private Map<String, IPostProcessor> _postProcessors;
	
	private IComponentManager _componentManager;
	private IDatabaseManager _databaseManager;
	
	public static final String REPORT_CONFIG_ITEM = "entandoComponentsReport";
	
}
