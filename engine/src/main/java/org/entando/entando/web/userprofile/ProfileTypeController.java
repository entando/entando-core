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
package org.entando.entando.web.userprofile;

import org.entando.entando.web.dataobject.validator.DataTypeValidator;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.entity.model.AttributeTypeDto;
import org.entando.entando.aps.system.services.entity.model.EntityAttributeFullDto;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.userprofile.IUserProfileTypeService;
import org.entando.entando.aps.system.services.userprofile.model.UserProfileTypeDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.userprofile.model.ProfileTypeDtoRequest;
import org.entando.entando.web.userprofile.validator.ProfileTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author E.Santoboni
 */
@RestController
public class ProfileTypeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserProfileTypeService userProfileTypeService;

    @Autowired
    private ProfileTypeValidator profileTypeValidator;

    protected IUserProfileTypeService getUserProfileTypeService() {
        return userProfileTypeService;
    }

    public void setUserProfileTypeService(IUserProfileTypeService userProfileTypeService) {
        this.userProfileTypeService = userProfileTypeService;
    }

    protected ProfileTypeValidator getProfileTypeValidator() {
        return profileTypeValidator;
    }

    public void setProfileTypeValidator(ProfileTypeValidator profileTypeValidator) {
        this.profileTypeValidator = profileTypeValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getUserProfileTypes(RestListRequest requestList) throws JsonProcessingException {
        this.getProfileTypeValidator().validateRestListRequest(requestList);
        PagedMetadata<EntityTypeShortDto> result = this.getUserProfileTypeService().getShortUserProfileTypes(requestList);
        logger.debug("Main Response -> {}", result);
        this.getProfileTypeValidator().validateRestListResult(requestList, result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes/{profileTypeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getUserProfileType(@PathVariable String profileTypeCode) throws JsonProcessingException {
        logger.debug("Requested profile type -> {}", profileTypeCode);
        if (!this.getProfileTypeValidator().existType(profileTypeCode)) {
            throw new RestRourceNotFoundException(DataTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "Profile Type", profileTypeCode);
        }
        UserProfileTypeDto dto = this.getUserProfileTypeService().getUserProfileType(profileTypeCode);
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addUserProfileTypes(@Valid @RequestBody ProfileTypeDtoRequest bodyRequest,
            BindingResult bindingResult) throws JsonProcessingException {
        //field validations
        this.getProfileTypeValidator().validate(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        if (this.getProfileTypeValidator().existType(bodyRequest.getCode())) {
            bindingResult.reject(DataTypeValidator.ERRCODE_ENTITY_TYPE_ALREADY_EXISTS, new String[]{bodyRequest.getCode()}, "entityType.exists");
        }
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        UserProfileTypeDto result = this.getUserProfileTypeService().addUserProfileType(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes/{profileTypeCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateUserProfileType(@PathVariable String profileTypeCode,
            @Valid @RequestBody ProfileTypeDtoRequest request, BindingResult bindingResult) throws JsonProcessingException {
        int result = this.getProfileTypeValidator().validateBodyName(profileTypeCode, request, bindingResult);
        if (bindingResult.hasErrors()) {
            if (result == 404) {
                throw new RestRourceNotFoundException(DataTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "profile type", profileTypeCode);
            } else {
                throw new ValidationGenericException(bindingResult);
            }
        }
        UserProfileTypeDto dto = this.getUserProfileTypeService().updateUserProfileType(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes/{profileTypeCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteUserProfileType(@PathVariable String profileTypeCode) throws ApsSystemException {
        logger.debug("Deleting profile type -> {}", profileTypeCode);
        this.getUserProfileTypeService().deleteUserProfileType(profileTypeCode);
        Map<String, String> result = new HashMap<>();
        result.put("code", profileTypeCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    // ********************* ATTRIBUTE TYPES *********************
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypeAttributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getUserProfileAttributeTypes(RestListRequest requestList) throws JsonProcessingException {
        this.getProfileTypeValidator().validateRestListRequest(requestList);
        PagedMetadata<String> result = this.getUserProfileTypeService().getAttributeTypes(requestList);
        logger.debug("Main Response -> {}", result);
        this.getProfileTypeValidator().validateRestListResult(requestList, result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypeAttributes/{attributeTypeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getUserProfileAttributeType(@PathVariable String attributeTypeCode) throws JsonProcessingException {
        logger.debug("Extracting attribute type -> {}", attributeTypeCode);
        AttributeTypeDto attribute = this.getUserProfileTypeService().getAttributeType(attributeTypeCode);
        logger.debug("Main Response -> {}", attribute);
        return new ResponseEntity<>(new RestResponse(attribute), HttpStatus.OK);
    }

    // ********************* ATTRIBUTEs *********************
    /*
GET  /profileTypes/<profileTypeCode>/attribute/<attributeCode>
POST /profileTypes/<profileTypeCode>/attribute/
PUT  /profileTypes/<profileTypeCode>/attribute/<attributeCode>
DELETE /profileTypes/<profileTypeCode>/attribute/<attributeCode>

-> RELOAD

POST /profileTypes/refresh/<profileTypeCode>
POST /profileTypes/refresh/
     */
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes/{profileTypeCode}/attribute/{attributeCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getUserProfileAttribute(@PathVariable String profileTypeCode, @PathVariable String attributeCode) throws JsonProcessingException {
        logger.debug("Requested profile type {} - attribute {}", profileTypeCode, attributeCode);
        EntityAttributeFullDto dto = this.getUserProfileTypeService().getUserProfileAttribute(profileTypeCode, attributeCode);
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes/{profileTypeCode}/attribute", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addUserProfileAttribute(@PathVariable String profileTypeCode, @Valid @RequestBody EntityAttributeFullDto bodyRequest,
            BindingResult bindingResult) throws JsonProcessingException {
        /*
        //field validations
        this.getProfileTypeValidator().validate(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        if (this.getProfileTypeValidator().existType(bodyRequest.getCode())) {
            bindingResult.reject(DataTypeValidator.ERRCODE_ENTITY_TYPE_ALREADY_EXISTS, new String[]{bodyRequest.getCode()}, "entityType.exists");
        }
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        UserProfileTypeDto result = this.getUserProfileTypeService().addUserProfileType(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", result);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
         */
        throw new RuntimeException("TODO");
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes/{profileTypeCode}/attribute/{attributeCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateUserProfileAttribute(@PathVariable String profileTypeCode,
            @PathVariable String attributeCode, @Valid @RequestBody EntityAttributeFullDto request, BindingResult bindingResult) throws JsonProcessingException {
        /*
        int result = this.getProfileTypeValidator().validateBodyName(profileTypeCode, request, bindingResult);
        if (bindingResult.hasErrors()) {
            if (result == 404) {
                throw new RestRourceNotFoundException(DataTypeValidator.ERRCODE_ENTITY_TYPE_DOES_NOT_EXIST, "profile type", profileTypeCode);
            } else {
                throw new ValidationGenericException(bindingResult);
            }
        }
        UserProfileTypeDto dto = this.getUserProfileTypeService().updateUserProfileType(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
         */
        throw new RuntimeException("TODO");
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/profileTypes/{profileTypeCode}/attribute/{attributeCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteUserProfileAttribute(@PathVariable String profileTypeCode, @PathVariable String attributeCode) throws ApsSystemException {
        /*
        logger.debug("Deleting profile type -> {}", profileTypeCode);
        this.getUserProfileTypeService().deleteUserProfileType(profileTypeCode);
        Map<String, String> result = new HashMap<>();
        result.put("code", profileTypeCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
         */
        throw new RuntimeException("TODO");
    }

}
