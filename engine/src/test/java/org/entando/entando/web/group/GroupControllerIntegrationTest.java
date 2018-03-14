package org.entando.entando.web.group;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Test
    public void testGetGroupsSort() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                                               get("/groups").param("page", "0")
                                               .param("direction", "DESC")
                                               .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.[0].code", is("management")));


        result = mockMvc.perform(
                                 get("/groups").param("page", "0")
                                 .param("pageSize", "4")
                                 .param("direction", "ASC")
                                 .header("Authorization", "Bearer " + accessToken));

        System.out.println(result.andReturn().getResponse().getContentAsString());

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].code", is("administrators")));

    }

}
