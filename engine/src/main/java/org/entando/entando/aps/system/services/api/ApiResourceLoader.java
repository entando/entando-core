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
package org.entando.entando.aps.system.services.api;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;

/**
 * Shortcut Loader Class.
 * @author E.Santoboni
 */
public class ApiResourceLoader {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiResourceLoader.class);
	
    protected ApiResourceLoader(String locationPatterns) throws ApsSystemException {
        try {
            StringTokenizer tokenizer = new StringTokenizer(locationPatterns, ",");
            while (tokenizer.hasMoreTokens()) {
                String locationPattern = tokenizer.nextToken().trim();
                this.loadApiResources(locationPattern);
            }
        } catch (Throwable t) {
        	_logger.error("Error loading Api Method definitions", t);
            //ApsSystemUtils.logThrowable(t, this, "ApiMethodLoader", "Error loading Api Method definitions");
            throw new ApsSystemException("Error loading Api Method definitions", t);
        }
    }
    
    private void loadApiResources(String locationPattern) throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources(locationPattern);
        ApiResourcesDefDOM dom = null;
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            InputStream is = null;
            String path = resource.getURL().getPath();
            try {
                is = resource.getInputStream();
                String xml = FileTextReader.getText(is);
                dom = new ApiResourcesDefDOM(xml, path);
                Map<String, ApiResource> extractedResources = dom.getResources();
                if (null != extractedResources) {
                    Iterator<ApiResource> extractedResourcesIter = extractedResources.values().iterator();
                    while (extractedResourcesIter.hasNext()) {
                        ApiResource apiResource = extractedResourcesIter.next();
                        if (null != this.getResources().get(apiResource.getCode())) {
                            _logger.info("Into definition file '{}' there is an API with namespace '{}', resource '{}' and there is just one already present - The old definition will be overrided!!!", path, apiResource.getNamespace(), apiResource.getResourceName());
                        }
                        this.getResources().put(apiResource.getCode(), apiResource);
                    }
                }
                _logger.debug("Loaded Api Resources definition by file {}", path);
            } catch (Throwable t) {
            	_logger.error("Error loading Api Resources definition by location Pattern '{}'",path, t);
                //ApsSystemUtils.logThrowable(t, this, "loadApiResources", "Error loading Api Resources definition by location Pattern '" + path + "'");
            } finally {
                if (null != is) {
                    is.close();
                }
            }
        }
    }
    
    public Map<String, ApiResource> getResources() {
        return this._resources;
    }
    
    private Map<String, ApiResource> _resources = new HashMap<String, ApiResource>();
    
}
