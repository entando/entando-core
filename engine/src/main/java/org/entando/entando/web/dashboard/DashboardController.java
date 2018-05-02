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
package org.entando.entando.web.dashboard;

import com.agiletec.aps.system.services.role.Permission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.services.api.IApiCatalogManager;
import org.entando.entando.aps.system.services.api.model.ApiResource;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.aps.system.services.page.model.PagesStatusDto;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * @author E.Santoboni
 */
@RestController
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IComponentManager componentManager;

    @Autowired
    private IApiCatalogManager apiCatalogManager;

    @Autowired
    private IPageService pageService;

    public IComponentManager getComponentManager() {
        return componentManager;
    }

    public void setComponentManager(IComponentManager componentManager) {
        this.componentManager = componentManager;
    }

    public IApiCatalogManager getApiCatalogManager() {
        return apiCatalogManager;
    }

    public void setApiCatalogManager(IApiCatalogManager apiCatalogManager) {
        this.apiCatalogManager = apiCatalogManager;
    }

    public IPageService getPageService() {
        return pageService;
    }

    public void setPageService(IPageService pageService) {
        this.pageService = pageService;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/integration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestResponse> getIntegrationInfos() {
        logger.debug("dashboard - getting integration");
        Map<String, String> infos = new HashMap<>();
        infos.put("components", String.valueOf(this.extractNumberOfComponents()));
        infos.put("apis", String.valueOf(this.extractNumberOfApis()));
        return new ResponseEntity<>(new RestResponse(infos), HttpStatus.OK);
    }

    private int extractNumberOfComponents() {
        return this.getComponentManager().getCurrentComponents().size();
    }

    private int extractNumberOfApis() {
        int count = 0;
        try {
            Collection<ApiResource> resources = this.getApiCatalogManager().getResources().values();
            Iterator<ApiResource> iter = resources.iterator();
            while (iter.hasNext()) {
                ApiResource resource = iter.next();
                if (null != resource.getPluginCode()) {
                    if (null != resource.getGetMethod()) {
                        count++;
                    }
                    if (null != resource.getPostMethod()) {
                        count++;
                    }
                    if (null != resource.getPutMethod()) {
                        count++;
                    }
                    if (null != resource.getDeleteMethod()) {
                        count++;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error extracting count of apis", e);
        }
        return count;
    }

    @RestAccessControl(permission = Permission.SUPERUSER)
    @RequestMapping(value = "/pageStatus", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPagesStatus() {
        logger.debug("getting pages status count");
        PagesStatusDto result = this.getPageService().getPagesStatus();
        Map<String, String> metadata = new HashMap<>();
        return new ResponseEntity<>(new RestResponse(result, new ArrayList<>(), metadata), HttpStatus.OK);
    }

}
