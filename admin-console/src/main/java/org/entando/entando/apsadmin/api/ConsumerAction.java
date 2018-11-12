/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
import java.util.Arrays;
import org.entando.entando.aps.system.services.oauth2.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author E.Santoboni
 */
public class ConsumerAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerAction.class);

    private String consumerKey;
    private int strutsAction;
    private String secret;
    private String description;
    private String name;
    private String callbackUrl;
    private String scope;
    private List<String> grantTypes;
    private Date expirationDate;
    private Date issuedDate;
    private IOAuthConsumerManager oauthConsumerManager;

    @Override
    public void validate() {
        super.validate();
        try {
            ConsumerRecordVO consumer = this.getOauthConsumerManager().getConsumerRecord(this.getConsumerKey());
            String[] args = {this.getConsumerKey()};
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD && null != consumer) {
                this.addFieldError("consumerKey", this.getText("error.consumer.exists", args));
            } else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT && null == consumer) {
                this.addFieldError("consumerKey", this.getText("error.consumer.notExists", args));
            }
        } catch (Exception t) {
            logger.error("Error validating consumer", t);
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
                this.addActionError(this.getText("error.consumer.notExists", args));
                return "list";
            }
            this.setCallbackUrl(consumer.getCallbackUrl());
            this.setDescription(consumer.getDescription());
            this.setName(consumer.getName());
            this.setScope(consumer.getScope());
            this.setExpirationDate(consumer.getExpirationDate());
            this.setIssuedDate(consumer.getIssuedDate());
            if (null != consumer.getAuthorizedGrantTypes()) {
                this.setGrantTypes(Arrays.asList(consumer.getAuthorizedGrantTypes().split(",")));
            }
        } catch (Exception t) {
            logger.error("error in edit", t);
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
            consumer.setScope(this.getScope());
            String grantTypesCsv = (null != this.getGrantTypes()) ? StringUtils.join(this.getGrantTypes(), ",") : null;
            consumer.setAuthorizedGrantTypes(grantTypesCsv);
            consumer.setSecret(this.getSecret());
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
                this.getOauthConsumerManager().addConsumer(consumer);
            } else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
                this.getOauthConsumerManager().updateConsumer(consumer);
            }
        } catch (Exception t) {
            logger.error("error in save", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String trash() {
        try {
            String check = this.checkForDelete();
            if (null != check) {
                return check;
            }
        } catch (Exception t) {
            logger.error("error in trash", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String delete() {
        try {
            String check = this.checkForDelete();
            if (null != check) {
                return check;
            }
            this.getOauthConsumerManager().deleteConsumer(this.getConsumerKey());
        } catch (Exception t) {
            logger.error("error in delete", t);
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

    public String[] getAllowedGrantTypes() {
        return IOAuthConsumerManager.GRANT_TYPES;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public int getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this.strutsAction = strutsAction;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
    }

    protected IOAuthConsumerManager getOauthConsumerManager() {
        return oauthConsumerManager;
    }

    public void setOauthConsumerManager(IOAuthConsumerManager oauthConsumerManager) {
        this.oauthConsumerManager = oauthConsumerManager;
    }

}
