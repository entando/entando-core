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

import com.agiletec.aps.system.services.role.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.storage.IFileBrowserService;
import org.entando.entando.aps.system.services.storage.model.BasicFileAttributeViewDto;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author E.Santoboni
 */
@RestController
@RequestMapping(value = "/fileBrowser")
public class FileBrowserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFileBrowserService fileBrowserService;

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> browseFolder(@RequestParam(value = "currentPath", required = false, defaultValue = "") String currentPath,
            @RequestParam(value = "protectedFolder", required = false, defaultValue = "false") Boolean protectedFolder) {
        logger.debug("browsing forlser {} - protected {}", currentPath, protectedFolder);
        List<BasicFileAttributeViewDto> result = this.getFileBrowserService().browseFolder(currentPath, protectedFolder);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("protectedFolder", protectedFolder);
        metadata.put("currentPath", currentPath);
        metadata.put("prevPath", this.getPrevFolderName(currentPath));
        return new ResponseEntity<>(new RestResponse(result, new ArrayList<>(), metadata), HttpStatus.OK);
    }

    protected String getPrevFolderName(String currentPath) {
        if (StringUtils.isEmpty(currentPath)) {
            return null;
        }
        String path = "";
        if (!currentPath.contains("/")) {
            return path;
        }
        String[] folders = currentPath.split("/");
        for (String folder : folders) {
            if (StringUtils.isEmpty(folder)) {
                break;
            } else {
                path += folder;
            }
        }
        return path;
    }

    public IFileBrowserService getFileBrowserService() {
        return fileBrowserService;
    }

    public void setFileBrowserService(IFileBrowserService fileBrowserService) {
        this.fileBrowserService = fileBrowserService;
    }

}
