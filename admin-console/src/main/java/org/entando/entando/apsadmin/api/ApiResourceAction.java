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
package org.entando.entando.apsadmin.api;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiMethod.HttpMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.util.SelectItem;

/**
 * @author E.Santoboni
 */
public class ApiResourceAction extends AbstractApiAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ApiResourceAction.class);
	
	@Override
    public void validate() {
        try {
            super.validate();
            String resourceName = this.getResourceName();
            String namespace = this.getNamespace();
            if (null == resourceName) {
                this.addActionError(this.getText("error.resource.invalidName"));
            } else if (null == this.getApiResource(namespace, resourceName)) {
				if (null != namespace) {
					this.addActionError(this.getText("error.resource.namespace.invalid", new String[]{namespace, resourceName}));
				} else {
					this.addActionError(this.getText("error.resource.noNamespace.invalid", new String[]{resourceName}));
				}
            }
        } catch (Throwable t) {
        	_logger.error("Error validating request", t);
            //ApsSystemUtils.logThrowable(t, this, "validate", "Error validating request");
        }
    }
	
	public String generateRequestBodySchema() {
        try {
            String result = this.checkMethod();
			if (null != result) return result;
            return super.generateRequestBodySchema(this.extractMethod());
        } catch (Throwable t) {
        	_logger.error("Error extracting request body Schema", t);
            //ApsSystemUtils.logThrowable(t, this, "generateRequestBodySchema", "Error extracting request body Schema");
            return FAILURE;
        }
    }
    
    public String generateResponseBodySchema() {
        try {
            String result = this.checkMethod();
			if (null != result) return result;
            return super.generateResponseBodySchema(this.extractMethod());
        } catch (Throwable t) {
        	_logger.error("Error extracting response body Schema", t);
            //ApsSystemUtils.logThrowable(t, this, "generateResponseBodySchema", "Error extracting response body Schema");
            return FAILURE;
        }
    }
	
	public String getSchemaFilePrefix() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getHttpMethod()).append("_");
		if (null != this.getNamespace()) {
			builder.append(this.getNamespace()).append("_");
		}
		builder.append(this.getResourceName());
		return builder.toString();
	}
    
    public String updateMethodStatus() {
        try {
			String result = this.checkMethod();
			if (null != result) return result;
            ApiMethod method = this.extractMethod();
            String requiredAuthority = this.getRequest().getParameter(method.getHttpMethod().toString() + "_methodAuthority");
            String active = this.getRequest().getParameter(method.getHttpMethod().toString() + "_active");
            String hidden = this.getRequest().getParameter(method.getHttpMethod().toString() + "_hidden");
            if (null == requiredAuthority) {
                //TODO MANAGE
                return SUCCESS;
            }
            this.updateMethodStatus(method, requiredAuthority, active, hidden);
        } catch (Throwable t) {
        	_logger.error("Error updating method status", t);
            //ApsSystemUtils.logThrowable(t, this, "updateMethodStatus", "Error updating method status");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String resetMethodStatus() {
        try {
			String result = this.checkMethod();
			if (null != result) return result;
            this.resetMethodStatus(this.extractMethod());
        } catch (Throwable t) {
        	_logger.error("Error resetting method status", t);
            //ApsSystemUtils.logThrowable(t, this, "updateMethodStatus", "Error resetting method status");
            return FAILURE;
        }
        return SUCCESS;
    }
    
	private String checkMethod() throws Throwable {
        if (null == this.extractMethod()) {
			ApiResource resource = this.getApiResource();
			this.addActionError(this.getText("error.resource.method.invalid", new String[]{resource.getCode(), this.getHttpMethod().toString()}));
        }
        return null;
    }
	
	private ApiMethod extractMethod() throws Throwable {
        ApiResource resource = this.getApiResource();
        return resource.getMethod(this.getHttpMethod());
    }
	
    public String updateAllMethodStatus() {
        try {
            ApiResource resource = this.getApiResource();
            String requiredAuthority = this.getRequest().getParameter("methodAuthority");
            String active = this.getRequest().getParameter("active");
            String hidden = this.getRequest().getParameter("hidden");
            if (null == requiredAuthority) {
                //TODO MANAGE
                return SUCCESS;
            }
            this.updateMethodStatus(resource.getGetMethod(), requiredAuthority, active, hidden);
            this.updateMethodStatus(resource.getPostMethod(), requiredAuthority, active, hidden);
            this.updateMethodStatus(resource.getPutMethod(), requiredAuthority, active, hidden);
            this.updateMethodStatus(resource.getDeleteMethod(), requiredAuthority, active, hidden);
        } catch (Throwable t) {
        	_logger.error("Error updating all method status", t);
            //ApsSystemUtils.logThrowable(t, this, "updateAllMethodStatus", "Error updating all method status");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    private void updateMethodStatus(ApiMethod method, String requiredAuthority, String active, String hidden) {
        if (null == method) return;
        try {
            if (null != requiredAuthority && requiredAuthority.equals("0")) {
                method.setRequiredAuth(true);
                method.setRequiredPermission(null);
            } else if (null != requiredAuthority && null != this.getRoleManager().getPermission(requiredAuthority)) {
                method.setRequiredAuth(true);
                method.setRequiredPermission(requiredAuthority);
            } else {
                method.setRequiredAuth(false);
                method.setRequiredPermission(null);
            }
            method.setStatus(active != null);
            method.setHidden(hidden != null);
            this.getApiCatalogManager().updateMethodConfig(method);
            String[] args = {method.getHttpMethod().toString()};
            this.addActionMessage(this.getText("message.resource.method.configUpdated", args));
        } catch (Throwable t) {
        	_logger.error("Error updating method status - {} - {}", method.getResourceName(), method.getHttpMethod(), t);
            //ApsSystemUtils.logThrowable(t, this, "updateMethodStatus", "Error updating method status - " + method.getResourceName() + " - " + method.getHttpMethod());
            throw new RuntimeException("Error updating method status", t);
        }
    }
    
    public String resetAllMethodStatus() {
        try {
            ApiResource resource = this.getApiResource();
            this.resetMethodStatus(resource.getGetMethod());
            this.resetMethodStatus(resource.getPostMethod());
            this.resetMethodStatus(resource.getPutMethod());
            this.resetMethodStatus(resource.getDeleteMethod());
        } catch (Throwable t) {
        	_logger.error("Error resetting all method status", t);
            //ApsSystemUtils.logThrowable(t, this, "resetAllMethodStatus", "Error resetting all method status");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public void resetMethodStatus(ApiMethod method) {
        if (null == method) return;
        try {
            this.getApiCatalogManager().resetMethodConfig(method);
            String[] args = {method.getHttpMethod().toString()};
            this.addActionMessage(this.getText("message.resource.method.configResetted", args));
        } catch (Throwable t) {
        	_logger.error("Error resetting method status - {} - {}", method.getResourceName() ,method.getHttpMethod(), t);
            //ApsSystemUtils.logThrowable(t, this, "resetMethodStatus", "Error resetting method status - " + method.getResourceName() + " - " + method.getHttpMethod());
            throw new RuntimeException("Error resetting method status", t);
        }
    }
    
    public List<SelectItem> getMethodAuthorityOptions() {
        List<SelectItem> masterList = super.getPermissionAutorityOptions();
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem("", this.getText("label.none")));
		items.add(new SelectItem("0", this.getText("label.api.authority.autenticationRequired")));
		items.addAll(masterList);
        return items;
    }
    
	@Override
	protected String getPermissionAutorityOptionPrefix() {
		return this.getText("label.api.authority.permission") + " ";
	}
	
    public ApiResource getApiResource() {
        return this.getApiResource(this.getNamespace(), this.getResourceName());
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
    
    public HttpMethod getHttpMethod() {
        return _httpMethod;
    }
    public void setHttpMethod(HttpMethod httpMethod) {
        this._httpMethod = httpMethod;
    }
    
    public Boolean getMethodStatus() {
        return _methodStatus;
    }
    public void setMethodStatus(Boolean methodStatus) {
        this._methodStatus = methodStatus;
    }
    
    private String _resourceName;
	private String _namespace;
    private ApiMethod.HttpMethod _httpMethod;
    private Boolean _methodStatus;
    
}