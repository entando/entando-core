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
package org.entando.entando.aps.system.services.dataobjectmodel.model;

import org.entando.entando.aps.system.services.dataobjectmodel.DataObjectModel;

/**
 * @author E.Santoboni
 */
public class DataModelDto {

    private String modelId;
    private String descr;
    private String type;
    private String model;
    private String stylesheet;

    public DataModelDto() {
    }

    public DataModelDto(DataObjectModel objectModel) {
        this.setModelId(String.valueOf(objectModel.getId()));
        this.setDescr(objectModel.getDescription());
        this.setModel(objectModel.getShape());
        this.setStylesheet(objectModel.getStylesheet());
        this.setType(objectModel.getDataType());
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStylesheet() {
        return stylesheet;
    }

    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }

    public static String getEntityFieldName(String dtoFieldName) {
        switch (dtoFieldName) {
            case "code":
                return "modelid";
            case "type":
                return "datatype";
            default:
                return dtoFieldName;
        }
    }

}
