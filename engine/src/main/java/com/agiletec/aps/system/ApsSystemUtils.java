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
package com.agiletec.aps.system;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AsyncAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.action.Action;
import org.apache.logging.log4j.core.appender.rolling.action.DeleteAction;
import org.apache.logging.log4j.core.appender.rolling.action.IfAccumulatedFileCount;
import org.apache.logging.log4j.core.appender.rolling.action.PathCondition;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for system logger
 *
 * @author E.Santoboni
 */
public class ApsSystemUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApsSystemUtils.class);

    public static final String INIT_PROP_LOG_ACTIVE_FILE_OUTPUT = "logActiveFileOutput";

    public static final String INIT_PROP_LOG_NAME = "logName";

    public static final String INIT_PROP_LOG_FILE_PATTERN = "logFileRotatePattern";

    public static final String INIT_PROP_LOG_LEVEL = "logLevel";

    public static final String INIT_PROP_LOG_FILE_SIZE = "logFileSize";

    public static final String INIT_PROP_LOG_FILES_COUNT = "logFilesCount";

    private static final long KILOBYTE = 1024L;

    private Map<String, Object> systemParams;

    public void init() throws Exception {
        String active = (String) this.systemParams.get(INIT_PROP_LOG_ACTIVE_FILE_OUTPUT);
        if (StringUtils.isEmpty(active) || !active.equalsIgnoreCase("true")) {
            return;
        }
        String appenderName = "ENTANDO";
        String conversionPattern = (String) this.systemParams.get("log4jConversionPattern");
        if (StringUtils.isBlank(conversionPattern)) {
            conversionPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} - %-5p -  %c - %m%n";
        }
        String maxFileSize = (String) this.systemParams.get(INIT_PROP_LOG_FILE_SIZE);
        if (StringUtils.isBlank(maxFileSize)) {
            maxFileSize = "1MB"; //default size
        } else {
            long mega = new Long(maxFileSize) / KILOBYTE;
            maxFileSize = mega + "KB";
        }
        String filePattern = (String) this.systemParams.get(INIT_PROP_LOG_FILE_PATTERN);
        String filename = (String) this.systemParams.get(INIT_PROP_LOG_NAME);
        int maxBackupIndex = Integer.parseInt((String) this.systemParams.get(INIT_PROP_LOG_FILES_COUNT));
        String log4jLevelString = (String) this.systemParams.get(INIT_PROP_LOG_LEVEL);
        if (StringUtils.isBlank(log4jLevelString)) {
            log4jLevelString = "INFO"; //default level
        }
        Configurator.setRootLevel(Level.getLevel(log4jLevelString));
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        loggerContext.getRootLogger().setLevel(Level.getLevel(log4jLevelString));
        Configurator.setAllLevels(loggerContext.getRootLogger().getName(), Level.getLevel(log4jLevelString));
        Configuration configuration = loggerContext.getConfiguration();
        RollingFileAppender fileAppender = (RollingFileAppender) configuration.getAppender(appenderName);
        if (null == fileAppender) {
            PathCondition[] pathConditions = new PathCondition[1];
            pathConditions[0] = IfAccumulatedFileCount.createFileCountCondition(maxBackupIndex);
            String basePath = filePattern.substring(0, filePattern.lastIndexOf(File.separator));
            DeleteAction deleteAction = DeleteAction.createDeleteAction(basePath, true, 1, false, null, pathConditions, null, configuration);
            Action[] actions = new Action[1];
            actions[0] = deleteAction;
            SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy(maxFileSize);
            PatternLayout layout = PatternLayout.newBuilder().withPattern(conversionPattern).build();
            DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
                    .withConfig(configuration).withMax(String.valueOf(maxBackupIndex)).withCustomActions(actions).build();
            fileAppender = RollingFileAppender.newBuilder()
                    .withName(appenderName)
                    .setConfiguration(configuration)
                    .withLayout(layout)
                    .withFileName(filename)
                    .withFilePattern(filePattern)
                    .withPolicy(policy)
                    .withStrategy(strategy)
                    .build();
            configuration.addAppender(fileAppender);
            Configurator.setLevel(appenderName, Level.getLevel(log4jLevelString));
            fileAppender.start();
        }
        AsyncAppender async = (AsyncAppender) loggerContext.getRootLogger().getAppenders().get("async");
        if (null == async) {
            AppenderRef ref = AppenderRef.createAppenderRef(appenderName, Level.getLevel(log4jLevelString), null);
            async = AsyncAppender.newBuilder().setName("async")
                    .setConfiguration(configuration)
                    .setAppenderRefs(new AppenderRef[]{ref}).build();
            configuration.addAppender(async);
            loggerContext.getRootLogger().addAppender(async);
            async.start();
        }
        loggerContext.updateLoggers();
    }

    public static Logger getLogger() {
        return logger;
    }

    /**
     * Draw an exception on the context logger. 
     *
     * @param t The exception to trace
     * @param caller The caller class
     * @param methodName The method in which the error occurred.
     * @param message The message to include
     */
    public static void logThrowable(Throwable t, Object caller,
            String methodName, String message) {
        String className = null;
        if (caller != null) {
            className = caller.getClass().getName();
        }
        logger.error("{} in {}.{}", message, className, methodName, t);
    }

    /**
     * Draw an exception on the context logger. 
     *
     * @param t The exception to trace
     * @param caller The caller class
     * @param methodName The method in which the error occurred.
     */
    public static void logThrowable(Throwable t, Object caller, String methodName) {
        logThrowable(t, caller, methodName, "Exception");
    }

    public void setSystemParams(Map<String, Object> systemParams) {
        this.systemParams = systemParams;
    }

}