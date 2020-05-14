package org.entando.entando.aps.system.services.assertionhelper;

import com.agiletec.aps.system.services.user.UserGroupPermissions;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UserGroupPermissionAssertionHelper {


    /**
     *
     * @param expectedList
     * @param actualList
     */
    public static void assertUserGroupPermissions(List<UserGroupPermissions> expectedList, List<UserGroupPermissions> actualList) {

        assertEquals(expectedList.size(), actualList.size());

        IntStream.range(0, expectedList.size())
                .forEach(i -> assertUserGroupPermissions(expectedList.get(i), actualList.get(i)));
    }


    /**
     *
     * @param expected
     * @param actual
     */
    public static void assertUserGroupPermissions(UserGroupPermissions expected, UserGroupPermissions actual) {

        assertEquals(expected.getGroup(), actual.getGroup());
        assertArrayEquals(expected.getPermissions().toArray(), actual.getPermissions().toArray());
    }
}
