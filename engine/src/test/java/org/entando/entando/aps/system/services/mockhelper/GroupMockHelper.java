package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.group.Group;

public class GroupMockHelper {

    public static final String GROUP_NAME = "groupName";
    public static final String GROUP_DESC = "groupDesc";


    /**
     * @return a mocked Group instance
     */
    public static Group mockGroup() {

       return mockGroup("");
    }


    /**
     * @return a mocked Group instance
     */
    public static Group mockGroup(String suffix) {

        Group group = new Group();
        group.setName(GROUP_NAME + suffix);
        group.setDescription(GROUP_DESC + suffix);
        return group;
    }
}
