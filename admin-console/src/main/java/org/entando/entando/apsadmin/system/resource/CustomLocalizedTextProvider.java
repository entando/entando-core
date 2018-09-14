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
package org.entando.entando.apsadmin.system.resource;

import com.opensymphony.xwork2.util.StrutsLocalizedTextProvider;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author E.Santoboni
 */
public class CustomLocalizedTextProvider extends StrutsLocalizedTextProvider {

    private static final String CLASSES_FOLDER = "/WEB-INF/classes/";
    private static final String LIB_FOLDER = "/WEB-INF/lib/";
    private static final String RESOURCE_FILE_NAME = "global-messages";

    public static final String DEFAULT_LOCATION_PATTERN
            = "classpath:com/agiletec/apsadmin/global-messages*.*,"
            + "classpath:org/entando/entando/apsadmin/global-messages*.*,"
            + "classpath*:/**/plugins/**/apsadmin/**/global-messages*.*";

    private static final Logger logger = LoggerFactory.getLogger(CustomLocalizedTextProvider.class);

    public CustomLocalizedTextProvider() {
        super();
        try {
            StringTokenizer tokenizer = new StringTokenizer(this.getLocationPattern(), ",");
            while (tokenizer.hasMoreTokens()) {
                String locationPattern = tokenizer.nextToken().trim();
                Set<String> resourceDirectories = this.loadResources(locationPattern);
                Iterator<String> itr = resourceDirectories.iterator();
                while (itr.hasNext()) {
                    String directory = itr.next();
                    logger.debug("Trying to load resources from directory '{}'", directory + RESOURCE_FILE_NAME);
                    super.addDefaultResourceBundle(directory + RESOURCE_FILE_NAME);
                }
            }
        } catch (Throwable t) {
            logger.error("Error loading default resources", t);
        }
    }

    private Set<String> loadResources(String locationPattern) throws Exception {
        Set<String> resourceDirectories = new HashSet<>();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(locationPattern);
        for (Resource resource : resources) {
            String urlPath = resource.getURL().getPath();
            if (urlPath.contains(LIB_FOLDER) && urlPath.contains("!") && urlPath.indexOf(LIB_FOLDER) < urlPath.indexOf("!")) {
                int start = urlPath.indexOf("!") + 2;
                int end = urlPath.lastIndexOf("/") + 1;
                String directory = urlPath.substring(start, end);
                resourceDirectories.add(directory);
            } else if (urlPath.contains(CLASSES_FOLDER)) {
                int start = urlPath.indexOf(CLASSES_FOLDER) + CLASSES_FOLDER.length();
                int end = urlPath.lastIndexOf("/") + 1;
                String directory = urlPath.substring(start, end);
                resourceDirectories.add(directory);
            } else {
                logger.warn("Unrecognized location of resource '{}'", urlPath);
            }
        }
        return resourceDirectories;
    }

    protected String getLocationPattern() {
        return DEFAULT_LOCATION_PATTERN;
    }

}
