package org.entando.entando.web.role;

import org.entando.entando.aps.system.services.role.model.RoleDto;
import org.entando.entando.web.role.model.RoleRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class RoleDtotoRoleRequestConverter implements Converter<RoleDto, RoleRequest> {

    @Override
    public RoleRequest convert(RoleDto source) {
        RoleRequest request = new RoleRequest();
        request.setCode(source.getCode());
        request.setName(source.getName());
        request.setPermissions(source.getPermissions());
        return request;
    }
}
