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
package org.entando.entando.aps.system.services.group;

import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.group.model.GroupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GroupService implements IGroupService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IGroupManager groupManager;

    @Autowired
    private IDtoBuilder<Group, GroupDto> dtoBuilder;


    protected IGroupManager getGroupManager() {
        return groupManager;
    }

    public void setGroupManager(IGroupManager groupManager) {
        this.groupManager = groupManager;
    }

    protected IDtoBuilder<Group, GroupDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<Group, GroupDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public PagedMetadata<GroupDto> getGroups(RestListRequest restListReq) {
        try {
            //transforms the filters by overriding the key specified in the request with the correct one known by the dto
            restListReq.getFieldSearchFilters()
                       .stream()
                       .filter(i -> i.getKey() != null)
                       .forEach(i -> i.setKey(GroupDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<Group> groups = this.getGroupManager().getGroups(restListReq.getFieldSearchFilters());
            List<GroupDto> dtoList = dtoBuilder.convert(groups.getList());

            PagedMetadata<GroupDto> pagedMetadata = new PagedMetadata<>(restListReq, groups);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search groups", t);
            throw new RestServerError("error in search groups", t);
        }
    }

    @Override
    public GroupDto getGroup(String groupCode) {
        Group group = this.getGroupManager().getGroup(groupCode);
        if (null == group) {
            logger.warn("no group found with code {}", groupCode);
            throw new RestRourceNotFoundException("group", groupCode);
        }
        return this.getDtoBuilder().convert(group);
    }

    @Override
    public GroupDto updateGroup(String groupCode, String descr) {
        Group group = this.getGroupManager().getGroup(groupCode);
        if (null == group) {
            throw new RestRourceNotFoundException("group", groupCode);
        }
        group.setDescription(descr);
        try {
            this.getGroupManager().updateGroup(group);
            return this.getDtoBuilder().convert(group);
        } catch (ApsSystemException e) {
            logger.error("Error updating group {}", groupCode, e);
            throw new RestServerError("error in update group", e);
        }
    }

    @Override
    public GroupDto addGroup(GroupRequest groupRequest) {
        try {
            Group group = this.createGroup(groupRequest);
            this.getGroupManager().addGroup(group);
            return this.getDtoBuilder().convert(group);
        } catch (ApsSystemException e) {
            logger.error("Error adding group", e);
            throw new RestServerError("error add group", e);
        }
    }

    @Override
    public void removeGroup(String groupName) {
        try {
            Group group = this.getGroupManager().getGroup(groupName);
            if (null != group) {
                this.getGroupManager().removeGroup(group);
            }
        } catch (ApsSystemException e) {
            logger.error("Error in delete group {}", groupName, e);
            throw new RestServerError("error in delete group", e);
        }
    }

    protected Group createGroup(GroupRequest groupRequest) {
        Group group = new Group();
        group.setName(groupRequest.getName());
        group.setDescription(groupRequest.getDescr());
        return group;
    }
}
