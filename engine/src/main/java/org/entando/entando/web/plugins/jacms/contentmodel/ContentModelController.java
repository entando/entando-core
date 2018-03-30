package org.entando.entando.web.plugins.jacms.contentmodel;

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelService;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.plugins.jacms.contentmodel.validator.ContentModelValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/plugins/cms/contentmodels")
public class ContentModelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private ContentModelValidator contentModelValidator = new ContentModelValidator();

    @Autowired
    private IContentModelService contentModelService;

    protected ContentModelValidator getContentModelValidator() {
        return contentModelValidator;
    }

    public void setContentModelValidator(ContentModelValidator contentModelValidator) {
        this.contentModelValidator = contentModelValidator;
    }

    protected IContentModelService getContentModelService() {
        return contentModelService;
    }

    public void setContentModelService(IContentModelService contentModelService) {
        this.contentModelService = contentModelService;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getContentModels(RestListRequest requestList) throws JsonProcessingException {
        this.getContentModelValidator().validateRestListRequest(requestList);
        PagedMetadata<ContentModelDto> result = this.getContentModelService().getContentModels(requestList);
        this.getContentModelValidator().validateRestListResult(requestList, result);
        logger.debug("loading contentModel list -> {}", result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getRole(@PathVariable Long modelId) {
        logger.debug("loading contentModel {}", modelId);
        ContentModelDto model = this.getContentModelService().getContentModel(modelId);
        return new ResponseEntity<>(new RestResponse(model), HttpStatus.OK);
    }

}
