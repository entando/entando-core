/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author paddeo
 */
public interface IUserService {

    public final static String BEAN_NAME = "UserService";

    public final static String STATUS_ACTIVE = "active";

    public final static String STATUS_DISABLED = "inactive";

    public List<UserAuthorityDto> addUserAuthorities(String username, UserAuthoritiesRequest request);

    public void deleteUserAuthorities(String username);

    public PagedMetadata<UserDto> getUsers(RestListRequest requestList);

    public UserDto getUser(String username);

    public UserDto updateUser(UserRequest userRequest);

    public UserDto addUser(UserRequest userRequest);

    public void removeUser(String username);

    public UserDto updateUserPassword(UserPasswordRequest passwordRequest);
}
