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
package org.entando.entando.web.activitystream;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordDto;
import org.entando.entando.aps.system.services.activitystream.IActivityStreamService;
import org.entando.entando.web.activitystream.valiator.ActivityStreamValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.SimpleRestResponse;

@RestController
@RequestMapping(value = "/activityStream")
public class ActivityStreamController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IActivityStreamService activityStreamService;

    private ActivityStreamValidator activityStreamValidator = new ActivityStreamValidator();

    protected IActivityStreamService getActivityStreamService() {
        return activityStreamService;
    }

    public void setActivityStreamService(IActivityStreamService activityStreamService) {
        this.activityStreamService = activityStreamService;
    }

    protected ActivityStreamValidator getActivityStreamValidator() {
        return activityStreamValidator;
    }

    public void setActivityStreamValidator(ActivityStreamValidator activityStreamValidator) {
        this.activityStreamValidator = activityStreamValidator;
    }

    @RestAccessControl(permission = Permission.ENTER_BACKEND)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<ActionLogRecordDto>> getActivityStream(RestListRequest requestList, HttpServletRequest request) throws JsonProcessingException {
        this.getActivityStreamValidator().validateRestListRequest(requestList, ActionLogRecordDto.class);
        PagedMetadata<ActionLogRecordDto> result = this.getActivityStreamService().getActivityStream(requestList, (UserDetails) request.getSession().getAttribute("user"));
        this.getActivityStreamValidator().validateRestListResult(requestList, result);
        logger.debug("loading activity stream list -> {}", result);
        return new ResponseEntity<>(new PagedRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.ENTER_BACKEND)
    @RequestMapping(value = "/{recordId}/like", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ActionLogRecordDto>> addLike(@PathVariable int recordId, HttpServletRequest request) throws JsonProcessingException {
        ActionLogRecordDto result = this.getActivityStreamService().addLike(recordId, (UserDetails) request.getSession().getAttribute("user"));
        logger.debug("adding like to activity stream record", result);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.ENTER_BACKEND)
    @RequestMapping(value = "/{recordId}/like", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ActionLogRecordDto>> removeLike(@PathVariable int recordId, HttpServletRequest request) throws JsonProcessingException {
        ActionLogRecordDto result = this.getActivityStreamService().removeLike(recordId, (UserDetails) request.getSession().getAttribute("user"));
        logger.debug("remove like to activity stream record", result);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.ENTER_BACKEND)
    @RequestMapping(value = "/{recordId}/comments", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ActionLogRecordDto>> addComment(@PathVariable int recordId,
            @Valid @RequestBody ActivityStreamCommentRequest commentRequest,
            BindingResult bindingResult,
            HttpServletRequest request) throws JsonProcessingException {
        this.getActivityStreamValidator().validateBodyName(recordId, commentRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ActionLogRecordDto result = this.getActivityStreamService().addComment(commentRequest, (UserDetails) request.getSession().getAttribute("user"));
        logger.debug("adding comment to activity stream record", result);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.ENTER_BACKEND)
    @RequestMapping(value = "/{recordId}/comments/{commentId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<ActionLogRecordDto>> removeComment(@PathVariable int recordId, @PathVariable int commentId, HttpServletRequest request) throws JsonProcessingException {
        ActionLogRecordDto result = this.getActivityStreamService().removeComment(recordId, commentId, (UserDetails) request.getSession().getAttribute("user"));
        logger.debug("remove comment to activity stream record", result);
        return new ResponseEntity<>(new SimpleRestResponse<>(result), HttpStatus.OK);
    }

}
