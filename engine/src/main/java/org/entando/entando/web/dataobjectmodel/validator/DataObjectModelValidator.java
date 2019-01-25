/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.dataobjectmodel.validator;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.dataobject.IDataObjectManager;
import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;
import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.aps.system.services.jsonpatch.validator.JsonPatchValidator;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.json.patch.PatchException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class DataObjectModelValidator extends AbstractPaginationValidator {

    //GET
    public static final String ERRCODE_DATAOBJECTID_INVALID = "1";
    public static final String ERRCODE_DATAOBJECTMODEL_DOES_NOT_EXIST = "1";

    //POST
    public static final String ERRCODE_POST_DATAOBJECTTYPE_DOES_NOT_EXIST = "1";

    public static final String ERRCODE_DATAOBJECT_MODEL_INVALID = "3";

    //PUT
    public static final String ERRCODE_DATAOBJECTMODEL_ALREADY_EXISTS = "1";
    public static final String ERRCODE_PUT_DATAOBJECTTYPE_DOES_NOT_EXIST = "2";
    //public static final String ERRCODE_PUT_EXTRACTED_MISMATCH = "2";
    public static final String ERRCODE_URINAME_MISMATCH = "2";
    public static final String ERRCODE_URINAME_INVALID = "3";
    public static final String ERRCODE_DATAOBJECTMODEL_REFERENCES = "4";

    //PATCH
    public static final String ERRCODE_INVALID_PATCH = "1";

    public static final String ERRCODE_CONTENTMODEL_TYPECODE_NOT_FOUND = "6";

    @Autowired
    private IDataObjectModelManager dataObjectModelManager;

    @Autowired
    private IDataObjectManager dataObjectManager;

    private JsonPatchValidator jsonPatchValidator;

    public JsonPatchValidator getJsonPatchValidator() {
        return jsonPatchValidator;
    }

    public void setJsonPatchValidator(JsonPatchValidator jsonPatchValidator) {
        this.jsonPatchValidator = jsonPatchValidator;
    }

    @Override
    public boolean supports(Class<?> paramClass) {
        return DataObjectModelRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DataObjectModelRequest request = (DataObjectModelRequest) target;
        String modelId = request.getModelId();
        Long dataModelLong = this.checkValidModelId(modelId, errors);
        if (null != dataModelLong && null != this.dataObjectModelManager.getDataObjectModel(dataModelLong)) {
            errors.reject(ERRCODE_DATAOBJECTMODEL_ALREADY_EXISTS, new String[]{String.valueOf(modelId)}, "dataObjectModel.exists");
        }
    }

    public void validateBodyName(String modelId, DataObjectModelRequest request, Errors errors) {
        if (!modelId.equals(request.getModelId())) {
            errors.rejectValue("modelId", ERRCODE_URINAME_MISMATCH,
                    new String[]{String.valueOf(modelId), String.valueOf(request.getModelId())}, "dataObjectModel.modelId.mismatch");
        }
        this.checkValidModelId(modelId, errors);
    }

    public int validateBody(DataObjectModelRequest request, boolean isPut, Errors errors) {
        Long dataModelId = Long.parseLong(request.getModelId());
        String typeCode = request.getType();
        try {
            IApsEntity prototype = this.dataObjectManager.getEntityPrototype(typeCode);
            if (null == prototype) {
                errors.rejectValue("type", (isPut) ? ERRCODE_PUT_DATAOBJECTTYPE_DOES_NOT_EXIST : ERRCODE_POST_DATAOBJECTTYPE_DOES_NOT_EXIST,
                        new String[]{typeCode}, "dataObjectModel.type.doesNotExist");
                return 404;
            }
            if (StringUtils.isEmpty(request.getModel())) {
                errors.rejectValue("model", ERRCODE_DATAOBJECT_MODEL_INVALID, new String[]{}, "dataObjectModel.model.notBlank");
                return 400;
            }
            if (isPut) {
                DataObjectModel dataModel = this.dataObjectModelManager.getDataObjectModel(dataModelId);
                if (null == dataModel) {
                    errors.rejectValue("modelId", ERRCODE_DATAOBJECTMODEL_DOES_NOT_EXIST,
                            new String[]{String.valueOf(dataModelId)}, "dataObjectModel.doesNotExist");
                    return 404;
                }
                /*
                else if (!dataModel.getDataType().equals(typeCode)) {
                    errors.rejectValue("type", ERRCODE_PUT_EXTRACTED_MISMATCH,
                            new String[]{typeCode, dataModel.getDataType()}, "dataObjectModel.type.doesNotMachWithModel");
                    return 400;
                }
                */
            }
        } catch (Exception e) {
            throw new RuntimeException("Error extracting model", e);
        }
        return 0;
    }

    public int checkModelId(String dataModelId, Errors errors) {
        Long dataModelLong = this.checkValidModelId(dataModelId, errors);
        if (null == dataModelLong) {
            return 400;
        }
        return this.checkExistingModelId(dataModelLong, errors);
    }

    public Long checkValidModelId(String dataModelId, Errors errors) {
        Long dataModelLong = null;
        try {
            dataModelLong = Long.parseLong(dataModelId);
        } catch (NumberFormatException e) {
            errors.rejectValue("modelId", ERRCODE_DATAOBJECTID_INVALID,
                    new String[]{String.valueOf(dataModelId)}, "dataObjectModel.modelId.invalid");
            return null;
        }
        return dataModelLong;
    }

    protected int checkExistingModelId(Long dataModelId, Errors errors) {
        try {
            if (null == this.dataObjectModelManager.getDataObjectModel(dataModelId)) {
                errors.rejectValue("modelId", ERRCODE_DATAOBJECTMODEL_DOES_NOT_EXIST,
                        new String[]{String.valueOf(dataModelId)}, "dataObjectModel.doesNotExist");
                return 404;
            }
        } catch (Throwable e) {
            throw new RuntimeException("Error extracting model", e);
        }
        return 0;
    }

    public void validateDataObjectModelJsonPatch(JsonNode jsonPatch, Errors errors) {
        try {
            jsonPatchValidator.validatePatch(jsonPatch);
        } catch (PatchException e) {
            errors.reject(ERRCODE_INVALID_PATCH, "jsonPatch.invalid");
        }

        for (JsonNode node : jsonPatch) {
            String operationPath = node.get("path").asText();

            if (operationPath.equals("/modelId")) {
                errors.reject(ERRCODE_INVALID_PATCH, new String[]{"modelId"}, "jsonPatch.field.protected");
            }
        }

    }

    @Override
    protected String getDefaultSortProperty() {
        return "modelId";
    }
}
