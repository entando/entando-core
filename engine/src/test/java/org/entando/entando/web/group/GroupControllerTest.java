package org.entando.entando.web.group;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.group.GroupService;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GroupControllerTest extends AbstractControllerTest {

    private MockMvc mockMvc;

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
        when(groupService.getGroups(any(RestListRequest.class))).thenReturn(new PagedMetadata<GroupDto>());
        ResultActions result = mockMvc.perform(
                                               get("/groups").param("page", "1")
                                               .param("pageSize", "4")
                                               .header("Authorization", "Bearer " + accessToken)
                );

        result.andExpect(status().isOk());

        RestListRequest restListReq = new RestListRequest();
        restListReq.setPage(1);
        restListReq.setPageSize(4);
        Mockito.verify(groupService, Mockito.times(1)).getGroups(restListReq);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should_load_the_list_of_groups_2() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(groupService.getGroups(any(RestListRequest.class))).thenReturn(new PagedMetadata<GroupDto>());


        ResultActions result = mockMvc.perform(
                                               get("/groups").param("page", "1")
                                               .param("pageSize", "4")
                                               .param("filter[0].attribute", "code")
                                               .param("filter[0].value", "free")
                                               .header("Authorization", "Bearer " + accessToken)
                );

        result.andExpect(status().isOk());

        RestListRequest restListReq = new RestListRequest();
        restListReq.setPage(1);
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
        //System.out.println(response);
        result.andExpect(status().isUnauthorized());
    }


    @Test
    public void should_validate_put_path_mismatch() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ObjectMapper mapper = new ObjectMapper();
        GroupRequest group = new GroupRequest();
        group.setCode("__helpdesk_");
        group.setName("Helpdesk");
        String payload = mapper.writeValueAsString(group);

        this.controller.setGroupValidator(new GroupValidator());
        ResultActions result = mockMvc.perform(
                                               put("/groups/{groupCode}",
                                                   "helpdesk")
                                                              .content(payload)
                                                              .contentType(MediaType.APPLICATION_JSON)
                                                              .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        //System.out.println(response);
    }

    @Test
    public void should_validate_delete_reserved_groups() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String groupName = Group.FREE_GROUP_NAME;


        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult("group", "group");
        bindingResult.reject(GroupValidator.ERRCODE_CANNOT_DELETE_RESERVED_GROUP, new String[]{groupName}, "group.cannot.delete.reserved");
        doThrow(new ValidationConflictException(bindingResult)).when(groupService).removeGroup(groupName);

        this.controller.setGroupValidator(new GroupValidator());
        ResultActions result = mockMvc.perform(
                                               delete("/groups/{groupName}",
                                                      groupName)
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isConflict());
        result.andExpect(jsonPath("$.errors[0].code", is(GroupValidator.ERRCODE_CANNOT_DELETE_RESERVED_GROUP)));
    }




}
