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
package org.entando.entando.aps.system.services.database.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.util.TableFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ComponentDto {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String code;
    private String description;
    private String artifactId;
    private String artifactGroupId;
    private String artifactVersion;

    private Map<String, List<String>> tableMapping = new HashMap<>();

    public ComponentDto() {

    }

    public ComponentDto(Component component) {
        this.setArtifactGroupId(component.getArtifactGroupId());
        this.setArtifactId(component.getArtifactId());
        this.setArtifactVersion(component.getArtifactVersion());
        this.setCode(component.getCode());
        this.setDescription(component.getDescription());
        Map<String, List<String>> mapping = component.getTableMapping();
        this.buildTableMapping(mapping);
    }

    public void buildTableMapping(Map<String, List<String>> mapping) {
        if (null == mapping) {
            return;
        }
        try {
            Iterator<String> iter = mapping.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                List<String> tableNames = new ArrayList<>();
                List<String> tableClasses = mapping.get(key);
                for (String className : tableClasses) {
                    Class tableClass = Class.forName(className);
                    String tableName = TableFactory.getTableName(tableClass);
                    tableNames.add(tableName);
                }
                this.getTableMapping().put(key, tableNames);
            }
        } catch (Throwable t) {
            logger.error("error building table mapping", t);
            throw new RestServerError("error building table mapping", t);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getArtifactGroupId() {
        return artifactGroupId;
    }

    public void setArtifactGroupId(String artifactGroupId) {
        this.artifactGroupId = artifactGroupId;
    }

    public String getArtifactVersion() {
        return artifactVersion;
    }

    public void setArtifactVersion(String artifactVersion) {
        this.artifactVersion = artifactVersion;
    }

    public Map<String, List<String>> getTableMapping() {
        return tableMapping;
    }

    public void setTableMapping(Map<String, List<String>> tableMapping) {
        this.tableMapping = tableMapping;
    }

}
