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
package org.entando.entando.aps.system.services.database;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.DataSourceDumpReport;
import org.entando.entando.aps.system.services.database.model.ComponentDto;
import org.entando.entando.aps.system.services.database.model.DumpReportDto;
import org.entando.entando.aps.system.services.database.model.ShortDumpReportDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class DatabaseService implements IDatabaseService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IDatabaseManager databaseManager;
    private IComponentManager componentManager;
    
    @Override
    public int getStatus() {
        return this.getDatabaseManager().getStatus();
    }

    @Override
    public PagedMetadata<ShortDumpReportDto> getShortDumpReportDtos(RestListRequest requestList) {
        PagedMetadata<ShortDumpReportDto> result = null;
        List<ShortDumpReportDto> dtos = new ArrayList<>();
        try {
            List<DataSourceDumpReport> reports = this.getDatabaseManager().getBackupReports();
            reports.stream().forEach(report -> dtos.add(new ShortDumpReportDto(report)));
            List<ShortDumpReportDto> sublist = requestList.getSublist(dtos);
            SearcherDaoPaginatedResult searchResult = new SearcherDaoPaginatedResult(reports.size(), sublist);
            result = new PagedMetadata<>(requestList, searchResult);
            result.setBody(sublist);
        } catch (Throwable t) {
            logger.error("error extracting database reports", t);
            throw new RestServerError("error extracting database reports", t);
        }
        return result;
    }

    @Override
    public DumpReportDto getDumpReportDto(String reportCode) {
        DumpReportDto dtos = null;
        try {
            DataSourceDumpReport report = this.getDatabaseManager().getBackupReport(reportCode);
            if (null == report) {
                logger.warn("no dump found with code {}", reportCode);
                throw new RestRourceNotFoundException("reportCode", reportCode);
            }
            dtos = new DumpReportDto(report, this.getComponentManager());
        } catch (RestRourceNotFoundException r) {
            throw r;
        } catch (Throwable t) {
            logger.error("error extracting database report {}", reportCode, t);
            throw new RestServerError("error extracting database report " + reportCode, t);
        }
        return dtos;
    }

    @Override
    public List<ComponentDto> getCurrentComponents() {
        List<ComponentDto> dtos = new ArrayList<>();
        this.getComponentManager().getCurrentComponents()
                .stream().forEach(component -> dtos.add(new ComponentDto(component)));
        return dtos;
    }
    
    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void setDatabaseManager(IDatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public IComponentManager getComponentManager() {
        return componentManager;
    }

    public void setComponentManager(IComponentManager componentManager) {
        this.componentManager = componentManager;
    }
    
}
