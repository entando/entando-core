package org.entando.entando.web.role;

import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerIntegrationTest extends AbstractControllerIntegrationTest {


    @Test
    public void testGetRoles() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/roles")
                                                                 .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetRolesFilterByCode() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/roles")
                                                            .param("filter[0].attribute", "code")
                                                            .param("filter[0].value", "admin")
                                                            .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(1)));
        System.out.println(result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGetRolesFilterByName() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/roles")
                                                            .param("filter[0].attribute", "name")
                                                            .param("filter[0].value", "gestore")
                                                            .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(2)));
    }

    @Test
    public void testGetRolesFilterByNameAndSort() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/roles")
                                                            .param("filter[0].attribute", "name")
                                                            .param("filter[0].value", "gestore")
                                                            .param("sort", "code")
                                                            .param("direction", "DESC")
                                                            .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(2)));
        result.andExpect(jsonPath("$.payload[0].name", is("Gestore di Pagine")));

        result = mockMvc
                        .perform(get("/roles")
                                              .param("filter[0].attribute", "name")
                                              .param("filter[0].value", "gestore")
                                              .param("sort", "code")
                                              .param("direction", "ASC")
                                              .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(2)));
        result.andExpect(jsonPath("$.payload[0].name", is("Gestore di Contenuti e Risorse")));

    }

    @Test
    public void testGetRoleOk() throws Exception {
        String code = "editor";
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/roles/{rolecode}", code)

                                                                             .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.permissions.length()", greaterThan(0)));
    }

    @Test
    public void testGetRoleNotFound() throws Exception {
        String code = "this_role_has_no_name";
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/roles/{rolecode}", code)

                                                                             .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
        //result.andExpect(jsonPath("$.payload.permissions.length()", greaterThan(0)));
    }


}
