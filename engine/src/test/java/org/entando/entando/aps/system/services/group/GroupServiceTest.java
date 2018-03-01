package org.entando.entando.aps.system.services.group;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private IGroupManager groupManager;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ValidationConflictException.class)
    public void should_raise_exception_on_delete_reserved_group() throws JsonProcessingException {
        Group group = new Group();
        group.setName(Group.ADMINS_GROUP_NAME);
        when(groupManager.getGroup(group.getName())).thenReturn(group);
        this.groupService.removeGroup(group.getName());
    }

}
