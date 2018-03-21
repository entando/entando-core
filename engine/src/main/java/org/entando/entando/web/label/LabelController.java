package org.entando.entando.web.label;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import org.entando.entando.aps.system.services.label.ILabelService;
import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
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
@RequestMapping(value = "/labels")
public class LabelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ILabelService labelService;

    @Autowired
    private LabelValidator labelValidator;

    protected ILabelService getLabelService() {
        return labelService;
    }

    public void setLabelService(ILabelService labelService) {
        this.labelService = labelService;
    }

    protected LabelValidator getLabelValidator() {
        return labelValidator;
    }

    public void setLabelValidator(LabelValidator labelValidator) {
        this.labelValidator = labelValidator;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getLables(RestListRequest requestList) {
        PagedMetadata<LabelDto> result = this.getLabelService().getLabelGroups(requestList);
        return new ResponseEntity<>(new RestResponse(result.getBody(), null, result), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{labelCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> geLabelGroup(@PathVariable String labelCode) {
        logger.debug("loading label {}", labelCode);
        LabelDto label = this.getLabelService().getLabelGroup(labelCode);
        return new ResponseEntity<>(new RestResponse(label, null, new HashMap<>()), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{labelCode}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateLabelGroup(@PathVariable String labelCode, @Valid @RequestBody LabelRequest labelRequest, BindingResult bindingResult) {
        logger.debug("updating label {}", labelRequest.getKey());
        this.getLabelValidator().validateBodyName(labelCode, labelRequest, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        LabelDto group = this.getLabelService().updateLabelGroup(labelRequest);
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addLabelGroup(@Valid @RequestBody LabelRequest labelRequest) throws ApsSystemException {
        logger.debug("adding label {}", labelRequest.getKey());
        LabelDto group = this.getLabelService().addLabelGroup(labelRequest);
        return new ResponseEntity<>(new RestResponse(group), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/{labelCode}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteLabelGroup(@PathVariable String labelCode) throws ApsSystemException {
        logger.debug("deleting label {}", labelCode);
        this.getLabelService().removeLabelGroup(labelCode);
        Map<String, String> result = new HashMap<>();
        result.put("key", labelCode);
        return new ResponseEntity<>(new RestResponse(result), HttpStatus.OK);
    }

}

