package org.entando.entando.aps.system.services.role;

import java.util.List;

import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.role.model.RoleDto;


public class RoleDdoBuilder extends DtoBuilder<Role, RoleDto> {

    private IRoleManager roleManager;

    public IRoleManager getRoleManager() {
        return roleManager;
    }

    public void setRoleManager(IRoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Override
    protected RoleDto toDto(Role src) {
        //List<String> permissionsCodes = this.getRoleManager().getPermissionsCodes();
        return new RoleDto(src);
    }

    //@Override
    protected RoleDto toDto(Role src, List<String> permissionsCodes) {
        //List<String> permissionsCodes = this.getRoleManager().getPermissionsCodes();
        return new RoleDto(src, permissionsCodes);
    }
}
