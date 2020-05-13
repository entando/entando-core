/*
 * Copyright 2018-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.role;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasEntry;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.JsonPatchBuilder;
import org.entando.entando.web.role.model.RoleRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class RoleControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IRoleManager roleManager;

    @Test
    public void testGetRoles() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/roles")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(1)));
    }

    @Test
    public void testGetRolesFilterByName() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/roles")
                        .param("filter[0].attribute", "name")
                        .param("filter[0].value", "gestore")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(2)));
        result.andExpect(jsonPath("$.payload[0].name", is("Gestore di Pagine")));

        result = mockMvc
                .perform(get("/roles")
                        .param("filter[0].attribute", "name")
                        .param("filter[0].value", "gestore")
                        .param("sort", "code")
                        .param("direction", "ASC")
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
        result.andExpect(status().isOk());

    }

    @Test
    public void testGetRoleNotFound() throws Exception {
        String code = "this_role_has_no_name";
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/roles/{rolecode}", code)
                        .header("Authorization", "Bearer " + accessToken).with(csrf()));
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
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isOk());

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
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isOk());

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
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isBadRequest());

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
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isOk());

            //--------------
            payload = new JsonPatchBuilder()
                    .withOperation("replace", "/name", "New Test Role")
                    .withOperation("replace", "/permissions", ImmutableMap.of("manageCategories", true) )
                    .getJsonPatchAsString();


            result = mockMvc
                    .perform(patch("/roles/{code}", code)
                                     .contentType(RestMediaTypes.JSON_PATCH_JSON)
                                     .content(payload)
                                     .header("Authorization", "Bearer " + accessToken).with(csrf()));


            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.name", is("New Test Role")));
            result.andExpect(jsonPath("$.payload.permissions.manageCategories", is(true)));

            //--------------

            payload = new JsonPatchBuilder()
                    .withOperation("replace", "/code", "newcode")
                    .getJsonPatchAsString();

            result = mockMvc
                    .perform(patch("/roles/{code}", code)
                                     .contentType(RestMediaTypes.JSON_PATCH_JSON)
                                     .content(payload)
                                     .header("Authorization", "Bearer " + accessToken).with(csrf()));


            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0]", allOf(
                   hasEntry("code", "1"),
                   hasEntry("message", "The field 'code' can not be updated via JSON patch")
                 )));

            //--------------

            result = mockMvc
                    .perform(delete("/roles/{code}", code)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken).with(csrf()));

            result.andExpect(status().isOk());

        } finally {
            Role role = this.roleManager.getRole(code);
            if (null != role) {
                this.roleManager.removeRole(role);
            }
        }
    }

}
