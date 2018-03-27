package org.entando.entando.aps.system.services.widgettype;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.entando.entando.aps.system.services.widgettype.model.WidgetTypeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class WidgetTypeService implements GroupServiceUtilizer<WidgetTypeDto> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IWidgetTypeManager widgetTypeManager;

    private IDtoBuilder<WidgetType, WidgetTypeDto> dtoBuilder;

    protected IWidgetTypeManager getWidgetTypeManager() {
        return widgetTypeManager;
    }

    public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
        this.widgetTypeManager = widgetTypeManager;
    }

    protected IDtoBuilder<WidgetType, WidgetTypeDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<WidgetType, WidgetTypeDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void setUp() {
        this.setDtoBuilder(new DtoBuilder<WidgetType, WidgetTypeDto>() {
            @Override
            protected WidgetTypeDto toDto(WidgetType src) {
                WidgetTypeDto dto = new WidgetTypeDto();
                dto.setCode(src.getCode());
                dto.setTitles(src.getTitles());
                if (null != src.getTypeParameters()) {
                    dto.setParameters(new ArrayList<WidgetTypeParameter>());
                    for (WidgetTypeParameter parameter : src.getTypeParameters()) {
                        dto.getParameters().add(parameter.clone());
                    }
                }
                dto.setAction(src.getAction());
                dto.setPluginCode(src.getPluginCode());
                dto.setParentTypeCode(src.getParentTypeCode());
                dto.setConfig(src.getConfig());
                dto.setLocked(src.isLocked());
                dto.setMainGroup(src.getMainGroup());
                return dto;
            }
        });
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getWidgetTypeManager()).getName();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<WidgetTypeDto> getGroupUtilizer(String groupCode) {
        try {
            List<WidgetType> list = ((GroupUtilizer<WidgetType>) this.getWidgetTypeManager()).getGroupUtilizers(groupCode);
            return this.getDtoBuilder().convert(list);
        } catch (ApsSystemException ex) {
            logger.error("Error loading WidgetType references for group {}", groupCode, ex);
            throw new RestServerError("Error loading WidgetType references for group", ex);
        }
    }

}
