/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.page;

import java.util.List;

import org.entando.entando.aps.system.services.page.model.PageConfigurationDto;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.page.model.WidgetConfigurationDto;
import org.entando.entando.web.page.model.PageRequest;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;

/**
 *
 * @author paddeo
 */
public interface IPageService {

    String BEAN_NAME = "PageService";

    public static final String STATUS_ONLINE = "published";
    public static final String STATUS_DRAFT = "draft";

    public PageDto getPage(String pageCode, String status);

    public PageDto addPage(PageRequest pageRequest);

    public void removePage(String pageName);

    public PageDto updatePage(String pageCode, PageRequest pageRequest);

    public List<PageDto> getPages(String parentCode);

    public PageDto movePage(String pageCode, PageRequest pageRequest);

    public PageConfigurationDto getPageConfiguration(String pageCode, String status);

    public WidgetConfigurationDto getWidgetConfiguration(String pageCode, int frameId, String status);

    public WidgetConfigurationDto updateWidgetConfiguration(String pageCode, int frameId, WidgetConfigurationRequest widget);

    public void deleteWidgetConfiguration(String pageCode, int frameId);

}
