/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.ComponentEnvironment;
import org.entando.entando.aps.system.init.model.ComponentInstallationReport;
import org.entando.entando.aps.system.init.model.DataInstallationReport;
import org.entando.entando.aps.system.init.model.DataSourceDumpReport;
import org.entando.entando.aps.system.init.model.DataSourceInstallationReport;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.aps.system.init.util.TableDataUtils;
import org.entando.entando.aps.system.init.util.TableFactory;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ServletContextAware;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.aps.util.FileTextReader;

/**
 * @author E.Santoboni
 */
public class DatabaseManager extends AbstractInitializerManager
        implements IDatabaseManager, IDatabaseInstallerManager, ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    public static final int STATUS_READY = 0;
    public static final int STATUS_DUMPING_IN_PROGRESS = 1;

    private Map<String, List<String>> entandoTableMapping;
    private Map<String, Resource> entandoDefaultSqlResources;
    private Map<String, Resource> testSqlResources;
    private Map<String, Resource> defaultSqlDump;
    private int status;

    private DatabaseDumper databaseDumper;
    private DatabaseRestorer databaseRestorer;

    private ServletContext servletContext;

    public void init() throws Exception {
        logger.debug("{} ready", this.getClass().getName());
    }

    @Override
    public SystemInstallationReport installDatabase(SystemInstallationReport report, boolean checkOnStatup) throws Exception {
        String lastLocalBackupFolder = null;
        if (null == report) {
            report = SystemInstallationReport.getInstance();
            if (checkOnStatup && !Environment.test.equals(this.getEnvironment())) {
                //non c'è db locale installato, cerca nei backup locali
                DataSourceDumpReport lastDumpReport = this.getLastDumpReport();
                if (null != lastDumpReport) {
                    lastLocalBackupFolder = lastDumpReport.getSubFolderName();
                    report.setStatus(SystemInstallationReport.Status.RESTORE);
                } else {
                    //SE NON c'è cerca il default dump
                    Map<String, Resource> sqlDump = this.getDefaultSqlDump();
                    if (null != sqlDump && sqlDump.size() > 0) {
                        report.setStatus(SystemInstallationReport.Status.RESTORE);
                    }
                }
            }
        }
        try {
            this.initMasterDatabases(report, checkOnStatup);
            List<Component> components = this.getComponentManager().getCurrentComponents();
            for (Component entandoComponentConfiguration : components) {
                this.initComponentDatabases(entandoComponentConfiguration, report, checkOnStatup);
            }
            this.initMasterDefaultResource(report, checkOnStatup);
            for (Component entandoComponentConfiguration : components) {
                this.initComponentDefaultResources(entandoComponentConfiguration, report, checkOnStatup);
            }
            if (checkOnStatup && report.getStatus().equals(SystemInstallationReport.Status.RESTORE)) {
                //ALTER SESSION SET NLS_TIMESTAMP_FORMAT = 'YYYY-MM-DD HH:MI:SS.FF'
                if (null != lastLocalBackupFolder) {
                    this.restoreBackup(lastLocalBackupFolder);
                } else {
                    this.restoreDefaultDump();
                }
            }
        } catch (Throwable t) {
            if (null != report && report.isUpdated()) {
                report.setUpdated();
                report.setStatus(SystemInstallationReport.Status.INCOMPLETE);
            }
            logger.error("Error while initializating Db Installer", t);
            throw new Exception("Error while initializating Db Installer", t);
        }
        return report;
    }

    private void initMasterDatabases(SystemInstallationReport report, boolean checkOnStatup) throws ApsSystemException {
        String logPrefix = "|   ";
        System.out.println("+ [ Component: Core ] :: SCHEMA\n" + logPrefix);
        ComponentInstallationReport componentReport = report.getComponentReport("entandoCore", true);
        DataSourceInstallationReport dataSourceReport = componentReport.getDataSourceReport();
        if (componentReport.getStatus().equals(SystemInstallationReport.Status.OK)) {
            logger.debug("{}( ok )  Already installed\n{}", logPrefix, logPrefix);
            System.out.println(logPrefix + "( ok )  Already installed\n" + logPrefix);
            return;
        }
        try {
            String[] dataSourceNames = this.extractBeanNames(DataSource.class);
            Map<String, SystemInstallationReport.Status> databasesStatus = dataSourceReport.getDatabaseStatus();
            System.out.println(logPrefix + "Starting installation");
            for (String dataSourceName : dataSourceNames) {
                if (report.getStatus().equals(SystemInstallationReport.Status.PORTING)) {
                    System.out.println(logPrefix + " - Already present! db " + dataSourceName);
                    SystemInstallationReport.Status status = (checkOnStatup)
                            ? report.getStatus()
                            : SystemInstallationReport.Status.SKIPPED;
                    databasesStatus.put(dataSourceName, status);
                    report.setUpdated();
                    continue;
                }
                SystemInstallationReport.Status dbStatus = databasesStatus.get(dataSourceName);
                if (dbStatus != null && (SystemInstallationReport.isSafeStatus(dbStatus))) {
                    System.out.println(logPrefix + "\n" + logPrefix + "( ok )  " + dataSourceName + " already installed");
                } else if (dbStatus == null || !dbStatus.equals(SystemInstallationReport.Status.OK)) {
                    DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
                    //System.out.println(logPrefix + " - '" + dataSourceName + "' Installation Started... ");
                    if (checkOnStatup) {
                        databasesStatus.put(dataSourceName, SystemInstallationReport.Status.INCOMPLETE);
                        System.out.println(logPrefix);
                        this.initMasterDatabase(dataSourceName, dataSource, dataSourceReport);
                        databasesStatus.put(dataSourceName, SystemInstallationReport.Status.OK);
                    } else {
                        databasesStatus.put(dataSourceName, SystemInstallationReport.Status.SKIPPED);
                    }
                    report.setUpdated();
                }
            }
            System.out.println(logPrefix + "\n" + logPrefix + "Installation complete\n" + logPrefix);
            logger.debug(logPrefix + "\n" + logPrefix + "Installation complete\n" + logPrefix);
        } catch (Throwable t) {
            logger.error("Error initializating master databases", t);
            throw new ApsSystemException("Error initializating master databases", t);
        }
    }

    private void initMasterDatabase(String databaseName, DataSource dataSource, DataSourceInstallationReport schemaReport) throws ApsSystemException {
        try {
            DatabaseType type = this.getDatabaseRestorer().getType(dataSource);
            if (type.equals(DatabaseType.DERBY)) {
                this.getDatabaseRestorer().initDerbySchema(dataSource);
            }
            List<String> tableClassNames = this.getEntandoTableMapping().get(databaseName);
            if (null == tableClassNames || tableClassNames.isEmpty()) {
                logger.debug("No Master Tables defined for db - " + databaseName);
                schemaReport.getDatabaseStatus().put(databaseName, SystemInstallationReport.Status.NOT_AVAILABLE);
            } else {
                this.createTables(databaseName, tableClassNames, dataSource, schemaReport);
            }
        } catch (Throwable t) {
            schemaReport.getDatabaseStatus().put(databaseName, SystemInstallationReport.Status.INCOMPLETE);
            logger.error("Error creating master tables to db {}", databaseName, t);
            throw new ApsSystemException("Error creating master tables to db " + databaseName, t);
        }
    }

    private void initComponentDatabases(Component componentConfiguration, SystemInstallationReport report, boolean checkOnStatup) throws ApsSystemException {
        String logPrefix = "|   ";
        System.out.println("+ [ Component: " + componentConfiguration.getCode() + " ] :: SCHEMA\n" + logPrefix);
        ComponentInstallationReport componentReport = report.getComponentReport(componentConfiguration.getCode(), true);
        if (componentReport.getStatus().equals(SystemInstallationReport.Status.OK)) {
            logger.debug(logPrefix + "( ok )  Already installed\n" + logPrefix);
            System.out.println(logPrefix + "( ok )  Already installed\n" + logPrefix);
            return;
        } else if (componentReport.getStatus().equals(SystemInstallationReport.Status.UNINSTALLED)) {
            logger.debug(logPrefix + "( ok )  Uninstalled\n" + logPrefix);
            System.out.println(logPrefix + "( ok )  Uninstalled\n" + logPrefix);
            return;
        }
        try {
            String[] dataSourceNames = this.extractBeanNames(DataSource.class);
            Map<String, List<String>> tableMapping = componentConfiguration.getTableMapping();
            DataSourceInstallationReport dataSourceReport = componentReport.getDataSourceReport();
            System.out.println(logPrefix + "Starting installation\n" + logPrefix);
            for (String dataSourceName : dataSourceNames) {
                List<String> tableClassNames = (null != tableMapping) ? tableMapping.get(dataSourceName) : null;
                if (null == tableClassNames || tableClassNames.isEmpty()) {
                    System.out.println(logPrefix + "( !! )  skipping " + dataSourceName + ": not available");
                    dataSourceReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.NOT_AVAILABLE);
                    report.setUpdated();
                    continue;
                }
                if (report.getStatus().equals(SystemInstallationReport.Status.PORTING)) {
                    SystemInstallationReport.Status status = (checkOnStatup)
                            ? report.getStatus()
                            : SystemInstallationReport.Status.SKIPPED;
                    dataSourceReport.getDatabaseStatus().put(dataSourceName, status);
                    logger.debug(logPrefix + "( ok )  " + dataSourceName + " already installed" + SystemInstallationReport.Status.PORTING);
                    System.out.println(logPrefix + "( ok )  " + dataSourceName + " already installed" + SystemInstallationReport.Status.PORTING);
                    continue;
                }
                SystemInstallationReport.Status schemaStatus = dataSourceReport.getDatabaseStatus().get(dataSourceName);
                if (SystemInstallationReport.isSafeStatus(schemaStatus)) {
                    //Already Done!
                    System.out.println(logPrefix + "( ok )  " + dataSourceName + " already installed" + SystemInstallationReport.Status.PORTING);
                    continue;
                }
                if (null == dataSourceReport.getDataSourceTables().get(dataSourceName)) {
                    dataSourceReport.getDataSourceTables().put(dataSourceName, new ArrayList<String>());
                }
                if (checkOnStatup) {
                    dataSourceReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.INCOMPLETE);
                    DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
                    this.createTables(dataSourceName, tableClassNames, dataSource, dataSourceReport);
                    System.out.println(logPrefix);
                    dataSourceReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.OK);
                } else {
                    dataSourceReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.SKIPPED);
                }
                report.setUpdated();
            }
            System.out.println(logPrefix + "\n" + logPrefix + "Installation complete\n" + logPrefix);
            logger.debug(logPrefix + "\n" + logPrefix + "Installation complete\n" + logPrefix);
        } catch (Throwable t) {
            logger.error("Error initializating component {}", componentConfiguration.getCode(), t);
            throw new ApsSystemException("Error initializating component " + componentConfiguration.getCode(), t);
        }
    }

    private void createTables(String databaseName, List<String> tableClassNames,
            DataSource dataSource, DataSourceInstallationReport schemaReport) throws ApsSystemException {
        try {
            DatabaseType type = this.getDatabaseRestorer().getType(dataSource);
            TableFactory tableFactory = new TableFactory(databaseName, dataSource, type);
            tableFactory.createTables(tableClassNames, schemaReport);
        } catch (Throwable t) {
            logger.error("Error creating tables to db {}", databaseName, t);
            throw new ApsSystemException("Error creating tables to db " + databaseName, t);
        }
    }

    //---------------- DATA ------------------- START
    private void initMasterDefaultResource(SystemInstallationReport report, boolean checkOnStatup) throws ApsSystemException {
        String logPrefix = "|   ";
        System.out.println("+ [ Component: Core ] :: DATA\n" + logPrefix);
        ComponentInstallationReport coreComponentReport = report.getComponentReport("entandoCore", false);
        if (coreComponentReport.getStatus().equals(SystemInstallationReport.Status.OK)) {
            String message = logPrefix + "( ok )  Already installed. " + coreComponentReport.getStatus() + "\n" + logPrefix;
            logger.debug(message);
            System.out.println(message);
            return;
        }
        DataInstallationReport dataReport = coreComponentReport.getDataReport();
        try {
            System.out.println(logPrefix + "Starting installation\n" + logPrefix);
            String[] dataSourceNames = this.extractBeanNames(DataSource.class);
            for (String dataSourceName : dataSourceNames) {
                if ((report.getStatus().equals(SystemInstallationReport.Status.PORTING)
                        || report.getStatus().equals(SystemInstallationReport.Status.RESTORE)) && checkOnStatup) {
                    dataReport.getDatabaseStatus().put(dataSourceName, report.getStatus());
                    report.setUpdated();
                    String message = logPrefix + "( ok )  " + dataSourceName + " already installed. " + report.getStatus() + "\n" + logPrefix;
                    logger.debug(message);
                    System.out.println(message);
                    continue;
                }
                SystemInstallationReport.Status schemaStatus = dataReport.getDatabaseStatus().get(dataSourceName);
                if (SystemInstallationReport.isSafeStatus(schemaStatus)) {
                    String message = logPrefix + "( ok )  " + dataSourceName + " already installed. " + report.getStatus() + "\n" + logPrefix;
                    System.out.println(message);
                    continue;
                }
                Resource resource = (Environment.test.equals(this.getEnvironment()))
                        ? this.getTestSqlResources().get(dataSourceName)
                        : this.getEntandoDefaultSqlResources().get(dataSourceName);
                String script = this.readFile(resource);
                if (null != script && script.trim().length() != 0) {
                    if (checkOnStatup) {
                        dataReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.INCOMPLETE);
                        DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
                        this.getDatabaseRestorer().initOracleSchema(dataSource);
                        TableDataUtils.valueDatabase(script, dataSourceName, dataSource, null);
                        dataReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.OK);
                        System.out.println("|   ( ok )  " + dataSourceName);
                    } else {
                        dataReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.SKIPPED);
                    }
                    report.setUpdated();
                } else {
                    System.out.println(logPrefix + "( !! )  skipping " + dataSourceName + ": not available");
                    dataReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.NOT_AVAILABLE);
                    report.setUpdated();
                }
            }
            System.out.println(logPrefix + "\n" + logPrefix + "Installation complete\n" + logPrefix);
            logger.debug(logPrefix + "\n" + logPrefix + "Installation complete\n" + logPrefix);
        } catch (Throwable t) {
            logger.error("Error initializating master DefaultResource", t);
            throw new ApsSystemException("Error initializating master DefaultResource", t);
        }
    }

    private void initComponentDefaultResources(Component componentConfiguration,
            SystemInstallationReport report, boolean checkOnStatup) throws ApsSystemException {
        String logPrefix = "|   ";
        System.out.println("+ [ Component: " + componentConfiguration.getCode() + " ] :: DATA\n" + logPrefix);
        ComponentInstallationReport componentReport = report.getComponentReport(componentConfiguration.getCode(), false);
        if (componentReport.getStatus().equals(SystemInstallationReport.Status.OK)) {
            logger.debug(logPrefix + "( ok )  Already installed\n" + logPrefix);
            System.out.println(logPrefix + "( ok )  Already installed\n" + logPrefix);
            return;
        } else if (componentReport.getStatus().equals(SystemInstallationReport.Status.UNINSTALLED)) {
            logger.debug(logPrefix + "( ok )  Uninstalled\n" + logPrefix);
            System.out.println(logPrefix + "( ok )  Uninstalled\n" + logPrefix);
            return;
        }
        DataInstallationReport dataReport = componentReport.getDataReport();
        try {
            System.out.println(logPrefix + "Starting installation\n" + logPrefix);
            String[] dataSourceNames = this.extractBeanNames(DataSource.class);
            for (String dataSourceName : dataSourceNames) {
                if ((report.getStatus().equals(SystemInstallationReport.Status.PORTING)
                        || report.getStatus().equals(SystemInstallationReport.Status.RESTORE)) && checkOnStatup) {
                    dataReport.getDatabaseStatus().put(dataSourceName, report.getStatus());
                    System.out.println("|   ( ok )  " + dataSourceName);
                    report.setUpdated();
                    continue;
                }
                DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
                SystemInstallationReport.Status dataStatus = dataReport.getDatabaseStatus().get(dataSourceName);
                if (SystemInstallationReport.isSafeStatus(dataStatus)) {
                    logger.debug(logPrefix + "( ok )  Already installed\n" + logPrefix);
                    System.out.println(logPrefix + "( ok )  Already installed\n" + logPrefix);
                    continue;
                }
                Map<String, ComponentEnvironment> environments = componentConfiguration.getEnvironments();
                String compEnvKey = (Environment.test.equals(this.getEnvironment()))
                        ? Environment.test.toString() : Environment.production.toString();
                ComponentEnvironment componentEnvironment = (null != environments) ? environments.get(compEnvKey) : null;
                Resource resource = (null != componentEnvironment) ? componentEnvironment.getSqlResources(dataSourceName) : null;
                String script = (null != resource) ? this.readFile(resource) : null;
                if (null != script && script.trim().length() > 0) {
                    if (checkOnStatup) {
                        dataReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.INCOMPLETE);
                        this.getDatabaseRestorer().initOracleSchema(dataSource);
                        TableDataUtils.valueDatabase(script, dataSourceName, dataSource, dataReport);
                        System.out.println("|   ( ok )  " + dataSourceName);
                        dataReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.OK);
                    } else {
                        dataReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.SKIPPED);
                    }
                    report.setUpdated();
                } else {
                    System.out.println(logPrefix + "( !! )  skipping " + dataSourceName + ": not available");
                    dataReport.getDatabaseStatus().put(dataSourceName, SystemInstallationReport.Status.NOT_AVAILABLE);
                    report.setUpdated();
                }
            }
            System.out.println(logPrefix + "\n" + logPrefix + "Installation complete\n" + logPrefix);
            logger.debug(logPrefix + "\n" + logPrefix + "Installation complete\n" + logPrefix);
        } catch (Throwable t) {
            logger.error("Error restoring default resources of component {}", componentConfiguration.getCode(), t);
            throw new ApsSystemException("Error restoring default resources of component " + componentConfiguration.getCode(), t);
        }
    }

    private void restoreDefaultDump() throws ApsSystemException {
        try {
            String[] dataSourceNames = this.extractBeanNames(DataSource.class);
            Map<String, Resource> defaultDump = this.getDefaultSqlDump();
            if (null == defaultDump || defaultDump.isEmpty()) {
                return;
            }
            for (String dataSourceName : dataSourceNames) {
                DataSource dataSource = (DataSource) this.getBeanFactory().getBean(dataSourceName);
                Resource resource = defaultDump.get(dataSourceName);
                String script = this.readFile(resource);
                if (null != script && script.trim().length() > 0) {
                    this.getDatabaseRestorer().initOracleSchema(dataSource);
                    TableDataUtils.valueDatabase(script, dataSourceName, dataSource, null);
                }
            }
        } catch (Throwable t) {
            logger.error("Error restoring default Dump", t);
            throw new ApsSystemException("Error restoring default Dump", t);
        }
    }

    private String readFile(Resource resource) throws Throwable {
        if (resource == null) {
            return null;
        }
        InputStream is = null;
        String text = null;
        try {
            is = resource.getInputStream();
            if (null == is) {
                return null;
            }
            text = FileTextReader.getText(is, "UTF-8");
        } catch (Throwable t) {
            logger.error("Error reading resource", t);
            throw new ApsSystemException("Error reading resource", t);
        } finally {
            if (null != is) {
                is.close();
            }
        }
        return text;
    }

    //---------------- DATA ------------------- END
    @Override
    public void createBackup() throws ApsSystemException {
        if (this.getStatus() != STATUS_READY) {
            return;
        }
        try {
            this.setStatus(DatabaseManager.STATUS_DUMPING_IN_PROGRESS);
            DatabaseDumperThread thread = new DatabaseDumperThread(this);
            String threadName = "DatabaseDumper_" + DateConverter.getFormattedDate(new Date(), "yyyyMMddHHmmss");
            thread.setName(threadName);
            thread.start();
        } catch (Throwable t) {
            this.setStatus(DatabaseManager.STATUS_READY);
            logger.error("Error while creating backup", t);
            throw new ApsSystemException("Error while creating backup", t);
        }
    }

    protected void executeBackup() throws ApsSystemException {
        try {

            this.getDatabaseDumper().createBackup(this.getEnvironment(), this.extractReport());
        } catch (Throwable t) {
            logger.error("Error while creating backup", t);
            throw new ApsSystemException("Error while creating backup", t);
        } finally {
            this.setStatus(DatabaseManager.STATUS_READY);
        }
    }

    @Override
    public void deleteBackup(String subFolderName) throws ApsSystemException {
        try {
            String directoryName = this.getLocalBackupsFolder() + subFolderName;
            this.getStorageManager().deleteDirectory(directoryName, true);
        } catch (Throwable t) {
            logger.error("Error while deleting backup", t);
            throw new ApsSystemException("Error while deleting backup", t);
        }
    }

    protected DataSourceDumpReport getLastDumpReport() throws ApsSystemException {
        if (Environment.develop.equals(this.getEnvironment())) {
            return this.getBackupReport(this.getEnvironment().toString());
        }
        List<DataSourceDumpReport> reports = this.getBackupReports();
        if (null == reports || reports.isEmpty()) {
            return null;
        }
        return reports.get(reports.size() - 1);
    }

    @Override
    public DataSourceDumpReport getBackupReport(String subFolderName) throws ApsSystemException {
        try {
            if (this.checkBackupFolder(subFolderName)) {
                return this.getDumpReport(subFolderName);
            }
        } catch (Throwable t) {
            logger.error("Error while extracting Backup Report of subfolder {}", subFolderName, t);
            throw new RuntimeException("Error while extracting Backup Report of subfolder " + subFolderName);
        }
        return null;
    }

    @Override
    public List<DataSourceDumpReport> getBackupReports() throws ApsSystemException {
        List<DataSourceDumpReport> reports = new ArrayList<DataSourceDumpReport>();
        try {
            String[] children = this.getStorageManager().listDirectory(this.getLocalBackupsFolder(), true); //backupsFolder.list();
            if (null == children || children.length == 0) {
                return null;
            }
            for (String subFolderName : children) {
                if (this.checkBackupFolder(subFolderName)) {
                    DataSourceDumpReport report = this.getDumpReport(subFolderName);
                    reports.add(report);
                }
            }
            Collections.sort(reports, new BeanComparator("date"));
        } catch (Throwable t) {
            logger.error("Error while extracting Backup Reports", t);
            throw new RuntimeException("Error while extracting Backup Reports");
        }
        return reports;
    }

    private boolean checkBackupFolder(String subFolderName) throws ApsSystemException {
        String dirName = this.getLocalBackupsFolder();
        /* shouldn't be no need to check if there is a folder for each Data Source defined
		String[] dataSourceNames = this.extractBeanNames(DataSource.class);
		for (int i = 0; i < dataSourceNames.length; i++) {
			String folderName = dirName + subFolderName + File.separator + dataSourceNames[i] + File.separator;
			String[] directoryContent = this.getStorageManager().listFile(folderName, true);
			if (null == directoryContent || directoryContent.length == 0) {
				return false;
			}
		}
         */
        String reportFileName = dirName + subFolderName + File.separator + DUMP_REPORT_FILE_NAME;
        if (!this.getStorageManager().exists(reportFileName, true)) {
            logger.warn("dump report file name not found in path {}", reportFileName);
            return false;
        }
        return true;
    }

    private DataSourceDumpReport getDumpReport(String subFolderName) throws ApsSystemException {
        InputStream is = null;
        DataSourceDumpReport report = null;
        try {
            String key = this.getLocalBackupsFolder() + subFolderName + File.separator + DUMP_REPORT_FILE_NAME;
            is = this.getStorageManager().getStream(key, true);
            String xml = FileTextReader.getText(is);
            report = new DataSourceDumpReport(xml);
        } catch (Throwable t) {
            logger.error("Error while extracting Dump Report of subfolder {}", subFolderName, t);
            throw new RuntimeException("Error while extracting Dump Report of subfolder " + subFolderName);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
        return report;
    }

    @Override
    public boolean dropAndRestoreBackup(String subFolderName) throws ApsSystemException {
        try {
            if (!this.checkBackupFolder(subFolderName)) {
                logger.error("backup not available - subfolder '{}'", subFolderName);
                return false;
            }
            //TODO future improvement - execute 'lifeline' backup
            this.getDatabaseRestorer().dropAndRestoreBackup(subFolderName);
            ApsWebApplicationUtils.executeSystemRefresh(this.getServletContext());
            return true;
        } catch (Throwable t) {
            //TODO future improvement - restore 'lifeline' backup
            logger.error("Error while restoring backup - subfolder {}", subFolderName, t);
            throw new ApsSystemException("Error while restoring backup - subfolder " + subFolderName, t);
        } finally {
            //TODO future improvement - delete 'lifeline' backup
        }
    }

    private boolean restoreBackup(String subFolderName) throws ApsSystemException {
        try {
            if (!this.checkBackupFolder(subFolderName)) {
                logger.error("backup not available - subfolder '{}'", subFolderName);
                return false;
            }
            this.getDatabaseRestorer().restoreBackup(subFolderName);
            return true;
        } catch (Throwable t) {
            logger.error("Error while restoring local backup", t);
            throw new ApsSystemException("Error while restoring local backup", t);
        }
    }

    private String[] extractBeanNames(Class beanClass) {
        ListableBeanFactory factory = (ListableBeanFactory) this.getBeanFactory();
        return factory.getBeanNamesForType(beanClass);
    }

    @Override
    public InputStream getTableDump(String tableName, String dataSourceName, String subFolderName) throws ApsSystemException {
        try {
            if (null == subFolderName) {
                return null;
            }
            StringBuilder fileName = new StringBuilder(this.getLocalBackupsFolder());
            fileName.append(subFolderName).append(File.separator)
                    .append(dataSourceName).append(File.separator).append(tableName).append(".sql");
            return this.getStorageManager().getStream(fileName.toString(), true);
        } catch (Throwable t) {
            logger.error("Error while extracting table dump - " + "table '{}' - datasource '{}' - SubFolder '{}'", tableName, dataSourceName, subFolderName, t);
            throw new RuntimeException("Error while extracting table dump - " + "table '" + tableName + "' - datasource '" + dataSourceName + "' - SubFolder '" + subFolderName + "'", t);
        }
    }

    @Override
    public DatabaseType getDatabaseType(DataSource dataSource) throws ApsSystemException {
        return this.getDatabaseRestorer().getType(dataSource);
    }

    private IStorageManager getStorageManager() {
        return (IStorageManager) this.getBeanFactory().getBean("StorageManager");
    }

    protected String getLocalBackupsFolder() {
        return this.getDatabaseDumper().getLocalBackupsFolder();
    }

    @Override
    public Map<String, List<String>> getEntandoTableMapping() {
        return entandoTableMapping;
    }

    public void setEntandoTableMapping(Map<String, List<String>> entandoTableMapping) {
        this.entandoTableMapping = entandoTableMapping;
    }

    protected Map<String, Resource> getEntandoDefaultSqlResources() {
        return entandoDefaultSqlResources;
    }

    public void setEntandoDefaultSqlResources(Map<String, Resource> entandoDefaultSqlResources) {
        this.entandoDefaultSqlResources = entandoDefaultSqlResources;
    }

    protected Map<String, Resource> getTestSqlResources() {
        return testSqlResources;
    }

    public void setTestSqlResources(Map<String, Resource> testSqlResources) {
        this.testSqlResources = testSqlResources;
    }

    protected Map<String, Resource> getDefaultSqlDump() {
        return defaultSqlDump;
    }

    public void setDefaultSqlDump(Map<String, Resource> defaultSqlDump) {
        this.defaultSqlDump = defaultSqlDump;
    }

    @Override
    public int getStatus() {
        return status;
    }

    protected void setStatus(int status) {
        this.status = status;
    }

    protected DatabaseDumper getDatabaseDumper() {
        return databaseDumper;
    }

    public void setDatabaseDumper(DatabaseDumper databaseDumper) {
        this.databaseDumper = databaseDumper;
    }

    protected DatabaseRestorer getDatabaseRestorer() {
        return databaseRestorer;
    }

    public void setDatabaseRestorer(DatabaseRestorer databaseRestorer) {
        this.databaseRestorer = databaseRestorer;
    }

    protected ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
