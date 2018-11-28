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
package org.entando.entando.web.filebrowser;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.storage.IFileBrowserService;
import org.entando.entando.aps.system.services.storage.model.BasicFileAttributeViewDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.filebrowser.model.FileBrowserFileRequest;
import org.entando.entando.web.filebrowser.model.FileBrowserRequest;
import org.entando.entando.web.filebrowser.validator.FileBrowserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
@RestController
@RequestMapping(value = "/fileBrowser")
public class FileBrowserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFileBrowserService fileBrowserService;

    @Autowired
    private FileBrowserValidator validator;

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<List<BasicFileAttributeViewDto>, Map>> browseFolder(@RequestParam(value = "currentPath", required = false, defaultValue = "") String currentPath,
            @RequestParam(value = "protectedFolder", required = false) Boolean protectedFolder) {
        logger.debug("browsing folder {} - protected {}", currentPath, protectedFolder);
        List<BasicFileAttributeViewDto> result = this.getFileBrowserService().browseFolder(currentPath, protectedFolder);
        Map<String, Object> metadata = new HashMap<>();
        if (null != protectedFolder) {
            metadata.put("protectedFolder", protectedFolder);
        }
        metadata.put("currentPath", currentPath);
        String prevPath = this.getPrevFolderName(currentPath);
        if (null != currentPath) {
            metadata.put("prevPath", prevPath);
        }
        logger.debug("Content folder -> {}", result);
        return new ResponseEntity<>(new RestResponse<>(result, metadata), HttpStatus.OK);
    }

    protected String getPrevFolderName(String currentPath) {
        if (StringUtils.isEmpty(currentPath)) {
            return null;
        }
        if (!currentPath.contains("/")) {
            return "";
        } else {
            return currentPath.substring(0, currentPath.lastIndexOf("/"));
        }
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/file", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Map, Map>> getFile(@RequestParam(value = "currentPath", required = false, defaultValue = "") String currentPath,
            @RequestParam(value = "protectedFolder", required = false, defaultValue = "false") Boolean protectedFolder) {
        logger.debug("required file {} - protected {}", currentPath, protectedFolder);
        byte[] base64 = this.getFileBrowserService().getFileStream(currentPath, protectedFolder);
        Map<String, Object> result = new HashMap<>();
        result.put("protectedFolder", protectedFolder);
        result.put("path", currentPath);
        result.put("filename", this.getFilename(currentPath));
        result.put("base64", base64);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("prevPath", this.getPrevFolderName(currentPath));
        return new ResponseEntity<>(new RestResponse<>(result, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/file", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Map, Map>> addFile(@Valid @RequestBody FileBrowserFileRequest request, BindingResult bindingResult) throws ApsSystemException {
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getValidator().validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getFileBrowserService().addFile(request, bindingResult);
        return this.executeFilePostPutRespose(request);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/file", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Map, Map>> updateFile(@Valid @RequestBody FileBrowserFileRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getValidator().validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getFileBrowserService().updateFile(request, bindingResult);
        return this.executeFilePostPutRespose(request);
    }

    public ResponseEntity<RestResponse<Map, Map>> executeFilePostPutRespose(FileBrowserRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("protectedFolder", request.isProtectedFolder());
        result.put("path", request.getPath());
        result.put("filename", this.getFilename(request.getPath()));
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("prevPath", this.getPrevFolderName(request.getPath()));
        return new ResponseEntity<>(new RestResponse<>(result, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/file", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Map, Map>> deleteFile(@RequestParam String currentPath, @RequestParam Boolean protectedFolder) {
        logger.debug("delete file {} - protected {}", currentPath, protectedFolder);
        this.getFileBrowserService().deleteFile(currentPath, protectedFolder);
        Map<String, Object> result = new HashMap<>();
        result.put("protectedFolder", protectedFolder);
        result.put("path", currentPath);
        result.put("filename", this.getFilename(currentPath));
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("prevPath", this.getPrevFolderName(currentPath));
        return new ResponseEntity<>(new RestResponse<>(result, metadata), HttpStatus.OK);
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/directory", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Map, Map>> addDirectory(@Valid @RequestBody FileBrowserRequest request, BindingResult bindingResult) throws ApsSystemException {
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getValidator().validate(request, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ValidationGenericException(bindingResult);
        }
        this.getFileBrowserService().addDirectory(request, bindingResult);
        return this.executeDirectoryRespose(request.getPath(), request.isProtectedFolder());
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/directory", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse<Map, Map>> deleteDirectory(@RequestParam String currentPath, @RequestParam Boolean protectedFolder) {
        logger.debug("delete directory {} - protected {}", currentPath, protectedFolder);
        this.getFileBrowserService().deleteDirectory(currentPath, protectedFolder);
        return this.executeDirectoryRespose(currentPath, protectedFolder);
    }

    public ResponseEntity<RestResponse<Map, Map>> executeDirectoryRespose(String path, Boolean protectedFolder) {
        Map<String, Object> result = new HashMap<>();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        result.put("protectedFolder", protectedFolder);
        result.put("path", path);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("prevPath", this.getPrevFolderName(path));
        return new ResponseEntity<>(new RestResponse<>(result, metadata), HttpStatus.OK);
    }

    private String getFilename(String currentPath) {
        String[] sections = currentPath.split("/");
        return sections[sections.length - 1];
    }

    public IFileBrowserService getFileBrowserService() {
        return fileBrowserService;
    }

    public void setFileBrowserService(IFileBrowserService fileBrowserService) {
        this.fileBrowserService = fileBrowserService;
    }

    public FileBrowserValidator getValidator() {
        return validator;
    }

    public void setValidator(FileBrowserValidator validator) {
        this.validator = validator;
    }

}
