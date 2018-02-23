package org.entando.entando.web.group;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.group.GroupService;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.group.model.GroupRequest;
import org.entando.entando.web.group.validator.GroupValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerTest extends AbstractControllerTest {

    @Mock
    private GroupService groupService;

    @InjectMocks
    private GroupController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_load_the_list_of_groups() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJsonResult = "{\n"
                + "  \"page\" : 1,\n"
                + "  \"size\" : 2,\n"
                + "  \"last\" : 1,\n"
                + "  \"count\" : 6,\n"
                + "  \"body\" : [ {\n"
                + "    \"code\" : \"helpdesk\",\n"
                + "    \"name\" : \"Helpdesk\"\n"
                + "  }, {\n"
                + "    \"code\" : \"management\",\n"
                + "    \"name\" : \"Management\"\n"
                + "  } ]\n"
                + "}";
        PagedMetadata<GroupDto> mockResult = (PagedMetadata<GroupDto>) this.createPagedMetadata(mockJsonResult);
        when(groupService.getGroups(any(RestListRequest.class))).thenReturn(mockResult);

        ResultActions result = mockMvc.perform(
                get("/groups")
                        .param("pageNum", "1")
                        .param("pageSize", "4")
                        .header("Authorization", "Bearer " + accessToken)
        );

        result.andExpect(status().isOk());

        RestListRequest restListReq = new RestListRequest();
        restListReq.setPageNum(1);
        restListReq.setPageSize(4);
        Mockito.verify(groupService, Mockito.times(1)).getGroups(restListReq);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_load_the_list_of_groups_2() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJsonResult = "{\n"
                + "  \"page\" : 1,\n"
                + "  \"size\" : 2,\n"
                + "  \"last\" : 1,\n"
                + "  \"count\" : 6,\n"
                + "  \"body\" : [ {\n"
                + "    \"code\" : \"helpdesk\",\n"
                + "    \"name\" : \"Helpdesk\"\n"
                + "  }, {\n"
                + "    \"code\" : \"management\",\n"
                + "    \"name\" : \"Management\"\n"
                + "  } ]\n"
                + "}";
        PagedMetadata<GroupDto> mockResult = (PagedMetadata<GroupDto>) this.createPagedMetadata(mockJsonResult);
        when(groupService.getGroups(any(RestListRequest.class))).thenReturn(mockResult);

        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/groups")
                        .param("pageNum", "1")
                        .param("pageSize", "4")
                        .param("filter[0].attribute", "code")
                        .param("filter[0].value", "free")
                        .header("Authorization", "Bearer " + accessToken)
        );
        // @formatter:on
        result.andExpect(status().isOk());

        RestListRequest restListReq = new RestListRequest();
        restListReq.setPageNum(1);
        restListReq.setPageSize(4);
        restListReq.addFilter(new Filter("code", "free"));
        Mockito.verify(groupService, Mockito.times(1)).getGroups(restListReq);
    }

    @Test
    public void should_be_unauthorized() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/groups")
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void should_validate_put_path_mismatch() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        GroupRequest group = new GroupRequest();
        group.setName("__helpdesk_");
        group.setDescr("Helpdesk");

        this.controller.setGroupValidator(new GroupValidator());
        ResultActions result = mockMvc.perform(
                put("/group/{groupCode}", "helpdesk")
                        .content(convertObjectToJsonBytes(group))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
    }

}
