package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Role;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AuthorizationMockHelper {


    public static final int TOTAL_PERMISSIONS = 4;


    /**
     * @return a List of mocked Authorizations
     */
    public static List<Authorization> mockAuthorizationList(int count) {

        return IntStream.range(0, count)
                .mapToObj(i -> {
                    Group group = GroupMockHelper.mockGroup(i+"");
                    Role role = RoleMockHelper.mockRole(i+"", TOTAL_PERMISSIONS);
                    return new Authorization(group, role);
                })
                .collect(Collectors.toList());
    }

    /**
     * @return a mocked Authorization instance
     */
    public static Authorization mockAuthorization() {

        Group group = GroupMockHelper.mockGroup();
        Role role = RoleMockHelper.mockRole();
        return new Authorization(group, role);
    }
}
