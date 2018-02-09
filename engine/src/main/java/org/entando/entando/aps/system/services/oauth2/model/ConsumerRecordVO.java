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
package org.entando.entando.aps.system.services.oauth2.model;

import java.util.Date;

/**
 * @author E.Santoboni
 */
public class ConsumerRecordVO {

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        this._key = key;
    }

    public String getSecret() {
        return _secret;
    }

    public void setSecret(String secret) {
        this._secret = secret;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public String getCallbackUrl() {
        return _callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this._callbackUrl = callbackUrl;
    }

    public Date getExpirationDate() {
        return _expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this._expirationDate = expirationDate;
    }

    public Date getIssuedDate() {
        return _issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this._issuedDate = issuedDate;
    }

    public String getName() { return _name; }

    public void setName(String _name) { this._name = _name; }

    public String getScope() { return _scope; }

    public void setScope(String _scope) { this._scope = _scope; }

    public String getAuthorizedGrantTypes() { return _authorizedGrantTypes; }

    public void setAuthorizedGrantTypes(String _authorizedGrantTypes) { this._authorizedGrantTypes = _authorizedGrantTypes; }

    private String _key;
    private String _secret;
    private String _name;
    private String _description;
    private String _callbackUrl;
    private String _scope;
    private String _authorizedGrantTypes;
    private Date _expirationDate;
    private Date _issuedDate;
}