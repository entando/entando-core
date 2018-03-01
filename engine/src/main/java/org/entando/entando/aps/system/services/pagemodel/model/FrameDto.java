package org.entando.entando.aps.system.services.pagemodel.model;

import com.agiletec.aps.system.services.pagemodel.FrameSketch;

public class FrameDto {

    private int pos;

    private String description;
    private boolean mainFrame;
    private DefaultWidgetDto defaultWidget;
    private FrameSketch sketch;


    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(boolean mainFrame) {
        this.mainFrame = mainFrame;
    }

    public DefaultWidgetDto getDefaultWidget() {
        return defaultWidget;
    }

    public void setDefaultWidget(DefaultWidgetDto defaultWidget) {
        this.defaultWidget = defaultWidget;
    }

    public FrameSketch getSketch() {
        return sketch;
    }

    public void setSketch(FrameSketch sketch) {
        this.sketch = sketch;
    }

}
