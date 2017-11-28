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
package org.entando.entando.aps.system.services.i18n.inlinediting;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.i18n.inlinediting.model.JAXBI18nLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.Iterator;

/**
 * @author E.Santoboni
 */
public class ApiI18nLabelInterface extends org.entando.entando.aps.system.services.i18n.ApiI18nLabelInterface {

    private static final Logger _logger = LoggerFactory.getLogger(ApiI18nLabelInterface.class);

    public void updateInlineLabel(JAXBI18nLabel jaxbI18nLabel) throws ApiException {
        try {
            this.checkLabels(jaxbI18nLabel);
            String key = jaxbI18nLabel.getKey();
            ApsProperties labelGroups = this.getI18nManager().getLabelGroup(key);
            _logger.info("KEY ->  {} ", key);
            boolean isEdit = (null != labelGroups);
            if (!isEdit) {
                labelGroups = new ApsProperties();

            } else {
                //
            }
            ApsProperties labels = jaxbI18nLabel.extractLabels();
            Iterator<Object> iterator = labels.keySet().iterator();
            while (iterator.hasNext()) {
                Object langKey = iterator.next();
                labelGroups.put(langKey, labels.get(langKey));
            }
            if (isEdit) {
                this.getI18nManager().updateLabelGroup(key, labelGroups);
                _logger.info("*** EDIT DONE *** -> {}", labelGroups);
            } else {
                this.getI18nManager().addLabelGroup(key, labelGroups);
                _logger.info("*** ADD DONE *** -> {}", labelGroups);
            }
        } catch (ApiException | ApsSystemException t) {
            _logger.error("Error updating label {} ", t);
            throw new ApiException("Error updating labels", t);
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
            _logger.error("Error method checkLabels ", ae);
            throw new ApiException("Error method checkLabels", ae) ;
        }
    }

}
