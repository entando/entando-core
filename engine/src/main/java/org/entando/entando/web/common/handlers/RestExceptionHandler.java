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
package org.entando.entando.web.common.handlers;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.web.common.RestErrorCodes;
import org.entando.entando.web.common.exceptions.*;
import org.entando.entando.web.common.model.RestError;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestResponse processRuntimeExceptionException(RuntimeException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        RestResponse response = new RestResponse();
        RestError error = new RestError(RestErrorCodes.INTERNAL_ERROR, this.resolveLocalizedErrorMessage("GENERIC_ERROR", new Object[]{ex.getMessage()}));
        List<RestError> errors = new ArrayList<>();
        errors.add(error);
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(value = EntandoAuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public RestResponse processEntandoAuthorizationException(EntandoAuthorizationException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        RestResponse response = new RestResponse();
        RestError error = new RestError(RestErrorCodes.UNAUTHORIZED, this.resolveLocalizedErrorMessage("UNAUTHORIZED", new Object[]{ex.getUsername(), ex.getRequestURI(), ex.getMethod()}));
        List<RestError> errors = new ArrayList<>();
        errors.add(error);
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(value = EntandoTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public RestResponse processEntandoTokenException(EntandoAuthorizationException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        RestResponse response = new RestResponse();
        RestError error = new RestError(RestErrorCodes.UNAUTHORIZED, this.resolveLocalizedErrorMessage("UNAUTHORIZED", new Object[]{ex.getUsername(), ex.getRequestURI(), ex.getMethod()}));
        List<RestError> errors = new ArrayList<>();
        errors.add(error);
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(value = ResourcePermissionsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public RestResponse processEntandoTokenException(ResourcePermissionsException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        RestResponse response = null;
        if (null != ex.getBindingResult()) {
            BindingResult result = ex.getBindingResult();
            response = processAllErrors(result);
        } else {
            response = new RestResponse();
            RestError error = new RestError(RestErrorCodes.UNAUTHORIZED, this.resolveLocalizedErrorMessage("UNAUTHORIZED_ON_RESOURCE", new Object[]{ex.getUsername(), ex.getResource()}));
            List<RestError> errors = new ArrayList<>();
            errors.add(error);
            response.setErrors(errors);
        }
        return response;
    }

    @ExceptionHandler(value = RestRourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestResponse processRestRourceNotFoundEx(RestRourceNotFoundException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        RestResponse response = null;
        if (null != ex.getBindingResult()) {
            BindingResult result = ex.getBindingResult();
            response = processAllErrors(result);
        } else {
            response = new RestResponse();
            RestError error = new RestError(ex.getErrorCode(), this.resolveLocalizedErrorMessage("NOT_FOUND", new Object[]{ex.getObjectName(), ex.getObjectCode()}));
            List<RestError> errors = new ArrayList<>();
            errors.add(error);
            response.setErrors(errors);
            response.setMetaData(new HashMap<>());
        }
        return response;
    }

    @ExceptionHandler(value = ValidationConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public RestResponse processValidationError(ValidationConflictException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        BindingResult result = ex.getBindingResult();
        RestResponse response = processAllErrors(result);
        return response;
    }

    @ExceptionHandler(value = ValidationGenericException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse processValidationError(ValidationGenericException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        BindingResult result = ex.getBindingResult();
        RestResponse response = processAllErrors(result);
        return response;
    }

    @ExceptionHandler(value = ValidationUpdateSelfException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public RestResponse processValidationError(ValidationUpdateSelfException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        BindingResult result = ex.getBindingResult();
        RestResponse response = processAllErrors(result);
        return response;
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestResponse processValidationError(MethodArgumentNotValidException ex) {
        logger.debug("Handling {} error", ex.getClass().getSimpleName());
        BindingResult result = ex.getBindingResult();
        RestResponse response = processAllErrors(result);
        return response;
    }

    private RestResponse processAllErrors(BindingResult result) {
        return processAllErrors(result.getFieldErrors(), result.getGlobalErrors());
    }

    private RestResponse processAllErrors(List<FieldError> fieldErrors, List<ObjectError> objectErrors) {
        RestResponse dto = new RestResponse();
        processFieldErrors(dto, fieldErrors);
        processGlobalErrors(dto, objectErrors);
        return dto;
    }

    private RestResponse processFieldErrors(RestResponse dto, List<FieldError> fieldErrors) {
        if (null != fieldErrors) {
            List<RestError> errors = new ArrayList<>();
            for (FieldError fieldError : fieldErrors) {
                String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
                errors.add(new RestError(fieldError.getCode(), localizedErrorMessage));
            }
            dto.addErrors(errors);
        }
        return dto;
    }

    private RestResponse processGlobalErrors(RestResponse dto, List<ObjectError> globalErrors) {
        if (null != globalErrors) {
            List<RestError> errors = new ArrayList<>();
            for (ObjectError globalError : globalErrors) {
                String localizedErrorMessage = resolveLocalizedErrorMessage(globalError);
                errors.add(new RestError(globalError.getCode(), localizedErrorMessage));
            }
            dto.addErrors(errors);
        }
        return dto;
    }

    /**
     * prova ad utilizzare il default message, altrimenti va sul default
     *
     * @param fieldError
     * @return
     */
    private String resolveLocalizedErrorMessage(DefaultMessageSourceResolvable fieldError) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String msgCode = StringUtils.isNotBlank(fieldError.getDefaultMessage()) ? fieldError.getDefaultMessage() : fieldError.getCode();
        String localizedErrorMessage = getMessageSource().getMessage(msgCode, fieldError.getArguments(), currentLocale);
        return localizedErrorMessage;
    }

    private String resolveLocalizedErrorMessage(String code, Object[] args) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        String localizedErrorMessage = getMessageSource().getMessage(code, args, currentLocale);
        return localizedErrorMessage;
    }

}
