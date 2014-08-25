/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.init.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.entando.entando.aps.system.init.model.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;

/**
 * @author E.Santoboni
 */
public class ComponentLoader {

	private static final Logger _logger = LoggerFactory.getLogger(ComponentLoader.class);
	
	public ComponentLoader(String locationPatterns, Map<String, String> postProcessClasses) throws ApsSystemException {
        try {
            StringTokenizer tokenizer = new StringTokenizer(locationPatterns, ",");
            while (tokenizer.hasMoreTokens()) {
                String locationPattern = tokenizer.nextToken().trim();
                this.loadComponent(locationPattern, postProcessClasses);
            }
        } catch (Throwable t) {
        	_logger.error("Error loading component definitions", t);
            //ApsSystemUtils.logThrowable(t, this, "ComponentLoader", "Error loading component definitions");
            throw new ApsSystemException("Error loading component definitions", t);
        }
    }
    
    private void loadComponent(String locationPattern, Map<String, String> postProcessClasses) throws Throwable {
		PathMatchingResourcePatternResolver resolver = 
					new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources(locationPattern);
		ComponentDefDOM dom = null;
		Set<String> codes = new HashSet<String>();
		for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            InputStream is = null;
            String path = resource.getURL().getPath();
            try {
                is = resource.getInputStream();
                String xml = FileTextReader.getText(is);
                dom = new ComponentDefDOM(xml, path);
				Component component = dom.getComponent(postProcessClasses);
				if (null != component) {
					if (codes.add(component.getCode())) {
						_logger.debug("Component '{}' loaded", component.getCode());
						this.getComponents().put(component.getCode(), component);
					} else {
						_logger.debug("Component '{}' already loaded", component.getCode());
					}
				}
            } catch (Throwable t) {
            	_logger.error("Error loading Component definition by location Pattern '{}'",path, t);
                //ApsSystemUtils.logThrowable(t, this, "ComponentLoader", "Error loading Component definition by location Pattern '" + path + "'");
            } finally {
                if (null != is) {
                    is.close();
                }
            }
        }
    }
	
	public Map<String, Component> getComponents() {
		return _components;
	}
    
	private Map<String, Component> _components = new HashMap<String, Component>();
	
}
