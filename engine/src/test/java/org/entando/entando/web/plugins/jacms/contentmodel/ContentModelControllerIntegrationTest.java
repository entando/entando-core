package org.entando.entando.web.plugins.jacms.contentmodel;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ContentModelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    private static final String BASE_URI = "/plugins/cms/contentmodels";

    @Test
    public void testGetContentModelsSortDefault() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get(BASE_URI)
                                                            .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].id", is(1)));

        result = mockMvc
                        .perform(get(BASE_URI)
                                              .param("direction", FieldSearchFilter.DESC_ORDER)
                                              .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
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
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload[0].descr", is("List Model")));

        result = mockMvc
                        .perform(get(BASE_URI)
                                              .param("direction", FieldSearchFilter.DESC_ORDER)
                                              .param("sort", "descr")
                                              .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
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
        //System.out.println(result.andReturn().getResponse().getContentAsString());
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
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.payload.length()", is(2)));
    }


}
