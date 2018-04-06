package org.entando.entando.web.label;

import java.util.HashMap;

import com.agiletec.aps.system.services.user.UserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.label.LabelService;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LabelControllerUnitTest extends AbstractControllerTest {

    @Mock
    private LabelService labelService;

    @Mock
    private LabelValidator labelValidator = new LabelValidator();

    @InjectMocks
    private LabelController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .addInterceptors(entandoOauth2Interceptor)
                                 .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                                 .build();
    }


    @Test
    public void testUpdateNoLanguages() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ObjectMapper mapper = new ObjectMapper();
        LabelRequest labelRequest = new LabelRequest();

        String payload = mapper.writeValueAsString(labelRequest);

        ResultActions result = mockMvc
                                      .perform(put("/labels/{labelCode}", new Object[]{"PAGE"})
                                                                                               .content(payload)
                                                                                               .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                               .header("Authorization", "Bearer " + accessToken));
        result.andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateEmpyLanguages() throws Exception {
        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);

        ObjectMapper mapper = new ObjectMapper();
        LabelRequest labelRequest = new LabelRequest();
        labelRequest.setTitles(new HashMap<>());

        String payload = mapper.writeValueAsString(labelRequest);

        ResultActions result = mockMvc
                                      .perform(put("/labels/{labelCode}", new Object[]{"PAGE"})
                                                                                               .content(payload)
                                                                                               .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                                                               .header("Authorization", "Bearer " + accessToken));
        //System.out.println(result.andReturn().getResponse().getContentAsString());
        result.andExpect(status().isBadRequest());
    }



}
