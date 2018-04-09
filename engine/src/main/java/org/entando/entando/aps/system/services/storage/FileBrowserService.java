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
package org.entando.entando.aps.system.services.storage;

import java.util.Arrays;
import java.util.List;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.storage.model.BasicFileAttributeViewDto;
import org.entando.entando.web.filebrowser.validator.FileBrowserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class FileBrowserService implements IFileBrowserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IStorageManager storageManager;

    protected IDtoBuilder<BasicFileAttributeView, BasicFileAttributeViewDto> getFileAttributeViewDtoDtoBuilder() {
        return new DtoBuilder<BasicFileAttributeView, BasicFileAttributeViewDto>() {
            @Override
            protected BasicFileAttributeViewDto toDto(BasicFileAttributeView src) {
                return new BasicFileAttributeViewDto(src);
            }
        };
    }

    @Override
    public List<BasicFileAttributeViewDto> browseFolder(String currentPath, boolean protectedFolder) {
        try {
            if (!this.getStorageManager().exists(currentPath, protectedFolder)) {
                logger.warn("no folder found for path {} - type {}", currentPath, protectedFolder);
                throw new RestRourceNotFoundException(FileBrowserValidator.ERRCODE_FOLDER_DOES_NOT_EXIST, "folder", currentPath);
            }
            BasicFileAttributeView[] views = this.getStorageManager().listAttributes(currentPath, protectedFolder);
            List<BasicFileAttributeViewDto> dtos = this.getFileAttributeViewDtoDtoBuilder().convert(Arrays.asList(views));
            dtos.stream().forEach(i -> i.buildPath(currentPath));
            return dtos;
        } catch (RestRourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error browsing folder {} , protected {} ", currentPath, protectedFolder, e);
            throw new RestServerError("error browsing folder", e);
        }
    }

    public IStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }

}
