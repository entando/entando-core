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
package org.entando.entando.web.filebrowser.validator;

import com.agiletec.aps.system.SystemConstants;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.validator.AbstractPaginationValidator;
import org.entando.entando.web.filebrowser.model.FileBrowserFileRequest;
import org.entando.entando.web.filebrowser.model.FileBrowserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author eu
 */
@Component
public class FileBrowserValidator extends AbstractPaginationValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String ERRCODE_RESOURCE_DOES_NOT_EXIST = "1";
    public static final String ERRCODE_RESOURCE_ALREADY_EXIST = "2";
    public static final String ERRCODE_REQUIRED_FOLDER_TYPE = "3";
    public static final String ERRCODE_INVALID_PATH = "4";
    public static final String ERRCODE_FILENAME_MISMATCH = "5";
    public static final String ERRCODE_INVALID_FILENAME = "6";

    @Autowired
    @Qualifier(SystemConstants.STORAGE_MANAGER)
    private IStorageManager storageManager;

    @Override
    public boolean supports(Class<?> paramClass) {
        return (FileBrowserFileRequest.class.equals(paramClass) || FileBrowserRequest.class.equals(paramClass));
    }

    @Override
    public void validate(Object target, Errors errors) {
        FileBrowserRequest request = (FileBrowserRequest) target;
        String path = request.getPath();
        if (path.endsWith("/")) {
            if (request instanceof FileBrowserFileRequest) {
                errors.rejectValue("path", ERRCODE_INVALID_PATH, new String[]{path}, "fileBrowser.filename.invalidPath");
                return;
            } else {
                path = path.substring(0, path.length() - 1);
            }
        }
        if (!path.contains("/")) {
            return;
        }
        try {
            String directory = path.substring(0, path.lastIndexOf("/"));
            if (!this.getStorageManager().exists(directory, request.isProtectedFolder())) {
                throw new RestRourceNotFoundException(FileBrowserValidator.ERRCODE_RESOURCE_DOES_NOT_EXIST, "parent folder", path);
            }
            if (request instanceof FileBrowserFileRequest) {
                FileBrowserFileRequest fileRequest = (FileBrowserFileRequest) target;
                String extractedFileName = path.substring(path.lastIndexOf("/") + 1, path.length());
                if (!extractedFileName.equalsIgnoreCase(fileRequest.getFilename())) {
                    errors.rejectValue("filename", ERRCODE_FILENAME_MISMATCH, new String[]{fileRequest.getFilename(), extractedFileName}, "fileBrowser.filename.body.mismatch");
                    throw new ValidationConflictException((BindingResult) errors);
                } else if (!extractedFileName.contains(".")) {
                    errors.rejectValue("filename", ERRCODE_INVALID_FILENAME, new String[]{extractedFileName}, "fileBrowser.filename.invalidFilename");
                }
            }
        } catch (ValidationConflictException vce) {
            throw vce;
        } catch (RestRourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error checking path {} , protected {} ", path, request.isProtectedFolder(), e);
            throw new RestServerError("error checking path", e);
        }
    }

    public IStorageManager getStorageManager() {
        return storageManager;
    }

    public void setStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }

}
