package org.entando.entando.web.usersettings;

import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.baseconfig.SystemParamsUtils;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.usersettings.IUserSettingsService;
import org.entando.entando.aps.system.services.usersettings.model.UserSettingsDto;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.usersettings.model.UserSettingsRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserSettigsControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IUserSettingsService userSettingsService;

    @Autowired
    private ConfigInterface configInterface;

    @Test
    public void testGetSettings() throws Throwable {

        Map<String, String> params = this.getSystemParams();

        assertEquals("false", params.get(UserSettingsDto.EXTENDED_PRIVACY_MODULE_ENABLED));
        assertEquals("false", params.getOrDefault(SystemConstants.CONFIG_PARAM_GRAVATAR_INTEGRATION_ENABLED, "false"));
        assertEquals("6", params.get(UserSettingsDto.MAX_MONTHS_SINCE_LASTACCESS));
        assertEquals("3", params.get(UserSettingsDto.MAX_MONTHS_SINCE_LASTPASSWORDCHANGE));


        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                                               get("/usersettings")
                                                                   .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isOk());

        System.out.println(result.andReturn().getResponse().getContentAsString());

        result.andExpect(jsonPath("$.payload.restrictionsActive", is(Boolean.parseBoolean(params.get(UserSettingsDto.EXTENDED_PRIVACY_MODULE_ENABLED)))));
        result.andExpect(jsonPath("$.payload.gravatarIntegrationEnabled", is(Boolean.parseBoolean(params.get(SystemConstants.CONFIG_PARAM_GRAVATAR_INTEGRATION_ENABLED)))));
        result.andExpect(jsonPath("$.payload.maxMonthsSinceLastAccess", is(Integer.valueOf(params.get(UserSettingsDto.MAX_MONTHS_SINCE_LASTACCESS)))));
        result.andExpect(jsonPath("$.payload.maxMonthsSinceLastPasswordChange", is(Integer.valueOf(params.get(UserSettingsDto.MAX_MONTHS_SINCE_LASTPASSWORDCHANGE)))));

    }

    @Test
    public void testUpdateSettings() throws Throwable {
        String xmlParams = this.configInterface.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
        try {

            Map<String, String> params = this.getSystemParams();

            assertEquals("false", params.get(UserSettingsDto.EXTENDED_PRIVACY_MODULE_ENABLED));
            assertEquals("false", params.getOrDefault(SystemConstants.CONFIG_PARAM_GRAVATAR_INTEGRATION_ENABLED, "false"));
            assertEquals("6", params.get(UserSettingsDto.MAX_MONTHS_SINCE_LASTACCESS));
            assertEquals("3", params.get(UserSettingsDto.MAX_MONTHS_SINCE_LASTPASSWORDCHANGE));

            UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
            String accessToken = mockOAuthInterceptor(user);

            ResultActions result = mockMvc.perform(
                                                   get("/usersettings")
                                                                       .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());

            System.out.println(result.andReturn().getResponse().getContentAsString());

            result.andExpect(jsonPath("$.payload.restrictionsActive", is(Boolean.parseBoolean(params.get(UserSettingsDto.EXTENDED_PRIVACY_MODULE_ENABLED)))));
            result.andExpect(jsonPath("$.payload.gravatarIntegrationEnabled", is(Boolean.parseBoolean(params.get(SystemConstants.CONFIG_PARAM_GRAVATAR_INTEGRATION_ENABLED)))));
            result.andExpect(jsonPath("$.payload.maxMonthsSinceLastAccess", is(Integer.valueOf(params.get(UserSettingsDto.MAX_MONTHS_SINCE_LASTACCESS)))));
            result.andExpect(jsonPath("$.payload.maxMonthsSinceLastPasswordChange", is(Integer.valueOf(params.get(UserSettingsDto.MAX_MONTHS_SINCE_LASTPASSWORDCHANGE)))));

            //-------------

            UserSettingsRequest userSettingsRequest = new UserSettingsRequest();
            userSettingsRequest.setExtendedPrivacyModuleEnabled(true);
            userSettingsRequest.setGravatarIntegrationEnabled(true);
            userSettingsRequest.setMaxMonthsSinceLastAccess(60);
            userSettingsRequest.setMaxMonthsSinceLastPasswordChange(30);

            result = mockMvc.perform(
                                     get("/usersettings")
                                                         .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isOk());
            System.out.println(result.andReturn().getResponse().getContentAsString());

            result.andExpect(jsonPath("$.payload.restrictionsActive", is(true)));
            result.andExpect(jsonPath("$.payload.gravatarIntegrationEnabled", is(true)));
            result.andExpect(jsonPath("$.payload.maxMonthsSinceLastAccess", is(60)));
            result.andExpect(jsonPath("$.payload.maxMonthsSinceLastPasswordChange", is(30)));
            //-------------

            userSettingsRequest = new UserSettingsRequest();
            userSettingsRequest.setExtendedPrivacyModuleEnabled(true);
            userSettingsRequest.setGravatarIntegrationEnabled(true);
            userSettingsRequest.setMaxMonthsSinceLastAccess(-1);

            result = mockMvc.perform(
                                     get("/usersettings")
                                                         .header("Authorization", "Bearer " + accessToken));

            result.andExpect(status().isBadRequest());
            System.out.println(result.andReturn().getResponse().getContentAsString());

        } finally {
            this.configInterface.updateConfigItem(SystemConstants.CONFIG_ITEM_PARAMS, xmlParams);
        }

    }

    private Map<String, String> getSystemParams() throws Throwable {
        String xmlParams = this.configInterface.getConfigItem(SystemConstants.CONFIG_ITEM_PARAMS);
        return SystemParamsUtils.getParams(xmlParams);
    }

}
