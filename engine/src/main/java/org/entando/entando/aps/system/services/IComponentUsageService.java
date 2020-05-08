package org.entando.entando.aps.system.services;

import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsageEntity;

public interface IComponentUsageService {

    Integer getComponentUsage(String componentCode);

    PagedMetadata<ComponentUsageEntity> getComponentUsageDetails(String componentCode, RestListRequest restListRequest);
}
