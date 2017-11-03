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
package org.entando.entando.apsadmin.api;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.entando.entando.aps.system.services.oauth2.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author E.Santoboni
 */
public class ConsumerAction extends BaseAction {

    private static final Logger _logger = LoggerFactory.getLogger(ConsumerAction.class);

    public ConsumerAction() {
        this._authorizedGrantTyped = GrantType.AUTHORIZATION_CODE.toString();
    }

    public void validate() {
        super.validate();
        try {
            ConsumerRecordVO consumer = this.getOauthConsumerManager().getConsumerRecord(this.getConsumerKey());
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD && null != consumer) {
                String[] args = {this.getConsumerKey()};
                this.addFieldError("consumerKey", this.getText("error.consumer.duplicated", args));
            } else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT && null == consumer) {
                this.addActionError(this.getText("error.consumer.notExist"));
            }
        } catch (Throwable t) {
            _logger.error("Error validating consumer", t);
            this.addActionError(this.getText("error.consumer.systemError"));
        }
    }

    public String newConsumer() {
        this.setStrutsAction(ApsAdminSystemConstants.ADD);
        return SUCCESS;
    }

    public String edit() {
        try {
            this.setStrutsAction(ApsAdminSystemConstants.EDIT);
            ConsumerRecordVO consumer = this.getOauthConsumerManager().getConsumerRecord(this.getConsumerKey());
            if (null == consumer) {
                String[] args = {this.getConsumerKey()};
                this.addActionError(this.getText("error.consumer.notExist", args));
                return "list";
            }
            this.setCallbackUrl(consumer.getCallbackUrl());
            this.setDescription(consumer.getDescription());
            this.setName(consumer.getName());
            this.setScope(consumer.getScope());
            this.setExpirationDate(consumer.getExpirationDate());
            this.setSecret(consumer.getSecret());
        } catch (Throwable t) {
            _logger.error("error in edit", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String save() {
        ConsumerRecordVO consumer = null;
        try {
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
                consumer = new ConsumerRecordVO();
                consumer.setKey(this.getConsumerKey());
            } else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
                consumer = this.getOauthConsumerManager().getConsumerRecord(this.getConsumerKey());
            }
            consumer.setCallbackUrl(this.getCallbackUrl());
            consumer.setDescription(this.getDescription());
            consumer.setName(this.getName());
            consumer.setExpirationDate(this.getExpirationDate());
            consumer.setIssuedDate(Calendar.getInstance().getTime());
            consumer.setScope(this.getScope());
            consumer.setAuthorizedGrantTypes(this.getAuthorizedGrantTyped());
            consumer.setSecret(this.getSecret());
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
                this.getOauthConsumerManager().addConsumer(consumer);
            } else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
                this.getOauthConsumerManager().updateConsumer(consumer);
            }
        } catch (Throwable t) {
            _logger.error("error in save", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String trash() {
        try {
            String check = this.checkForDelete();
            if (null != check) return check;
        } catch (Throwable t) {
            _logger.error("error in trash", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String delete() {
        try {
            String check = this.checkForDelete();
            if (null != check) return check;
            this.getOauthConsumerManager().deleteConsumer(this.getConsumerKey());
        } catch (Throwable t) {
            _logger.error("error in delete", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    protected String checkForDelete() throws ApsSystemException {
        ConsumerRecordVO consumer = this.getOauthConsumerManager().getConsumerRecord(this.getConsumerKey());
        if (null == consumer) {
            String[] args = {this.getConsumerKey()};
            this.addActionError(this.getText("error.consumer.notExist", args));
            return "list";
        }
        return null;
    }

    public ConsumerRecordVO getConsumer(String key) throws Throwable {
        return this.getOauthConsumerManager().getConsumerRecord(key);
    }

    public String getConsumerKey() {
        return _consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this._consumerKey = consumerKey;
    }

    public int getStrutsAction() {
        return _strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this._strutsAction = strutsAction;
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

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getScope() {
        return _scope;
    }

    public void setScope(String scope) {
        this._scope = scope;
    }

    public String getAuthorizedGrantTyped() {
        return _authorizedGrantTyped;
    }

    public void setAuthorizedGrantTyped(String authorizedGrantTyped) {
        this._authorizedGrantTyped = authorizedGrantTyped;
    }


    protected IOAuthConsumerManager getOauthConsumerManager() {
        return _oauthConsumerManager;
    }

    public void setOauthConsumerManager(IOAuthConsumerManager oauthConsumerManager) {
        this._oauthConsumerManager = oauthConsumerManager;
    }


    private String _consumerKey;
    private int _strutsAction;
    private String _secret;
    private String _description;
    private String _name;
    private String _callbackUrl;
    private String _scope;
    private String _authorizedGrantTyped;
    private Date _expirationDate;
    private IOAuthConsumerManager _oauthConsumerManager;

}
