package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.role.Role;
import org.apache.lucene.document.IntRange;
import org.entando.entando.aps.system.services.role.model.RoleDto;

import java.util.stream.IntStream;

public class RoleMockHelper {

    public static final String ROLE_NAME = "roleName";
    public static final String ROLE_DESC = "roleDesc";
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

        return mockRole(suffix, 0);
    }


    /**
     * @return a mocked Role instance
     */
    public static Role mockRole(String suffix, int permissionCount) {

        Role role = new Role();
        role.setName(ROLE_NAME + suffix);
        role.setDescription(ROLE_DESC + suffix);

        IntStream.range(0, permissionCount).forEach(i -> role.addPermission(PERMISSION + i));

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
//        role.setPermissions(PERMISSION + suffix);
        return role;
    }
}
