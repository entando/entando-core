package org.entando.entando.web.language;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.language.LanguageService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LanguageControllerUnitTest extends AbstractControllerTest {

    @Mock
    private LanguageService languageService;

    @InjectMocks
    private LanguageController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .addInterceptors(entandoOauth2Interceptor)
                                 .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                                 .build();
    }


    @Test
    public void testUpdateNoPayload() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String payload = "{}";
        ResultActions result = mockMvc
                                      .perform(put("/languages/{code}", new Object[]{"de"})
                                                                                           .content(payload)
                                                                                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                           .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateWrongPayload() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String payload = "{\"isActive\": \"WRONG\"}";
        ResultActions result = mockMvc
                                      .perform(put("/languages/{code}", new Object[]{"de"})
                                                                                           .content(payload)
                                                                                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                           .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());
    }


    @Test
    public void testUpdatePayloadOk() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String payload = "{\"isActive\": false}";
        ResultActions result = mockMvc
                                      .perform(put("/languages/{code}", new Object[]{"de"})
                                                                                           .content(payload)
                                                                                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                           .header("Authorization", "Bearer " + accessToken));
        Mockito.verify(languageService, Mockito.times(1)).updateLanguage("de", false);
        result.andExpect(status().isOk());
    }

}
