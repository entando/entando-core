package org.entando.entando.web.common.assembler;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.page.model.PageSearchRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PagedMetadataMapper {

    /**
     * craetes a PagedMetadata<T> starting from the received
     *
     * @param restListRequest
     * @param list
     * @param <T>
     * @return the created
     */
    public <T> PagedMetadata<T> getPagedResult(RestListRequest restListRequest, List<T> list) {

       return getPagedResult(restListRequest, list, restListRequest.getSort());
    }


    /**
     * craetes a PagedMetadata<T> starting from the received
     *
     * @param restListRequest
     * @param list
     * @param <T>
     * @return the created
     */
    public <T> PagedMetadata<T> getPagedResult(RestListRequest restListRequest, List<T> list, String sortField) {

        return getPagedResult(restListRequest, list, sortField, list.size());
    }


    /**
     * craetes a PagedMetadata<T> starting from the received
     *
     * @param restListRequest
     * @param list
     * @param <T>
     * @return the created
     */
    public <T> PagedMetadata<T> getPagedResult(RestListRequest restListRequest, List<T> list, String sortField, int totalItems) {

        BeanComparator<T> comparator = new BeanComparator<>(sortField);

        if (restListRequest.getDirection().equals(FieldSearchFilter.DESC_ORDER)) {
            list.sort(comparator.reversed());
        } else {
            list.sort(comparator);
        }

        PagedMetadata<T> result = new PagedMetadata<T>(restListRequest, list, totalItems);
        result.imposeLimits();

        return result;
    }

}
