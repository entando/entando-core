package org.entando.entando.web.system;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReloadConfigurationControllerTest extends AbstractControllerTest {


    private MockMvc mockMvc;

    @Mock
    private ApplicationContext applicationContext;

    @InjectMocks
    private ReloadConfigurationController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .addInterceptors(entandoOauth2Interceptor)
                                 .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                                 .build();

    }

    @Test
    public void should_load_the_list_of_groups() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc
                                      .perform(post("/reloadConfiguration")
                                                                           .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());


    }

}
