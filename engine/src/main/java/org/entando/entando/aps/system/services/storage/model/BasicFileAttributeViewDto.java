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
package org.entando.entando.aps.system.services.storage.model;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.util.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.storage.BasicFileAttributeView;
import org.entando.entando.aps.system.services.storage.RootFolderAttributeView;

/**
 * @author E.Santoboni
 */
public class BasicFileAttributeViewDto {

    private String name;
    private String lastModifiedTime;
    private Long size;
    private Boolean directory;
    private String path;
    private Boolean protectedFolder;

    public BasicFileAttributeViewDto() {
    }

    public BasicFileAttributeViewDto(BasicFileAttributeView bfad) {
        this.setName(bfad.getName());
        this.setDirectory(bfad.isDirectory());
        if (null != bfad.getLastModifiedTime()) {
            this.setLastModifiedTime(DateConverter.getFormattedDate(bfad.getLastModifiedTime(), SystemConstants.API_DATE_FORMAT));
        }
        this.setSize(bfad.getSize());
        if (bfad instanceof RootFolderAttributeView) {
            this.setProtectedFolder(((RootFolderAttributeView) bfad).isProtectedFolder());
        }
    }

    public void buildPath(String subfolder) {
        String path = subfolder;
        if (!StringUtils.isEmpty(path) && !path.endsWith("/")) {
            path += "/";
        }
        path += this.getName();
        this.setPath(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getDirectory() {
        return directory;
    }

    public void setDirectory(Boolean directory) {
        this.directory = directory;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getProtectedFolder() {
        return protectedFolder;
    }

    public void setProtectedFolder(Boolean protectedFolder) {
        this.protectedFolder = protectedFolder;
    }

}
