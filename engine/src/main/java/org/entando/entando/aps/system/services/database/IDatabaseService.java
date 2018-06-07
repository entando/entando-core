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

import java.util.List;
import org.entando.entando.aps.system.services.database.model.ComponentDto;
import org.entando.entando.aps.system.services.database.model.DumpReportDto;
import org.entando.entando.aps.system.services.database.model.ShortDumpReportDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

/**
 * @author E.Santoboni
 */
public interface IDatabaseService {

    public int getStatus();

    public PagedMetadata<ShortDumpReportDto> getShortDumpReportDtos(RestListRequest requestList);

    public List<ComponentDto> getCurrentComponents();

    public void startDatabaseBackup();
    
    public void startDatabaseRestore(String reportCode);

    public DumpReportDto getDumpReportDto(String reportCode);

    public void deleteDumpReport(String reportCode);

    public byte[] getTableDump(String reportCode, String dataSource, String tableName);

}
