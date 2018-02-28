package org.entando.entando.web.pagemodel;

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.pagemodel.IPageModelService;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
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
@RequestMapping(value = "/pagemodels")
public class PageModelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPageModelService pageModelService;

    protected IPageModelService getPageModelService() {
        return pageModelService;
    }

    public void setPageModelService(IPageModelService pageModelService) {
        this.pageModelService = pageModelService;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageModels(RestListRequest requestList) {
        PagedMetadata<PageModelDto> result = this.getPageModelService().getPageModels(requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }


    //    @RestAccessControl(permission = Permission.SUPERUSER)
    //    @RequestMapping(value = "/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    //    public ResponseEntity<?> getPageModel(@PathVariable String groupCode) {
    //        PageModelDto group = this.getPageModelService().getGroup(groupCode);
    //        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    //    }
    //
    //    @RestAccessControl(permission = Permission.SUPERUSER)
    //    @RequestMapping(value = "/{code}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
    //    public ResponseEntity<?> updatePageModel(@PathVariable String groupName, @Valid @RequestBody GroupRequest groupRequest, BindingResult bindingResult) {
    //        //field validations
    //        if (bindingResult.hasErrors()) {
    //            throw new ValidationGenericException(bindingResult);
    //        }
    //        this.getGroupValidator().validateBodyName(groupName, groupRequest, bindingResult);
    //        if (bindingResult.hasErrors()) {
    //            throw new ValidationGenericException(bindingResult);
    //        }
    //
    //        GroupDto group = this.getPageModelService().updateGroup(groupName, groupRequest.getDescr());
    //        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    //    }
    //
    //    @RestAccessControl(permission = Permission.SUPERUSER)
    //    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
    //    public ResponseEntity<?> addPageModel(@Valid @RequestBody GroupRequest groupRequest, BindingResult bindingResult) throws ApsSystemException {
    //        //field validations
    //        if (bindingResult.hasErrors()) {
    //            throw new ValidationGenericException(bindingResult);
    //        }
    //        //business validations 
    //        getGroupValidator().validate(groupRequest, bindingResult);
    //        if (bindingResult.hasErrors()) {
    //            throw new ValidationConflictException(bindingResult);
    //        }
    //        GroupDto dto = this.getPageModelService().addGroup(groupRequest);
    //        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    //    }
    //
    //    @RestAccessControl(permission = Permission.SUPERUSER)
    //    @RequestMapping(value = "/{code}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, name = "roleGroup")
    //    public ResponseEntity<?> deleteGroup(@PathVariable String groupName) throws ApsSystemException {
    //        logger.info("deleting {}", groupName);
    //        this.getPageModelService().removeGroup(groupName);
    //        return new ResponseEntity<>(new RestResponse(groupName), HttpStatus.OK);
    //    }
    //

}
