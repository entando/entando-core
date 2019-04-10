package org.entando.entando.web.page;

import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.web.page.model.PageRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class PageDtoToRequestConverter implements Converter<PageDto, PageRequest> {

    @Override
    public PageRequest convert(PageDto source) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setCode(source.getCode());
        pageRequest.setCharset(source.getCharset());
        pageRequest.setContentType(source.getContentType());
        pageRequest.setDisplayedInMenu(source.isDisplayedInMenu());
        pageRequest.setJoinGroups(source.getJoinGroups());
        pageRequest.setOwnerGroup(source.getOwnerGroup());
        pageRequest.setPageModel(source.getPageModel());
        pageRequest.setParentCode(source.getParentCode());
        pageRequest.setStatus(source.getStatus());
        pageRequest.setTitles(source.getTitles());
        pageRequest.setSeo(source.isSeo());
        return pageRequest;
    }
}
