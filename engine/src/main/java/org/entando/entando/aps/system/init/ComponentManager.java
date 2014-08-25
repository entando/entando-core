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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.util.ComponentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class ComponentManager implements IComponentManager {

	private static final Logger _logger = LoggerFactory.getLogger(ComponentManager.class);
	
	public void init() throws Exception {
		this.loadComponents();
		_logger.debug("{} ready.", this.getClass().getName());
	}
	
    protected void loadComponents() throws ApsSystemException {
        try {
			ComponentLoader loader = 
					new ComponentLoader(this.getLocationPatterns(), this.getPostProcessClasses());
			Map<String, Component> componentMap = loader.getComponents();
			List<Component> components = new ArrayList<Component>();
			components.addAll(componentMap.values());
			for (int i = 0; i < components.size(); i++) {
				Component component = components.get(i);
				List<String> dependencies = component.getDependencies();
				if (null != dependencies && !dependencies.isEmpty()) {
					for (int j = 0; j < dependencies.size(); j++) {
						String dependency = dependencies.get(j);
						this.checkSubDependency(componentMap, dependency, dependencies);
					}
				}
			}
			Collections.sort(components);
			this.setComponents(components);
        } catch (Throwable t) {
        	_logger.error("Error loading components definitions", t);
            //ApsSystemUtils.logThrowable(t, this, "loadComponents", "Error loading components definitions");
            throw new ApsSystemException("Error loading components definitions", t);
        }
    }
	
	private void checkSubDependency(Map<String, Component> componentMap, String dependency, List<String> masterDependencies) {
		Component component = componentMap.get(dependency);
		if (null == component) {
			return;
		}
		List<String> subDependencies = component.getDependencies();
		if (null != subDependencies && !subDependencies.isEmpty()) {
			for (int i = 0; i < subDependencies.size(); i++) {
				String subDependency = subDependencies.get(i);
				if (!masterDependencies.contains(subDependency)) {
					masterDependencies.add(subDependency);
				}
				this.checkSubDependency(componentMap, subDependency, masterDependencies);
			}
		}
	}
	
	@Override
	public List<Component> getCurrentComponents() throws ApsSystemException {
		return this.getComponents();
	}
	
    protected String getLocationPatterns() {
        if (null == this._locationPatterns) {
            return DEFAULT_LOCATION_PATTERN;
        }
        return _locationPatterns;
    }
    public void setLocationPatterns(String locationPatterns) {
        this._locationPatterns = locationPatterns;
    }
	
	protected List<Component> getComponents() {
		return _components;
	}
	protected void setComponents(List<Component> components) {
		this._components = components;
	}
	
	protected Map<String, String> getPostProcessClasses() {
		return _postProcessClasses;
	}
	public void setPostProcessClasses(Map<String, String> postProcessClasses) {
		this._postProcessClasses = postProcessClasses;
	}
	
	private String _locationPatterns;
	
	private List<Component> _components;
	private Map<String, String> _postProcessClasses;
	
	public static final String DEFAULT_LOCATION_PATTERN = "classpath*:component/**/**component.xml";
}
