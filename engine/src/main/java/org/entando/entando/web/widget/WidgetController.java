
package org.entando.entando.web.widget;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.aps.system.services.widget.IWidgetService;
import org.entando.entando.aps.system.services.widget.model.WidgetDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.entando.entando.web.widget.validator.WidgetValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WidgetController {

    //TODO check permissions

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    IWidgetService widgetService;

    @Autowired
    private WidgetValidator widgetValidator;

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/widgets/{widgetCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getWidget(@PathVariable String widgetCode) {
        WidgetDto group = this.widgetService.getWidget(widgetCode);
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = "widget_delete")
    @RequestMapping(value = "/widgets/{widgetCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, name = "widget")
    public ResponseEntity<?> deleteWidget(@PathVariable String widgetCode) throws ApsSystemException {
        logger.info("deleting widget {}", widgetCode);
        this.widgetService.removeWidget(widgetCode);
        return new ResponseEntity<>(new RestResponse(widgetCode), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/widgets/{widgetCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, name = "widget")
    public ResponseEntity<?> updateWidget(@PathVariable String widgetCode, @Valid @RequestBody WidgetRequest widgetRequest, BindingResult bindingResult) {

        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.widgetValidator.validateWidgetCode(widgetCode, widgetRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        WidgetDto widgetDto = this.widgetService.updateWidget(widgetCode, widgetRequest);
        return new ResponseEntity<>(new RestResponse(widgetDto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/widgets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, name = "widget")
    public ResponseEntity<?> addWidget(@Valid @RequestBody WidgetRequest widgetRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations
        this.widgetValidator.validate(widgetRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        WidgetDto widgetDto = this.widgetService.addWidget(widgetRequest);
        return new ResponseEntity<>(new RestResponse(widgetDto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/widgets", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGroups(RestListRequest requestList) {
        PagedMetadata<WidgetDto> result = this.widgetService.getWidgets(requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }
}
