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
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.util.TableDataUtils;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public abstract class AbstractDatabaseUtils implements BeanFactoryAware {

	private static final Logger _logger = LoggerFactory.getLogger(AbstractDatabaseUtils.class);
	
	protected IDatabaseManager.DatabaseType getType(DataSource dataSource) throws ApsSystemException {
		String typeString = null;
		try {
			String driverClassName = this.invokeGetMethod("getDriverClassName", dataSource);
			Iterator<Object> typesIter = this.getDatabaseTypeDrivers().keySet().iterator();
			while (typesIter.hasNext()) {
				String typeCode = (String) typesIter.next();
				List<String> driverClassNames = (List<String>) this.getDatabaseTypeDrivers().get(typeCode);
				if (null != driverClassNames && driverClassNames.contains(driverClassName)) {
					typeString = typeCode;
					break;
				}
			}
			if (null == typeString) {
				_logger.error("Type not recognized for Driver '{}' - Recognized types '{}'", driverClassName, IDatabaseManager.DatabaseType.values());
				return IDatabaseManager.DatabaseType.UNKNOWN;
			}
			return Enum.valueOf(IDatabaseManager.DatabaseType.class, typeString.toUpperCase());
		} catch (Throwable t) {
			_logger.error("Invalid type for db - '{}' - ", typeString, t);
			throw new ApsSystemException("Invalid type for db - '" + typeString + "'", t);
		}
	}
	
	protected String invokeGetMethod(String methodName, DataSource dataSource) throws Throwable {
		Method method = dataSource.getClass().getDeclaredMethod(methodName);
		return (String) method.invoke(dataSource);
	}
	
	protected String getLocalBackupsFolder() {
		StringBuilder dirName = new StringBuilder();
		dirName.append("databaseBackups").append(File.separator);
		return dirName.toString();
	}
	
	protected List<Component> getComponents() throws ApsSystemException {
		return this.getComponentManager().getCurrentComponents();
	}
	
	protected String[] extractBeanNames(Class beanClass) {
		ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
		return factory.getBeanNamesForType(beanClass);
	}
	
	protected BeanFactory getBeanFactory() {
		return _beanFactory;
	}
	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this._beanFactory = beanFactory;
	}
	
	protected Map<String, List<String>> getEntandoTableMapping() {
		return _entandoTableMapping;
	}
	public void setEntandoTableMapping(Map<String, List<String>> entandoTableMapping) {
		this._entandoTableMapping = entandoTableMapping;
	}
	
	protected Properties getDatabaseTypeDrivers() {
		return _databaseTypeDrivers;
	}
	public void setDatabaseTypeDrivers(Properties databaseTypeDrivers) {
		this._databaseTypeDrivers = databaseTypeDrivers;
	}
	
	protected IComponentManager getComponentManager() {
		return _componentManager;
	}
	public void setComponentManager(IComponentManager componentManager) {
		this._componentManager = componentManager;
	}
	
	protected IStorageManager getStorageManager() {
		return _storageManager;
	}
	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}
	
	private BeanFactory _beanFactory;
	private Map<String, List<String>> _entandoTableMapping;
	
	private Properties _databaseTypeDrivers;
	
	private IComponentManager _componentManager;
	private IStorageManager _storageManager;
	
}
