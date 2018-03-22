/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.user;

import java.util.List;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;

/**
 *
 * @author paddeo
 */
public interface IUserService {

    String BEAN_NAME = "UserService";

    public List<UserAuthorityDto> addUserAuthorities(String username, UserAuthoritiesRequest request);

    public void deleteUserAuthorities(String username);
}
