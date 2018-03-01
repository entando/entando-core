/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.page;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.validator.PageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author paddeo
 */
@RestController
public class PageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String ERRCODE_PAGE_ALREADY_EXISTS = "101";
    public static final String ERRCODE_URINAME_MISMATCH = "102";
    public static final String ERRCODE_ONLINE_PAGE = "103";
    public static final String ERRCODE_PAGE_HAS_CHILDREN = "104";

    @Autowired
    private IPageService pageService;

    @Autowired
    private PageValidator pageValidator;

    public IPageService getPageService() {
        return pageService;
    }

    public void setPageService(IPageService pageService) {
        this.pageService = pageService;
    }

    public PageValidator getPageValidator() {
        return pageValidator;
    }

    public void setPageValidator(PageValidator pageValidator) {
        this.pageValidator = pageValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages/{parentCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPages(@PathVariable String parentCode) {
        List<PageDto> result = this.getPageService().getPages(parentCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/page/{pageCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPage(@PathVariable String pageCode) {
        PageDto page = this.getPageService().getPage(pageCode);
        return new ResponseEntity<>(new RestResponse(page), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/page/{pageCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePage(@PathVariable String pageCode, @Valid @RequestBody PageRequest pageRequest, BindingResult bindingResult) {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageValidator().validateBodyCode(pageCode, pageRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }

        PageDto page = this.getPageService().updatePage(pageCode, pageRequest);
        return new ResponseEntity<>(new RestResponse(page), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pages", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addPage(@Valid @RequestBody PageRequest pageRequest, BindingResult bindingResult) throws ApsSystemException {
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations 
        getPageValidator().validate(pageRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationConflictException(bindingResult);
        }
        PageDto dto = this.getPageService().addPage(pageRequest);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = "page_delete")
    @RequestMapping(value = "/pages/{pageCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deletePage(@PathVariable String pageCode) throws ApsSystemException {
        logger.info("deleting {}", pageCode);
        DataBinder binder = new DataBinder(pageCode);
        BindingResult bindingResult = binder.getBindingResult();
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations 
        getPageValidator().validateOnlinePage(pageCode, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        //business validations 
        getPageValidator().validateChildren(pageCode, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getPageService().removePage(pageCode);
        return new ResponseEntity<>(new RestResponse(pageCode), HttpStatus.OK);
    }

}
