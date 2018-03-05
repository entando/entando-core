/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.web.page;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.entando.entando.aps.system.services.page.PageService;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.validator.PageValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author paddeo
 */
public class PageControllerTest extends AbstractControllerTest {

    @Mock
    IPageManager pageManager;

    @Mock
    private PageService pageService;

    @InjectMocks
    private PageController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
        PageValidator pageValidator = new PageValidator();
        pageValidator.setPageManager(pageManager);
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
                + "            \"titles\": [\n"
                + "                {\n"
                + "                    \"lang\": \"en\",\n"
                + "                    \"title\": \"Page not found\"\n"
                + "                },\n"
                + "                {\n"
                + "                    \"lang\": \"it\",\n"
                + "                    \"title\": \"Pagina non trovata\"\n"
                + "                }\n"
                + "            ],\n"
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
                + "            \"titles\": [\n"
                + "                {\n"
                + "                    \"lang\": \"en\",\n"
                + "                    \"title\": \"Error page\"\n"
                + "                },\n"
                + "                {\n"
                + "                    \"lang\": \"it\",\n"
                + "                    \"title\": \"Pagina di errore\"\n"
                + "                }\n"
                + "            ],\n"
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
                + "            \"titles\": [\n"
                + "                {\n"
                + "                    \"lang\": \"en\",\n"
                + "                    \"title\": \"Login\"\n"
                + "                },\n"
                + "                {\n"
                + "                    \"lang\": \"it\",\n"
                + "                    \"title\": \"Pagina di login\"\n"
                + "                }\n"
                + "            ],\n"
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
                + "            \"titles\": [\n"
                + "                {\n"
                + "                    \"lang\": \"en\",\n"
                + "                    \"title\": \"My Title\"\n"
                + "                },\n"
                + "                {\n"
                + "                    \"lang\": \"it\",\n"
                + "                    \"title\": \"Mio Titolo\"\n"
                + "                }\n"
                + "            ],\n"
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

        ResultActions result = mockMvc.perform(
                get("/pages/{parentCode}", "mock_page")
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
                + "            \"titles\": [\n"
                + "                {\n"
                + "                    \"lang\": \"en\",\n"
                + "                    \"title\": \"My Title\"\n"
                + "                },\n"
                + "                {\n"
                + "                    \"lang\": \"it\",\n"
                + "                    \"title\": \"Mio Titolo\"\n"
                + "                }\n"
                + "            ],\n"
                + "            \"ownerGroup\": \"free\",\n"
                + "            \"joinGroups\": [\n"
                + "                \"free\",\n"
                + "                \"administrators\"\n"
                + "            ],\n"
                + "            \"position\": 7\n"
                + "        }";
        PageDto mockResult = (PageDto) this.createMetadata(mockJsonResult, PageDto.class);
        when(pageService.updatePage(any(String.class), any(PageRequest.class))).thenReturn(mockResult);

        ResultActions result = mockMvc.perform(
                put("/page/{pageCode}", "wrong_page")
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
    public void shouldBeUnauthorized() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/pages/{parentCode}", "mock_page")
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

        when(this.controller.getPageValidator().getPageManager().getDraftPage(any(String.class))).thenReturn(new Page());
        ResultActions result = mockMvc.perform(
                post("/pages")
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

        when(this.controller.getPageValidator().getPageManager().getOnlinePage(any(String.class))).thenReturn(new Page());
        ResultActions result = mockMvc.perform(
                delete("/pages/{pageCode}", "online_page")
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

        when(this.controller.getPageValidator().getPageManager().getDraftPage(any(String.class))).thenReturn(page);
        ResultActions result = mockMvc.perform(
                delete("/pages/{pageCode}", "page_with_children")
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        String response = result.andReturn().getResponse().getContentAsString();
        result.andExpect(jsonPath("$.errors", hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", is(PageController.ERRCODE_PAGE_HAS_CHILDREN)));
    }

    private List<PageDto> createMetadataList(String json) throws IOException, JsonParseException, JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        List<PageDto> result = mapper.readValue(json, new TypeReference<List<PageDto>>() {
        });
        return result;
    }
}
