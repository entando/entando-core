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

import com.agiletec.aps.system.services.role.Permission;
import com.fasterxml.jackson.core.JsonProcessingException;
import javax.validation.Valid;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.services.entity.model.EntityDto;
import org.entando.entando.aps.system.services.userprofile.IUserProfileService;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.entity.validator.EntityValidator;
import org.entando.entando.web.userprofile.validator.ProfileValidator;
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
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private ProfileValidator profileValidator;

    protected IUserProfileService getUserProfileService() {
        return userProfileService;
    }

    public void setUserProfileService(IUserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public ProfileValidator getProfileValidator() {
        return profileValidator;
    }

    public void setProfileValidator(ProfileValidator profileValidator) {
        this.profileValidator = profileValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/userProfiles/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getUserProfile(@PathVariable String username) throws JsonProcessingException {
        logger.debug("Requested profile -> {}", username);
        if (!this.getProfileValidator().existProfile(username)) {
            throw new RestRourceNotFoundException(EntityValidator.ERRCODE_ENTITY_DOES_NOT_EXIST, "Profile", username);
        }
        EntityDto dto = this.getUserProfileService().getUserProfile(username);
        logger.debug("Main Response -> {}", dto);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/userProfiles", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addUserProfile(@Valid @RequestBody EntityDto bodyRequest, BindingResult bindingResult) {
        logger.debug("Add new user profile -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getProfileValidator().validate(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        EntityDto response = this.getUserProfileService().addUserProfile(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return new ResponseEntity<>(new RestResponse(response), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/userProfiles/{username}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateUserProfile(@PathVariable String username,
            @Valid @RequestBody EntityDto bodyRequest, BindingResult bindingResult) {
        logger.debug("Update user profile -> {}", bodyRequest);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getProfileValidator().validateBodyName(username, bodyRequest, bindingResult);
        EntityDto response = this.getUserProfileService().updateUserProfile(bodyRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        return new ResponseEntity<>(new RestResponse(response), HttpStatus.OK);
    }

}
