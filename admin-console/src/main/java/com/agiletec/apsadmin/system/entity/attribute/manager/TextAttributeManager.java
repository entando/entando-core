/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
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
package com.agiletec.apsadmin.system.entity.attribute.manager;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.util.TextAttributeValidationRules;
import com.agiletec.aps.system.services.lang.Lang;
import com.opensymphony.xwork2.ActionSupport;

/**
 * Manager class for the 'Multi-language Text' Attribute.
 * @author E.Santoboni
 */
public class TextAttributeManager extends AbstractMultiLangAttributeManager {

	@Override
    protected void setValue(AttributeInterface attribute, Lang lang, String value) {
        ((TextAttribute) attribute).setText(value, lang.getCode());
    }

	@Override
    protected String getCustomAttributeErrorMessage(AttributeFieldError attributeFieldError, ActionSupport action) {
        AttributeInterface attribute = attributeFieldError.getAttribute();
        TextAttributeValidationRules valRules = (TextAttributeValidationRules) attribute.getValidationRules();
        if (null != valRules) {
            ITextAttribute textAttribute = (ITextAttribute) attribute;
            Lang lang = attributeFieldError.getTracer().getLang();
            String langCode = (null != lang) ? lang.getCode() : null;
            String text = textAttribute.getTextForLang(langCode);
            String errorCode = attributeFieldError.getErrorCode();
            if (errorCode.equals(FieldError.INVALID_MIN_LENGTH)) {
                String[] args = {String.valueOf(text.length()), String.valueOf(valRules.getMinLength()), lang.getDescr()};
                return action.getText("TextAttribute.fieldError.invalidMinLength", args);
            } else if (errorCode.equals(FieldError.INVALID_MAX_LENGTH)) {
                String[] args = {String.valueOf(text.length()), String.valueOf(valRules.getMaxLength()), lang.getDescr()};
                return action.getText("TextAttribute.fieldError.invalidMaxLength", args);
            } else if (errorCode.equals(FieldError.INVALID_FORMAT)) {
                String[] args = {lang.getDescr()};
                return action.getText("TextAttribute.fieldError.invalidInsertedText", args);
            }
        }
        return action.getText(this.getInvalidAttributeMessage());
    }

}