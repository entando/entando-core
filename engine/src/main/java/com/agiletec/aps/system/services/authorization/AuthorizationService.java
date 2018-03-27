package com.agiletec.aps.system.services.authorization;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.model.UserDto;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationService implements GroupServiceUtilizer<UserDto> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private IAuthorizationManager authorizationManager;
    private IUserManager userManager;
    private IDtoBuilder<UserDetails, UserDto> dtoBuilder;

    protected IAuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

    public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    protected IDtoBuilder<UserDetails, UserDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<UserDetails, UserDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    protected IUserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    @PostConstruct
    public void setUp() {
        setDtoBuilder(new DtoBuilder<UserDetails, UserDto>() {
            @Override
            protected UserDto toDto(UserDetails src) {
                UserDto dto = new UserDto();
                if (src.isEntandoUser()) {

                    dto.setRegistrationDate(((User) src).getCreationDate());
                    dto.setLastAccess(((User) src).getLastAccess());
                    dto.setLastPasswordChange(((User) src).getLastPasswordChange());
                    dto.setMaxMonthsSinceLastAccess(((User) src).getMaxMonthsSinceLastAccess());
                    dto.setMaxMonthsSinceLastPasswordChange(((User) src).getMaxMonthsSinceLastPasswordChange());

                }
                dto.setDisabled(src.isDisabled());
                dto.setAccountNotExpired(src.isAccountNotExpired());
                dto.setCredentialsNotExpired(src.isCredentialsNotExpired());
                return dto;
            }
        });
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getAuthorizationManager()).getName();
    }

    @Override
    public List<UserDto> getGroupUtilizer(String groupCode) {
        try {

            List<String> usernames = ((GroupUtilizer<String>) this.getAuthorizationManager()).getGroupUtilizers(groupCode);
            List<UserDto> dtoList = new ArrayList<>();
            if (null != usernames) {
                usernames.stream().forEach(i -> {
                    try {
                        dtoList.add(this.getDtoBuilder().convert(this.getUserManager().getUser(i)));
                    } catch (ApsSystemException e) {
                        logger.error("error loading {}", i, e);

                    }
                });
            }
            return dtoList;
        } catch (ApsSystemException ex) {
            logger.error("Error loading user references for group {}", groupCode, ex);
            throw new RestServerError("Error loading user references for group", ex);
        }
    }


}
