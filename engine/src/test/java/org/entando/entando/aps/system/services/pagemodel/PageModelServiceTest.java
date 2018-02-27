package org.entando.entando.aps.system.services.pagemodel;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.FrameSketch;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDtoBuilder;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class PageModelServiceTest {

    @Mock
    private IPageModelManager pageModelManager;

    //@Mock
    private PageModelDtoBuilder dtoBuilder = new PageModelDtoBuilder();

    @InjectMocks
    private PageModelService pageModelService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pageModelService.setDtoBuilder(dtoBuilder);
    }


    @Test
    public void test_searchPageModels() throws ApsSystemException {

        RestListRequest restListReq = new RestListRequest();

        List<PageModel> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list.add(this.createPageModel("M_" + i, "<code></code>", null));
        }

        SearcherDaoPaginatedResult<PageModel> paginatedResult = new SearcherDaoPaginatedResult<>(list.size(), list);
        when(pageModelManager.searchPageModels(Mockito.any(List.class))).thenReturn(paginatedResult);

        PagedMetadata<PageModelDto> result = this.pageModelService.getPageModels(restListReq);
        System.out.println(result);
    }

    private PageModel createPageModel(String code, String template, List<Frame> framesConfiguration) {
        PageModel pageModel = new PageModel();
        pageModel.setCode(code);
        pageModel.setDescription(code + "_" + "description");
        pageModel.setTemplate(template);
        //Frame[] frames = framesConfiguration.toArray(new Frame[framesConfiguration.size()]);
        //pageModel.setConfiguration(frames);

        return pageModel;
    }

    public Frame createFrame(int pos, boolean mainFrame) {
        Frame frame = new Frame();
        frame.setPos(pos);
        frame.setDescription("description_" + pos);
        frame.setMainFrame(mainFrame);
        frame.setSketch(new FrameSketch());
        return frame;
    }
}
