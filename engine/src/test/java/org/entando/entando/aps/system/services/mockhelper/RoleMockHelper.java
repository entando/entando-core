package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.role.Role;
import org.entando.entando.aps.system.services.role.model.RoleDto;

public class RoleMockHelper {

    public static final String ROLE_NAME = "roleName";
    public static final String ROLE_DESC = "roleDesc";
    public static final String PERMISSION_NAME = "permission";
    public static final String PERMISSION = "permission";

    /**
     * @return a mocked Role instance
     */
    public static Role mockRole() {

       return mockRole("");
    }


    /**
     * @return a mocked Role instance
     */
    public static Role mockRole(String suffix) {

        Role role = new Role();
        role.setName(ROLE_NAME + suffix);
        role.setDescription(ROLE_DESC + suffix);
        return role;
    }



    /**
     * @return a mocked RoleDto instance
     */
    public static RoleDto mockRoleDto() {

        return mockRoleDto("");
    }


    /**
     * @return a mocked RoleDto instance
     */
    public static RoleDto mockRoleDto(String suffix) {

        RoleDto role = new RoleDto();
        role.setName(ROLE_NAME + suffix);
        role.setPermissions(PERMISSION + suffix);
        return role;
    }
}
