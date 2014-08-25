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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.entando.entando.aps.system.services.api.ApiResourcesDefDOM;

import org.jdom.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class ApiMethod implements Serializable {

	private static final Logger _logger = LoggerFactory.getLogger(ApiMethod.class);
	
    protected ApiMethod() {}
    
    public ApiMethod(Element element) {
        this.setResourceName(element.getAttributeValue(ApiResourcesDefDOM.RESOURCE_ATTRIBUTE_NAME));
		this.setNamespace(element.getAttributeValue(ApiResourcesDefDOM.RESOURCE_ATTRIBUTE_NAMESPACE));
        Element sourceElement = element.getChild(ApiResourcesDefDOM.SOURCE_ELEMENT_NAME);
        if (null != sourceElement) {
            this.setSource(sourceElement.getText());
            this.setPluginCode(sourceElement.getAttributeValue(ApiResourcesDefDOM.PLUGIN_CODE_ATTRIBUTE_NAME));
        }
        this.buildMethod(element);
    }
    
    public ApiMethod(String resourceName, String namespace, 
			String source, String pluginCode, Element element) {
        this.setResourceName(resourceName);
		this.setNamespace(namespace);
        this.setSource(source);
        this.setPluginCode(pluginCode);
        this.buildMethod(element);
    }
    
	private void buildMethod(Element element) {
		try {
			this.setDefaultRequiredAuth(Boolean.parseBoolean(element.getAttributeValue("requiredAuth")));
			this.setRequiredAuth(this.getDefaultRequiredAuth());
			this.setDefaultRequiredPermission(element.getAttributeValue("requiredPermission"));
			this.setRequiredPermission(this.getDefaultRequiredPermission());
			String httpMethod = element.getAttributeValue("httpMethod");
			if (null != httpMethod) {
				this.setHttpMethod(Enum.valueOf(ApiMethod.HttpMethod.class, httpMethod.toUpperCase()));
			} else {
				this.setHttpMethod(HttpMethod.GET);
			}
			this.setDefaultStatus(Boolean.parseBoolean(element.getAttributeValue(ApiResourcesDefDOM.ACTIVE_ATTRIBUTE_NAME)));
			this.setStatus(this.getDefaultStatus());
			this.setDefaultHidden(Boolean.parseBoolean(element.getAttributeValue(ApiResourcesDefDOM.HIDDEN_ATTRIBUTE_NAME)));
			this.setHidden(this.getDefaultHidden());
			this.setCanSpawnOthers(Boolean.parseBoolean(element.getAttributeValue(ApiResourcesDefDOM.CAN_SPAWN_OTHER_ATTRIBUTE_NAME)));
			this.setDescription(element.getChildText(ApiResourcesDefDOM.METHOD_DESCRIPTION_ELEMENT_NAME));
			Element springBeanElement = element.getChild(ApiResourcesDefDOM.SPRING_BEAN_ELEMENT_NAME);
			this.setSpringBean(springBeanElement.getAttributeValue(ApiResourcesDefDOM.SPRING_BEAN_NAME_ATTRIBUTE_NAME));
			this.setSpringBeanMethod(springBeanElement.getAttributeValue(ApiResourcesDefDOM.SPRING_BEAN_METHOD_ATTRIBUTE_NAME));
			this.setResponseClassName(element.getChildText(ApiResourcesDefDOM.RESPONSE_CLASS_ELEMENT_NAME));
			Element parametersElement = element.getChild(ApiResourcesDefDOM.PARAMETERS_ELEMENT_NAME);
			if (null != parametersElement) {
				List<Element> parametersElements = parametersElement.getChildren(ApiResourcesDefDOM.PARAMETER_ELEMENT_NAME);
				for (int i = 0; i < parametersElements.size(); i++) {
					Element parameterElement = parametersElements.get(i);
					ApiMethodParameter parameter = new ApiMethodParameter(parameterElement);
					if (null == this.getParameters()) {
						this.setParameters(new ArrayList<ApiMethodParameter>());
					}
					this.getParameters().add(parameter);
				}
			}
			Element relatedWidgetElement = element.getChild(ApiResourcesDefDOM.RELATED_WIDGET_ELEMENT_NAME);
			if (null != relatedWidgetElement) {
				this.setRelatedWidget(new ApiMethodRelatedWidget(relatedWidgetElement));
			}
			if (this.getHttpMethod().equals(HttpMethod.POST) || this.getHttpMethod().equals(HttpMethod.PUT)) {
				Element expectedTypeElement = element.getChild("expectedType");
				String className = (null != expectedTypeElement) ? expectedTypeElement.getText() : null;
				if (null == className || className.trim().length() == 0) {
					throw new ApsSystemException("Expected Class required for Http Methods POST and PUT");
				}
				Class beanClass = Class.forName(className);
				this.setExpectedType(beanClass);
			}
		} catch (Throwable t) {
			_logger.error("Error building api method '{}'", this.getResourceName(), t);
			throw new RuntimeException("Error building api method", t);
		}
	}
    
	@Override
    public ApiMethod clone() {
        ApiMethod clone = new ApiMethod();
        clone.setResourceName(this.getResourceName());
		clone.setNamespace(this.getNamespace());
        clone.setDefaultStatus(this.getDefaultStatus());
        clone.setStatus(this.getStatus());
        clone.setDefaultHidden(this.getDefaultHidden());
        clone.setHidden(this.getHidden());
        clone.setDescription(this.getDescription());
        if (null != this.getParameters()) {
            List<ApiMethodParameter> clonedParameters = new ArrayList<ApiMethodParameter>();
            for (int i = 0; i < this.getParameters().size(); i++) {
                ApiMethodParameter clonedParameter = this.getParameters().get(i).clone();
                clonedParameters.add(clonedParameter);
            }
            clone.setParameters(clonedParameters);
        }
        clone.setPluginCode(this.getPluginCode());
        clone.setResponseClassName(this.getResponseClassName());
        clone.setSource(this.getSource());
        clone.setSpringBean(this.getSpringBean());
        clone.setSpringBeanMethod(this.getSpringBeanMethod());
        clone.setCanSpawnOthers(this.isCanSpawnOthers());
        if (null != this.getRelatedWidget()) {
            clone.setRelatedWidget(this.getRelatedWidget().clone());
        }
        clone.setHttpMethod(this.getHttpMethod());
        clone.setDefaultRequiredAuth(this.getDefaultRequiredAuth());
        clone.setDefaultRequiredPermission(this.getDefaultRequiredPermission());
        clone.setRequiredAuth(this.getRequiredAuth());
        clone.setRequiredPermission(this.getRequiredPermission());
        clone.setDefaultRequiredAuth(this.getDefaultRequiredAuth());
        clone.setDefaultRequiredPermission(this.getDefaultRequiredPermission());
        if (null != this.getExpectedType()) {
            try {
                clone.setExpectedType(Class.forName(this.getExpectedType().getName()));
            } catch (Throwable t) {
                //nothing to catch
            }
        }
        return clone;
    }
    
    public void resetConfiguration() {
        this.setRequiredAuth(this.getDefaultRequiredAuth());
        this.setRequiredPermission(this.getDefaultRequiredPermission());
        this.setStatus(this.getDefaultStatus());
		this.setHidden(this.getDefaultHidden());
    }
    
    public String getResourceName() {
        return _resourceName;
    }
    protected void setResourceName(String resourceName) {
        this._resourceName = resourceName;
    }
    
    public HttpMethod getHttpMethod() {
        return _httpMethod;
    }
    protected void setHttpMethod(HttpMethod httpMethod) {
        this._httpMethod = httpMethod;
    }
    
    public String getNamespace() {
        return _namespace;
    }
    protected void setNamespace(String namespace) {
        this._namespace = namespace;
    }
    
    public Boolean getStatus() {
        return _status;
    }
    public void setStatus(Boolean status) {
        this._status = status;
    }
	
    public Boolean getDefaultStatus() {
		if (null == this._defaultStatus) return false;
        return _defaultStatus;
    }
    protected void setDefaultStatus(Boolean defaultStatus) {
        this._defaultStatus = defaultStatus;
    }
    
	public Boolean getHidden() {
		return _hidden;
	}
	public void setHidden(Boolean hidden) {
		this._hidden = hidden;
	}
    
	public Boolean getDefaultHidden() {
		if (null == this._defaultHidden) return false;
		return _defaultHidden;
	}
	public void setDefaultHidden(Boolean defaultHidden) {
		this._defaultHidden = defaultHidden;
	}
    
    public Class getExpectedType() {
        return _expectedType;
    }
    protected void setExpectedType(Class expectedType) {
        this._expectedType = expectedType;
    }
    
    public Boolean getDefaultRequiredAuth() {
        if (null == this._defaultRequiredAuth) return false;
        return _defaultRequiredAuth;
    }
    protected void setDefaultRequiredAuth(Boolean defaultRequiredAuth) {
        this._defaultRequiredAuth = defaultRequiredAuth;
    }
    
    public String getDefaultRequiredPermission() {
        return _defaultRequiredPermission;
    }
    protected void setDefaultRequiredPermission(String defaultRequiredPermission) {
        this._defaultRequiredPermission = defaultRequiredPermission;
    }
    
    public Boolean getRequiredAuth() {
        if (null == this._requiredAuth) return false;
        return _requiredAuth;
    }
    public void setRequiredAuth(Boolean requiredAuth) {
        this._requiredAuth = requiredAuth;
    }
    
    public String getRequiredPermission() {
        return _requiredPermission;
    }
    public void setRequiredPermission(String requiredPermission) {
        this._requiredPermission = requiredPermission;
    }
    
    public String getSource() {
        return _source;
    }
    public void setSource(String source) {
        this._source = source;
    }

    public String getPluginCode() {
        return _pluginCode;
    }
    public void setPluginCode(String pluginCode) {
        this._pluginCode = pluginCode;
    }
    
    @Deprecated
    public String getMethodName() {
        return this.getResourceName();
    }
    @Deprecated
    protected void setMethodName(String methodName) {
        this.setResourceName(methodName);
    }

    public String getDescription() {
        return _description;
    }
    protected void setDescription(String description) {
        this._description = description;
    }
    
    public boolean isActive() {
        if (null == this.getStatus()) return false;
        return this.getStatus();
    }

    public boolean isCanSpawnOthers() {
        return _canSpawnOthers;
    }
    protected void setCanSpawnOthers(boolean canSpawnOthers) {
        this._canSpawnOthers = canSpawnOthers;
    }

    public String getSpringBean() {
        return _springBean;
    }
    protected void setSpringBean(String springBean) {
        this._springBean = springBean;
    }

    public String getSpringBeanMethod() {
        return _springBeanMethod;
    }
    protected void setSpringBeanMethod(String springBeanMethod) {
        this._springBeanMethod = springBeanMethod;
    }

    public String getResponseClassName() {
        return _responseClassName;
    }
    protected void setResponseClassName(String responseClassName) {
        this._responseClassName = responseClassName;
    }

    public List<ApiMethodParameter> getParameters() {
        return _parameters;
    }
    protected void setParameters(List<ApiMethodParameter> parameters) {
        this._parameters = parameters;
    }

    public ApiMethodParameter getParameter(String key) {
        if (null == key || key.trim().length() == 0 || this._parameters == null) {
            return null;
        }
        for (int i = 0; i < this._parameters.size(); i++) {
            ApiMethodParameter parameter = this._parameters.get(i);
            if (parameter.getKey().equals(key)) {
                return parameter;
            }
        }
        return null;
    }
	
	@Deprecated
    public ApiMethodRelatedWidget getRelatedShowlet() {
        return this.getRelatedWidget();
    }
	@Deprecated
    protected void setRelatedShowlet(ApiMethodRelatedWidget relatedShowlet) {
        this.setRelatedWidget(relatedShowlet);
    }
	
	public ApiMethodRelatedWidget getRelatedWidget() {
		return _relatedWidget;
	}
	public void setRelatedWidget(ApiMethodRelatedWidget relatedWidget) {
		this._relatedWidget = relatedWidget;
	}
    
    public static enum HttpMethod {
        GET,POST,PUT,DELETE
    }
    
    private String _resourceName;
    private HttpMethod _httpMethod;
	private String _namespace;
    
    private Boolean _defaultStatus;
    private Boolean _status;
	private Boolean _defaultHidden;
	private Boolean _hidden;
    
    private Boolean _defaultRequiredAuth;
    private String _defaultRequiredPermission;
    private Boolean _requiredAuth;
    private String _requiredPermission;
    
    private Class _expectedType;
    
    private String _source;
    private String _pluginCode;
    
    private String _description;
    
    private boolean _canSpawnOthers;
    private String _springBean;
    private String _springBeanMethod;
    private String _responseClassName;
    private List<ApiMethodParameter> _parameters;
    private ApiMethodRelatedWidget _relatedWidget;
    
}