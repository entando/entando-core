package org.entando.entando.web.language;

import javax.validation.Valid;

import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.language.ILanguageService;
import org.entando.entando.aps.system.services.language.LanguageDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.language.model.LanguageRequest;
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

@RestController
@RequestMapping(value = "/languages")
public class LanguageController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ILanguageService languageService;
    
    protected ILanguageService getLanguageService() {
        return languageService;
    }

    public void setLanguageService(ILanguageService languageService) {
        this.languageService = languageService;
    }
    
    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLanguages(RestListRequest requestList) {
        logger.trace("loading languages list");
        PagedMetadata<LanguageDto> result = this.getLanguageService().getLanguages(requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLanguage(@PathVariable String code) {
        logger.trace("loading language {}", code);
        LanguageDto result = this.getLanguageService().getLanguage(code);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{code}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLanguage(
                                            @PathVariable String code,
                                            @Valid @RequestBody LanguageRequest languageRequest,
                                            BindingResult bindingResult) {
        logger.trace("loading language {}", code);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        LanguageDto result = this.getLanguageService().updateLanguage(code, languageRequest.getStatus());
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}
