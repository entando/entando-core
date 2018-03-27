/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.web.page;

import java.io.IOException;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.PageUtilizer;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.entando.entando.aps.system.services.page.PageAuthorizationService;
import org.entando.entando.aps.system.services.page.PageService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.page.model.PagePositionRequest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.PageStatusRequest;
import org.entando.entando.web.page.validator.PageValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author paddeo
 */
public class PageControllerTest extends AbstractControllerTest {

    @Mock
    IPageManager pageManager;

    @Mock
    IContentManager contentManager;

    @Mock
    PageUtilizer pageUtilizer;

    @Mock
    private PageService pageService;

    @Mock
    private PageAuthorizationService authorizationService;

    @InjectMocks
    private PageController controller;

    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
        PageValidator pageValidator = new PageValidator();
        pageValidator.setPageManager(pageManager);
        pageValidator.setContentManager(contentManager);
        pageValidator.setPageUtilizer(pageUtilizer);
        this.controller.setPageValidator(pageValidator);
    }

    @Test
    public void shouldLoadAPageTree() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJsonResult = "[\n"
                + "        {\n"
                + "            \"code\": \"notfound\",\n"
                + "            \"status\": \"draft\",\n"
                + "            \"displayedInMenu\": true,\n"
                + "            \"pageModel\": \"service\",\n"
                + "            \"charset\": null,\n"
                + "            \"contentType\": null,\n"
                + "            \"parentCode\": \"service\",\n"
                + "            \"seo\": false,\n"
                + "            \"titles\": {\"en\": \"Page not found\",\n"
                + "                \"it\": \"Pagina non trovata\"\n"
                + "                },\n"
                + "            \"ownerGroup\": \"free\",\n"
                + "            \"joinGroups\": [],\n"
                + "            \"position\": 4\n"
                + "        },\n"
                + "        {\n"
                + "            \"code\": \"errorpage\",\n"
                + "            \"status\": \"draft\",\n"
                + "            \"displayedInMenu\": true,\n"
                + "            \"pageModel\": \"service\",\n"
                + "            \"charset\": null,\n"
                + "            \"contentType\": null,\n"
                + "            \"parentCode\": \"service\",\n"
                + "            \"seo\": false,\n"
                + "            \"titles\": {\n"
                + "                \"en\": \"Error page\",\n"
                + "                \"it\": \"Pagina di errore\"\n"
                + "                },\n"
                + "            \"ownerGroup\": \"free\",\n"
                + "            \"joinGroups\": [],\n"
                + "            \"position\": 5\n"
                + "        },\n"
                + "        {\n"
                + "            \"code\": \"login\",\n"
                + "            \"status\": \"draft\",\n"
                + "            \"displayedInMenu\": true,\n"
                + "            \"pageModel\": \"service\",\n"
                + "            \"charset\": null,\n"
                + "            \"contentType\": null,\n"
                + "            \"parentCode\": \"service\",\n"
                + "            \"seo\": false,\n"
                + "            \"titles\": {\n"
                + "                    \"en\": \"Login\",\n"
                + "                \"it\": \"Pagina di login\"\n"
                + "                },\n"
                + "            \"ownerGroup\": \"free\",\n"
                + "            \"joinGroups\": [],\n"
                + "            \"position\": 6\n"
                + "        },\n"
                + "        {\n"
                + "            \"code\": \"hello_page\",\n"
                + "            \"status\": \"draft\",\n"
                + "            \"displayedInMenu\": true,\n"
                + "            \"pageModel\": \"service\",\n"
                + "            \"charset\": \"utf8\",\n"
                + "            \"contentType\": \"text/html\",\n"
                + "            \"parentCode\": \"service\",\n"
                + "            \"seo\": false,\n"
                + "            \"titles\": {\n"
                + "                    \"en\": \"My Title\",\n"
                + "                \"it\": \"Mio Titolo\"\n"
                + "                },\n"
                + "            \"ownerGroup\": \"free\",\n"
                + "            \"joinGroups\": [\n"
                + "                \"free\",\n"
                + "                \"administrators\"\n"
                + "            ],\n"
                + "            \"position\": 7\n"
                + "        }\n"
                + "    ]";
        List<PageDto> mockResult = (List<PageDto>) this.createMetadataList(mockJsonResult);
        when(pageService.getPages(any(String.class))).thenReturn(mockResult);
        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        ResultActions result = mockMvc.perform(
                get("/pages").
                        param("parentCode", "service")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken)
        );
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.errors", hasSize(0)));
    }

    @Test
    public void shouldValidatePutPathMismatch() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String mockJsonResult = "{\n"
                + "            \"code\": \"hello_page\",\n"
                + "            \"status\": \"draft\",\n"
                + "            \"displayedInMenu\": true,\n"
                + "            \"pageModel\": \"service\",\n"
                + "            \"charset\": \"utf8\",\n"
                + "            \"contentType\": \"text/html\",\n"
                + "            \"parentCode\": \"service\",\n"
                + "            \"seo\": false,\n"
                + "            \"titles\": {\n"
                + "                    \"en\": \"My Title\",\n"
                + "                \"it\": \"Mio Titolo\"\n"
                + "                },\n"
                + "            \"ownerGroup\": \"free\",\n"
                + "            \"joinGroups\": [\n"
                + "                \"free\",\n"
                + "                \"administrators\"\n"
                + "            ],\n"
                + "            \"position\": 7\n"
                + "        }";
        PageDto mockResult = (PageDto) this.createMetadata(mockJsonResult, PageDto.class);
        when(pageService.updatePage(any(String.class), any(PageRequest.class))).thenReturn(mockResult);
        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        ResultActions result = mockMvc.perform(
                put("/pages/{pageCode}", "wrong_page")
                        .sessionAttr("user", user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockJsonResult)
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_URINAME_MISMATCH)));

    }

    @Test
    public void shouldValidateStatusPutDraftRef() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PageStatusRequest request = new PageStatusRequest();
        request.setStatus("draft");

        PageM pageToUnpublish = new PageM(true);
        pageToUnpublish.setCode("page_to_unpublish");
        pageToUnpublish.setParentCode("service");
        pageToUnpublish.setChildrenCodes(new String[]{"child_page"});

        PageM child = new PageM(true);
        child.setCode("child_page");
        child.setParentCode("page_to_unpublish");

        PageM root = new PageM(true);
        root.setCode("home");
        root.setParentCode("home");

        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        when(this.controller.getPageValidator().getPageManager().getDraftPage(any(String.class))).thenReturn(pageToUnpublish, child);
        when(this.controller.getPageValidator().getPageManager().getOnlineRoot()).thenReturn(root);
        when(this.controller.getPageValidator().getPageUtilizer().getPageUtilizers(any(String.class))).thenReturn(new ArrayList());
        ResultActions result = mockMvc.perform(
                put("/pages/{pageCode}/status", "page_to_publish")
                        .sessionAttr("user", user)
                        .content(convertObjectToJsonBytes(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("RESPONSE: " + response);
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_REFERENCED_ONLINE_PAGE)));
    }

    @Test
    public void shouldValidateStatusPutOnlineRef() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PageStatusRequest request = new PageStatusRequest();
        request.setStatus("published");

        PageM pageToPublish = new PageM(false);
        pageToPublish.setCode("page_to_publish");
        pageToPublish.setParentCode("unpublished");

        PageM unpublished = new PageM(false);
        unpublished.setCode("unpublished");
        unpublished.setParentCode("service");

        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        when(this.controller.getPageValidator().getPageManager().getDraftPage(any(String.class))).thenReturn(pageToPublish, unpublished);
        when(this.controller.getPageValidator().getPageUtilizer().getPageUtilizers(any(String.class))).thenReturn(new ArrayList());
        ResultActions result = mockMvc.perform(
                put("/pages/{pageCode}/status", "page_to_publish")
                        .sessionAttr("user", user)
                        .content(convertObjectToJsonBytes(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println("RESPONSE: " + response);
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_REFERENCED_DRAFT_PAGE)));
    }

    @Test
    public void shouldBeUnauthorized() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/pages/{parentCode}", "mock_page")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldValidatePostConflict() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PageRequest page = new PageRequest();
        page.setCode("existing_page");
        page.setPageModel("existing_model");
        page.setParentCode("existing_parent");
        page.setOwnerGroup("existing_group");
        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        when(this.controller.getPageValidator().getPageManager().getDraftPage(any(String.class))).thenReturn(new Page());
        ResultActions result = mockMvc.perform(
                post("/pages")
                        .sessionAttr("user", user)
                        .content(convertObjectToJsonBytes(page))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isConflict());
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_PAGE_ALREADY_EXISTS)));
    }

    @Test
    public void shouldValidateDeleteOnlinePage() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        when(this.controller.getPageValidator().getPageManager().getOnlinePage(any(String.class))).thenReturn(new Page());
        ResultActions result = mockMvc.perform(
                delete("/pages/{pageCode}", "online_page")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_ONLINE_PAGE)));
    }

    @Test
    public void shouldValidateDeletePageWithChildren() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        Page page = new Page();
        page.setCode("page_with_children");
        page.addChildCode("child");
        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        when(this.controller.getPageValidator().getPageManager().getDraftPage(any(String.class))).thenReturn(page);
        ResultActions result = mockMvc.perform(
                delete("/pages/{pageCode}", "page_with_children")
                        .sessionAttr("user", user)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_PAGE_HAS_CHILDREN)));
    }

    @Test
    public void shouldValidateMovePageInvalidRequest() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PagePositionRequest request = new PagePositionRequest();
        request.setCode("page_to_move");
        request.setParentCode(null);
        request.setPosition(0);
        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        ResultActions result = mockMvc.perform(
                put("/pages/{pageCode}/position", "page_to_move")
                        .sessionAttr("user", user)
                        .content(convertObjectToJsonBytes(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is("NotBlank")));
    }

    @Test
    public void shouldValidateMovePageGroupMismatch() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PagePositionRequest request = new PagePositionRequest();
        request.setCode("page_to_move");
        request.setParentCode("new_parent_page");
        request.setPosition(1);

        Page pageToMove = new Page();
        pageToMove.setCode("page_to_move");
        pageToMove.setParentCode("old_parent_page");
        pageToMove.setGroup("page_to_move_group");

        Page newParent = new Page();
        newParent.setCode("new_parent_page");
        newParent.setParentCode("another_parent_page");
        newParent.setGroup("another_group");
        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        when(this.controller.getPageValidator().getPageManager().getDraftPage("page_to_move")).thenReturn(pageToMove);
        when(this.controller.getPageValidator().getPageManager().getDraftPage("new_parent_page")).thenReturn(newParent);
        ResultActions result = mockMvc.perform(
                put("/pages/{pageCode}/position", "page_to_move")
                        .sessionAttr("user", user)
                        .content(convertObjectToJsonBytes(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_GROUP_MISMATCH)));
    }

    @Test
    public void shouldValidateMovePageStatusMismatch() throws ApsSystemException, Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PagePositionRequest request = new PagePositionRequest();
        request.setCode("page_to_move");
        request.setParentCode("new_parent_page");
        request.setPosition(1);

        PageM pageToMove = new PageM(true);
        pageToMove.setCode("page_to_move");
        pageToMove.setParentCode("old_parent_page");
        pageToMove.setGroup("valid_group");

        PageM newParent = new PageM(false);
        newParent.setCode("new_parent_page");
        newParent.setParentCode("another_parent_page");
        newParent.setGroup("valid_group");
        when(authorizationService.isAuth(any(UserDetails.class), any(String.class))).thenReturn(true);
        when(this.controller.getPageValidator().getPageManager().getDraftPage("page_to_move")).thenReturn(pageToMove);
        when(this.controller.getPageValidator().getPageManager().getDraftPage("new_parent_page")).thenReturn(newParent);
        ResultActions result = mockMvc.perform(
                put("/pages/{pageCode}/position", "page_to_move")
                        .sessionAttr("user", user)
                        .content(convertObjectToJsonBytes(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_STATUS_PAGE_MISMATCH)));
    }

    private List<PageDto> createMetadataList(String json) throws IOException, JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        List<PageDto> result = mapper.readValue(json, new TypeReference<List<PageDto>>() {
        });
        return result;
    }

    private class PageM extends Page {

        public PageM(boolean isOnline) {
            this.setOnline(isOnline);
        }
    }
}
