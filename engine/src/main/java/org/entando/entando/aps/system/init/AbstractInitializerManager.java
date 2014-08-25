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

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public abstract class AbstractInitializerManager implements BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractInitializerManager.class);
	
	protected SystemInstallationReport extractReport() throws ApsSystemException {
		SystemInstallationReport report = null;
		try {
			InstallationReportDAO dao = new InstallationReportDAO();
			DataSource dataSource = (DataSource) this.getBeanFactory().getBean("portDataSource");
			dao.setDataSource(dataSource);
			report = dao.loadReport(this.getConfigVersion());
		} catch (Throwable t) {
			_logger.error("error Error extracting report", t);
			//ApsSystemUtils.logThrowable(t, this, "Error extracting report");
			throw new ApsSystemException("Error extracting report", t);
		}
		return report;
	}
	
	protected String getConfigVersion() {
		return _configVersion;
	}
	public void setConfigVersion(String configVersion) {
		this._configVersion = configVersion;
	}
	
	protected Environment getEnvironment() {
		return _environment;
	}
	public void setEnvironment(Environment environment) {
		this._environment = environment;
	}
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this._beanFactory = beanFactory;
	}
	
	private String _configVersion;
	private Environment _environment = Environment.production;
	
	private BeanFactory _beanFactory;
	
	public enum Environment {test, develop, production}
	
}
