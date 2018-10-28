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
package org.entando.entando.web.language;

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.language.ILanguageService;
import org.entando.entando.aps.system.services.language.LanguageDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.language.model.LanguageRequest;
import org.entando.entando.web.language.validator.LanguageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/languages")
public class LanguageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ILanguageService languageService;

    @Autowired
    private LanguageValidator languageValidator;

    protected ILanguageService getLanguageService() {
        return languageService;
    }

    public void setLanguageService(ILanguageService languageService) {
        this.languageService = languageService;
    }

    protected LanguageValidator getLanguageValidator() {
        return languageValidator;
    }

    public void setLanguageValidator(LanguageValidator languageValidator) {
        this.languageValidator = languageValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<LanguageDto>>> getLanguages(RestListRequest requestList) {
        logger.trace("loading languages list");
        this.getLanguageValidator().validateRestListRequest(requestList, LanguageDto.class);
        PagedMetadata<LanguageDto> result = this.getLanguageService().getLanguages(requestList);
        this.getLanguageValidator().validateRestListResult(requestList, result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<LanguageDto>> getLanguage(@PathVariable String code) {
        logger.trace("loading language {}", code);
        LanguageDto result = this.getLanguageService().getLanguage(code);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<LanguageDto>> updateLanguage(@PathVariable String code,
            @Valid @RequestBody LanguageRequest languageRequest, BindingResult bindingResult) {
        logger.trace("loading language {}", code);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        LanguageDto result = this.getLanguageService().updateLanguage(code, languageRequest.getStatus());
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}
