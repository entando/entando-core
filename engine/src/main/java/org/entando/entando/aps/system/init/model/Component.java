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

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * @author E.Santoboni
 */
public class Component implements Comparable<Component> {

	private static final Logger _logger = LoggerFactory.getLogger(Component.class);
	
	public Component(Element rootElement, Map<String, String> postProcessClasses) throws Throwable {
		try {
			String code = rootElement.getChildText("code");
			this.setCode(code);
			String description = rootElement.getChildText("description");
			this.setDescription(description);
			Element dependenciesElement = rootElement.getChild("dependencies");
			if (null != dependenciesElement) {
				List<Element> dependenciesElementd = dependenciesElement.getChildren("code");
				for (int i = 0; i < dependenciesElementd.size(); i++) {
					Element element = dependenciesElementd.get(i);
					this.addDependency(element.getText());
				}
			}
			Element installationElement = rootElement.getChild("installation");
			if (null != installationElement) {
				Element tableMappingElement = installationElement.getChild("tableMapping");
				this.extractTableMapping(tableMappingElement);
				List<Element> enviromentElements = installationElement.getChildren("environment");
				if (enviromentElements.size() > 0) {
					this.setEnvironments(new HashMap<String, ComponentEnvironment>());
				}
				for (int i = 0; i < enviromentElements.size(); i++) {
					Element environmentElement = enviromentElements.get(i);
					ComponentEnvironment environment = 
							new ComponentEnvironment(environmentElement, postProcessClasses);
					this.getEnvironments().put(environment.getCode(), environment);
				}
			}
        } catch (Throwable t) {
        	_logger.error("Error loading component", t);
            //ApsSystemUtils.logThrowable(t, this, "Component", "Error loading component");
        }
	}
	
	private void extractTableMapping(Element tableMappingElement) {
		if (null != tableMappingElement) {
			this.setTableMapping(new HashMap<String, List<String>>());
			List<Element> datasourceElements = tableMappingElement.getChildren("datasource");
			for (int i = 0; i < datasourceElements.size(); i++) {
				Element datasourceElement = datasourceElements.get(i);
				String datasourceName = datasourceElement.getAttributeValue("name");
				List<String> tableMapping = new ArrayList<String>();
				List<Element> tableClasses = datasourceElement.getChildren("class");
				for (int j = 0; j < tableClasses.size(); j++) {
					tableMapping.add(tableClasses.get(j).getText());
				}
				if (tableMapping.size() > 0) {
					this.getTableMapping().put(datasourceName, tableMapping);
				}
			}
		}
	}
	
	public String getCode() {
		return _code;
	}
	protected void setCode(String code) {
		this._code = code;
	}
	
	public String getDescription() {
		return _description;
	}
	protected void setDescription(String description) {
		this._description = description;
	}
	
	public List<String> getDependencies() {
		return _dependencies;
	}
	protected void setDependencies(List<String> dependencies) {
		this._dependencies = dependencies;
	}
	protected void addDependency(String dependency) {
		if (null == dependency || dependency.trim().length() == 0) {
			return;
		}
		if (null == this.getDependencies()) {
			this.setDependencies(new ArrayList<String>());
		}
		if (!this.getDependencies().contains(dependency)) {
			this.getDependencies().add(dependency);
		}
	}
	
	public Map<String, List<String>> getTableMapping() {
		return _tableMapping;
	}
	protected void setTableMapping(Map<String, List<String>> tableMapping) {
		this._tableMapping = tableMapping;
	}
	
	public Map<String, ComponentEnvironment> getEnvironments() {
		return _environments;
	}
	protected void setEnvironments(Map<String, ComponentEnvironment> environments) {
		this._environments = environments;
	}
	
	@Override
	public int compareTo(Component other) {
		boolean depsEmpty = (null == this.getDependencies() || this.getDependencies().isEmpty());
		boolean otherDepsEmpty = (null == other.getDependencies() || other.getDependencies().isEmpty());
		if (!depsEmpty && 
				(otherDepsEmpty || this.getDependencies().contains(other.getCode()))) {
			return 1;
		} else if (!otherDepsEmpty && 
				(depsEmpty || other.getDependencies().contains(this.getCode()))) {
			return -1;
		}
		return 0;
	}
	
	private String _code;
	private String _description;
	private List<String> _dependencies;
	private Map<String, List<String>> _tableMapping;
	
	private Map<String, ComponentEnvironment> _environments;
	
}