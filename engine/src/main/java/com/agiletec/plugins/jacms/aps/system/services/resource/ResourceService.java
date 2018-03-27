package com.agiletec.plugins.jacms.aps.system.services.resource;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.AbstractResource;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceDto;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceService implements IResourceService, GroupServiceUtilizer<ResourceDto> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IResourceManager resourceManager;
    private IDtoBuilder<ResourceInterface, ResourceDto> dtoBuilder;

    public IResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(IResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }


    protected IDtoBuilder<ResourceInterface, ResourceDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<ResourceInterface, ResourceDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void setUp() {
        this.setDtoBuilder(new DtoBuilder<ResourceInterface, ResourceDto>() {

            @Override
            protected ResourceDto toDto(ResourceInterface src) {
                ResourceDto resourceDto = new ResourceDto(((AbstractResource) src));
                return resourceDto;
            }
        });
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getResourceManager()).getName();
    }

    @Override
    public List<ResourceDto> getGroupUtilizer(String groupCode) {
        try {
            List<String> resourcesId = ((GroupUtilizer<String>) this.getResourceManager()).getGroupUtilizers(groupCode);
            List<ResourceDto> dtoList = new ArrayList<>();
            if (null != resourcesId) {
                resourcesId.stream().forEach(i -> {
                    try {
                        dtoList.add(this.getDtoBuilder().convert(this.getResourceManager().loadResource(i)));
                    } catch (ApsSystemException e) {
                        logger.error("error loading {}", i, e);

                    }
                });
            }
            return dtoList;
        } catch (ApsSystemException ex) {
            logger.error("Error loading resource references for group {}", groupCode, ex);
            throw new RestServerError("Error loading resource references for group", ex);
        }
    }



}
