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
package org.entando.entando.aps.system.services.i18n;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.i18n.model.JAXBI18nLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author E.Santoboni
 */
public class ApiI18nLabelInterface {

    private static final Logger _logger = LoggerFactory.getLogger(ApiI18nLabelInterface.class);

    public JAXBI18nLabel getLabel(Properties properties) throws ApiException, ApsSystemException {
        JAXBI18nLabel jaxbI18nLabel = null;
        try {
            String key = properties.getProperty("key");
            ApsProperties labelGroups = this.getI18nManager().getLabelGroup(key);
            if (null == labelGroups) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Label with key '" + key + "' does not exist", Response.Status.CONFLICT);
            }
            jaxbI18nLabel = new JAXBI18nLabel(key, labelGroups);
        } catch (ApiException ae) {
            throw ae;
        } catch (ApsSystemException t) {
            _logger.error("error loading labels", t);
            throw new ApsSystemException("Error loading labels", t);
        }
        return jaxbI18nLabel;
    }

    public void addLabel(JAXBI18nLabel jaxbI18nLabel) throws ApiException, ApsSystemException {
        try {
            this.checkLabels(jaxbI18nLabel);
            String key = jaxbI18nLabel.getKey();
            ApsProperties labelGroups = this.getI18nManager().getLabelGroup(key);
            if (null != labelGroups) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Label with key '" + key + "' already exists", Response.Status.CONFLICT);
            }
            ApsProperties labels = jaxbI18nLabel.extractLabels();
            this.getI18nManager().addLabelGroup(key, labels);
        } catch (ApiException | ApsSystemException ae) {
            _logger.error("Error adding label", ae);
            throw new ApsSystemException("Error adding labels", ae);
        }
    }

    public void updateLabel(JAXBI18nLabel jaxbI18nLabel) throws ApiException, ApsSystemException {
        try {
            this.checkLabels(jaxbI18nLabel);
            String key = jaxbI18nLabel.getKey();
            ApsProperties labelGroups = this.getI18nManager().getLabelGroup(key);
            if (null == labelGroups) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Label with key '" + key + "' does not exist", Response.Status.CONFLICT);
            }
            ApsProperties labels = jaxbI18nLabel.extractLabels();
            this.getI18nManager().updateLabelGroup(key, labels);
        } catch (ApiException ae) {
            throw new ApiException("Error updating label", ae);
        } catch (ApsSystemException t) {
            _logger.error("Error updating label", t);
            throw new ApsSystemException("Error updating labels", t);
        }
    }

    public void deleteLabel(Properties properties) throws ApsSystemException, ApiException {
        try {
            String key = properties.getProperty("key");
            ApsProperties labelGroups = this.getI18nManager().getLabelGroup(key);
            if (null == labelGroups) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Label with key '" + key + "' does not exist", Response.Status.CONFLICT);
            }
            this.getI18nManager().deleteLabelGroup(key);
        } catch (ApiException | ApsSystemException ae) {
            _logger.error("Error deleting label", ae);
            throw new ApsSystemException("Error deleting labels", ae);
        }
    }

    protected void checkLabels(JAXBI18nLabel jaxbI18nLabel) throws ApiException {
        try {
            String key = jaxbI18nLabel.getKey();
            if (null == key || key.trim().length() == 0) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Label key required", Response.Status.CONFLICT);
            }
            ApsProperties labels = jaxbI18nLabel.extractLabels();
            if (null == labels || labels.isEmpty()) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Label list can't be empty", Response.Status.CONFLICT);
            }
            Lang defaultLang = this.getLangManager().getDefaultLang();
            Object defaultLangValue = labels.get(defaultLang.getCode());
            if (null == defaultLangValue || defaultLangValue.toString().trim().length() == 0) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                        "Label list must contain a label for the default language '" + defaultLang.getCode() + "'", Response.Status.CONFLICT);
            }
            Iterator<Object> labelCodeIter = labels.keySet().iterator();
            while (labelCodeIter.hasNext()) {
                Object langCode = labelCodeIter.next();
                Object value = labels.get(langCode);
                if (null == value || value.toString().trim().length() == 0) {
                    throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR,
                            "Label for the language '" + langCode + "' is empty", Response.Status.CONFLICT);
                }
            }
        } catch (ApiException ae) {
            throw new ApiException("Error in method checkLabels ", ae);
        }
    }

    protected II18nManager getI18nManager() {
        return _i18nManager;
    }

    public void setI18nManager(II18nManager i18nManager) {
        this._i18nManager = i18nManager;
    }

    protected ILangManager getLangManager() {
        return _langManager;
    }

    public void setLangManager(ILangManager langManager) {
        this._langManager = langManager;
    }

    private II18nManager _i18nManager;
    private ILangManager _langManager;

}
