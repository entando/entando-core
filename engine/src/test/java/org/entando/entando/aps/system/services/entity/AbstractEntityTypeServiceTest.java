package org.entando.entando.aps.system.services.entity;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.parse.IApsEntityDOM;
import com.google.common.collect.*;
import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.entando.entando.web.common.model.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AbstractEntityTypeServiceTest {

    private static final String ENTITY_MANAGER_CODE = "TEST_MANAGER";

    @Spy
    AbstractEntityTypeService service;

    @Mock
    IEntityManager entityManager;

    @Mock
    IApsEntityDOM entityDom;

    @Before
    public void setUp() {
        service.setEntityManagers(ImmutableList.of(entityManager));
    }

    @Test
    public void getShortEntityTypesFilterWorks() {
        UserProfile user2 = createUserProfile("user2");
        UserProfile user1 = createUserProfile("USER1");

        Map<String, IApsEntity> mapOfEntities = ImmutableMap.of(
                "B",  user2,
                "A", user1,
                "C",  createUserProfile("xyz")
        );

        when(entityManager.getName()).thenReturn(ENTITY_MANAGER_CODE);
        when(entityManager.getEntityPrototypes()).thenReturn(mapOfEntities);

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setValue("user");
        requestList.addFilter(filter);

        PagedMetadata entities = service.getShortEntityTypes(ENTITY_MANAGER_CODE, requestList);

        assertThat(entities).isNotNull();
        assertThat(entities.getTotalItems()).isEqualTo(2);
        //noinspection unchecked
        assertThat(entities.getBody()).containsExactly(
                new EntityTypeShortDto(user1), new EntityTypeShortDto(user2));
    }

    @Test
    public void getShortEntityTypesFilterWorksReversed() {
        UserProfile user2 = createUserProfile("user2");
        UserProfile user1 = createUserProfile("USER1");

        Map<String, IApsEntity> mapOfEntities = ImmutableMap.of(
                "B",  user2,
                "A", user1,
                "C",  createUserProfile("xyz")
        );

        when(entityManager.getName()).thenReturn(ENTITY_MANAGER_CODE);
        when(entityManager.getEntityPrototypes()).thenReturn(mapOfEntities);

        RestListRequest requestList = new RestListRequest();
        Filter filter = new Filter();
        filter.setAttribute("id");
        filter.setValue("user");
        requestList.addFilter(filter);

        requestList.setDirection(FieldSearchFilter.Order.DESC.toString());

        PagedMetadata entities = service.getShortEntityTypes(ENTITY_MANAGER_CODE, requestList);

        assertThat(entities).isNotNull();
        assertThat(entities.getTotalItems()).isEqualTo(2);
        //noinspection unchecked
        assertThat(entities.getBody()).containsExactly(
                new EntityTypeShortDto(user2), new EntityTypeShortDto(user1));
    }

    private UserProfile createUserProfile(String userId) {
        when(entityDom.clone()).thenReturn(entityDom);

        UserProfile userProfile = new UserProfile();
        userProfile.setId(userId);
        userProfile.setEntityDOM(entityDom);
        userProfile.setTypeCode(userId);
        return userProfile;
    }

}