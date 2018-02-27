package org.entando.entando.aps.system.services.pagemodel;

import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageModelService implements IPageModelService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IPageModelManager pageModelManager;

    private IDtoBuilder<PageModel, PageModelDto> dtoBuilder;

    protected IPageModelManager getPageModelManager() {
        return pageModelManager;
    }

    public void setPageModelManager(IPageModelManager pageModelManager) {
        this.pageModelManager = pageModelManager;
    }

    public IDtoBuilder<PageModel, PageModelDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<PageModel, PageModelDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @Override
    public PagedMetadata<PageModelDto> getPageModels(RestListRequest restListReq) {
        try {
            //transforms the filters by overriding the key specified in the request with the correct one known by the dto
            restListReq.getFieldSearchFilters()
                       .stream()
                       .filter(i -> i.getKey() != null)
                       .forEach(i -> i.setKey(PageModelDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<PageModel> pageModels = this.getPageModelManager().searchPageModels(restListReq.getFieldSearchFilters());

            List<PageModelDto> dtoList = null;
            if (null != pageModels) {
                dtoList = this.getDtoBuilder().convert(pageModels.getList());
            }

            PagedMetadata<PageModelDto> pagedMetadata = new PagedMetadata<>(restListReq, pageModels);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search pageModels", t);
            throw new RestServerError("error in search pageModels", t);
        }
    }

    @Override
    public PageModelDto getPageModel(String code) {
        PageModel pageModel = this.getPageModelManager().getPageModel(code);
        if (null == pageModel) {
            logger.warn("no pageModel found with code {}", code);
            throw new RestRourceNotFoundException("pageModel", code);
        }
        return this.getDtoBuilder().convert(pageModel);
    }

    @Override
    public PageModelDto updatePageModel(PageModelRequest pageModelRequest) {

        //        Group group = this.getGroupManager().getGroup(pageModelRequest.getClass);
        //        if (null == group) {
        //            throw new RestRourceNotFoundException("group", groupCode);
        //        }

        PageModel pageModel = this.createPageModel(pageModelRequest);
        // TODO Auto-generated method stub
        return null;
    }

    private PageModel createPageModel(PageModelRequest pageModelRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageModelDto addPageModel(PageModelRequest pageModelRequest) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removePageModel(String groupName) {
        // TODO Auto-generated method stub

    }
}
