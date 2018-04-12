/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.user;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.user.model.UserAuthorityDto;
import org.entando.entando.aps.system.services.user.model.UserDto;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.user.model.UserAuthoritiesRequest;
import org.entando.entando.web.user.model.UserPasswordRequest;
import org.entando.entando.web.user.model.UserRequest;
import org.entando.entando.web.user.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

/**
 *
 * @author paddeo
 */
public class UserService implements IUserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ERRCODE_USER_NOT_FOUND = "1";

    @Autowired
    private IUserManager userManager;

    @Autowired
    private IUserProfileManager userProfileManager;

    @Autowired
    private IAuthorizationManager authorizationManager;

    @Autowired
    private IAuthenticationProviderManager authenticationProvider;

    @Autowired
    private IDtoBuilder<UserDetails, UserDto> dtoBuilder;

    public IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public IUserProfileManager getUserProfileManager() {
        return userProfileManager;
    }

    public void setUserProfileManager(IUserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
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

    public IDtoBuilder<UserDetails, UserDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<UserDetails, UserDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
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
        try {
            //transforms the filters by overriding the key specified in the request with the correct one known by the dto
            List<FieldSearchFilter> filters = new ArrayList<>(requestList.buildFieldSearchFilters());
            filters.stream()
                    .filter(i -> ((i.getKey() != null) && (UserDto.getEntityFieldName(i.getKey()) != null)))
                    .forEach(i -> i.setKey(UserDto.getEntityFieldName(i.getKey())));
            List<String> userNames = null;
            List<UserDetails> users = new ArrayList<>();
            if (filters.size() > 0) {
                String text = (String) filters.get(0).getValue();
                userNames = this.getUserManager().searchUsernames(text);
            } else {
                userNames = this.getUserManager().getUsernames();
            }
            userNames.forEach(username -> users.add(this.loadUser(username)));
            List<UserDto> dtoList = dtoBuilder.convert(users);
            SearcherDaoPaginatedResult<UserDetails> result = new SearcherDaoPaginatedResult<>(users.size(), users);
            PagedMetadata<UserDto> pagedMetadata = new PagedMetadata<>(requestList, result);
            pagedMetadata.setBody(dtoList);
            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search users", t);
            throw new RestServerError("error in search users", t);
        }
    }

    @Override
    public UserDto getUser(String username) {
        UserDetails user = this.loadUser(username);
        return dtoBuilder.convert(user);
    }

    @Override
    public UserDto updateUser(UserRequest userRequest) {
        UserDetails user = this.loadUser(userRequest.getUsername());
        try {
            UserDetails newUser = this.updateUser(user, userRequest);
            if (userRequest.isReset() && (user instanceof User)) {
                ((User) newUser).setLastAccess(new Date());
                ((User) newUser).setLastPasswordChange(new Date());
            }
            this.getUserManager().updateUser(newUser);
            UserDetails modifiedUser = this.getUserManager().getUser(userRequest.getUsername());
            return dtoBuilder.convert(modifiedUser);
        } catch (ApsSystemException e) {
            logger.error("Error in updating user {}", userRequest.getUsername(), e);
            throw new RestServerError("Error in updating user", e);
        }
    }

    @Override
    public UserDto addUser(UserRequest userRequest) {
        try {
            String username = userRequest.getUsername();
            if (null != this.getUserManager().getUser(username)) {
                BindingResult bindingResult = new BeanPropertyBindingResult(userRequest, "user");
                bindingResult.reject(UserValidator.ERRCODE_USER_ALREADY_EXISTS, new String[]{username}, "user.exists");
                throw new ValidationConflictException(bindingResult);
            }
            UserDetails newUser = this.createUser(userRequest);
            this.getUserManager().addUser(newUser);
            UserDetails addedUser = this.getUserManager().getUser(username);
            return dtoBuilder.convert(addedUser);
        } catch (ApsSystemException e) {
            logger.error("Error in adding user {}", userRequest.getUsername(), e);
            throw new RestServerError("Error in adding user", e);
        }
    }

    @Override
    public void removeUser(String username) {
        try {
            this.getUserManager().removeUser(username);
        } catch (ApsSystemException e) {
            logger.error("Error in deleting user {}", username, e);
            throw new RestServerError("Error in deleting user", e);
        }
    }

    @Override
    public UserDto updateUserPassword(UserPasswordRequest passwordRequest) {
        UserDetails user = this.loadUser(passwordRequest.getUsername());
        try {
            this.getUserManager().changePassword(passwordRequest.getUsername(), passwordRequest.getNewPassword());
            return dtoBuilder.convert(user);
        } catch (ApsSystemException e) {
            logger.error("Error in updating password for user {}", passwordRequest.getUsername(), e);
            throw new RestServerError("Error in updating password", e);
        }
    }

    private UserDetails loadUser(String username) {
        try {
            UserDetails user = this.getUserManager().getUser(username);
            if (user == null) {
                throw new RestRourceNotFoundException(ERRCODE_USER_NOT_FOUND, "user", username);
            }
            return user;
        } catch (RestRourceNotFoundException e) {
            throw e;
        } catch (ApsSystemException e) {
            logger.error("Error in loading user {}", username, e);
            throw new RestServerError("Error in loading user", e);
        }
    }

    private UserDetails updateUser(UserDetails oldUser, UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setDisabled(userRequest.getStatus() != null && !userRequest.getStatus().equals(IUserService.STATUS_ACTIVE));
        if (oldUser instanceof User) {
            User userToClone = (User) oldUser;
            user.setLastAccess(userToClone.getLastAccess());
            user.setLastPasswordChange(userToClone.getLastPasswordChange());
        }
        return user;
    }

    private UserDetails createUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setDisabled(userRequest.getStatus() != null && !userRequest.getStatus().equals(IUserService.STATUS_ACTIVE));
        return user;
    }
}
