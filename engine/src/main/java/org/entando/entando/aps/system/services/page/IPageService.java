/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.entando.entando.aps.system.services.page;

import java.util.List;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.page.model.PageRequest;

/**
 *
 * @author paddeo
 */
public interface IPageService {

    public PageDto getPage(String pageCode);

    public PageDto addPage(PageRequest pageRequest);

    public void removePage(String pageName);

    public PageDto updatePage(String pageCode, PageRequest pageRequest);

    public List<PageDto> getPages(String parentCode);

}
