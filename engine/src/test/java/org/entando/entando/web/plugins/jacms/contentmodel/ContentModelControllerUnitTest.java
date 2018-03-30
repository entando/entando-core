package org.entando.entando.web.plugins.jacms.contentmodel;

import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.plugins.jacms.contentmodel.model.ContentModelRequest;
import org.entando.entando.web.plugins.jacms.contentmodel.validator.ContentModelValidator;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ContentModelControllerUnitTest extends AbstractControllerTest {

    private static final String BASE_URI = "/plugins/cms/contentmodels";
    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private ContentModelService contentModelService;

    @Spy
    private ContentModelValidator contentModelValidator;

    @InjectMocks
    private ContentModelController controller;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .addInterceptors(entandoOauth2Interceptor)
                                 .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                                 .build();
    }


    @Test
    public void testCrudContentModel() throws Exception {

        String payload = null;

        UserDetails user = new OAuth2TestUtils.UserBuilder("jack_bauer", "0x24").grantedToRoleAdmin().build();
        String accessToken = mockOAuthInterceptor(user);
        ContentModelRequest request = new ContentModelRequest();
        payload = mapper.writeValueAsString(request);
        ResultActions result = mockMvc
                                      .perform(post(BASE_URI)
                                                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                                                             .content(payload)
                                                             .header("Authorization", "Bearer " + accessToken));

        result.andExpect(status().isBadRequest());

    }
}
