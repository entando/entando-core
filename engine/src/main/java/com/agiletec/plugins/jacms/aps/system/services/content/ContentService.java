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
package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.CategoryUtilizer;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.category.CategoryServiceUtilizer;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentService implements GroupServiceUtilizer<ContentDto>, CategoryServiceUtilizer<ContentDto> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IContentManager contentManager;
    private IDtoBuilder<Content, ContentDto> dtoBuilder;

    public IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    public IDtoBuilder<Content, ContentDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<Content, ContentDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    private void setUp() {
        this.setDtoBuilder(new DtoBuilder<Content, ContentDto>() {
            @Override
            protected ContentDto toDto(Content src) {
                return new ContentDto(src);
            }
        });
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getContentManager()).getName();
    }

    @Override
    public List<ContentDto> getGroupUtilizer(String groupCode) {
        try {
            List<String> contentIds = ((GroupUtilizer<String>) this.getContentManager()).getGroupUtilizers(groupCode);
            return this.buildDtoList(contentIds);
        } catch (ApsSystemException ex) {
            logger.error("Error loading content references for group {}", groupCode, ex);
            throw new RestServerError("Error loading content references for group", ex);
        }
    }

    @Override
    public List<ContentDto> getCategoryUtilizer(String categoryCode) {
        try {
            List<String> contentIds = ((CategoryUtilizer) this.getContentManager()).getCategoryUtilizers(categoryCode);
            return this.buildDtoList(contentIds);
        } catch (ApsSystemException ex) {
            logger.error("Error loading content references for category {}", categoryCode, ex);
            throw new RestServerError("Error loading content references for category", ex);
        }
    }

    private List<ContentDto> buildDtoList(List<String> contentIds) {
        List<ContentDto> dtoList = new ArrayList<>();
        if (null != contentIds) {
            contentIds.stream().forEach(i -> {
                try {
                    dtoList.add(this.getDtoBuilder().convert(this.getContentManager().loadContent(i, true)));
                } catch (ApsSystemException e) {
                    logger.error("error loading content {}", i, e);
                }
            });
        }
        return dtoList;
    }

}
