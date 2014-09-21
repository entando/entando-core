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
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.util.ComponentLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			List<Component> orderedComponents = this.getOrderedComponents(components);
			this.setComponents(orderedComponents);
        } catch (Throwable t) {
        	_logger.error("Error loading components definitions", t);
            throw new ApsSystemException("Error loading components definitions", t);
        }
    }
	
	private List<Component> getOrderedComponents(List<Component> components) {
		List<Component> ordered = new ArrayList<Component>();
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			boolean added = false;
			for (int j = 0; j < ordered.size(); j++) {
				Component current = ordered.get(j);
				if (null != current.getDependencies() && current.getDependencies().contains(component.getCode())) {
					ordered.add(j, component);
					added = true;
					break;
				}
			}
			if (!added) {
				ordered.add(component);
			}
		}
		return ordered;
	}
	
	@Override
	public List<Component> getCurrentComponents() {
		return this.getComponents();
	}
	
	@Override
	public boolean isComponentInstalled(String componentCode) {
		List<Component> components = this.getComponents();
		if (null != components) {
			for (int i = 0; i < components.size(); i++) {
				Component component = components.get(i);
				if (null != component && component.getCode().equals(componentCode)) {
					return true;
				}
			}
		}
		return false;
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
