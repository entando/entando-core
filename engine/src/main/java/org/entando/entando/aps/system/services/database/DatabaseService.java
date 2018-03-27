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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
import org.entando.entando.web.database.validator.DatabaseValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;

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
            if (null != reports) {
                reports.stream().forEach(report -> dtos.add(new ShortDumpReportDto(report)));
            }
            List<ShortDumpReportDto> sublist = requestList.getSublist(dtos);
            int size = (null != reports) ? reports.size() : 0;
            SearcherDaoPaginatedResult searchResult = new SearcherDaoPaginatedResult(size, sublist);
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
                throw new RestRourceNotFoundException(DatabaseValidator.ERRCODE_NO_DUMP_FOUND, "reportCode", reportCode);
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
    public void deleteDumpReport(String reportCode) {
        try {
            this.getDatabaseManager().deleteBackup(reportCode);
        } catch (Throwable t) {
            logger.error("error deleting database report {}", reportCode, t);
            throw new RestServerError("error deleting database report " + reportCode, t);
        }
    }

    @Override
    public List<ComponentDto> getCurrentComponents() {
        List<ComponentDto> dtos = new ArrayList<>();
        this.getComponentManager().getCurrentComponents()
                .stream().forEach(component -> dtos.add(new ComponentDto(component)));
        return dtos;
    }

    @Override
    public void startDatabaseBackup() {
        try {
            this.getDatabaseManager().createBackup();
        } catch (Throwable t) {
            logger.error("error starting backup", t);
            throw new RestServerError("error starting backup", t);
        }
    }

    @Override
    public byte[] getTableDump(String reportCode, String dataSource, String tableName) {
        byte[] bytes = null;
        try {
            InputStream stream = this.getDatabaseManager().getTableDump(tableName, dataSource, reportCode);
            if (null == stream) {
                logger.warn("no dump found with code {}, dataSource {}, table {}", reportCode, dataSource, tableName);
                BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(tableName, "tableName");
                bindingResult.reject(DatabaseValidator.ERRCODE_NO_TABLE_DUMP_FOUND, new Object[]{reportCode, dataSource, tableName}, "database.dump.table.notFound");
                throw new RestRourceNotFoundException("code - dataSource - table",
                        "'" + reportCode + "' - '" + dataSource + "' - '" + tableName + "'");
            }
            File tempFile = this.createTempFile(new Random().nextInt(100) + reportCode + "_" + dataSource + "_" + tableName, stream);
            bytes = this.fileToByteArray(tempFile);
            tempFile.delete();
        } catch (RestRourceNotFoundException r) {
            throw r;
        } catch (Throwable t) {
            logger.error("error extracting database dump with code {}, dataSource {}, table {}", reportCode, dataSource, tableName, t);
            throw new RestServerError("error extracting database dump", t);
        }
        return bytes;
    }

    protected File createTempFile(String filename, InputStream is) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + File.separator + filename;
        FileOutputStream outStream = null;
        try {
            byte[] buffer = new byte[1024];
            int length = -1;
            outStream = new FileOutputStream(filePath);
            while ((length = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (IOException t) {
            logger.error("Error on saving temporary file", t);
            throw t;
        } finally {
            if (null != outStream) {
                outStream.close();
            }
            if (null != is) {
                is.close();
            }
        }
        return new File(filePath);
    }

    private byte[] fileToByteArray(File file) throws IOException {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(file);
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            logger.error("Error creating byte array", ex);
            throw ex;
        } finally {
            if (null != fis) {
                fis.close();
            }
        }
        return bos.toByteArray();
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
