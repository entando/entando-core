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
package org.entando.entando.web.common.validator;

import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

/**
 * @author E.Santoboni
 */
public abstract class AbstractValidator implements Validator {

    public static final String ERRCODE_PAGE_INVALID = "110";
    public static final String ERRCODE_NO_ITEM_ON_PAGE = "111";
    public static final String ERRCODE_PAGE_SIZE_INVALID = "112";

    public void validateRestListRequest(RestListRequest listRequest) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(listRequest, "listRequest");
        if (listRequest.getPage() < 1) {
            bindingResult.reject(ERRCODE_PAGE_INVALID, new Object[]{}, "pagination.page.invalid");
            throw new ValidationGenericException(bindingResult);
        }
        if (listRequest.getPageSize() < 0) {
            bindingResult.reject(ERRCODE_PAGE_SIZE_INVALID, new Object[]{}, "pagination.page.size.invalid");
        } else if (listRequest.getPage() > 1 && listRequest.getPageSize() == 0) {
            bindingResult.reject(ERRCODE_NO_ITEM_ON_PAGE, new String[]{String.valueOf(listRequest.getPage())}, "pagination.item.empty");
            throw new RestRourceNotFoundException(bindingResult);
        }
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
    }

    public void validateRestListResult(RestListRequest listRequest, PagedMetadata<?> result) {
        if (listRequest.getPage() > 1 && (null == result.getBody() || result.getBody().isEmpty())) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(listRequest, "listRequest");
            bindingResult.reject(ERRCODE_NO_ITEM_ON_PAGE, new String[]{String.valueOf(listRequest.getPage())}, "pagination.item.empty");
            throw new RestRourceNotFoundException(bindingResult);
        }
    }

}
