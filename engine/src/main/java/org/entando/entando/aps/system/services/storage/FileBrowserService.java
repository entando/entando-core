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

import com.agiletec.aps.util.FileTextReader;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.storage.model.BasicFileAttributeViewDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.filebrowser.model.FileBrowserFileRequest;
import org.entando.entando.web.filebrowser.model.FileBrowserRequest;
import org.entando.entando.web.filebrowser.validator.FileBrowserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.springframework.validation.BeanPropertyBindingResult;

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
    public List<BasicFileAttributeViewDto> browseFolder(String currentPath, Boolean protectedFolder) {
        this.checkResource(currentPath, "Folder", protectedFolder);
        List<BasicFileAttributeViewDto> dtos = null;
        try {
            BasicFileAttributeView[] views = null;
            if (null == protectedFolder) {
                views = new BasicFileAttributeView[]{this.getRootFolder(false), this.getRootFolder(true)};
            } else {
                views = this.getStorageManager().listAttributes(currentPath, protectedFolder);
            }
            dtos = this.getFileAttributeViewDtoDtoBuilder().convert(Arrays.asList(views));
            dtos.stream().forEach(i -> i.buildPath(currentPath));
            if (null != protectedFolder) {
                dtos.stream().forEach(i -> i.setProtectedFolder(protectedFolder));
            }
        } catch (Throwable t) {
            logger.error("error browsing folder {} - type {}", currentPath, protectedFolder);
            throw new RestServerError("error browsing folder", t);
        }
        return dtos;
    }

    private RootFolderAttributeView getRootFolder(boolean protectedFolder) {
        String folderName = (protectedFolder) ? "protected" : "public";
        return new RootFolderAttributeView(folderName, protectedFolder);
    }

    @Override
    public byte[] getFileStream(String currentPath, Boolean protectedFolder) {
        this.checkResource(currentPath, "File", protectedFolder);
        File tempFile = null;
        byte[] bytes = null;
        try {
            String[] sections = currentPath.split("/");
            InputStream stream = this.getStorageManager().getStream(currentPath, protectedFolder);
            tempFile = FileTextReader.createTempFile(new Random().nextInt(100) + sections[sections.length - 1], stream);
            bytes = FileTextReader.fileToByteArray(tempFile);
        } catch (Throwable t) {
            logger.error("error extracting stream for path {} - type {}", currentPath, protectedFolder);
            throw new RestServerError("error extracting stream", t);
        } finally {
            if (null != tempFile) {
                boolean deleted = tempFile.delete();

                if (!deleted) {
                    logger.warn("Failed to delete temp file {}", tempFile.getAbsolutePath());
                }
            }
        }
        return bytes;
    }

    @Override
    public void addFile(FileBrowserFileRequest request, BindingResult bindingResult) {
        String path = request.getPath();
        String parentFolder = path.substring(0, path.lastIndexOf("/"));
        this.checkResource(parentFolder, "Parent Folder", request.isProtectedFolder());
        try {
            if (this.getStorageManager().exists(request.getPath(), request.isProtectedFolder())) {
                bindingResult.reject(FileBrowserValidator.ERRCODE_RESOURCE_ALREADY_EXIST,
                        new String[]{request.getPath(), String.valueOf(request.isProtectedFolder())}, "fileBrowser.file.exists");
                throw new ValidationConflictException(bindingResult);
            }
            InputStream is = new ByteArrayInputStream(request.getBase64());
            this.getStorageManager().saveFile(path, request.isProtectedFolder(), is);
        } catch (ValidationConflictException vge) {
            throw vge;
        } catch (Throwable t) {
            logger.error("error adding file path {} - type {}", path, request.isProtectedFolder());
            throw new RestServerError("error adding file", t);
        }
    }

    @Override
    public void updateFile(FileBrowserFileRequest request, BindingResult bindingResult) {
        String path = request.getPath();
        this.checkResource(path, "File", request.isProtectedFolder());
        try {
            InputStream is = new ByteArrayInputStream(request.getBase64());
            this.getStorageManager().saveFile(path, request.isProtectedFolder(), is);
        } catch (ValidationConflictException vge) {
            throw vge;
        } catch (Throwable t) {
            logger.error("error updating file path {} - type {}", path, request.isProtectedFolder());
            throw new RestServerError("error updating file", t);
        }
    }

    @Override
    public void deleteFile(String currentPath, Boolean protectedResource) {
        try {
            this.getStorageManager().deleteFile(currentPath, protectedResource);
        } catch (ValidationConflictException vge) {
            throw vge;
        } catch (Throwable t) {
            logger.error("error deleting file path {} - type {}", currentPath, protectedResource);
            throw new RestServerError("error deleting file", t);
        }
    }

    @Override
    public void addDirectory(FileBrowserRequest request, BindingResult bindingResult) {
        String path = request.getPath();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 2);
        }
        String parentFolder = path.substring(0, path.lastIndexOf("/"));
        this.checkResource(parentFolder, "Parent Folder", request.isProtectedFolder());
        try {
            if (this.getStorageManager().exists(request.getPath(), request.isProtectedFolder())) {
                bindingResult.reject(FileBrowserValidator.ERRCODE_RESOURCE_ALREADY_EXIST,
                        new String[]{request.getPath(), String.valueOf(request.isProtectedFolder())}, "fileBrowser.directory.exists");
                throw new ValidationConflictException(bindingResult);
            }
            this.getStorageManager().createDirectory(path, request.isProtectedFolder());
        } catch (ValidationConflictException vge) {
            throw vge;
        } catch (Throwable t) {
            logger.error("error adding directory path {} - type {}", path, request.isProtectedFolder());
            throw new RestServerError("error adding directory", t);
        }
    }

    @Override
    public void deleteDirectory(String currentPath, Boolean protectedFolder) {
        try {
            this.getStorageManager().deleteDirectory(currentPath, protectedFolder);
        } catch (ValidationConflictException vge) {
            throw vge;
        } catch (Throwable t) {
            logger.error("error deleting directory path {} - type {}", currentPath, protectedFolder);
            throw new RestServerError("error deleting directory", t);
        }
    }

    protected void checkResource(String currentPath, String objectName, Boolean protectedFolder) {
        if (null == protectedFolder && !StringUtils.isEmpty(currentPath)) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(objectName, objectName);
            bindingResult.reject(FileBrowserValidator.ERRCODE_REQUIRED_FOLDER_TYPE, "fileBrowser.browser.protectedFolder.required");
            throw new ValidationGenericException(bindingResult);
        }
        if (null == protectedFolder) {
            return;
        }
        try {
            boolean exists = this.getStorageManager().exists(currentPath, protectedFolder);
            if (!exists) {
                logger.warn("no resource found for path {} - type {}", currentPath, protectedFolder);
                throw new RestRourceNotFoundException(FileBrowserValidator.ERRCODE_RESOURCE_DOES_NOT_EXIST, objectName, currentPath);
            }
        } catch (RestRourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error checking resource {} , protected {} ", currentPath, protectedFolder, e);
            throw new RestServerError("error checking resource", e);
        }
    }

    public IStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }

}
