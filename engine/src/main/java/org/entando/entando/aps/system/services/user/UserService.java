/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.user;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.aps.system.services.user.model.UserDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.model.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author paddeo
 */
public class UserService implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserManager userManager;

    @Autowired
    private IAuthorizationManager authorizationManager;

    @Autowired
    private IAuthenticationProviderManager authenticationProvider;

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    public IAuthenticationProviderManager getAuthenticationProvider() {
        return authenticationProvider;
    }

    public void setAuthenticationProvider(IAuthenticationProviderManager authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public List<UserAuthorityDto> addUserAuthorities(String username, UserAuthoritiesRequest request) {
        try {
            List<UserAuthorityDto> authorizations = new ArrayList<>();

            final UserDetails user = this.getUserManager().getUser(username);;
            request.forEach(authorization
                    -> {
                try {
                    if (!this.getAuthorizationManager().isAuthOnGroupAndRole(user, authorization.getGroup(), authorization.getRole(), true)) {
                        this.getAuthorizationManager().addUserAuthorization(username, authorization.getGroup(), authorization.getRole());
                    }
                } catch (ApsSystemException ex) {
                    logger.error("Error in add authorities for {}", username, ex);
                    throw new RestServerError("Error in add authorities", ex);
                }
                authorizations.add(new UserAuthorityDto(authorization.getGroup(), authorization.getRole()));
            });
            return authorizations;
        } catch (ApsSystemException ex) {
            logger.error("Error in add authorities for {}", username, ex);
            throw new RestServerError("Error in add authorities", ex);
        }
    }

    @Override
    public void deleteUserAuthorities(String username) {
        try {
            this.getAuthorizationManager().deleteUserAuthorizations(username);
        } catch (ApsSystemException e) {
            logger.error("Error in delete authorities for {}", username, e);
            throw new RestServerError("Error in delete authorities", e);
        }
    }

    @Override
    public PagedMetadata<UserDto> getUsers(RestListRequest requestList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserDto getUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserDto updateUser(UserRequest userRequest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserDto addUser(UserRequest userRequest) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeUser(String username) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
