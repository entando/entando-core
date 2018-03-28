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
package com.agiletec.plugins.jacms.aps.system.services.resource;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDto;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.category.CategoryServiceUtilizer;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceService implements IResourceService,
        GroupServiceUtilizer<ResourceDto>, CategoryServiceUtilizer<ResourceDto> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IResourceManager resourceManager;
    private IDtoBuilder<ResourceInterface, ResourceDto> dtoBuilder;

    public IResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    protected IDtoBuilder<ResourceInterface, ResourceDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<ResourceInterface, ResourceDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void setUp() {
        this.setDtoBuilder(new DtoBuilder<ResourceInterface, ResourceDto>() {

            @Override
            protected ResourceDto toDto(ResourceInterface src) {
                ResourceDto resourceDto = new ResourceDto(((AbstractResource) src));
                return resourceDto;
            }
        });
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getResourceManager()).getName();
    }

    @Override
    public List<ResourceDto> getGroupUtilizer(String groupCode) {
        try {
            List<String> resourcesId = ((GroupUtilizer<String>) this.getResourceManager()).getGroupUtilizers(groupCode);
            return this.buildDtoList(resourcesId);
        } catch (ApsSystemException ex) {
            logger.error("Error loading resource references for group {}", groupCode, ex);
            throw new RestServerError("Error loading resource references for group", ex);
        }
    }

    @Override
    public List<ResourceDto> getCategoryUtilizer(String categoryCode) {
        try {
            List<String> resourcesId = ((CategoryUtilizer) this.getResourceManager()).getCategoryUtilizers(categoryCode);
            return this.buildDtoList(resourcesId);
        } catch (ApsSystemException ex) {
            logger.error("Error loading resource references for category {}", categoryCode, ex);
            throw new RestServerError("Error loading resource references for category", ex);
        }
    }

    private List<ResourceDto> buildDtoList(List<String> idList) {
        List<ResourceDto> dtoList = new ArrayList<>();
        if (null != idList) {
            idList.stream().forEach(i -> {
                try {
                    dtoList.add(this.getDtoBuilder().convert(this.getResourceManager().loadResource(i)));
                } catch (ApsSystemException e) {
                    logger.error("error loading {}", i, e);

                }
            });
        }
        return dtoList;
    }

}
