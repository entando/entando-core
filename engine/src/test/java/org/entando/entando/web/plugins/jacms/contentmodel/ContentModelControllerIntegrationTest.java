package org.entando.entando.web.plugins.jacms.contentmodel;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.plugins.jacms.contentmodel.model.ContentModelRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ContentModelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URI = "/plugins/cms/contentmodels";

    @Autowired
    private IContentModelManager contentModelManager;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetContentModelsSortDefault() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI)
                        .param("sort", "id")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].id", is(1)));

        result = mockMvc
                .perform(get(BASE_URI)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("sort", "id")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].id", is(11)));

    }

    @Test
    public void testGetContentModelsSortByDescr() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI)
                        .param("direction", FieldSearchFilter.ASC_ORDER)
                        .param("sort", "descr")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].descr", is("List Model")));

        result = mockMvc
                .perform(get(BASE_URI)
                        .param("direction", FieldSearchFilter.DESC_ORDER)
                        .param("sort", "descr")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].descr", is("scheda di un articolo")));

    }

    @Test
    public void testGetContentModelsWithFilters() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI)
                        .param("direction", FieldSearchFilter.ASC_ORDER)
                        .param("sort", "descr")
                        .param("filters[0].attribute", "contentType")
                        .param("filters[0].value", "ART")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(4)));

        result = mockMvc
                .perform(get(BASE_URI)
                        .param("direction", FieldSearchFilter.ASC_ORDER)
                        .param("sort", "descr")
                        .param("filters[0].attribute", "contentType")
                        .param("filters[0].value", "ART")
                        .param("filters[1].attribute", "descr")
                        .param("filters[1].value", "MoDeL")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(2)));
    }

    @Test
    public void testGetContentModelOk() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/{modelId}", "1")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.id", is(1)));

    }

    @Test
    public void testGetContentModelKo() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/{modelId}", "0")
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.errors[0].code", is("1")));
    }

    @Test
    public void testGetContentModelDictionary() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/dictionary")
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());

    }

    @Test
    public void testGetContentModelDictionaryWithTypeCode() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/dictionary")
                        .param("typeCode", "EVN")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
        System.err.println(result.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGetContentModelDictionaryValidTypeCodeInvalid() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/dictionary")
                        .param("typeCode", "LOL")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.errors[0].code", is("6")));
    }

    @Test
    public void testCrudContentModel() throws Exception {
        long modelId = 2001;
        try {
            String payload = null;

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ContentModelRequest request = new ContentModelRequest();
            request.setId(modelId);
            request.setContentType("ART");
            request.setDescr("testCrudContentModel");
            request.setContentShape("testCrudContentModel");

            payload = mapper.writeValueAsString(request);

            ResultActions result = mockMvc
                    .perform(post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());
            ContentModel contentModelAdded = this.contentModelManager.getContentModel(modelId);
            assertThat(contentModelAdded.getId(), is(request.getId()));
            assertThat(contentModelAdded.getContentType(), is(request.getContentType()));
            assertThat(contentModelAdded.getDescription(), is(request.getDescr()));
            assertThat(contentModelAdded.getContentShape(), is(request.getContentShape()));

            //----------------------------------------------
            request.setId(modelId);
            request.setContentType("ART");
            request.setDescr("testCrudContentModel".toUpperCase());
            request.setContentShape("testCrudContentModel".toUpperCase());
            request.setStylesheet("Stylesheet".toUpperCase());

            payload = mapper.writeValueAsString(request);

            result = mockMvc
                    .perform(put(BASE_URI + "/{id}", modelId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());
            contentModelAdded = this.contentModelManager.getContentModel(modelId);
            assertThat(contentModelAdded.getId(), is(request.getId()));
            assertThat(contentModelAdded.getContentType(), is(request.getContentType()));
            assertThat(contentModelAdded.getDescription(), is(request.getDescr()));
            assertThat(contentModelAdded.getContentShape(), is(request.getContentShape()));

            //----------------------------------------------
            result = mockMvc
                    .perform(delete(BASE_URI + "/{id}", modelId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());
            result.andExpect(jsonPath("$.payload.modelId", is(String.valueOf(modelId))));
            contentModelAdded = this.contentModelManager.getContentModel(modelId);
            assertThat(contentModelAdded, is(nullValue()));

            //----------------------------------------------
        } finally {
            ContentModel model = this.contentModelManager.getContentModel(modelId);
            if (null != model) {

                this.contentModelManager.removeContentModel(model);
            }
        }
    }

    @Test
    public void testAddWithInvalidContentType() throws Exception {
        long modelId = 2001;
        try {
            String payload = null;

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ContentModelRequest request = new ContentModelRequest();
            request.setId(modelId);
            request.setContentType("XXX");
            request.setDescr("testChangeContentType");
            request.setContentShape("testChangeContentType");

            payload = mapper.writeValueAsString(request);

            ResultActions result = mockMvc
                    .perform(post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isConflict());
            result.andExpect(jsonPath("$.errors[0].code", is("6")));

        } finally {
            ContentModel model = this.contentModelManager.getContentModel(modelId);
            if (null != model) {

                this.contentModelManager.removeContentModel(model);
            }
        }
    }

    @Test
    public void testChangeContentType() throws Exception {
        long modelId = 2001;
        try {
            String payload = null;

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ContentModelRequest request = new ContentModelRequest();
            request.setId(modelId);
            request.setContentType("ART");
            request.setDescr("testChangeContentType");
            request.setContentShape("testChangeContentType");

            payload = mapper.writeValueAsString(request);

            ResultActions result = mockMvc
                    .perform(post(BASE_URI)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            //----------------------------------------------
            request.setId(modelId);
            request.setContentType("EVN");
            request.setDescr("testCrudContentModel".toUpperCase());
            request.setContentShape("testCrudContentModel".toUpperCase());
            request.setStylesheet("Stylesheet".toUpperCase());

            payload = mapper.writeValueAsString(request);

            result = mockMvc
                    .perform(put(BASE_URI + "/{id}", modelId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(payload)
                            .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isConflict());
            result.andExpect(jsonPath("$.errors[0].code", is("4")));

        } finally {
            ContentModel model = this.contentModelManager.getContentModel(modelId);
            if (null != model) {

                this.contentModelManager.removeContentModel(model);
            }
        }
    }

    @Test
    public void testDeleteReferencedModel() throws Throwable {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc
                .perform(delete(BASE_URI + "/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isConflict());
        result.andExpect(jsonPath("$.errors[0].code", is(String.valueOf("5"))));
    }

    @Test
    public void testGetModelPageReferences() throws Throwable {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc
                .perform(get(BASE_URI + "/{id}/pagereferences", 2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());
    }

}
