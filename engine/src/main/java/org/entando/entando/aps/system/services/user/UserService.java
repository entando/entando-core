/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.user;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
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
    private IAuthorizationManager authorizationManager;

    @Autowired
    private IAuthenticationProviderManager authenticationProvider;

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
        List<UserAuthorityDto> authorizations = new ArrayList<>();
        try {
            request.getAuthorities().forEach(authorization
                    -> {
                try {
                    this.getAuthorizationManager().getUserAuthorizations(username);
                    this.getAuthorizationManager().addUserAuthorization(username, authorization.getGroup(), authorization.getRole());
                } catch (ApsSystemException ex) {
                    throw new RuntimeException(ex);
                }
                authorizations.add(new UserAuthorityDto(authorization.getGroup(), authorization.getRole()));
            });
        } catch (RuntimeException e) {
            logger.error("Error in delete authorities for {}", username, e);
            throw new RestServerError("Error in delete authorities", e);
        }
        return authorizations;
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

}
