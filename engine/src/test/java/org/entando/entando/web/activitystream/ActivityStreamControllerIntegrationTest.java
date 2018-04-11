package org.entando.entando.web.activitystream;

import java.sql.Timestamp;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ActivityStreamControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Test
    public void testGetActivityStream() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ResultActions result = mockMvc
                                      .perform(get("/activitystream")
                                                                     .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetActivityStreamDate() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String start = new Timestamp(DateConverter.parseDate("2017/01/01", "yyyy/MM/dd").getTime()).toString();
        String end = new Timestamp(DateConverter.parseDate("2017/01/01", "yyyy/MM/dd").getTime()).toString();


        ResultActions result = mockMvc
                                      .perform(get("/activitystream")
                                                                     .param("filters[0].attribute", "createdAt")
                                                                     .param("filters[0].value", String.format("[%s TO %s]", start, end))
                                                                     .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetActivityStreamDate_2() throws Exception {

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String start = new Timestamp(DateConverter.parseDate("2018/03/01", "yyyy/MM/dd").getTime()).toString();
        String end = new Timestamp(DateConverter.parseDate("2018/05/01", "yyyy/MM/dd").getTime()).toString();

        ResultActions result = mockMvc
                                      .perform(get("/activitystream")
                                                                     .param("filters[0].attribute", "createdAt")
                                                                     .param("filters[0].value", String.format("[%s TO %s]", start, end))
                                                                     .header("Authorization", "Bearer " + accessToken));
        System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isOk());
    }

}
