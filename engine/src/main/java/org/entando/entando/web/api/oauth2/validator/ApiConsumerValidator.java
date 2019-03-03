/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.api.oauth2.validator;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.aps.system.services.oauth2.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.ApiConsumer;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;

@Component
public class ApiConsumerValidator extends AbstractPaginationValidator {

    public static final String ERRCODE_INVALID_GRANT_TYPE = "2";
    public static final String ERRCODE_URINAME_MISMATCH = "3";

    @Override
    public boolean supports(Class<?> clazz) {
        return ApiConsumer.class.equals(clazz) || RestListRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof ApiConsumer) {
            ApiConsumer consumer = (ApiConsumer) target;

            List<String> validGrantTypes = Arrays.asList(IOAuthConsumerManager.GRANT_TYPES);

            for (String grantType : consumer.getAuthorizedGrantTypes()) {
                if (!validGrantTypes.contains(grantType)) {
                    errors.reject(ERRCODE_INVALID_GRANT_TYPE, new String[]{grantType}, "api.consumer.grantType.invalid");
                }
            }
        }
    }

    /**
     * The secret must be not null for creating the consumer. Instead it can be
     * null during the update.
     */
    public void validateForCreate(ApiConsumer consumer) {
        if (StringUtils.isEmpty(consumer.getSecret())) {
            DataBinder dataBinder = new DataBinder(consumer);
            BindingResult bindingResult = dataBinder.getBindingResult();
            bindingResult.rejectValue("secret", "notBlank", new String[]{"secret"}, "string.notBlank");
            throw new ValidationGenericException(bindingResult);
        }
    }

    public void validateForUpdate(String consumerKey, ApiConsumer consumer) {
        if (!consumer.getKey().equals(consumerKey)) {
            DataBinder dataBinder = new DataBinder(consumer);
            BindingResult bindingResult = dataBinder.getBindingResult();
            bindingResult.rejectValue("key", ERRCODE_URINAME_MISMATCH, new String[]{consumerKey, consumer.getKey()}, "api.consumer.key.mismatch");
            throw new ValidationGenericException(bindingResult);
        }
    }

    @Override
    protected String getDefaultSortProperty() {
        return "key";
    }
}
