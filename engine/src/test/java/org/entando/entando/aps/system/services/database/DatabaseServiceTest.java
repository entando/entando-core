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
package org.entando.entando.aps.system.services.database;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.io.ByteArrayInputStream;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.init.IDatabaseManager;
import org.entando.entando.aps.system.init.model.DataSourceDumpReport;
import org.entando.entando.aps.system.services.database.model.DumpReportDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

public class DatabaseServiceTest {

    @InjectMocks
    private DatabaseService databaseService;

    @Mock
    private IDatabaseManager databaseManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void getInvalidReport() throws Throwable {
        when(databaseManager.getBackupReport(ArgumentMatchers.anyString())).thenReturn(null);
        this.databaseService.getDumpReportDto("reportCode");
    }

    @Test
    public void getValidReport() throws Throwable {
        String xml = null;
        DataSourceDumpReport report = new DataSourceDumpReport(xml);
        when(databaseManager.getBackupReport(ArgumentMatchers.anyString())).thenReturn(report);
        DumpReportDto dto = this.databaseService.getDumpReportDto("reportCode");
        Assert.assertNotNull(dto);
    }

    @Test
    public void getValidTableDump() throws Throwable {
        ByteArrayInputStream is = new ByteArrayInputStream("dump".getBytes());
        when(databaseManager.getTableDump(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(is);
        byte[] base64 = this.databaseService.getTableDump("reportCode", "dataSourcePort", "categories");
        Assert.assertNotNull(base64);
    }

    @Test(expected = RestRourceNotFoundException.class)
    public void getInValidTableDump_1() throws Throwable {
        when(databaseManager.getTableDump(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(null);
        this.databaseService.getTableDump("reportCode", "dataSourcePort", "categories");
    }

    @Test(expected = RestServerError.class)
    public void getInValidTableDump_2() throws Throwable {
        when(databaseManager.getTableDump(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                .thenThrow(new ApsSystemException("Error"));
        this.databaseService.getTableDump("reportCode", "dataSourcePort", "categories");
    }

}
