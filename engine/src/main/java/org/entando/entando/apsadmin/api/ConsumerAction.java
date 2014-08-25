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

import java.util.Date;

import org.entando.entando.aps.system.services.oauth.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth.model.ConsumerRecordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

/**
 * @author E.Santoboni
 */
public class ConsumerAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(ConsumerAction.class);
	
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
            //ApsSystemUtils.logThrowable(t, this, "validate", "Error validating consumer");
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
            this.setExpirationDate(consumer.getExpirationDate());
            this.setSecret(consumer.getSecret());
        } catch (Throwable t) {
        	_logger.error("error in edit", t);
            //ApsSystemUtils.logThrowable(t, this, "edit");
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
            consumer.setExpirationDate(this.getExpirationDate());
            consumer.setSecret(this.getSecret());
            if (this.getStrutsAction() == ApsAdminSystemConstants.ADD) {
                this.getOauthConsumerManager().addConsumer(consumer);
            } else if (this.getStrutsAction() == ApsAdminSystemConstants.EDIT) {
                this.getOauthConsumerManager().updateConsumer(consumer);
            }
        } catch (Throwable t) {
        	_logger.error("error in save", t);
            //ApsSystemUtils.logThrowable(t, this, "save");
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
            //ApsSystemUtils.logThrowable(t, this, "trash");
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
            //ApsSystemUtils.logThrowable(t, this, "delete");
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
    private String _callbackUrl;
    private Date _expirationDate;
    
    private IOAuthConsumerManager _oauthConsumerManager;
    
}
