/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.page;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.List;
import org.entando.entando.aps.system.services.group.GroupService;
import org.entando.entando.aps.system.services.group.model.GroupDto;
import org.entando.entando.aps.system.services.page.PageService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.group.GroupController;
import org.entando.entando.web.group.model.GroupRequest;
import org.entando.entando.web.group.validator.GroupValidator;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.validator.PageValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author paddeo
 */
public class PageControllerTest extends AbstractControllerTest {

    @Mock
    private PageService pageService;

    @InjectMocks
    private PageController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

//    @SuppressWarnings("unchecked")
//    @Test
//    public void shouldLoadTheListOfPages() throws Exception {
//        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
//        String accessToken = mockOAuthInterceptor(user);
//
//        String mockJsonResult = "{\n"
//                + "  \"page\" : 1,\n"
//                + "  \"size\" : 2,\n"
//                + "  \"last\" : 1,\n"
//                + "  \"count\" : 6,\n"
//                + "  \"body\" : [ {\n"
//                + "    \"code\" : \"helpdesk\",\n"
//                + "    \"name\" : \"Helpdesk\"\n"
//                + "  }, {\n"
//                + "    \"code\" : \"management\",\n"
//                + "    \"name\" : \"Management\"\n"
//                + "  } ]\n"
//                + "}";
//        List<PageDto> mockResult = (List<PageDto>) this.createMetadata(mockJsonResult, List.class);
//        when(pageService.getPages(any(String.class))).thenReturn(mockResult);
//
//        ResultActions result = mockMvc.perform(
//                get("/pages")
//                        .param("pageNum", "1")
//                        .param("pageSize", "4")
//                        .header("Authorization", "Bearer " + accessToken)
//        );
//
//        result.andExpect(status().isOk());
//
//        RestRequest restListReq = new RestListRequest();
//        restListReq.setPageNum(1);
//        restListReq.setPageSize(4);
//        Mockito.verify(pageService, Mockito.times(1)).getPages(restListReq);
//    }
//    @SuppressWarnings("unchecked")
//    @Test
//    public void should_load_the_list_of_pages_2() throws Exception {
//
//        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
//        String accessToken = mockOAuthInterceptor(user);
//
//        String mockJsonResult = "{\n"
//                + "  \"page\" : 1,\n"
//                + "  \"size\" : 2,\n"
//                + "  \"last\" : 1,\n"
//                + "  \"count\" : 6,\n"
//                + "  \"body\" : [ {\n"
//                + "    \"code\" : \"helpdesk\",\n"
//                + "    \"name\" : \"Helpdesk\"\n"
//                + "  }, {\n"
//                + "    \"code\" : \"management\",\n"
//                + "    \"name\" : \"Management\"\n"
//                + "  } ]\n"
//                + "}";
//        PagedMetadata<PageDto> mockResult = (PagedMetadata<PageDto>) this.createPagedMetadata(mockJsonResult);
//        when(pageService.getPages(any(RestListRequest.class))).thenReturn(mockResult);
//
//        // @formatter:off
//        ResultActions result = mockMvc.perform(
//                get("/pages")
//                        .param("pageNum", "1")
//                        .param("pageSize", "4")
//                        .param("filter[0].attribute", "code")
//                        .param("filter[0].value", "free")
//                        .header("Authorization", "Bearer " + accessToken)
//        );
//        // @formatter:on
//        result.andExpect(status().isOk());
//
//        RestListRequest restListReq = new RestListRequest();
//        restListReq.setPageNum(1);
//        restListReq.setPageSize(4);
//        restListReq.addFilter(new Filter("code", "free"));
//        Mockito.verify(pageService, Mockito.times(1)).getPages(restListReq);
//    }
//
//    @Test
//    public void should_be_unauthorized() throws Exception {
//        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
//                .withGroup(Group.FREE_GROUP_NAME)
//                .build();
//        String accessToken = mockOAuthInterceptor(user);
//
//        ResultActions result = mockMvc.perform(
//                get("/pages")
//                        .header("Authorization", "Bearer " + accessToken)
//        );
//
//        String response = result.andReturn().getResponse().getContentAsString();
//        System.out.println(response);
//        result.andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void should_validate_put_path_mismatch() throws ApsSystemException, Exception {
//        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
//        String accessToken = mockOAuthInterceptor(user);
//
//        PageRequest page = new PageRequest();
//        page.setName("__helpdesk_");
//        page.setDescr("Helpdesk");
//
//        this.controller.setPageValidator(new PageValidator());
//        ResultActions result = mockMvc.perform(
//                put("/page/{pageCode}", "helpdesk")
//                        .content(convertObjectToJsonBytes(page))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("Authorization", "Bearer " + accessToken));
//
//        result.andExpect(status().isBadRequest());
//        String response = result.andReturn().getResponse().getContentAsString();
//        System.out.println(response);
//    }
}
