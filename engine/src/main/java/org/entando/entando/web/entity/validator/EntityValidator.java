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
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author E.Santoboni
 */
public abstract class EntityValidator extends AbstractPaginationValidator implements Validator {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String ERRCODE_ENTITY_DOES_NOT_EXIST = "1";

    public static final String ERRCODE_ENTITY_ALREADY_EXISTS = "1";

    public static final String ERRCODE_URINAME_MISMATCH = "2";

    public static final String ERRCODE_TYPE_MISMATCH = "3";

    public static final String ERRCODE_ATTRIBUTE_INVALID = "4";

    @Override
    public boolean supports(Class<?> paramClass) {
        return EntityDto.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EntityDto request = (EntityDto) target;
        if (this.existEntity(request.getId())) {
            errors.reject(ERRCODE_ENTITY_ALREADY_EXISTS, new String[]{request.getId()}, "entity.exists");
        }
    }

    public void validateBodyName(String id, EntityDto request, BindingResult bindingResult) {
        if (!StringUtils.equals(id, request.getId())) {
            bindingResult.rejectValue("id", ERRCODE_URINAME_MISMATCH, new String[]{id, request.getId()}, "entity.id.mismatch");
            throw new ValidationConflictException(bindingResult);
        }
        if (!this.existEntity(id)) {
            bindingResult.reject(ERRCODE_ENTITY_DOES_NOT_EXIST, new String[]{id}, "entity.notExists");
            throw new ResourceNotFoundException(bindingResult);
        }
    }

    public boolean existEntity(String id) {
        try {
            return (null != this.getEntityManager().getEntity(id));
        } catch (Exception e) {
            logger.error("Error extracting entity", e);
            throw new RestServerError("error extracting entity", e);
        }
    }

    protected abstract IEntityManager getEntityManager();

}
