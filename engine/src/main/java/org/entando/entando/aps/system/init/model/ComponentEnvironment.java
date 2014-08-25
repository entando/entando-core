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
package org.entando.entando.aps.system.init.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class ComponentEnvironment {

	private static final Logger _logger = LoggerFactory.getLogger(ComponentEnvironment.class);
	
	public ComponentEnvironment(Element environmentElement, Map<String, String> postProcessClasses) throws Throwable {
		try {
			String environmentCode = environmentElement.getAttributeValue("code");
			this.setCode(environmentCode);
			Element defaultSqlResourcesElement = environmentElement.getChild("defaultSqlResources");
			if (null != defaultSqlResourcesElement) {
				List<Element> datasourceElements = defaultSqlResourcesElement.getChildren("datasource");
				for (int j = 0; j < datasourceElements.size(); j++) {
					Element datasourceElement = datasourceElements.get(j);
					String datasourceName = datasourceElement.getAttributeValue("name");
					String sqlResourcePath = datasourceElement.getText().trim();
					this.getDefaultSqlResourcesPaths().put(datasourceName, sqlResourcePath);
				}
			}
			Element postProcessesElement = environmentElement.getChild("postProcesses");
			if (null != postProcessesElement) {
				List<Element> postProcessElements = postProcessesElement.getChildren();
				if (null != postProcessElements && !postProcessElements.isEmpty()) {
					for (int i = 0; i < postProcessElements.size(); i++) {
						Element postProcessElement = postProcessElements.get(i);
						this.createPostProcess(postProcessElement, postProcessClasses);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error creating ComponentEnvironment", t);
			//ApsSystemUtils.logThrowable(t, this, "ComponentEnvironment");
			throw new ApsSystemException("Error creating ComponentEnvironment", t);
		}
	}
	
	private void createPostProcess(Element postProcessElement, Map<String, String> postProcessClasses) throws ApsSystemException {
		try {
			String name = postProcessElement.getName();
			String className = postProcessClasses.get(name);
			if (null != className) {
				Class postProcessClass = Class.forName(className);
				IPostProcess postProcess = (IPostProcess) postProcessClass.newInstance();
				postProcess.createConfig(postProcessElement);
				if (null == this.getPostProcesses()) {
					this.setPostProcesses(new ArrayList<IPostProcess>());
				}
				this.getPostProcesses().add(postProcess);
			} else {
				_logger.error("Null post process class for process name '{}'", name);
			}
		} catch (Throwable t) {
			_logger.error("Error creating Post Process", t);
			//ApsSystemUtils.logThrowable(t, this, "createPostProcess");
			throw new ApsSystemException("Error creating Post Process", t);
		}
	}
	
	public String getCode() {
		return _code;
	}
	protected void setCode(String code) {
		this._code = code;
	}
	
	public Resource getSqlResources(String datasourceName) {
		String path = this.getDefaultSqlResourcesPaths().get(datasourceName);
		if (null == path) {
			return null;
		}
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		return resolver.getResource(path);
	}
	
	public Map<String, String> getDefaultSqlResourcesPaths() {
		return _defaultSqlResourcesPaths;
	}
	protected void setDefaultSqlResourcesPaths(Map<String, String> defaultSqlResourcesPaths) {
		this._defaultSqlResourcesPaths = defaultSqlResourcesPaths;
	}
	
	public List<IPostProcess> getPostProcesses() {
		return _postProcesses;
	}
	protected void setPostProcesses(List<IPostProcess> postProcesses) {
		this._postProcesses = postProcesses;
	}
	
	private String _code;
	private Map<String, String> _defaultSqlResourcesPaths = new HashMap<String, String>();
	
	private List<IPostProcess> _postProcesses;
	
}
