package org.entando.entando.web.widget;

import java.util.Map;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.AbstractControllerIntegrationTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.entando.entando.web.widget.validator.WidgetValidator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WidgetControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    private ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testUpdateWidgetLocked() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        String code = "login_form";
        WidgetType widgetType = this.widgetTypeManager.getWidgetType(code);
        WidgetRequest request = new WidgetRequest();
        request.setCode(code);
        request.setGroup(Group.FREE_GROUP_NAME);
        request.setTitles((Map) widgetType.getTitles());

        String payload = mapper.writeValueAsString(request);
        ResultActions result = mockMvc.perform(
                                               put("/widgets/" + code)
                                                                         .contentType(MediaType.APPLICATION_JSON)
                                                                         .content(payload)
                                                                         .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.errors[0].code", is(WidgetValidator.ERRCODE_OPERATION_FORBIDDEN_LOCKED)));
    }

}

