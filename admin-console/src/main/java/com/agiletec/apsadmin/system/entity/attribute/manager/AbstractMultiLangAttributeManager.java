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
package com.agiletec.apsadmin.system.entity.attribute.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * Abstract class for the managers of simple multi-language attributes.
 * @author E.Santoboni
 */
public abstract class AbstractMultiLangAttributeManager extends AbstractAttributeManager {

	@Override
    protected void updateAttribute(AttributeInterface attribute, AttributeTracer tracer, HttpServletRequest request) {
        List<Lang> langs = this.getLangManager().getLangs();
        for (int i = 0; i < langs.size(); i++) {
            Lang currentLang = langs.get(i);
            tracer.setLang(currentLang);
            String value = this.getValueFromForm(attribute, tracer, request);
            //TODO PAY ATTENTION TO THIS CHECK
            if (value != null) {
                if (value.trim().length() == 0) {
                    value = null;
                }
                this.setValue(attribute, currentLang, value);
            }
        }
    }

    /**
     * Set the value of the specified attribute.
     *
     * @param attribute The current attribute (simple or composed) to update.
     * @param lang The language in which the value is expressed.
     * @param value The value to assign to the attribute.
     */
    protected abstract void setValue(AttributeInterface attribute, Lang lang, String value);

}
