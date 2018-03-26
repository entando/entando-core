package com.agiletec.plugins.jacms.aps.system.services.content;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.services.group.GroupServiceUtilizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentService implements GroupServiceUtilizer<ContentDto> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private IContentManager contentManager;
    private IDtoBuilder<Content, ContentDto> dtoBuilder;

    public IContentManager getContentManager() {
        return contentManager;
    }

    public void setContentManager(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    public IDtoBuilder<Content, ContentDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<Content, ContentDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    private void setUp() {
        this.setDtoBuilder(new DtoBuilder<Content, ContentDto>() {

            @Override
            protected ContentDto toDto(Content src) {
                ContentDto dto = new ContentDto(src);

                return dto;
            }
        });
    }

    @Override
    public String getManagerName() {
        return ((IManager) this.getContentManager()).getName();
    }

    @Override
    public List<ContentDto> getGroupUtilizer(String groupCode) {
        try {
            List<String> resourcesId = ((GroupUtilizer<String>) this.getContentManager()).getGroupUtilizers(groupCode);
            List<ContentDto> dtoList = new ArrayList<>();
            if (null != resourcesId) {
                resourcesId.stream().forEach(i -> {
                    try {
                        dtoList.add(this.getDtoBuilder().convert(this.getContentManager().loadContent(i, false)));
                    } catch (ApsSystemException e) {
                        logger.error("error loading {}", i, e);

                    }
                });
            }
            return dtoList;
        } catch (ApsSystemException ex) {
            logger.error("Error loading content references for group {}", groupCode, ex);
            throw new RestServerError("Error loading content references for group", ex);
        }
    }
}
