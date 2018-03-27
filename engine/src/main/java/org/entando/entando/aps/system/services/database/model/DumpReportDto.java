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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.init.model.DataSourceDumpReport;
import org.entando.entando.aps.system.init.model.TableDumpReport;

/**
 * @author E.Santoboni
 */
public class DumpReportDto extends ShortDumpReportDto {
    
    private Map<String, List<TableDumpReportDto>> dataSourcesReports = new HashMap<>();
	private List<ComponentInstallationReportDto> componentsHistory = new ArrayList<>();

    public DumpReportDto() {
        super();
    }
    
    public DumpReportDto(DataSourceDumpReport report, IComponentManager componentManager) {
        super(report);
        Map<String, List<TableDumpReport>> dsReports = report.getDataSourcesReports();
        for (String key : dsReports.keySet()) {
            List<TableDumpReport> list = dsReports.get(key);
            List<TableDumpReportDto> dtos = this.dataSourcesReports.get(key);
            if (null == dtos) {
                dtos = new ArrayList<>();
                this.dataSourcesReports.put(key, dtos);
            }
            for (TableDumpReport tableDumpReport : list) {
                dtos.add(new TableDumpReportDto(tableDumpReport));
            }
        }
        report.getComponentsHistory().stream()
                .forEach(ch -> this.componentsHistory.add(new ComponentInstallationReportDto(ch, componentManager)));
    }

    public Map<String, List<TableDumpReportDto>> getDataSourcesReports() {
        return dataSourcesReports;
    }

    public void setDataSourcesReports(Map<String, List<TableDumpReportDto>> dataSourcesReports) {
        this.dataSourcesReports = dataSourcesReports;
    }

    public List<ComponentInstallationReportDto> getComponentsHistory() {
        return componentsHistory;
    }

    public void setComponentsHistory(List<ComponentInstallationReportDto> componentsHistory) {
        this.componentsHistory = componentsHistory;
    }
    
}
