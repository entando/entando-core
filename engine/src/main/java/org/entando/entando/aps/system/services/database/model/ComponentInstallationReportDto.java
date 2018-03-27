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

import com.agiletec.aps.util.DateConverter;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.ComponentInstallationReport;

/**
 * @author E.Santoboni
 */
public class ComponentInstallationReportDto {
    
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    private String code;
    private String description;
	private String artifactId;
	private String artifactGroupId;
	private String artifactVersion;
    private String date;
    
    public ComponentInstallationReportDto(){}
    
    public ComponentInstallationReportDto(ComponentInstallationReport report, IComponentManager componentManager) {
        this.setCode(report.getComponentCode());
        this.setDate(DateConverter.getFormattedDate(report.getDate(), DATE_FORMAT));
        Component component = componentManager.getInstalledComponent(code);
        if (null != component) {
            this.setArtifactGroupId(component.getArtifactGroupId());
            this.setArtifactId(component.getArtifactId());
            this.setArtifactVersion(component.getArtifactVersion());
            this.setDescription(component.getDescription());
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
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
}
