/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.page;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Page;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.Title;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author paddeo
 */
public class PageService implements IPageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPageManager pageManager;

    @Autowired
    private IPageModelManager pageModelManager;

    @Autowired
    private IDtoBuilder<IPage, PageDto> dtoBuilder;

    public IPageManager getPageManager() {
        return pageManager;
    }

    public void setPageManager(IPageManager pageManager) {
        this.pageManager = pageManager;
    }

    public IPageModelManager getPageModelManager() {
        return pageModelManager;
    }

    public void setPageModelManager(IPageModelManager pageModelManager) {
        this.pageModelManager = pageModelManager;
    }

    public IDtoBuilder<IPage, PageDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<IPage, PageDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
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
        IPage page = status.equals(STATUS_ONLINE) ? this.getPageManager().getOnlinePage(pageCode) : this.getPageManager().getDraftPage(pageCode);
        if (null == page) {
            logger.warn("no page found with code {}", pageCode);
            throw new RestRourceNotFoundException("page", pageCode);
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
            throw new RestRourceNotFoundException("page", pageCode);
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

}
