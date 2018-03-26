package org.entando.entando.web.pagemodel;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PageModelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    //    @Autowired
    //    private PageModelController controller;

    @Test
    public void testGetPageModels() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/pagemodels")
                                                                 .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());

        //System.out.println(result.andReturn().getResponse().getContentAsString());

    }

}
