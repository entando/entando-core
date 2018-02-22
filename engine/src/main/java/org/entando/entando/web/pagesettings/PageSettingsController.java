/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.pagesettings;

import javax.validation.Valid;
import org.entando.entando.aps.system.services.pagesettings.IPageSettingsService;
import org.entando.entando.aps.system.services.pagesettings.model.PageSettingsDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.pagesettings.model.PageSettingsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author paddeo
 */
@RestController
public class PageSettingsController {

    @Autowired
    private IPageSettingsService pageSettingsService;

    public IPageSettingsService getPageSettingsService() {
        return pageSettingsService;
    }

    public void setPageSettingsService(IPageSettingsService pageSettingsService) {
        this.pageSettingsService = pageSettingsService;
    }

    @RestAccessControl(permission = "pageSettings_read")
    @RequestMapping(value = "/pageSettings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPageSettings() {
        PageSettingsDto pageSettings = this.getPageSettingsService().getPageSettings();
        return new ResponseEntity<>(new RestResponse(pageSettings), HttpStatus.OK);
    }

    @RestAccessControl(permission = "pageSettings_write")
    @RequestMapping(value = "/pageSettings", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePageSettings(@Valid @RequestBody PageSettingsRequest request, BindingResult bindingResult) {
        //params validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        PageSettingsDto pageSettings = this.getPageSettingsService().updatePageSettings(request);
        return new ResponseEntity<>(new RestResponse(pageSettings), HttpStatus.OK);
    }

}
