package org.entando.entando.web.system;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ReloadConfigurationControllerTest extends AbstractControllerIntegrationTest {

    @Autowired
    @InjectMocks
    private ReloadConfigurationController controller;


    @Test
    public void should_execute_reload() throws Exception {


        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(post("/reloadConfiguration")
                                                                           .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

}
