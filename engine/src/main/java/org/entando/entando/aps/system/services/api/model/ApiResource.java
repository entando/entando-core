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
package org.entando.entando.aps.system.services.api.model;

/**
 * @author E.Santoboni
 */
public class ApiResource {
    
    public ApiResource clone() {
        ApiResource clone = new ApiResource();
        clone.setDescription(this.getDescription());
        clone.setNamespace(this.getNamespace());
        clone.setPluginCode(this.getPluginCode());
        clone.setResourceName(this.getResourceName());
        clone.setSource(this.getSource());
        if (null != this.getGetMethod()) {
            clone.setGetMethod(this.getGetMethod().clone());
        }
        if (null != this.getPutMethod()) {
            clone.setPutMethod(this.getPutMethod().clone());
        }
        if (null != this.getPostMethod()) {
            clone.setPostMethod(this.getPostMethod().clone());
        }
        if (null != this.getDeleteMethod()) {
            clone.setDeleteMethod(this.getDeleteMethod().clone());
        }
        return clone;
    }
	
	public String getCode() {
		return getCode(this.getNamespace(), this.getResourceName());
	}
    
	public static String getCode(String namespace, String resourceName) {
		StringBuilder buffer = new StringBuilder();
		if (null != namespace && namespace.trim().length() > 0) {
			buffer.append(namespace.trim()).append(":");
		}
		buffer.append(resourceName.trim());
		return buffer.toString();
	}
    
    public String getResourceName() {
        return _resourceName;
    }
    public void setResourceName(String resourceName) {
        this._resourceName = resourceName;
    }
    
    public String getNamespace() {
        return _namespace;
    }
    public void setNamespace(String namespace) {
        this._namespace = namespace;
    }
    
    public String getDescription() {
        return _description;
    }
    public void setDescription(String description) {
        this._description = description;
    }
    
    public String getPluginCode() {
        return _pluginCode;
    }
    public void setPluginCode(String pluginCode) {
        this._pluginCode = pluginCode;
    }
    
    public String getSectionCode() {
        if (null != this.getPluginCode() && this.getPluginCode().trim().length() > 0) {
            return this.getPluginCode();
        } else if (this.getSource().equalsIgnoreCase("core")) {
            return this.getSource().toLowerCase();
        }
        return "custom";
    }
    
    public String getSource() {
        return _source;
    }
    public void setSource(String source) {
        this._source = source;
    }
    
    public ApiMethod getGetMethod() {
        return _getMethod;
    }
    public void setGetMethod(ApiMethod getMethod) {
        this._getMethod = getMethod;
    }
    
    public ApiMethod getPostMethod() {
        return _postMethod;
    }
    public void setPostMethod(ApiMethod postMethod) {
        this._postMethod = postMethod;
    }
    
    public ApiMethod getPutMethod() {
        return _putMethod;
    }
    public void setPutMethod(ApiMethod putMethod) {
        this._putMethod = putMethod;
    }
    
    public ApiMethod getDeleteMethod() {
        return _deleteMethod;
    }
    public void setDeleteMethod(ApiMethod deleteMethod) {
        this._deleteMethod = deleteMethod;
    }
    
    public void setMethod(ApiMethod method) {
        if (method.getHttpMethod().equals(ApiMethod.HttpMethod.GET)) {
            this.setGetMethod(method);
        } else if (method.getHttpMethod().equals(ApiMethod.HttpMethod.POST)) {
            this.setPostMethod(method);
        } else if (method.getHttpMethod().equals(ApiMethod.HttpMethod.PUT)) {
            this.setPutMethod(method);
        } else if (method.getHttpMethod().equals(ApiMethod.HttpMethod.DELETE)) {
            this.setDeleteMethod(method);
        }
    }
    
    public ApiMethod getMethod(ApiMethod.HttpMethod httpMethod) {
        if (null == httpMethod) return null;
        if (httpMethod.equals(ApiMethod.HttpMethod.GET)) {
            return this.getGetMethod();
        } else if (httpMethod.equals(ApiMethod.HttpMethod.POST)) {
            return this.getPostMethod();
        } else if (httpMethod.equals(ApiMethod.HttpMethod.PUT)) {
            return this.getPutMethod();
        } else if (httpMethod.equals(ApiMethod.HttpMethod.DELETE)) {
            return this.getDeleteMethod();
        }
        return null;
    }
    
    private String _resourceName;
	private String _namespace;
    private String _description;
    private String _source;
    private String _pluginCode;
    
    private ApiMethod _getMethod;
    private ApiMethod _postMethod;
    private ApiMethod _putMethod;
    private ApiMethod _deleteMethod;
    
}
