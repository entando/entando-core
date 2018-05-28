/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

public class TestEntandoJndiUtils {

    private static final Logger logger = LoggerFactory.getLogger(TestEntandoJndiUtils.class);

    public static void setupJndi() {
        SimpleNamingContextBuilder builder = null;
        try {
            String path = "target/test/conf/contextTestParams.properties";
            logger.debug("CREATING JNDI RESOURCES BASED ON {} (test)", path);

            InputStream in = new FileInputStream(path);
            builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
            Properties testConfig = new Properties();
            testConfig.load(in);
            in.close();

            buildContextProperties(builder, testConfig);

            createDatasources(builder, testConfig);
            builder.activate();
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("Error on creation naming context", t);
        }

    }

    private static void buildContextProperties(SimpleNamingContextBuilder builder, Properties testConfig) {
        builder.bind("java:comp/env/logName", testConfig.getProperty("logName"));
        builder.bind("java:comp/env/logFilePrefix", testConfig.getProperty("logFilePrefix"));
        builder.bind("java:comp/env/logLevel", testConfig.getProperty("logLevel"));
        builder.bind("java:comp/env/logFileSize", testConfig.getProperty("logFileSize"));
        builder.bind("java:comp/env/logFilesCount", testConfig.getProperty("logFilesCount"));

        builder.bind("java:comp/env/configVersion", testConfig.getProperty("configVersion"));

        builder.bind("java:comp/env/applicationBaseURL", testConfig.getProperty("applicationBaseURL"));
        builder.bind("java:comp/env/resourceRootURL", testConfig.getProperty("resourceRootURL"));
        builder.bind("java:comp/env/protectedResourceRootURL", testConfig.getProperty("protectedResourceRootURL"));
        builder.bind("java:comp/env/resourceDiskRootFolder", testConfig.getProperty("resourceDiskRootFolder"));
        builder.bind("java:comp/env/protectedResourceDiskRootFolder", testConfig.getProperty("protectedResourceDiskRootFolder"));

        builder.bind("java:comp/env/indexDiskRootFolder", testConfig.getProperty("indexDiskRootFolder"));
        builder.bind("java:comp/env/portDataSourceClassName", testConfig.getProperty("portDataSourceClassName"));
        builder.bind("java:comp/env/servDataSourceClassName", testConfig.getProperty("servDataSourceClassName"));
        Iterator<Entry<Object, Object>> configIter = testConfig.entrySet().iterator();
        while (configIter.hasNext()) {
            Entry<Object, Object> entry = configIter.next();
            builder.bind("java:comp/env/" + (String) entry.getKey(), (String) entry.getValue());
            logger.trace("{} : {}", entry.getKey(), entry.getValue());
        }
    }

    private static void createDatasources(SimpleNamingContextBuilder builder, Properties testConfig) {
        List<String> dsNameControlKeys = new ArrayList<String>();
        Enumeration<Object> keysEnum = testConfig.keys();
        while (keysEnum.hasMoreElements()) {
            String key = (String) keysEnum.nextElement();
            if (key.startsWith("jdbc.")) {
                String[] controlKeys = key.split("\\.");
                String dsNameControlKey = controlKeys[1];
                if (!dsNameControlKeys.contains(dsNameControlKey)) {
                    createDatasource(dsNameControlKey, builder, testConfig);
                    dsNameControlKeys.add(dsNameControlKey);
                }
            }
        }
    }

    private static void createDatasource(String dsNameControlKey, SimpleNamingContextBuilder builder, Properties testConfig) {
        String beanName = testConfig.getProperty("jdbc." + dsNameControlKey + ".beanName");
        try {
            String className = testConfig.getProperty("jdbc." + dsNameControlKey + ".driverClassName");
            String url = testConfig.getProperty("jdbc." + dsNameControlKey + ".url");
            String username = testConfig.getProperty("jdbc." + dsNameControlKey + ".username");
            String password = testConfig.getProperty("jdbc." + dsNameControlKey + ".password");
            Class.forName(className);
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setMaxActive(8);
            ds.setMaxIdle(4);
            ds.setDriverClassName(className);
            builder.bind("java:comp/env/jdbc/" + beanName, ds);
        } catch (Throwable t) {
            throw new RuntimeException("Error on creation datasource '" + beanName + "'", t);
        }
        logger.debug("created datasource {}", beanName);
    }
}
