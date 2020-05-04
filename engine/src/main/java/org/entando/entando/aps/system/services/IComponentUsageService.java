package org.entando.entando.aps.system.services;

import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.component.ComponentUsageEntity;
import org.entando.entando.web.page.model.PageSearchRequest;

public interface IComponentUsageService {

    Integer getComponentUsage(String componentCode);

    PagedMetadata<ComponentUsageEntity> getComponentUsageDetails(String componentCode, PageSearchRequest searchRequest);
}
