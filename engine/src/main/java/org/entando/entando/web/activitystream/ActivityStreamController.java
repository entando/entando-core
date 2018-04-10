package org.entando.entando.web.activitystream;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordDto;
import org.entando.entando.aps.system.services.activitystream.IActivityStreamService;
import org.entando.entando.web.activitystream.valiator.ActivityStreamValidator;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/activitystream")
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

    public ActivityStreamValidator getActivityStreamValidator() {
        return activityStreamValidator;
    }

    public void setActivityStreamValidator(ActivityStreamValidator activityStreamValidator) {
        this.activityStreamValidator = activityStreamValidator;
    }

    @RestAccessControl(permission = Permission.ENTER_BACKEND)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getActivityStream(RestListRequest requestList, HttpServletRequest request) throws JsonProcessingException {

        this.getActivityStreamValidator().validateRestListRequest(requestList);
        PagedMetadata<ActionLogRecordDto> result = this.getActivityStreamService().getActivityStream(requestList, (UserDetails) request.getSession().getAttribute("user"));
        this.getActivityStreamValidator().validateRestListResult(requestList, result);
        logger.debug("loading activity stream list -> {}", result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }


}
