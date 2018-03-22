package org.entando.entando.web.usersettings;

import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.usersettings.UserSettingsService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.usersettings.model.UserSettingsRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserSettigsControllerUnitTest extends AbstractControllerTest {

    @Mock
    private UserSettingsService userSettingsService;

    @InjectMocks
    private UserSettingsController controller;

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .addInterceptors(entandoOauth2Interceptor)
                                 .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                                 .build();
        mapper = new ObjectMapper();
    }

    @Test
    public void testUpdateSettingsWithEmptyParams() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        UserSettingsRequest userSettingsRequest = new UserSettingsRequest();

        ResultActions result = mockMvc.perform(
                                               put("/usersettings")
                                                                   .content(mapper.writeValueAsString(userSettingsRequest))
                                                                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                   .header("Authorization", "Bearer " + accessToken));

        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateSettingsWithInvalidParams() throws Throwable {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        UserSettingsRequest userSettingsRequest = new UserSettingsRequest();
        userSettingsRequest.setMaxMonthsSinceLastAccess(-6);
        userSettingsRequest.setMaxMonthsSinceLastPasswordChange(-3);

        ResultActions result = mockMvc.perform(
                                               put("/usersettings")
                                                                   .content(mapper.writeValueAsString(userSettingsRequest))
                                                                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                   .header("Authorization", "Bearer " + accessToken));

        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isBadRequest());
    }

}
