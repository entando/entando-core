package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.user.User;

public class UserMockHelper {

    public static final String USERNAME = "bugsbynny";


    /**
     * @return a mocked User instance
     */
    public static User mockUser() {

        User user = new User();
        user.setUsername(USERNAME);
        return user;
    }


}
