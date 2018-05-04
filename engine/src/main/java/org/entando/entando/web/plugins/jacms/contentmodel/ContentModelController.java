package org.entando.entando.web.plugins.jacms.contentmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelService;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentModelDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.plugins.jacms.contentmodel.model.ContentModelRequest;
import org.entando.entando.web.plugins.jacms.contentmodel.validator.ContentModelValidator;
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
import org.springframework.web.bind.annotation.RequestParam;
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
        this.getContentModelValidator().validateRestListRequest(requestList, ContentModelDto.class);
        PagedMetadata<ContentModelDto> result = this.getContentModelService().getContentModels(requestList);
        this.getContentModelValidator().validateRestListResult(requestList, result);
        logger.debug("loading contentModel list -> {}", result);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getContentModel(@PathVariable Long modelId) {
        logger.debug("loading contentModel {}", modelId);
        ContentModelDto model = this.getContentModelService().getContentModel(modelId);
        return new ResponseEntity<>(new RestResponse(model), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> addContentModel(@Valid @RequestBody ContentModelRequest contentModel, BindingResult bindingResult) throws ApsSystemException {
        logger.debug("adding content model");
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ContentModelDto dto = this.getContentModelService().addContentModel(contentModel);
        return new ResponseEntity<>(new RestResponse(dto), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> updateContentModel(@PathVariable Long modelId, @Valid @RequestBody ContentModelRequest contentModelRequest, BindingResult bindingResult) {
        logger.debug("updating contentModel {}", modelId);
        //field validations
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getContentModelValidator().validateBodyName(modelId, contentModelRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        ContentModelDto role = this.getContentModelService().updateContentModel(contentModelRequest);
        return new ResponseEntity<>(new RestResponse(role), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> deleteContentModel(@PathVariable Long modelId) throws ApsSystemException {
        logger.info("deleting content model {}", modelId);
        this.getContentModelService().removeContentModel(modelId);
        Map<String, String> result = new HashMap<>();
        result.put("modelId", String.valueOf(modelId));
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{modelId}/pagereferences", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getReferences(@PathVariable Long modelId) {
        logger.debug("loading contentModel references for model {}", modelId);
        Map<String, List<String>> references = this.getContentModelService().getPageReferences(modelId);
        return new ResponseEntity<>(new RestResponse(references), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/dictionary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getDictionary(@RequestParam(value = "typeCode", required = false) String typeCode) {
        logger.debug("loading contentModel dictionary for type {}", typeCode);
        IEntityModelDictionary dictionary = this.getContentModelService().getContentModelDictionary(typeCode);
        return new ResponseEntity<>(new RestResponse(dictionary), HttpStatus.OK);
    }

}
