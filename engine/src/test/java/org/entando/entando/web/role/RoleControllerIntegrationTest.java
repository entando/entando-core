package org.entando.entando.web.role;

import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.role.model.RoleRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RoleControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IRoleManager roleManager;

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
    public void testGetRoleUserReferences() throws Exception {
        String code = "editor";
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/roles/{rolecode}/userreferences", code)
                        .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());

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

    /**
     * Add a role without permissions
     * </p>
     *
     * Update the role with 1 valid permissions
     * </p>
     *
     * Update the role by adding 1 invalid permissions
     * </p>
     *
     * Update the role by adding 1 valid permissions
     * </p>
     *
     * delete the role
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void testCrudRole() throws Exception {
        String code = "test";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ObjectMapper mapper = new ObjectMapper();
            RoleRequest request = new RoleRequest();
            request.setCode(code);
            request.setName(code);
            String payload = mapper.writeValueAsString(request);

            ResultActions result = mockMvc
                    .perform(post("/roles")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());
            System.out.println("ADD");
            //{"payload":{"code":"test","name":"test","permissions":{"enterBackend":false,"managePages":false,"editContents":false,"validateContents":false,"manageResources":false,"superuser":false,"manageCategories":false}},"errors":[],"metaData":{}}

            System.out.println(result.andReturn().getResponse().getContentAsString());

            //--------------
            request = new RoleRequest();
            request.setCode(code);
            request.setName(code.toUpperCase());
            request.getPermissions().put("editContents", true);
            payload = mapper.writeValueAsString(request);

            result = mockMvc
                    .perform(put("/roles/{code}", code)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            System.out.println("UPDATE");
            System.out.println(result.andReturn().getResponse().getContentAsString());
            //{"payload":{"code":"test","name":"TEST","permissions":{"enterBackend":false,"managePages":false,"editContents":true,"validateContents":false,"manageResources":false,"superuser":false,"manageCategories":false}},"errors":[],"metaData":{}}

            //--------------
            request = new RoleRequest();
            request.setCode(code);
            request.setName(code.toUpperCase());
            request.getPermissions().put("editContents", true);
            request.getPermissions().put("WRONG", true);
            payload = mapper.writeValueAsString(request);

            result = mockMvc
                    .perform(put("/roles/{code}", code)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());

            System.out.println("UPDATE-WRONG");
            System.out.println(result.andReturn().getResponse().getContentAsString());
            //{"payload":[],"errors":[{"code":"4","message":"role.permission.notFound"}],"metaData":{}}
            //--------------

            request = new RoleRequest();
            request.setCode(code);
            request.setName(code.toUpperCase());
            request.getPermissions().put("editContents", true);
            request.getPermissions().put("manageResources", true);
            request.getPermissions().put("manageCategories", false);
            payload = mapper.writeValueAsString(request);

            result = mockMvc
                    .perform(put("/roles/{code}", code)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            System.out.println("UPDATE-OK");
            System.out.println(result.andReturn().getResponse().getContentAsString());

            //--------------
            result = mockMvc
                    .perform(delete("/roles/{code}", code)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            System.out.println("UPDATE-OK");
            System.out.println(result.andReturn().getResponse().getContentAsString());

        } finally {
            Role role = this.roleManager.getRole(code);
            if (null != role) {
                this.roleManager.removeRole(role);
            }
        }
    }

}
