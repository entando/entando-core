package org.entando.entando.web.common.assembler;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.apache.commons.beanutils.BeanComparator;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

import java.util.List;

public class PagedMetadataMapper {

    // here Spring singleton is not used to keep consinstency with MapStruct library
    public static final PagedMetadataMapper INSTANCE = new PagedMetadataMapper();


    /**
     * craetes a PagedMetadata<T> starting from the received
     *
     * @param restListRequest
     * @param list
     * @param <T>
     * @return the created
     */
    public <T> PagedMetadata<T> getComponentUsagePagedResult(RestListRequest restListRequest, List<T> list) {

        BeanComparator<T> comparator = new BeanComparator<>(restListRequest.getSort());

        if (restListRequest.getDirection().equals(FieldSearchFilter.DESC_ORDER)) {
            list.sort(comparator.reversed());
        } else {
            list.sort(comparator);
        }

        PagedMetadata<T> result = new PagedMetadata<T>(restListRequest, list, list.size());
        result.imposeLimits();

        return result;
    }
}
