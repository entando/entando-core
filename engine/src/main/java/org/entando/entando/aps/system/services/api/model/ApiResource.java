/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.api.model;

import java.io.Serializable;

/**
 * @author E.Santoboni
 */
public class ApiResource implements Serializable {

    private String resourceName;
    private String namespace;
    private String description;
    private String source;
    private String pluginCode;

    private ApiMethod getMethod;
    private ApiMethod postMethod;
    private ApiMethod putMethod;
    private ApiMethod deleteMethod;

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
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPluginCode() {
        return pluginCode;
    }

    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode;
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
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ApiMethod getGetMethod() {
        return getMethod;
    }

    public void setGetMethod(ApiMethod getMethod) {
        this.getMethod = getMethod;
    }

    public ApiMethod getPostMethod() {
        return postMethod;
    }

    public void setPostMethod(ApiMethod postMethod) {
        this.postMethod = postMethod;
    }

    public ApiMethod getPutMethod() {
        return putMethod;
    }

    public void setPutMethod(ApiMethod putMethod) {
        this.putMethod = putMethod;
    }

    public ApiMethod getDeleteMethod() {
        return deleteMethod;
    }

    public void setDeleteMethod(ApiMethod deleteMethod) {
        this.deleteMethod = deleteMethod;
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
        if (null == httpMethod) {
            return null;
        }
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

}
