/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.page.model.PageConfigurationDto;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.aps.system.services.widget.validators.WidgetProcessorFactory;
import org.entando.entando.aps.system.services.widget.validators.WidgetValidatorFactory;
import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.web.common.exceptions.ValidationConflictException;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.Title;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BeanPropertyBindingResult;

/**
 *
 * @author paddeo
 */
public class PageService implements IPageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ERRCODE_PAGE_NOT_FOUND = "1";
    private static final String ERRCODE_PAGE_ONLY_DRAFT = "2";
    private static final String ERRCODE_FRAME_INVALID = "3";
    private static final String ERRCODE_WIDGET_INVALID = "4";

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IPageModelManager pageModelManager;

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    @Autowired
    private WidgetValidatorFactory widgetValidatorFactory;

    @Autowired
    private WidgetProcessorFactory widgetProcessorFactory;

    @Autowired
    private IDtoBuilder<IPage, PageDto> dtoBuilder;

    protected IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    protected IPageModelManager getPageModelManager() {
        return pageModelManager;
    }

    public void setPageModelManager(IPageModelManager pageModelManager) {
        this.pageModelManager = pageModelManager;
    }

    protected IDtoBuilder<IPage, PageDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<IPage, PageDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    protected WidgetValidatorFactory getWidgetValidatorFactory() {
        return widgetValidatorFactory;
    }

    public void setWidgetValidatorFactory(WidgetValidatorFactory widgetValidatorFactory) {
        this.widgetValidatorFactory = widgetValidatorFactory;
    }

    protected WidgetProcessorFactory getWidgetProcessorFactory() {
        return widgetProcessorFactory;
    }

    public void setWidgetProcessorFactory(WidgetProcessorFactory widgetProcessorFactory) {
        this.widgetProcessorFactory = widgetProcessorFactory;
    }

    protected IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }

    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

    @Override
    public List<PageDto> getPages(String parentCode) {
        List<PageDto> res = new ArrayList<>();
        IPage parent = this.getPageManager().getDraftPage(parentCode);
        Optional<String[]> optional = Optional.ofNullable(parent.getChildrenCodes());
        optional.ifPresent(children -> Arrays.asList(children).forEach(childCode -> {
            IPage child = this.getPageManager().getOnlinePage(childCode) != null
                    ? this.getPageManager().getOnlinePage(childCode) : this.getPageManager().getDraftPage(childCode);
            res.add(dtoBuilder.convert(child));
        }));
        return res;
    }

    @Override
    public PageDto getPage(String pageCode, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            logger.warn("no page found with code {}", pageCode);
            throw new RestRourceNotFoundException(null, "page", pageCode);
        }
        return this.getDtoBuilder().convert(page);
    }

    @Override
    public PageDto addPage(PageRequest pageRequest) {
        try {
            IPage page = this.createPage(pageRequest);
            this.getPageManager().addPage(page);
            return this.getDtoBuilder().convert(page);
        } catch (ApsSystemException e) {
            logger.error("Error adding page", e);
            throw new RestServerError("error add page", e);
        }
    }

    @Override
    public void removePage(String pageCode) {
        try {
            IPage page = this.getPageManager().getDraftPage(pageCode);
            if (null != page) {
                this.getPageManager().deletePage(pageCode);
            }
        } catch (ApsSystemException e) {
            logger.error("Error in delete page {}", pageCode, e);
            throw new RestServerError("error in delete page", e);
        }
    }

    @Override
    public PageDto updatePage(String pageCode, PageRequest pageRequest) {
        IPage oldPage = this.getPageManager().getDraftPage(pageCode);
        if (null == oldPage) {
            throw new RestRourceNotFoundException(null, "page", pageCode);
        }
        try {
            IPage newPage = this.updatePage(oldPage, pageRequest);
            this.getPageManager().updatePage(newPage);
            if (pageRequest.getStatus() != null && pageRequest.getStatus().equals(STATUS_ONLINE)) {
                this.getPageManager().setPageOnline(pageCode);
                newPage = this.getPageManager().getOnlinePage(pageCode);
            } else if (pageRequest.getStatus() != null && pageRequest.getStatus().equals(STATUS_DRAFT)) {
                this.getPageManager().setPageOffline(pageCode);
            }
            return this.getDtoBuilder().convert(newPage);
        } catch (ApsSystemException e) {
            logger.error("Error updating page {}", pageCode, e);
            throw new RestServerError("error in update page", e);
        }
    }

    @Override
    public PageDto movePage(String pageCode, PageRequest pageRequest) {
        IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode()),
                page = this.getPageManager().getDraftPage(pageCode);
        boolean moved = true;
        int iterations = Math.abs(page.getPosition() - pageRequest.getPosition());
        boolean moveUp = page.getPosition() > pageRequest.getPosition();
        try {
            if (page.getParentCode().equals(parent.getCode())) {
                while (iterations-- > 0 && (moved = this.getPageManager().movePage(pageCode, moveUp)));
            } else {
                moved = this.getPageManager().movePage(page, parent);
            }
            page = this.getPageManager().getDraftPage(pageCode);
        } catch (ApsSystemException e) {
            logger.error("Error moving page {}", pageCode, e);
            throw new RestServerError("error in moving page", e);
        }
        return this.getDtoBuilder().convert(page);
    }

    @Override
    public PageConfigurationDto getPageConfiguration(String pageCode, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
        }
        PageConfigurationDto pageConfigurationDto = new PageConfigurationDto(page, status);
        return pageConfigurationDto;
    }

    @Override
    public WidgetConfigurationDto getWidgetConfiguration(String pageCode, int frameId, String status) {
        IPage page = this.loadPage(pageCode, status);
        if (null == page) {
            throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
        }
        if (frameId > page.getWidgets().length) {
            throw new RestRourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
        }
        Widget widget = page.getWidgets()[frameId];
        if (null == widget) {
            return null;
        }
        return new WidgetConfigurationDto(widget);
    }

    @Override
    public WidgetConfigurationDto updateWidgetConfiguration(String pageCode, int frameId, WidgetConfigurationRequest widgetReq) {
        try {
            IPage page = this.loadPage(pageCode, STATUS_DRAFT);
            if (null == page) {
                throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
            }
            if (frameId > page.getWidgets().length) {
                throw new RestRourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
            }
            if (null == this.getWidgetType(widgetReq.getCode())) {
                throw new RestRourceNotFoundException(ERRCODE_WIDGET_INVALID, "widget", String.valueOf(widgetReq.getCode()));
            }

            BeanPropertyBindingResult validation = this.getWidgetValidatorFactory().get(widgetReq.getCode()).validate(widgetReq, page);
            if (null != validation && validation.hasErrors()) {
                throw new ValidationConflictException(validation);
            }

            ApsProperties properties = (ApsProperties) this.getWidgetProcessorFactory().get(widgetReq.getCode()).buildConfiguration(widgetReq);

            WidgetType widgetType = this.getWidgetType(widgetReq.getCode());
            Widget widget = new Widget();
            widget.setType(widgetType);
            widget.setConfig(properties);
            this.getPageManager().joinWidget(pageCode, widget, frameId);

            ApsProperties outProperties = this.getWidgetProcessorFactory().get(widgetReq.getCode()).extractConfiguration(widget.getConfig());
            return new WidgetConfigurationDto(widget.getType().getCode(), outProperties);
        } catch (ApsSystemException e) {
            logger.error("Error in update widget configuration {}", pageCode, e);
            throw new RestServerError("error in update widget configuration", e);
        }
    }

    @Override
    public void deleteWidgetConfiguration(String pageCode, int frameId) {
        try {
            IPage page = this.loadPage(pageCode, STATUS_DRAFT);
            if (null == page) {
                throw new RestRourceNotFoundException(ERRCODE_PAGE_NOT_FOUND, "page", pageCode);
            }
            if (frameId > page.getWidgets().length) {
                throw new RestRourceNotFoundException(ERRCODE_FRAME_INVALID, "frame", String.valueOf(frameId));
            }
            this.pageManager.removeWidget(pageCode, frameId);
        } catch (ApsSystemException e) {
            logger.error("Error in delete widget configuration for page {} and frame {}", pageCode, frameId, e);
            throw new RestServerError("error in delete widget configuration", e);
        }
    }

    public WidgetType getWidgetType(String typeCode) {
        return this.getWidgetTypeManager().getWidgetType(typeCode);
    }

    private IPage createPage(PageRequest pageRequest) {
        Page page = new Page();
        page.setCode(pageRequest.getCode());
        page.setShowable(pageRequest.isDisplayedInMenu());
        PageModel model = this.getPageModelManager().getPageModel(pageRequest.getPageModel());
        page.setModel(model);
        page.setCharset(pageRequest.getCharset());
        page.setMimeType(pageRequest.getContentType());
        page.setParentCode(pageRequest.getParentCode());
        page.setUseExtraTitles(pageRequest.isSeo());
        Optional<List<Title>> titles = Optional.ofNullable(pageRequest.getTitles());
        ApsProperties apsTitles = new ApsProperties();
        titles.ifPresent(values -> values.forEach((title) -> {
            apsTitles.put(title.getLang(), title.getTitle());
        }));
        page.setTitles(apsTitles);
        page.setGroup(pageRequest.getOwnerGroup());
        Optional<List<String>> groups = Optional.ofNullable(pageRequest.getJoinGroups());
        groups.ifPresent(values -> values.forEach((group) -> {
            page.addExtraGroup(group);
        }));
        page.setParentCode(pageRequest.getParentCode());
        if (pageRequest.getParentCode() != null) {
            IPage parent = this.getPageManager().getDraftPage(pageRequest.getParentCode());
            page.setParent(parent);
        }
        return page;
    }

    private IPage updatePage(IPage oldPage, PageRequest pageRequest) {
        Page page = new Page();
        page.setCode(pageRequest.getCode());
        page.setShowable(pageRequest.isDisplayedInMenu());
        if (oldPage.getModel().getCode().equals(pageRequest.getPageModel())) {
            PageModel model = this.getPageModelManager().getPageModel(pageRequest.getPageModel());
            page.setModel(model);
        }
        page.setCharset(pageRequest.getCharset());
        page.setMimeType(pageRequest.getContentType());
        page.setParentCode(pageRequest.getParentCode());
        page.setUseExtraTitles(pageRequest.isSeo());
        Optional<List<Title>> titles = Optional.ofNullable(pageRequest.getTitles());
        ApsProperties apsTitles = new ApsProperties();
        titles.ifPresent(values -> values.forEach((title) -> {
            apsTitles.put(title.getLang(), title.getTitle());
        }));
        page.setTitles(apsTitles);
        page.setGroup(pageRequest.getOwnerGroup());
        Optional<List<String>> groups = Optional.ofNullable(pageRequest.getJoinGroups());
        groups.ifPresent(values -> values.forEach((group) -> {
            page.addExtraGroup(group);
        }));
        page.setParentCode(pageRequest.getParentCode());
        return page;
    }


    private IPage loadPage(String pageCode, String status) {
        IPage page = null;
        switch (status) {
            case STATUS_DRAFT:
                page = this.getPageManager().getDraftPage(pageCode);
                break;
            case STATUS_ONLINE:
                page = this.getPageManager().getOnlinePage(pageCode);
                break;
            default:
                break;
        }
        if (status.equals(STATUS_ONLINE) && null == page && null != this.getPageManager().getDraftPage(pageCode)) {
            BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(page, "page");
            bindingResult.reject(ERRCODE_PAGE_ONLY_DRAFT, new Object[]{pageCode}, "page.status.draftOnly");
            throw new ValidationGenericException(bindingResult);
        }
        return page;
    }



}

