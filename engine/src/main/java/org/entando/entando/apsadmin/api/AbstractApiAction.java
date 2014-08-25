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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.services.api.IApiCatalogManager;
import org.entando.entando.aps.system.services.api.model.ApiMethod;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.api.model.ApiService;
import org.entando.entando.apsadmin.api.helper.SchemaGeneratorActionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.BaseAction;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public abstract class AbstractApiAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(AbstractApiAction.class);
	
    public List<SelectItem> getPermissionAutorityOptions() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        try {
            List<Permission> permissions = new ArrayList<Permission>();
            permissions.addAll(this.getRoleManager().getPermissions());
            BeanComparator comparator = new BeanComparator("description");
            Collections.sort(permissions, comparator);
            for (int i = 0; i < permissions.size(); i++) {
                Permission permission = permissions.get(i);
                items.add(new SelectItem(permission.getName(), 
						this.getPermissionAutorityOptionPrefix() + permission.getDescription()));
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting autority options", t);
            //ApsSystemUtils.logThrowable(t, this, "getPermissionAutorityOptions", "Error extracting autority options");
        }
        return items;
    }
	
	protected String getPermissionAutorityOptionPrefix() {
		return "";
	}
	
    protected String generateRequestBodySchema(ApiMethod method) {
        try {
            if (method.getHttpMethod().equals(ApiMethod.HttpMethod.GET) 
                    || method.getHttpMethod().equals(ApiMethod.HttpMethod.DELETE)) {
                String[] args = {method.getResourceName(), method.getHttpMethod().toString()};
                this.addActionError(this.getText("error.resource.method.request.schemaNotAvailable", args));
                return INPUT;
            }
            if (null == method.getExpectedType()) {
                throw new ApsSystemException("Null expectedType for Method " + method.getHttpMethod() + " for resource " + method.getResourceName());
            }
            String result = this.generateAndCheckSchema(method.getExpectedType());
            if (INPUT.equals(result)) {
                String[] args = {method.getResourceName(), method.getHttpMethod().toString()};
                this.addActionError(this.getText("error.resource.method.request.schemaNotAvailable", args));
                return INPUT;
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting request body Schema", t);
            //ApsSystemUtils.logThrowable(t, this, "generateRequestBodySchema", "Error extracting request body Schema");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    protected String generateResponseBodySchema(ApiMethod method) {
        try {
            Class responseClass = this.getSchemaGeneratorHelper().extractResponseClass(method, this.getRequest());
            String result = this.generateAndCheckSchema(responseClass);
            if (INPUT.equals(result)) {
                String[] args = {method.getResourceName(), method.getHttpMethod().toString()};
                this.addActionError(this.getText("error.resource.method.response.schemaNotAvailable", args));
                return INPUT;
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting response body Schema", t);
            //ApsSystemUtils.logThrowable(t, this, "generateResponseBodySchema", "Error extracting response body Schema");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    private String generateAndCheckSchema(Class jaxbObject) throws IOException {
        InputStream stream = null;
        try {
            String text = this.getSchemaGeneratorHelper().generateSchema(jaxbObject);
            if (null == text || text.trim().length() == 0) {
                return INPUT;
            } else {
                stream = new ByteArrayInputStream(text.getBytes());
            }
        } catch (Throwable t) {
        	_logger.error("Error extracting generating schema from class {}", jaxbObject, t);
            //ApsSystemUtils.logThrowable(t, this, "generateSchema", "Error extracting generating schema from class " + jaxbObject);
            throw new RuntimeException("Error extracting generating schema", t);
        }
        this.setSchemaStream(stream);
        return SUCCESS;
    }
	
    public ApiResource getApiResource(String namespace, String resourceName) {
		try {
            return this.getApiCatalogManager().getResource(namespace, resourceName);
        } catch (Throwable t) {
        	_logger.error("Error extracting resource '{}' namespace'{}'", resourceName, namespace, t);
            //ApsSystemUtils.logThrowable(t, this, "getApiResource", "Error extracting resource '" + resourceName + "' namespace'" + namespace + "'");
            throw new RuntimeException("Error extracting resource '" + resourceName + "' namespace'" + namespace + "'", t);
        }
    }
    
	public ApiService getApiService(String key) throws ApsSystemException {
		try {
            return this.getApiCatalogManager().getApiService(key);
        } catch (Throwable t) {
        	_logger.error("Error extracting service '{}'", key, t);
            //ApsSystemUtils.logThrowable(t, this, "getApiService", "Error extracting service '" + key + "'");
            throw new RuntimeException("Error extracting api service '" + key + "'", t);
        }
	}
	
	protected SchemaGeneratorActionHelper getSchemaGeneratorHelper() {
        return new SchemaGeneratorActionHelper();
    }
	
	public Permission getPermission(String name) {
		return this.getRoleManager().getPermission(name);
	}
	
	public InputStream getSchemaStream() {
        return _schemaStream;
    }
    public void setSchemaStream(InputStream schemaStream) {
        this._schemaStream = schemaStream;
    }
	
    protected IApiCatalogManager getApiCatalogManager() {
        return _apiCatalogManager;
    }
    public void setApiCatalogManager(IApiCatalogManager apiCatalogManager) {
        this._apiCatalogManager = apiCatalogManager;
    }
    
    protected IRoleManager getRoleManager() {
        return _roleManager;
    }
    public void setRoleManager(IRoleManager roleManager) {
        this._roleManager = roleManager;
    }
	
	private InputStream _schemaStream;
    
    private IApiCatalogManager _apiCatalogManager;
    private IRoleManager _roleManager;
    
}