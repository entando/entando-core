package org.entando.entando.web.label;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/labels")
public class LabelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLables(RestListRequest requestList) {
        //        PagedMetadata<GroupDto> result = this.getGroupService().getGroups(requestList);
        //        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
        return null;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{labelCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> geLabel(@PathVariable String labelCode) {
        //        GroupDto group = this.getGroupService().getGroup(groupCode);
        //        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
        return null;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{labelCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGroup(@PathVariable String labelCode, @Valid @RequestBody LabelRequest labelRequest, BindingResult bindingResult) {
        //        //field validations
        //        if (bindingResult.hasErrors()) {
        //            throw new ValidationGenericException(bindingResult);
        //        }
        //        this.getGroupValidator().validateBodyName(groupCode, groupRequest, bindingResult);
        //        if (bindingResult.hasErrors()) {
        //            throw new ValidationGenericException(bindingResult);
        //        }
        //
        //        GroupDto group = this.getGroupService().updateGroup(groupCode, groupRequest.getName());
        //        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
        return null;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addGroup(@Valid @RequestBody LabelRequest labelRequest, BindingResult bindingResult) throws ApsSystemException {
        //        //field validations
        //        if (bindingResult.hasErrors()) {
        //            throw new ValidationGenericException(bindingResult);
        //        }
        //        //business validations 
        //        getGroupValidator().validate(groupRequest, bindingResult);
        //        if (bindingResult.hasErrors()) {
        //            throw new ValidationConflictException(bindingResult);
        //        }
        //        GroupDto dto = this.getGroupService().addGroup(groupRequest);
        //        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
        return null;
    }

    @RestAccessControl(permission = "group_delete")
    @RequestMapping(value = "/{labelCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteGroup(@PathVariable String groupName) throws ApsSystemException {
        //        logger.info("deleting {}", groupName);
        //        this.getGroupService().removeGroup(groupName);
        //        return new ResponseEntity<>(new RestResponse(groupName), HttpStatus.OK);
        return null;
    }

}
