/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.pagesettings;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.aps.system.services.pagesettings.PageSettingsService;
import org.entando.entando.aps.system.services.pagesettings.model.PageSettingsDto;
import org.entando.entando.aps.system.services.pagesettings.model.ParamDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.pagesettings.model.PageSettingsRequest;
import org.entando.entando.web.pagesettings.model.Param;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
public class PageSettingsControllerTest extends AbstractControllerTest {

    @Mock
    protected PageSettingsService pageSettingsService;

    @InjectMocks
    private PageSettingsController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void should_load_the_list_of_settings() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup("pageSettingsGroup")
                .withAuthorization("pageSettingsGroup", "pageSessings", "pageSettings_read", "pageSettings_write")
                .build();
        String accessToken = mockOAuthInterceptor(user);
        when(pageSettingsService.getPageSettings()).thenReturn(createMockDto());
        ResultActions result = mockMvc.perform(
                get("/pageSettings")
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isOk());
    }

    @Test
    public void should_not_update_with_empty_list_of_settings() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup("pageSettingsGroup")
                .withAuthorization("pageSettingsGroup", "pageSessings", "pageSettings_read", "pageSettings_write")
                .build();
        String accessToken = mockOAuthInterceptor(user);
        when(pageSettingsService.updatePageSettings(createMockRequestEmptyParams())).thenReturn(createMockDto());
        ResultActions result = mockMvc.perform(
                put("/pageSettings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(createMockRequestEmptyParams()))
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void should_update_with_a_valid_list_of_settings() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup("pageSettingsGroup")
                .withAuthorization("pageSettingsGroup", "pageSessings", "pageSettings_read", "pageSettings_write")
                .build();
        String accessToken = mockOAuthInterceptor(user);
        when(pageSettingsService.updatePageSettings(createMockRequest())).thenReturn(createMockDto());
        ResultActions result = mockMvc.perform(
                put("/pageSettings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(createMockRequest()))
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isOk());
    }

    @Test
    public void should_be_unauthorized() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/pageSettings")
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isUnauthorized());
    }

    private PageSettingsDto createMockDto() {
        PageSettingsDto dto = new PageSettingsDto();
        List<ParamDto> params = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            ParamDto param = new ParamDto();
            param.setName("param_" + i);
            param.setValue("value_" + i);
            params.add(param);
        }
        dto.setParams(params);
        return dto;
    }

    private PageSettingsRequest createMockRequestEmptyParams() {
        PageSettingsRequest request = new PageSettingsRequest();
        request.setParams(new ArrayList<>());
        return request;
    }

    private PageSettingsRequest createMockRequest() {
        PageSettingsRequest request = new PageSettingsRequest();
        List<Param> params = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Param param = new Param();
            param.setName("param_" + i);
            param.setValue("value_" + i);
            params.add(param);
        }
        request.setParams(params);
        return request;
    }

}
