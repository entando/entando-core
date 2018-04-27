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
package org.entando.entando.web.entity.validator;

import com.agiletec.aps.system.common.entity.IEntityManager;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.entity.model.EntityTypeDtoRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author E.Santoboni
 */
public abstract class AbstractEntityTypeValidator extends AbstractPaginationValidator implements Validator {

    public static final String ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST = "1";

    public static final String ERRCODE_ENTITY_TYPE_ALREADY_EXISTS = "1";

    public static final String ERRCODE_URINAME_MISMATCH = "6";
    public static final String ERRCODE_ENTITY_TYPE_REFERENCES = "7";

    public static final String ERRCODE_ENTITY_TYPES_EMPTY = "8";

    public static final String ERRCODE_INVALID_TYPE_CODE = "11";
    public static final String ERRCODE_INVALID_TYPE_DESCR = "12";
    public static final String ERRCODE_INVALID_ATTRIBUTE_TYPE = "13";

    public static final String ERRCODE_ENTITY_ATTRIBUTE_ALREADY_EXISTS = "14";
    public static final String ERRCODE_ENTITY_ATTRIBUTE_NOT_EXISTS = "15";
    public static final String ERRCODE_ENTITY_ATTRIBUTE_TYPE_MISMATCH = "16";
    public static final String ERRCODE_ENTITY_ATTRIBUTE_INVALID_MOVEMENT = "18";

    public static final String ERRCODE_INVALID_TEXT_RANGE = "26";
    public static final String ERRCODE_INVALID_DATE_RANGE_START = "27";
    public static final String ERRCODE_INVALID_DATE_RANGE_END = "28";
    public static final String ERRCODE_INVALID_DATE_VALUE = "29";
    public static final String ERRCODE_INVALID_NUMBER_RANGE = "30";
    public static final String ERRCODE_INVALID_OGNL_ERROR = "31";
    public static final String ERRCODE_INVALID_OGNL_HELP = "32";

    public static final String ERRCODE_INVALID_ENUMERATOR = "35";
    public static final String ERRCODE_INVALID_LIST = "36";
    public static final String ERRCODE_INVALID_COMPOSITE = "37";

    @Override
    public boolean supports(Class<?> paramClass) {
        return EntityTypeDtoRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public int validateBodyName(String typeCode, EntityTypeDtoRequest request, Errors errors) {
        if (!StringUtils.equals(typeCode, request.getCode())) {
            errors.rejectValue("code", ERRCODE_URINAME_MISMATCH, new String[]{typeCode, request.getCode()}, "entityType.code.mismatch");
            return 400;
        }
        if (!this.existType(typeCode)) {
            errors.reject(ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, new String[]{typeCode}, "entityType.notExists");
            return 404;
        }
        return 0;
    }

    public boolean existType(String typeCode) {
        return (null != this.getEntityManager().getEntityPrototype(typeCode));
    }

    protected abstract IEntityManager getEntityManager();

}
