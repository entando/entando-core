package org.entando.entando.aps.system.services.label;

import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface ILabelService {

    String BEAN_NAME = "LabelService";

    PagedMetadata<LabelDto> getLabelGroups(RestListRequest restRequest);

    LabelDto getLabelGroup(String code);

    LabelDto updateLabelGroup(LabelDto labelRequest);

    LabelDto addLabelGroup(LabelDto labelRequest);

    void removeLabelGroup(String code);
}
