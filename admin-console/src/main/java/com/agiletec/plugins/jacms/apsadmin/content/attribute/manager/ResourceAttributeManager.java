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
package com.agiletec.plugins.jacms.apsadmin.content.attribute.manager;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.entity.attribute.manager.TextAttributeManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.AbstractResourceAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.util.ICmsAttributeErrorCodes;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe manager degli attributi tipo risorsa (Image o Attach).
 *
 * @author E.Santoboni
 */
public class ResourceAttributeManager extends TextAttributeManager {

    @Override
    protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
        super.updateAttribute(attribute, tracer, request);
        IResourceManager resourceManager = (IResourceManager) ApsWebApplicationUtils.getBean(JacmsSystemConstants.RESOURCE_MANAGER, request);
        Map<String, List<String>> mapping = resourceManager.getMetadataMapping();
        if (null == mapping || mapping.isEmpty()) {
            return;
        }
        AbstractResourceAttribute resourceAttribute = (AbstractResourceAttribute) attribute;
        List<Lang> langs = this.getLangManager().getLangs();
        for (Lang currentLang : langs) {
            tracer.setLang(currentLang);
            String formFieldPrefix = tracer.getFormFieldName(attribute) + "_metadata_";
            mapping.keySet().stream().forEach(key -> {
                String value = request.getParameter(formFieldPrefix + key);
                if (null != value) {
                    resourceAttribute.setMetadata(key, currentLang.getCode(), value);
                }
            });
        }
    }

    protected String getResourceValueFromForm(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request, String suffix) {
        String formFieldName = tracer.getFormFieldName(attribute) + "_" + suffix;
        return request.getParameter(formFieldName);
    }

    @Override
    protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        String errorCode = attributeFieldError.getErrorCode();
        String messageKey = null;
        if (errorCode.equals(ICmsAttributeErrorCodes.INVALID_RESOURCE_GROUPS)) {
            messageKey = "ResourceAttribute.fieldError.invalidGroup";
        }
        if (null != messageKey) {
            return action.getText(messageKey);
        } else {
            return super.getCustomAttributeErrorMessage(attributeFieldError, action);
        }
    }

    @Override
    protected String getInvalidAttributeMessage() {
        return "ResourceAttribute.fieldError.invalidResource";
    }

}
