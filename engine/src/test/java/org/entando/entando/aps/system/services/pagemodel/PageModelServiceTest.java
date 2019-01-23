package org.entando.entando.aps.system.services.pagemodel;

import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDtoBuilder;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.entando.entando.aps.system.services.pagemodel.PageModelTestUtil.createValidPageModelRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PageModelServiceTest {

    @Mock IPageModelManager pageModelManager;

    private PageModelService pageModelService;

    @Before
    public void setUp() throws Exception {
        PageModelDtoBuilder dtoBuilder = new PageModelDtoBuilder();

        pageModelService = new PageModelService(pageModelManager, dtoBuilder);
    }

    @Test public void
    add_page_model_calls_page_model_manager() throws Exception {

        pageModelService.addPageModel(createValidPageModelRequest());

        verify(pageModelManager, times(1)).addPageModel(any());
    }
}
