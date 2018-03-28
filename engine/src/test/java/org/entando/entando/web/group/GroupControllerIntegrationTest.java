package org.entando.entando.web.group;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.group.IGroupService;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.group.model.GroupRequest;
import org.entando.entando.web.group.validator.GroupValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IGroupService groupService;

    @Autowired
    private IGroupManager groupManager;

    @Test
    public void testGetGroupsPagination() throws Exception {
        List<Group> testGroups = new ArrayList<>();
        try {
            for (int i = 0; i < 25; i++) {
                String x = ("tmp_" + i);
                Group group = new Group();
                group.setDescription(x);
                group.setName(x);
                testGroups.add(group);
                this.groupManager.addGroup(group);
            }

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc.perform(
                    get("/groups")
                    .param("pageSize", "5")
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(31)));
            result.andExpect(jsonPath("$.metaData.page", is(1)));
            result.andExpect(jsonPath("$.metaData.lastPage", is(7)));
            result.andExpect(jsonPath("$.payload[0].code", is("administrators")));

            //-------------
            result = mockMvc.perform(
                    get("/groups")
                    .param("pageSize", "5")
                    .param("page", "1")
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(31)));
            result.andExpect(jsonPath("$.metaData.page", is(1)));
            result.andExpect(jsonPath("$.metaData.lastPage", is(7)));
            result.andExpect(jsonPath("$.payload[0].code", is("administrators")));

            //-------------
            result = mockMvc.perform(
                    get("/groups")
                    .param("pageSize", "5")
                    .param("page", "7")
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(jsonPath("$.metaData.pageSize", is(5)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(31)));
            result.andExpect(jsonPath("$.metaData.page", is(7)));
            result.andExpect(jsonPath("$.metaData.lastPage", is(7)));
            result.andExpect(jsonPath("$.payload[0].code", is("tmp_9")));

            //-------------
            result = mockMvc.perform(
                    get("/groups")
                    .param("pageSize", "0")
                    .param("page", "7")
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isNotFound());

            //-------------
            result = mockMvc.perform(
                    get("/groups")
                    .param("pageSize", "7")
                    .param("page", "0")
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());

            //-------------
            result = mockMvc.perform(
                    get("/groups")
                    .param("pageSize", "1")
                    .param("page", "7")
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(jsonPath("$.metaData.pageSize", is(1)));
            result.andExpect(jsonPath("$.metaData.totalItems", is(31)));
            result.andExpect(jsonPath("$.metaData.page", is(7)));
            result.andExpect(jsonPath("$.metaData.lastPage", is(31)));
            result.andExpect(jsonPath("$.payload[0].code", is("tmp_0")));

        } finally {
            for (Group group : testGroups) {
                this.groupManager.removeGroup(group);
            }
        }
    }

    @Test
    public void testGetGroupsSort() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/groups").param("page", "0")
                .param("direction", "DESC")
                .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());

        result = mockMvc.perform(
                get("/groups").param("page", "1")
                .param("direction", "DESC")
                .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.[0].code", is("management")));

        result = mockMvc.perform(
                get("/groups").param("page", "1")
                .param("pageSize", "4")
                .param("direction", "ASC")
                .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].code", is("administrators")));

    }

    @Test
    public void testAddExistingGroup() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        GroupDto group = this.groupService.getGroup(Group.FREE_GROUP_NAME);
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setCode(group.getCode());
        groupRequest.setName(group.getName());

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(groupRequest);

        ResultActions result = mockMvc.perform(
                post("/groups")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + accessToken));

        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isConflict());

    }

    @Test
    public void testGetInvalidGroup() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        GroupDto group = this.groupService.getGroup(Group.FREE_GROUP_NAME);
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setCode(group.getCode());
        groupRequest.setName(group.getName());

        ResultActions result = mockMvc.perform(
                get("/groups/{code}", "invalid_code")
                .header("Authorization", "Bearer " + accessToken));

        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.errors[0].code", is(GroupValidator.ERRCODE_GROUP_NOT_FOUND)));

    }

    @Test
    public void testUpdateInvalidGroup() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setCode("invalid");
        groupRequest.setName("invalid");

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(groupRequest);

        ResultActions result = mockMvc.perform(
                put("/groups/{code}", groupRequest.getCode())
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + accessToken));

        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.errors[0].code", is(GroupValidator.ERRCODE_GROUP_NOT_FOUND)));

    }

    @Test
    public void testGetGroupDetails() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/groups/{code}", Group.FREE_GROUP_NAME)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + accessToken));

        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.references.length()", is(6)));

        String[] managers = "PageManager,DataObjectManager,WidgetTypeManager,jacmsResourceManager,AuthorizationManager,jacmsContentManager".split(",");

        for (String managerName : managers) {

            result = mockMvc.perform(
                    get(
                            "/groups/{code}/references/{manager}",
                            Group.FREE_GROUP_NAME, managerName)
                    .param("page", "1")
                    .param("pageSize", "3")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken));

        }

    }

    @Test
    public void testParamSize() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setCode(StringUtils.repeat("a", 21));
        groupRequest.setName(StringUtils.repeat("a", 51));

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(groupRequest);

        ResultActions result = mockMvc.perform(
                post("/groups")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken));

        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isBadRequest());

    }

}
