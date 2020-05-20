package org.entando.entando.web.common.assembler;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.aps.system.services.page.model.PageDto;
import org.entando.entando.aps.system.services.page.model.PageSearchDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PageSearchMapper {

    /**
     * craetes a PageSearchDto starting from the received data
     *
     * @param request
     * @param pages
     * @return the created
     */
    public PagedMetadata<PageDto> toPageSearchDto(PageSearchRequest request, List<PageDto> pages) {

        BeanComparator<PageDto> comparator = new BeanComparator<>(request.getSort());

        if (request.getDirection().equals(FieldSearchFilter.DESC_ORDER)) {
            pages.sort(comparator.reversed());
        } else {
            pages.sort(comparator);
        }

        PageSearchDto result = new PageSearchDto(request, pages);
        result.imposeLimits();

        return result;
    }


    /**
     * craetes a PageSearchDto starting from the received data
     *
     * @param request
     * @param pages
     * @return the created
     */
    public PagedMetadata<PageDto> toPageSearchDto(RestListRequest request, List<PageDto> pages) {

        PageSearchRequest pageSearchReq = new PageSearchRequest();
        BeanUtils.copyProperties(request, pageSearchReq);

        return toPageSearchDto(pageSearchReq, pages);
    }

}
