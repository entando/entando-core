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

import org.entando.entando.aps.system.services.dataobjectmodel.IDataObjectModelManager;
import org.entando.entando.web.dataobjectmodel.model.DataObjectModelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DataObjectModelValidator implements Validator {

    //GET
    public static final String ERRCODE_DATAOBJECTID_INVALID = "1";
    public static final String ERRCODE_DATAOBJECTMODEL_DOES_NOT_EXIST = "1";

    public static final String ERRCODE_DATAOBJECTMODEL_ALREADY_EXISTS = "1";
    public static final String ERRCODE_URINAME_MISMATCH = "2";
    public static final String ERRCODE_URINAME_INVALID = "3";
    public static final String ERRCODE_DATAOBJECTMODEL_REFERENCES = "4";

    @Autowired
    private IDataObjectModelManager dataObjectModelManager;

    @Override
    public boolean supports(Class<?> paramClass) {
        return DataObjectModelRequest.class.equals(paramClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DataObjectModelRequest request = (DataObjectModelRequest) target;
        String modelId = request.getModelId();
        int result = this.checkModelId(modelId, errors);
        if (result == 0 && null != this.dataObjectModelManager.getDataObjectModel(Long.parseLong(modelId))) {
            errors.reject(ERRCODE_DATAOBJECTMODEL_ALREADY_EXISTS, new String[]{String.valueOf(modelId)}, "dataObjectModel.exists");
        }
    }

    public void validateBodyName(String modelId, DataObjectModelRequest request, Errors errors) {
        if (!modelId.equals(request.getModelId())) {
            errors.rejectValue("modelId", ERRCODE_URINAME_MISMATCH,
                    new String[]{String.valueOf(modelId), String.valueOf(request.getModelId())}, "dataObjectModel.modelId.mismatch");
        }
    }

    public int checkModelId(String dataModelId, Errors errors) {
        Long dataModelLong = null;
        try {
            dataModelLong = Long.parseLong(dataModelId);
            if (null == this.dataObjectModelManager.getDataObjectModel(dataModelLong)) {
                errors.rejectValue("modelId", ERRCODE_DATAOBJECTMODEL_DOES_NOT_EXIST,
                        new String[]{dataModelId, String.valueOf(dataModelLong)}, "dataObjectModel.doesNotExist");
                return 404;
            }
        } catch (NumberFormatException e) {
            errors.rejectValue("modelId", ERRCODE_DATAOBJECTID_INVALID,
                    new String[]{String.valueOf(dataModelId)}, "dataObjectModel.modelId.invalid");
            return 400;
        }
        return 0;
    }

}
