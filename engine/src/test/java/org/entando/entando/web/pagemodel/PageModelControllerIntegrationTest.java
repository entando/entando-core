package org.entando.entando.web.pagemodel;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.pagemodel.PageModelService;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PageModelControllerIntegrationTest extends AbstractControllerTest {


    private MockMvc mockMvc;

    @Mock
    private PageModelService pageModelService;

    @InjectMocks
    private PageModelController controller;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .addInterceptors(entandoOauth2Interceptor)
                                 .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                                 .build();
    }


    @Test
    public void should_load_the_list_of_pageModels() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(pageModelService.getPageModels(any(RestListRequest.class))).thenReturn(new PagedMetadata<PageModelDto>());
        ResultActions result = mockMvc.perform(
                                               get("/pagemodels")
                                                                 .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());

        RestListRequest restListReq = new RestListRequest();
        Mockito.verify(pageModelService, Mockito.times(1)).getPageModels(restListReq);
        //System.out.println(result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void should_add_page_model() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(pageModelService.getPageModels(any(RestListRequest.class))).thenReturn(new PagedMetadata<PageModelDto>());

        ResultActions result = mockMvc.perform(
                                               post("/pagemodels")
                                                                  .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());

        RestListRequest restListReq = new RestListRequest();
        Mockito.verify(pageModelService, Mockito.times(1)).getPageModels(restListReq);
        //System.out.println(result.andReturn().getResponse().getContentAsString());
    }

}
