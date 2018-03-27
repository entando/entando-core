package org.entando.entando.aps.system.services.pagemodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.Frame;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelUtilizer;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.pagemodel.model.PageModelDto;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.pagemodel.model.PageModelFrameReq;
import org.entando.entando.web.pagemodel.model.PageModelRequest;
import org.entando.entando.web.pagemodel.validator.PageModelValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

@Service
public class PageModelService implements IPageModelService, ApplicationContextAware {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IPageModelManager pageModelManager;

    private IDtoBuilder<PageModel, PageModelDto> dtoBuilder;

    private ApplicationContext applicationContext;

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
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public PagedMetadata<PageModelDto> getPageModels(RestListRequest restListReq) {
        try {
            //transforms the filters by overriding the key specified in the request with the correct one known by the dto
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(restListReq.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(PageModelDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<PageModel> pageModels = this.getPageModelManager().searchPageModels(filters);

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
            throw new RestRourceNotFoundException(PageModelValidator.ERRCODE_PAGEMODEL_NOT_FOUND, "pageModel", code);
        }
        return this.getDtoBuilder().convert(pageModel);
    }

    @Override
    public PageModelDto addPageModel(PageModelRequest pageModelRequest) {
        try {
            BeanPropertyBindingResult validationResult = this.validateAdd(pageModelRequest);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            PageModel pageModel = this.createPageModel(pageModelRequest);
            this.getPageModelManager().addPageModel(pageModel);
            return this.getDtoBuilder().convert(pageModel);
        } catch (ApsSystemException e) {
            logger.error("Error in add pageModel", e);
            throw new RestServerError("error in add pageModel", e);
        }
    }

    @Override
    public PageModelDto updatePageModel(PageModelRequest pageModelRequest) {
        try {
            PageModel pageModel = this.getPageModelManager().getPageModel(pageModelRequest.getCode());
            if (null == pageModel) {
                throw new RestRourceNotFoundException(null, "pageModel", pageModelRequest.getCode());
            }
            this.copyProperties(pageModelRequest, pageModel);
            this.getPageModelManager().updatePageModel(pageModel);
            return this.getDtoBuilder().convert(pageModel);
        } catch (ApsSystemException e) {
            logger.error("Error in update pageModel {}", pageModelRequest.getCode(), e);
            throw new RestServerError("error in update pageMdel", e);
        }
    }

    @Override
    public void removePageModel(String code) {
        try {
            PageModel pageModel = this.getPageModelManager().getPageModel(code);
            if (null == pageModel) {
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateDelete(pageModel);
            if (validationResult.hasErrors()) {
                throw new ValidationConflictException(validationResult);
            }
            this.getPageModelManager().deletePageModel(code);

        } catch (ApsSystemException e) {
            logger.error("Error in delete pagemodel {}", code, e);
            throw new RestServerError("error in delete pagemodel", e);
        }

    }

    protected PageModel createPageModel(PageModelRequest pageModelRequest) {
        PageModel pageModel = new PageModel();
        copyProperties(pageModelRequest, pageModel);
        return pageModel;
    }

    protected void copyProperties(PageModelRequest srcPpageModelRequest, PageModel descPageModel) {
        descPageModel.setCode(srcPpageModelRequest.getCode());
        descPageModel.setDescription(srcPpageModelRequest.getDescr());
        descPageModel.setTemplate(srcPpageModelRequest.getTemplate());
        descPageModel.setPluginCode(srcPpageModelRequest.getPluginCode());
        descPageModel.setConfiguration(this.createPageModelConfiguration(srcPpageModelRequest));
    }


    protected Frame[] createPageModelConfiguration(PageModelRequest pageModelRequest) {
        Frame[] destConfiguration = null;
        List<PageModelFrameReq> frameRequestList = pageModelRequest.getConfiguration().getFrames();
        if (null == frameRequestList || frameRequestList.isEmpty()) {
            return destConfiguration;
        }
        List<Frame> frames = frameRequestList.stream()
                                             .map(p -> createFrame(p))
                                             .collect(Collectors.toList());

        destConfiguration = frames.toArray(new Frame[frames.size()]);
        return destConfiguration;
    }

    protected Frame createFrame(PageModelFrameReq pageModelFrameReq) {
        Frame frame = new Frame();
        frame.setPos(pageModelFrameReq.getPos());
        frame.setDescription(pageModelFrameReq.getDescr());
        frame.setMainFrame(pageModelFrameReq.isMainFrame());
        frame.setSketch(pageModelFrameReq.getSketch());
        return frame;
    }

    protected BeanPropertyBindingResult validateAdd(PageModelRequest pageModelRequest) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageModelRequest, "pageModel");
        PageModel pageModel = this.getPageModelManager().getPageModel(pageModelRequest.getCode());
        if (null == pageModel) {
            return bindingResult;
        }
        bindingResult.reject(PageModelValidator.ERRCODE_CODE_EXISTS, new String[]{pageModelRequest.getCode()}, "pageModel.code.exists");
        return bindingResult;
    }

    protected BeanPropertyBindingResult validateDelete(PageModel pageModel) throws ApsSystemException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(pageModel, "pageModel");

        Map<String, List<Object>> references = this.getReferencingObjects(pageModel);
        if (references.size() > 0) {
            bindingResult.reject(PageModelValidator.ERRCODE_PAGEMODEL_REFERENCES, new Object[]{pageModel.getCode(), references}, "pageModel.cannot.delete.references");
        }
        return bindingResult;

    }

    @SuppressWarnings("unchecked")
    public Map<String, List<Object>> getReferencingObjects(PageModel pageModel) throws ApsSystemException {
        Map<String, List<Object>> references = new HashMap<String, List<Object>>();
        try {
            String[] defNames = applicationContext.getBeanNamesForType(PageModelUtilizer.class);

            for (String beanName : defNames) {
                Object service = null;
                try {
                    service = applicationContext.getBean(beanName);
                } catch (Throwable t) {
                    logger.error("error in hasReferencingObjects", t);
                    service = null;
                }
                if (service != null) {
                    PageModelUtilizer pageModelUtilizer = (PageModelUtilizer) service;
                    List<Object> utilizers = pageModelUtilizer.getPageModelUtilizers(pageModel.getCode());
                    if (utilizers != null && !utilizers.isEmpty()) {
                        references.put(pageModelUtilizer.getName() + "Utilizers", utilizers);
                    }
                }
            }

        } catch (Throwable t) {
            throw new ApsSystemException("Error on getReferencingObjects methods", t);
        }
        return references;
    }

}
