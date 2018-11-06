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
package com.agiletec.apsadmin;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Properties;
import java.util.Set;

public class TestLabelsProperties extends TestCase {

    private static final Logger logger = LoggerFactory.getLogger(TestLabelsProperties.class);

    protected void testGlobalMessagesLabelsTranslations(String propertiesFolder) throws Throwable {
        testLabelsTranslations(propertiesFolder,
                "global-messages_en.properties",
                "global-messages_it.properties");
    }

    protected void testPackageLabelsTranslations(String propertiesFolder) throws Throwable {
        testLabelsTranslations(propertiesFolder,
                "package_en.properties",
                "package_it.properties");
    }

    protected void testLabelsTranslations(String propertiesFolder, String properties1, String properties2) throws Throwable {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources1 = resolver.getResources(propertiesFolder + properties1);
        Resource[] resources2 = resolver.getResources(propertiesFolder + properties2);
        Properties props1 = new Properties();
        Properties props2 = new Properties();
        props1.load(resources1[0].getInputStream());
        props2.load(resources2[0].getInputStream());
        Set<String> stringPropertyNames1 = props1.stringPropertyNames();
        Set<String> stringPropertyNames2 = props2.stringPropertyNames();
        stringPropertyNames1.removeAll(stringPropertyNames2);
        stringPropertyNames1.forEach((v) -> {
            logger.error("{}{} -> found error for the key {} check this or {} file to fix this error", propertiesFolder, properties1, v, properties2);
        });
        assertEquals(0, stringPropertyNames1.size());

        stringPropertyNames1 = props1.stringPropertyNames();
        stringPropertyNames2 = props2.stringPropertyNames();
        stringPropertyNames2.removeAll(stringPropertyNames1);
        stringPropertyNames2.forEach((v) -> {
            logger.error("{}{} found error for the key {} check this or {} file to fix this error", propertiesFolder, properties2, v, properties1);
        });
        assertEquals(0, stringPropertyNames2.size());
    }

}
