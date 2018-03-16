package org.entando.entando.aps.system.services.page.model;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PageConfigurationDto {

    private String code;
    private String parentCode;
    private String status;
    private int position;
    private String ownerGroup;

    private PageMetadataDto metadata;
    private WidgetConfigurationDto[] widgets;

    @JsonProperty(value = "online")
    private boolean onLine;
    private boolean changed;
    private boolean root;

    public PageConfigurationDto(IPage src, String status) {
        this.setCode(src.getCode());
        this.setMetadata(new PageMetadataDto(src.getMetadata()));
        this.setOnLine(src.isOnline());
        this.setOwnerGroup(src.getGroup());
        this.setParentCode(src.getParentCode());
        this.setPosition(src.getPosition());
        this.setRoot(src.isRoot());
        this.setStatus(status);
        this.buildWidgetsDto(src.getWidgets());
        this.setChanged(src.isChanged());
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getOwnerGroup() {
        return ownerGroup;
    }

    public void setOwnerGroup(String ownerGroup) {
        this.ownerGroup = ownerGroup;
    }

    public PageMetadataDto getMetadata() {
        return metadata;
    }

    public void setMetadata(PageMetadataDto metadata) {
        this.metadata = metadata;
    }

    public WidgetConfigurationDto[] getWidgets() {
        return widgets;
    }

    public void setWidgets(WidgetConfigurationDto[] widgets) {
        this.widgets = widgets;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    protected void buildWidgetsDto(Widget[] widgets) {
        if (null == widgets) {
            return;
        }
        List<WidgetConfigurationDto> widgetDtoList = new ArrayList<>();
        for (Widget widget : widgets) {
            if (null == widget) {
                widgetDtoList.add(null);
            } else {
                widgetDtoList.add(new WidgetConfigurationDto(widget.getType().getCode(), widget.getConfig()));
            }
        }
        this.setWidgets(widgetDtoList.toArray(new WidgetConfigurationDto[widgetDtoList.size()]));
    }



}
