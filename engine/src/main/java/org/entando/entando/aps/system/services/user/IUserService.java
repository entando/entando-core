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
package org.entando.entando.aps.system.services.user;

import java.util.List;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.aps.system.services.user.model.UserDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.model.UserPasswordRequest;
import org.entando.entando.web.user.model.UserRequest;

/**
 * @author paddeo
 */
public interface IUserService {

    public final static String BEAN_NAME = "UserService";

    public final static String STATUS_ACTIVE = "active";

    public final static String STATUS_DISABLED = "inactive";

    public List<UserAuthorityDto> getUserAuthorities(String username);

    public List<UserAuthorityDto> addUserAuthorities(String username, UserAuthoritiesRequest request);

    public List<UserAuthorityDto> updateUserAuthorities(String username, UserAuthoritiesRequest request);

    public void deleteUserAuthorities(String username);

    public PagedMetadata<UserDto> getUsers(RestListRequest requestList);

    public UserDto getUser(String username);

    public UserDto updateUser(UserRequest userRequest);

    public UserDto addUser(UserRequest userRequest);

    public void removeUser(String username);

    public UserDto updateUserPassword(UserPasswordRequest passwordRequest);
}
