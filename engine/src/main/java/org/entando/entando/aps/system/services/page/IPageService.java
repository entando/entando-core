package org.entando.entando.aps.system.services.page;

import java.util.List;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.page.model.PageRequest;

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

    public PageDto movePage(String pageCode, PageRequest pageRequest);

    public List<PageDto> getPages(String parentCode);

    public PageDto updatePageStatus(String pageCode, String status);

}
