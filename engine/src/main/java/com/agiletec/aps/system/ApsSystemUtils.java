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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe di utilità. E' la classe detentrice del log di sistema.
 *
 * @author E.Santoboni
 */
public class ApsSystemUtils {

    private static final Logger logger = LoggerFactory.getLogger(ApsSystemUtils.class);

    public static final String INIT_PROP_LOG_ACTIVE_FILE_OUTPUT = "logActiveFileOutput";

    public static final String INIT_PROP_LOG_NAME = "logName";

    public static final String INIT_PROP_LOG_FILE_PREFIX = "logFilePrefix";

    public static final String INIT_PROP_LOG_LEVEL = "logLevel";

    public static final String INIT_PROP_LOG_FILE_SIZE = "logFileSize";

    public static final String INIT_PROP_LOG_FILES_COUNT = "logFilesCount";

    private static final long KILOBYTE = 1024L;

    private Map<String, Object> systemParams;

    /**
     * Inizializzazione della classe di utilità.
     *
     * @throws Exception
     */
    public void init() throws Exception {
        String active = (String) this.systemParams.get(INIT_PROP_LOG_ACTIVE_FILE_OUTPUT);
        if (StringUtils.isEmpty(active) || !active.equalsIgnoreCase("true")) {
            return;
        }
        String appenderName = "ENTANDO";
        String conversionPattern = (String) this.systemParams.get("log4jConversionPattern");
        if (StringUtils.isBlank(conversionPattern)) {
            conversionPattern = "%d{yyyy-MM-dd HH:mm:ss.SSS} - %-5p -  %c - %m%n"; //default conversionPattern
        }

        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = loggerContext.getConfiguration();
        //LoggerConfig rootLoggerConfig = configuration.getLoggerConfig("");

        PatternLayout layout = PatternLayout.createDefaultLayout(configuration);
        String maxFileSize = (String) this.systemParams.get(INIT_PROP_LOG_FILE_SIZE);
        if (StringUtils.isBlank(maxFileSize)) {
            maxFileSize = "1MB"; //default size
        } else {
            long mega = new Long(maxFileSize) / KILOBYTE;
            maxFileSize = mega + "KB";
        }
        String filename = (String) this.systemParams.get(INIT_PROP_LOG_FILE_PREFIX);
        int maxBackupIndex = Integer.parseInt((String) this.systemParams.get(INIT_PROP_LOG_FILES_COUNT));
        String log4jLevelString = (String) this.systemParams.get(INIT_PROP_LOG_LEVEL);
        if (StringUtils.isBlank(log4jLevelString)) {
            log4jLevelString = "INFO"; //default level
        }

        /*
        RolloverStrategy strategy = new RolloverStrategy() {
            @Override
            public RolloverDescription rollover(RollingFileManager rfm) throws SecurityException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
         */
        //ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        SizeBasedTriggeringPolicy policy = SizeBasedTriggeringPolicy.createPolicy(maxFileSize);
        DefaultRolloverStrategy strategy = DefaultRolloverStrategy.newBuilder()
                .withMax(String.valueOf(maxBackupIndex)).build();
        RollingFileAppender fileAppender = RollingFileAppender.newBuilder()
                .withName(appenderName)
                //.setConfiguration(configuration)
                .withLayout(layout)
                .withFileName(filename)
                .withPolicy(policy)
                .withStrategy(strategy)
                .build();
        fileAppender.start();
        LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        lc.getConfiguration().addAppender(fileAppender);
        lc.getRootLogger().addAppender(lc.getConfiguration().getAppender(fileAppender.getName()));
        lc.updateLoggers();

        //appender.setHandler(handler);
        /*
        //FileAppender fileAppender = (FileAppender) LogManager.getRootLogger().getAppender(appenderName);
        if (null == fileAppender) {
            fileAppender = new RollingFileAppender();
            fileAppender.setName(appenderName);
        }
        fileAppender.setMaxBackupIndex(maxBackupIndex);
        fileAppender.setThreshold(org.apache.log4j.Level.toLevel(log4jLevelString));
        fileAppender.setLayout(layout);
        fileAppender.setMaxFileSize(maxFileSize);
        fileAppender.setFile(filename);
        fileAppender.activateOptions();
         */
 /*
        org.apache.log4j.Logger logbackLogger
                = (org.apache.log4j.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logbackLogger.addAppender(fileAppender);
         */
 /*
        AsyncAppender async = (AsyncAppender) LogManager.getRootLogger();//.getAppender("async");
        if (null == async) {
            async = new AsyncAppender();
            async.setName("async");
            LogManager.getRootLogger().addAppender(async);
        }
        async.addAppender(fileAppender);
         */
    }

    /**
     * Restituisce il logger di sistema.
     *
     * @return Il logger
     */
    public static Logger getLogger() {
        return logger;
    }

    /**
     * Traccia una eccezione sul logger del contesto. Se il livello di soglia
     * del logger è superiore a FINER, viene emesso solo un breve messaggio di
     * livello SEVERE, altrimenti viene tracciato anche lo stack trace della
     * eccezione (con il livello FINER).
     *
     * @param t L'eccezione da tracciare
     * @param caller La classe chiamante, in cui si è verificato l'errore.
     * @param methodName Il metodo in cui si è verificato l'errore.
     * @param message Testo da includere nel tracciamento.
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
     * Traccia una eccezione sul logger del contesto. Se il livello di soglia
     * del logger è superiore a FINER, viene emesso solo un breve messaggio di
     * livello SEVERE, altrimenti viene tracciato anche lo stack trace della
     * eccezione (con il livello FINER).
     *
     * @param t L'eccezione da tracciare
     * @param caller La classe chiamante, in cui si è verificato l'errore.
     * @param methodName Il metodo in cui si è verificato l'errore.
     */
    public static void logThrowable(Throwable t, Object caller, String methodName) {
        logThrowable(t, caller, methodName, "Exception");
    }

    /**
     * Setta la mappa dei parametri di inizializzazione.
     *
     * @param systemParams I parametri di inizializzazione.
     */
    public void setSystemParams(Map<String, Object> systemParams) {
        this.systemParams = systemParams;
    }

}
