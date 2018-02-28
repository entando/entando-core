package org.entando.entando.web.widget;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import org.entando.entando.aps.system.services.group.GroupService;
import org.entando.entando.aps.system.services.widget.WidgetService;
import org.entando.entando.aps.system.services.widget.model.WidgetDto;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.group.GroupController;
import org.entando.entando.web.group.validator.GroupValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.entando.entando.web.widget.model.WidgetRequest;
import org.entando.entando.web.widget.validator.WidgetValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WidgetControllerTest extends AbstractControllerTest {

    @Mock
    private WidgetService widgetService;

    @InjectMocks
    private WidgetController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void testGetWidget() throws Exception{

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets/1")
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testGetWidgetList() throws Exception{

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        PagedMetadata<WidgetDto> pagedDto = new PagedMetadata<>();

        when(widgetService.getWidgets(any())).thenReturn(pagedDto);

        // @formatter:off
        ResultActions result = mockMvc.perform(
                get("/widgets")
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testRemoveWidget() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        // @formatter:off
        ResultActions result = mockMvc.perform(
                delete("/widgets/1")
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testAddWidget() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        this.controller.setWidgetValidator(new WidgetValidator());

        // @formatter:off
        ResultActions result = mockMvc.perform(
                post("/widgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(createMockRequest()))
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testUpdateWidget() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        when(widgetService.updateWidget(any(), any())).thenReturn(new WidgetDto());
        this.controller.setWidgetValidator(new WidgetValidator());

        // @formatter:off
        ResultActions result = mockMvc.perform(
                put("/widgets/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(createMockRequest()))
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertNotNull(response);
    }

    @Test
    public void testUnauthorized() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24")
                .withGroup(Group.FREE_GROUP_NAME)
                .build();
        String accessToken = mockOAuthInterceptor(user);

        ResultActions result = mockMvc.perform(
                get("/widgets")
                        .header("Authorization", "Bearer " + accessToken)
        );

        String response = result.andReturn().getResponse().getContentAsString();
        System.out.println(response);
        result.andExpect(status().isUnauthorized());
    }

    private WidgetRequest createMockRequest(){

        Map<String,String> titles = new HashMap<>();
        titles.put("it", "il titolo");
        titles.put("en", "the test title");

        WidgetRequest req = new WidgetRequest();
        req.setCode("test");
        req.setCreatedAt(new Date());
        req.setGroup("test");
        req.setName("widgetName");
        req.setTitles(titles);
        req.setUpdatedAt(new Date());
        req.setUsed(true);
        return req;
    }
}
