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
package org.entando.entando.aps.system.services.database.model;

import com.agiletec.aps.util.DateConverter;
import org.entando.entando.aps.system.init.model.DataSourceDumpReport;

/**
 * @author E.Santoboni
 */
public class ShortDumpReportDto {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String date;
    private String code;
    private Long requiredTime;

    public ShortDumpReportDto() {
    }

    public ShortDumpReportDto(DataSourceDumpReport report) {
        this.setRequiredTime(report.getRequiredTime());
        this.setCode(report.getSubFolderName());
        this.setDate(DateConverter.getFormattedDate(report.getDate(), DATE_FORMAT));
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(Long requiredTime) {
        this.requiredTime = requiredTime;
    }

}
