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


import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.group.model.GroupRequest;

public interface IGroupService {

    String BEAN_NAME = "GroupService";

    PagedMetadata<GroupDto> getGroups(RestListRequest restRequest);

    GroupDto getGroup(String groupName);

    GroupDto updateGroup(String groupName, String descr);

    GroupDto addGroup(GroupRequest groupRequest);

    void removeGroup(String groupName);
}
