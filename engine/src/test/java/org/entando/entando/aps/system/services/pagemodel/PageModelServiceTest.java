package org.entando.entando.aps.system.services.pagemodel;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.*;
import org.entando.entando.aps.system.services.pagemodel.model.*;
import org.entando.entando.web.common.model.*;
import org.entando.entando.web.pagemodel.model.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.entando.entando.aps.system.services.pagemodel.PageModelTestUtil.validPageModelRequest;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PageModelServiceTest {

    private static final int DEFAULT_MAIN_FRAME = -1;
    private static final String PAGE_MODEL_CODE = "TEST_PM_CODE";
    private static final String DE_PAGE_MODEL_CODE = "TEST_DE_PM_CODE";

    private static final RestListRequest EMPTY_REQUEST = new RestListRequest();
    private static final String EXCHANGE = "Leonardo's Exchange";


    @Mock IPageModelManager pageModelManager;
    @Mock DigitalExchangePageModelService dePageModelService;

    private PageModelDtoBuilder dtoBuilder;
    private PageModelService pageModelService;

    @Before
    public void setUp() throws Exception {
        dtoBuilder = new PageModelDtoBuilder();
        pageModelService = new PageModelService(pageModelManager, dtoBuilder);
    }

    @Test public void
    add_page_model_calls_page_model_manager() throws Exception {

        PageModelRequest pageModelRequest = validPageModelRequest();
        PageModel pageModel = pageModelFrom(pageModelRequest);

        PageModelDto result = pageModelService.addPageModel(pageModelRequest);

        verify(pageModelManager, times(1)).addPageModel(pageModel);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo(pageModelRequest.getCode());
        assertThat(result.getDescr()).isEqualTo(pageModelRequest.getDescr());
        assertThat(result.getPluginCode()).isEqualTo(pageModelRequest.getPluginCode());
        assertThat(result.getMainFrame()).isEqualTo(DEFAULT_MAIN_FRAME);
        assertThat(result.getTemplate()).isEqualTo(pageModelRequest.getTemplate());
    }

    @Test public void
    get_local_page_models_return_only_local_page_models() throws ApsSystemException {
        when(pageModelManager.searchPageModels(any())).thenReturn(pageModels());

        PagedMetadata<PageModelDto> result = pageModelService.getLocalPageModels(EMPTY_REQUEST);

        PagedMetadata<PageModelDto> expected = localResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    @Test public void
    get_all_page_models_without_de_service_configured_returns_local_page_models() throws ApsSystemException {
        when(pageModelManager.searchPageModels(any())).thenReturn(pageModels());

        PagedMetadata<PageModelDto> result = pageModelService.getAllPageModels(EMPTY_REQUEST);

        PagedMetadata<PageModelDto> expected = localResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    @Test public void
    get_all_page_models_with_de_service_configured_returns_de_and_page_models() throws ApsSystemException {
        pageModelService.setDePageModelService(dePageModelService);

        when(pageModelManager.searchPageModels(any())).thenReturn(pageModels());
        when(dePageModelService.getDePageModels()).thenReturn(dePageModels());

        PagedMetadata<PageModelDto> result = pageModelService.getAllPageModels(EMPTY_REQUEST);

        PagedMetadata<PageModelDto> expected = completeResultPagedMetadata();
        assertThat(result).isEqualTo(expected);
    }

    private List<PageModelDto> dePageModels() {
        return asList(dtoBuilder.convert(dePageModel()));
    }

    private PagedMetadata<PageModelDto> localResultPagedMetadata() {
        RestListRequest request = new RestListRequest();

        return new PagedMetadata<>(request, asList(dtoBuilder.convert(localPageModel())), 1);
    }

    private PagedMetadata<PageModelDto> completeResultPagedMetadata() {
        RestListRequest request = new RestListRequest();

        return new PagedMetadata<>(request,
                asList(dtoBuilder.convert(localPageModel()), dtoBuilder.convert(dePageModel())),
                2);
    }

    private static SearcherDaoPaginatedResult<PageModel> pageModels() {
        return new SearcherDaoPaginatedResult<>(asList(localPageModel()));
    }

    private static PageModel localPageModel() {
        PageModel localPageModel = new PageModel();
        localPageModel.setCode(PAGE_MODEL_CODE);
        return localPageModel;
    }

    private PageModel dePageModel() {
        PageModel pageModel = new PageModel();
        pageModel.setCode(DE_PAGE_MODEL_CODE);
        pageModel.setDigitalExchange(EXCHANGE);
        return pageModel;
    }

    private static PageModel pageModelFrom(PageModelRequest pageModelRequest) {
        Frame[] frames = framesFrom(pageModelRequest.getConfiguration());

        PageModel pageModel = new PageModel();
        pageModel.setCode(pageModelRequest.getCode());
        pageModel.setDescription(pageModelRequest.getDescr());
        pageModel.setConfiguration(frames);

        return pageModel;
    }

    private static Frame[] framesFrom(PageModelConfigurationRequest configuration) {
        List<PageModelFrameReq> requestFrames = configuration.getFrames();

        if (requestFrames == null) {
            return new Frame[]{};
        }

        Frame[] frames = new Frame[requestFrames.size()];
        for (int i = 0; i < requestFrames.size(); i++) {
            frames[i] = frameFrom(requestFrames.get(i));
        }

        return frames;
    }

    private static Frame frameFrom(PageModelFrameReq request) {
        Frame frame = new Frame();
        frame.setDescription(request.getDescr());
        return frame;
    }
}
