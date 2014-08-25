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
package com.agiletec.aps.system.common.entity.model;

import java.io.Serializable;

/**
 * @author E.Santoboni
 */
public class FieldError implements Serializable {

    public FieldError(String fieldCode, String errorCode) {
        this.setErrorCode(errorCode);
        this.setFieldCode(fieldCode);
    }

    public String getFieldCode() {
        return _fieldCode;
    }
    protected void setFieldCode(String fieldCode) {
        this._fieldCode = fieldCode;
    }

    public String getErrorCode() {
        return _errorCode;
    }
    protected void setErrorCode(String errorCode) {
        this._errorCode = errorCode;
    }

    public String getMessage() {
        return _message;
    }
    public void setMessage(String message) {
        this._message = message;
    }

    public String getMessageKey() {
        return _messageKey;
    }
    public void setMessageKey(String messageKey) {
        this._messageKey = messageKey;
    }

    private String _fieldCode;
    private String _errorCode;
    private String _message;
    private String _messageKey;

    public static final String MANDATORY = "MANDATORY";
    public static final String INVALID = "INVALID";
    public static final String INVALID_FORMAT = "INVALID_FORMAT";
    public static final String INVALID_MIN_LENGTH = "INVALID_MIN_LENGTH";
    public static final String INVALID_MAX_LENGTH = "INVALID_MAX_LENGTH";
    public static final String LESS_THAN_ALLOWED = "LESS_THAN_ALLOWED";
    public static final String GREATER_THAN_ALLOWED = "GREATER_THAN_ALLOWED";
    public static final String NOT_EQUALS_THAN_ALLOWED = "NOT_EQUALS_THAN_ALLOWED";

}