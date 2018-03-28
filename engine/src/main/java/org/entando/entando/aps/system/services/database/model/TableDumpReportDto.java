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

import org.entando.entando.aps.system.init.model.TableDumpReport;

/**
 * @author E.Santoboni
 */
public class TableDumpReportDto {

    private String tableName;
    private Integer rows;
    private Long requiredTime;

    public TableDumpReportDto() {

    }

    public TableDumpReportDto(TableDumpReport report) {
        this.setRequiredTime(report.getRequiredTime());
        this.setRows(report.getRows());
        this.setTableName(report.getTableName());
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Long getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(Long requiredTime) {
        this.requiredTime = requiredTime;
    }

}
