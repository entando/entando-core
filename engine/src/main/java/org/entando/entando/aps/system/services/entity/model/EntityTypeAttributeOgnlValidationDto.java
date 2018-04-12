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
package org.entando.entando.aps.system.services.entity.model;

import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.util.IAttributeValidationRules;
import com.agiletec.aps.system.common.entity.model.attribute.util.OgnlValidationRule;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.entity.validator.AbstractEntityTypeValidator;
import org.springframework.validation.BindingResult;

/**
 * @author E.Santoboni
 */
public class EntityTypeAttributeOgnlValidationDto {

    private String ognlExpression;
    private boolean applyOnlyToFilledAttr;
    private String helpMessage;
    private String keyForHelpMessage;
    private String errorMessage;
    private String keyForErrorMessage;

    public EntityTypeAttributeOgnlValidationDto() {

    }

    public EntityTypeAttributeOgnlValidationDto(OgnlValidationRule ognlValidationRule) {
        this.setApplyOnlyToFilledAttr(ognlValidationRule.isEvalExpressionOnValuedAttribute());
        this.setErrorMessage(ognlValidationRule.getErrorMessage());
        this.setHelpMessage(ognlValidationRule.getHelpMessage());
        this.setKeyForErrorMessage(ognlValidationRule.getErrorMessageKey());
        this.setKeyForHelpMessage(ognlValidationRule.getHelpMessageKey());
        this.setOgnlExpression(ognlValidationRule.getExpression());
    }

    public void buildAttributeOgnlValidation(String typeCode, AttributeInterface attribute, BindingResult bindingResult) {
        IAttributeValidationRules validationRules = attribute.getValidationRules();
        if (!StringUtils.isEmpty(this.getOgnlExpression())) {
            // to check into validator
            OgnlValidationRule ognlValidationRule = new OgnlValidationRule();
            if (StringUtils.isEmpty(this.getErrorMessage()) && StringUtils.isEmpty(this.getKeyForErrorMessage())) {
                this.addError(AbstractEntityTypeValidator.ERRCODE_INVALID_OGNL_ERROR, bindingResult, new String[]{typeCode, attribute.getName()}, "entityType.attribute.ognl.missingErrorMessage");
            }
            if (StringUtils.isEmpty(this.getHelpMessage()) && StringUtils.isEmpty(this.getKeyForHelpMessage())) {
                this.addError(AbstractEntityTypeValidator.ERRCODE_INVALID_OGNL_HELP, bindingResult, new String[]{typeCode, attribute.getName()}, "entityType.attribute.ognl.missingHelpMessage");
            }
            ognlValidationRule.setErrorMessage(this.getErrorMessage());
            ognlValidationRule.setErrorMessageKey(this.getKeyForErrorMessage());
            ognlValidationRule.setEvalExpressionOnValuedAttribute(this.isApplyOnlyToFilledAttr());
            ognlValidationRule.setExpression(this.getOgnlExpression());
            ognlValidationRule.setHelpMessage(this.getHelpMessage());
            ognlValidationRule.setHelpMessageKey(this.getKeyForHelpMessage());
            validationRules.setOgnlValidationRule(ognlValidationRule);
        }
    }

    protected void addError(String errorCode, BindingResult bindingResult, String[] args, String message) {
        bindingResult.reject(errorCode, args, message);
    }

    public String getOgnlExpression() {
        return ognlExpression;
    }

    public void setOgnlExpression(String ognlExpression) {
        this.ognlExpression = ognlExpression;
    }

    public boolean isApplyOnlyToFilledAttr() {
        return applyOnlyToFilledAttr;
    }

    public void setApplyOnlyToFilledAttr(boolean applyOnlyToFilledAttr) {
        this.applyOnlyToFilledAttr = applyOnlyToFilledAttr;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public void setHelpMessage(String helpMessage) {
        this.helpMessage = helpMessage;
    }

    public String getKeyForHelpMessage() {
        return keyForHelpMessage;
    }

    public void setKeyForHelpMessage(String keyForHelpMessage) {
        this.keyForHelpMessage = keyForHelpMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getKeyForErrorMessage() {
        return keyForErrorMessage;
    }

    public void setKeyForErrorMessage(String keyForErrorMessage) {
        this.keyForErrorMessage = keyForErrorMessage;
    }

}
