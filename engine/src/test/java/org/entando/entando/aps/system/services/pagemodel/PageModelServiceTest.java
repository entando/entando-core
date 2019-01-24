package org.entando.entando.aps.system.services.pagemodel;

import com.agiletec.aps.system.services.pagemodel.*;
import org.entando.entando.aps.system.services.pagemodel.model.*;
import org.entando.entando.web.pagemodel.model.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.entando.entando.aps.system.services.pagemodel.PageModelTestUtil.validPageModelRequest;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PageModelServiceTest {

    private static final int DEFAULT_MAIN_FRAME = -1;

    @Mock IPageModelManager pageModelManager;

    private PageModelService pageModelService;

    @Before
    public void setUp() throws Exception {
        PageModelDtoBuilder dtoBuilder = new PageModelDtoBuilder();

        pageModelService = new PageModelService(pageModelManager, dtoBuilder);
    }

    @Test public void
    add_page_model_calls_page_model_manager() throws Exception {

        PageModelRequest pageModelRequest = validPageModelRequest();
        PageModel pageModel = pageModelFromPageModelRequest(pageModelRequest);

        PageModelDto result = pageModelService.addPageModel(pageModelRequest);

        verify(pageModelManager, times(1)).addPageModel(pageModel);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(pageModelRequest.getCode());
        assertThat(result.getDescr()).isEqualTo(pageModelRequest.getDescr());
        assertThat(result.getPluginCode()).isEqualTo(pageModelRequest.getPluginCode());
        assertThat(result.getMainFrame()).isEqualTo(DEFAULT_MAIN_FRAME);
        assertThat(result.getTemplate()).isEqualTo(pageModelRequest.getTemplate());
    }

    private PageModel pageModelFromPageModelRequest(PageModelRequest pageModelRequest) {
        Frame[] frames = framesFromRequest(pageModelRequest.getConfiguration());

        PageModel pageModel = new PageModel();
        pageModel.setCode(pageModelRequest.getCode());
        pageModel.setDescription(pageModelRequest.getDescr());
        pageModel.setConfiguration(frames);

        return pageModel;
    }

    private Frame[] framesFromRequest(PageModelConfigurationRequest configuration) {
        List<PageModelFrameReq> requestFrames = configuration.getFrames();

        if (requestFrames == null) {
            return new Frame[]{};
        }

        Frame[] frames = new Frame[requestFrames.size()];
        for (int i = 0; i < requestFrames.size(); i++) {
            frames[i] = singleFrameFromRequest(requestFrames.get(i));
        }

        return frames;
    }

    private Frame singleFrameFromRequest(PageModelFrameReq request) {
        Frame frame = new Frame();
        frame.setDescription(request.getDescr());
        return frame;
    }
}
