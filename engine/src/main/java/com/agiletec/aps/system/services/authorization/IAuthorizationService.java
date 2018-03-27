package com.agiletec.aps.system.services.authorization;

import java.util.List;

import com.agiletec.aps.system.services.authorization.model.UserDto;

public interface IAuthorizationService {

    public List<UserDto> getRoleUtilizer(String roleCode);
}
