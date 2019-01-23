package org.entando.entando.aps.system.services.pagemodel;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDtoBuilder;
import org.entando.entando.web.pagemodel.model.PageModelRequest;
import org.junit.*;
import org.mockito.*;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PageModelServiceTest {

    @Mock
    private IPageModelManager pageModelManager;

    private PageModelDtoBuilder dtoBuilder = new PageModelDtoBuilder();

    @InjectMocks
    private PageModelService pageModelService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pageModelService.setDtoBuilder(dtoBuilder);
    }

    @Test
    public void test_add_page_model() throws ApsSystemException, IOException {

        String payload = " {\n" +
                         "            \"code\": \"test\",\n" +
                         "            \"descr\": \"test\",\n" +
                         "            \"configuration\": {\n" +
                         "                \"frames\": [\n" +
                         "                    {\n" +
                         "                        \"pos\": 0,\n" +
                         "                        \"descr\": \"test_frame\",\n" +
                         "                        \"mainFrame\": false,\n" +
                         "                        \"defaultWidget\": null,\n" +
                         "                        \"sketch\": null\n" +
                         "                    }\n" +
                         "                ]\n" +
                         "            },\n" +
                         "            \"pluginCode\": null,\n" +
                         "            \"template\": \"hello world\"\n" +
                         "        }";

        ObjectMapper objectMapper = new ObjectMapper();
        PageModelRequest pageModelRequest = objectMapper.readValue(payload, PageModelRequest.class);

        pageModelService.addPageModel(pageModelRequest);
        verify(pageModelManager, times(1)).addPageModel(any());
    }

}
