package org.entando.entando.web.pagemodel;

import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelManager;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PageModelControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private PageModelManager pageModelManager;

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

    @Test
    public void testAddPageModelWithExistingCode() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        String payload = " {\n" +
                         "    \"code\": \"home\",\n" +
                         "    \"descr\": \"test\",\n" +
                         "    \"configuration\": {\n" +
                         "        \"frames\": [{\n" +
                         "            \"pos\": 0,\n" +
                         "            \"descr\": \"test_frame\",\n" +
                         "            \"mainFrame\": false,\n" +
                         "            \"defaultWidget\": null,\n" +
                         "            \"sketch\": null\n" +
                         "        }]\n" +
                         "    },\n" +
                         "    \"pluginCode\": null,\n" +
                         "    \"template\": \"ciao\"\n" +
                         " }";

        ResultActions result = mockMvc
                                      .perform(post("/pagemodels")
                                                                  .content(payload)
                                                                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                  .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isConflict());

    }

    @Test
    public void testAddPageModel() throws Exception {
        String pageModelCode = "testPM";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            String payload = " {\n" +
                             "    \"code\": \"" + pageModelCode + "\"," +
                    "    \"descr\": \"test\",\n" +
                    "    \"configuration\": {\n" +
                    "        \"frames\": [{\n" +
                    "            \"pos\": 0,\n" +
                    "            \"descr\": \"test_frame\",\n" +
                    "            \"mainFrame\": false,\n" +
                    "            \"defaultWidget\": null,\n" +
                    "            \"sketch\": null\n" +
                    "        }]\n" +
                    "    },\n" +
                    "    \"pluginCode\": null,\n" +
                    "    \"template\": \"ciao\"\n" +
                    " }";

            ResultActions result = mockMvc
                    .perform(post("/pagemodels")
                             .content(payload)
                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                             .header("Authorization", "Bearer " + accessToken));

            //System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isOk());
        } finally {
            this.pageModelManager.deletePageModel(pageModelCode);
        }
    }

    @Test
    public void testGetPageModelUnexisting() throws Exception {
        String pageModelCode = "testPM";
        try {
            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);
            ResultActions result = mockMvc
                                          .perform(get("/pagemodels/{code}", pageModelCode)

                                                                                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                           .header("Authorization", "Bearer " + accessToken));
            //System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isNotFound());
        } finally {
            this.pageModelManager.deletePageModel(pageModelCode);
        }
    }

    @Test
    public void testDeletePageModel() throws Exception {
        String pageModelCode = "testPM";
        try {
            PageModel pageModel = new PageModel();
            pageModel.setCode(pageModelCode);
            pageModel.setDescription(pageModelCode);
            this.pageModelManager.addPageModel(pageModel);

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc
                                          .perform(delete("/pagemodels/{code}", pageModelCode)

                                                                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                              .header("Authorization", "Bearer " + accessToken));
            //System.out.println(result.andReturn().getResponse().getContentAsString());
            result.andExpect(status().isOk());
        } finally {
            this.pageModelManager.deletePageModel(pageModelCode);
        }
    }

    @Test
    public void testDeletePageModelUnexistigCode() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc
                                      .perform(delete("/pagemodels/{code}", "unexistingPageModel")

                                                                                                  .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                                  .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());

    }

}
