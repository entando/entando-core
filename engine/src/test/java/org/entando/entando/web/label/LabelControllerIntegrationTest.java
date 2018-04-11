package org.entando.entando.web.label;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LabelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private II18nManager ii18nManager;

    @Autowired
    private ILangManager langManager;

    @Test
    public void testGetLabels_1() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/labels")
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.pageSize", is(100)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(10)));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.lastPage", is(1)));
    }

    @Test
    public void testGetLabels_2() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/labels")
                        .param("sort", "key")
                        .param("pageSize", "2")
                        .param("page", "1")
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.metaData.pageSize", is(2)));
        result.andExpect(jsonPath("$.metaData.totalItems", is(10)));
        result.andExpect(jsonPath("$.metaData.page", is(1)));
        result.andExpect(jsonPath("$.metaData.lastPage", is(5)));
    }

    @Test
    public void testGetLabels_3() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/labels")
                        .param("direction", FieldSearchFilter.DESC_ORDER).param("sort", "key")
                        .param("filter[0].attribute", "titles").param("filter[0].value", "gina")
                        .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload", hasSize(10)));
    }

    @Test
    public void testGetInvalidFilter() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/labels")
                        .param("direction", FieldSearchFilter.DESC_ORDER).param("sort", "invalid")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(jsonPath("$.payload", Matchers.hasSize(0)));
        result.andExpect(jsonPath("$.errors", Matchers.hasSize(1)));
        result.andExpect(jsonPath("$.errors[0].code", Matchers.is("100")));
    }

    @Test
    public void testGetLabelGroupOk() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                .perform(get("/labels/{labelCode}", "PAGE")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetLabelGroupNotPresent() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result
                = mockMvc.perform(get("/labels/{labelCode}", "THIS_LABEL_DO_NOT_EXISTS")
                        .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.errors[0].code", is("1")));
    }

    @Test
    public void testCrudLabelGroup() throws Exception {
        String code = "THIS_LABEL_HAS_NO_NAME";
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

            String defaultLangCode = langManager.getDefaultLang().getCode();

            LabelRequest request = new LabelRequest();
            request.setKey(code);
            Map<String, String> languages = new HashMap<>();
            languages.put(defaultLangCode, "this label has no name");
            request.setTitles(languages);
            String payLoad = mapper.writeValueAsString(request);

            ResultActions result
                    = mockMvc.perform(post("/labels")
                            .content(payLoad)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isOk());

            assertThat(this.ii18nManager.getLabelGroup(code), is(not(nullValue())));
            ApsProperties enlabel = this.ii18nManager.getLabelGroup(code);
            assertThat(enlabel.get(defaultLangCode), is("this label has no name"));
            //-------------------------------------------------
            request = new LabelRequest();
            request.setKey(code);
            languages = new HashMap<>();
            languages.put(langManager.getDefaultLang().getCode(), "this label has no name");
            request.setTitles(languages);
            payLoad = mapper.writeValueAsString(request);

            result = mockMvc.perform(put("/labels/{labelCode}", "THIS_LABEL_DO_NOT_EXISTS")
                    .content(payLoad)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());

            //System.out.println(result.andReturn().getResponse().getContentAsString());
            //-------------------------------------------------
            request = new LabelRequest();
            request.setKey(code);
            languages = new HashMap<>();
            languages.put("en", "this label has no name");
            request.setTitles(languages);
            payLoad = mapper.writeValueAsString(request);

            result = mockMvc.perform(put("/labels/{labelCode}", code)
                    .content(payLoad)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
            result.andExpect(jsonPath("$.errors[0].code", is("2")));

            //-------------------------------------------------
            request = new LabelRequest();
            request.setKey(code);
            languages = new HashMap<>();
            languages.put(defaultLangCode, "this label has no name!");
            request.setTitles(languages);
            payLoad = mapper.writeValueAsString(request);

            result = mockMvc.perform(put("/labels/{labelCode}", code)
                    .content(payLoad)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            enlabel = this.ii18nManager.getLabelGroup(code);
            assertThat(enlabel.get(defaultLangCode), is("this label has no name!"));

            //-------------------------------------------------
            result = mockMvc.perform(delete("/labels/{labelCode}", code)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

        } finally {
            this.ii18nManager.deleteLabelGroup(code);
        }
    }

    @Test
    public void testAddLabelWithMissingTextForDefaultLang() throws Exception {
        String code = "THIS_LABEL_HAS_NO_NAME";
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

            LabelRequest request = new LabelRequest();
            request.setKey(code);
            Map<String, String> languages = new HashMap<>();
            languages.put(langManager.getDefaultLang().getCode(), "");
            request.setTitles(languages);
            String payLoad = mapper.writeValueAsString(request);

            ResultActions result
                    = mockMvc.perform(post("/labels")
                            .content(payLoad)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isConflict());

        } finally {
            this.ii18nManager.deleteLabelGroup(code);
        }
    }

    @Test
    public void testAddLabelWithMissingDefaultLang() throws Exception {
        String code = "THIS_LABEL_HAS_NO_NAME";
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

            LabelRequest request = new LabelRequest();
            request.setKey(code);
            Map<String, String> languages = new HashMap<>();
            languages.put("en", "hello");
            request.setTitles(languages);
            String payLoad = mapper.writeValueAsString(request);

            ResultActions result
                    = mockMvc.perform(post("/labels")
                            .content(payLoad)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isConflict());

        } finally {
            this.ii18nManager.deleteLabelGroup(code);
        }
    }

    @Test
    public void testAddLabelWithInvalidLang() throws Exception {
        String code = "THIS_LABEL_HAS_NO_NAME";
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

            LabelRequest request = new LabelRequest();
            request.setKey(code);
            Map<String, String> languages = new HashMap<>();
            languages.put(langManager.getDefaultLang().getCode(), "hello");
            languages.put("de", "hello");
            request.setTitles(languages);
            String payLoad = mapper.writeValueAsString(request);

            ResultActions result
                    = mockMvc.perform(post("/labels")
                            .content(payLoad)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isConflict());

        } finally {
            this.ii18nManager.deleteLabelGroup(code);
        }
    }

    @Test
    public void testUpdateLabelWithInvalidLang() throws Exception {
        String code = "THIS_LABEL_HAS_NO_NAME";
        ObjectMapper mapper = new ObjectMapper();
        try {
            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

            ApsProperties labels = new ApsProperties();
            labels.put(this.langManager.getDefaultLang().getCode(), "hello");
            this.ii18nManager.addLabelGroup(code, labels);

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            LabelRequest request = new LabelRequest();
            request.setKey(code);
            Map<String, String> languages = new HashMap<>();
            languages.put(langManager.getDefaultLang().getCode(), "hello");
            languages.put("de", "hello");
            request.setTitles(languages);
            String payLoad = mapper.writeValueAsString(request);

            ResultActions result
                    = mockMvc.perform(put("/labels/{code}", code)
                            .content(payLoad)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isBadRequest());

        } finally {
            this.ii18nManager.deleteLabelGroup(code);
        }
    }

    @Test
    public void testUpdateLabelWithUnexistingLang() throws Exception {
        String code = "THIS_LABEL_HAS_NO_NAME";
        ObjectMapper mapper = new ObjectMapper();
        try {
            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

            ApsProperties labels = new ApsProperties();
            labels.put(this.langManager.getDefaultLang().getCode(), "hello");
            this.ii18nManager.addLabelGroup(code, labels);

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            LabelRequest request = new LabelRequest();
            request.setKey(code);
            Map<String, String> languages = new HashMap<>();
            languages.put(langManager.getDefaultLang().getCode(), "hello");
            languages.put("kk", "hello");
            request.setTitles(languages);
            String payLoad = mapper.writeValueAsString(request);

            ResultActions result
                    = mockMvc.perform(put("/labels/{code}", code)
                            .content(payLoad)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isBadRequest());

        } finally {
            this.ii18nManager.deleteLabelGroup(code);
        }
    }

    @Test
    public void testUpdateLabelWithUnexistingCode() throws Exception {
        String code = "THIS_LABEL_HAS_NO_NAME";
        ObjectMapper mapper = new ObjectMapper();
        try {
            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            LabelRequest request = new LabelRequest();
            request.setKey(code);
            Map<String, String> languages = new HashMap<>();
            languages.put(langManager.getDefaultLang().getCode(), "hello");

            request.setTitles(languages);
            String payLoad = mapper.writeValueAsString(request);

            ResultActions result
                    = mockMvc.perform(put("/labels/{code}", code)
                            .content(payLoad)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isNotFound());

        } finally {
            this.ii18nManager.deleteLabelGroup(code);
        }
    }

    @Test
    public void testAddLabelWithoutDefaultLang() throws Exception {
        String code = "THIS_LABEL_HAS_NO_NAME";
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            assertThat(this.ii18nManager.getLabelGroup(code), is(nullValue()));

            LabelRequest request = new LabelRequest();
            request.setKey(code);
            Map<String, String> languages = new HashMap<>();
            languages.put(langManager.getDefaultLang().getCode(), "hello");
            languages.put("en", "hello");
            languages.put("de", "hello");
            languages.put("KK", "hello");
            request.setTitles(languages);
            String payLoad = mapper.writeValueAsString(request);

            ResultActions result
                    = mockMvc.perform(post("/labels")
                            .content(payLoad)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .header("Authorization", "Bearer " + accessToken));
            result.andExpect(status().isConflict());
        } finally {
            this.ii18nManager.deleteLabelGroup(code);
        }
    }

}
