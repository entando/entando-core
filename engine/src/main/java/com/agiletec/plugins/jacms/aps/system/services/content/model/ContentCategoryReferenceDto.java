/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package com.agiletec.plugins.jacms.aps.system.services.content.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.DateConverter;

/**
 * @author E.Santoboni
 */
public class ContentCategoryReferenceDto {

    private String code;
    private String descr;
    private String type;
    private String lastModified;

    public ContentCategoryReferenceDto() {
    }

    public ContentCategoryReferenceDto(ContentRecordVO vo) {
        this.setCode(vo.getId());
        this.setDescr(vo.getDescription());
        this.setType(vo.getTypeCode());
        if (null != vo.getModify()) {
            this.setLastModified(DateConverter.getFormattedDate(vo.getModify(), SystemConstants.API_DATE_FORMAT));
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

}
