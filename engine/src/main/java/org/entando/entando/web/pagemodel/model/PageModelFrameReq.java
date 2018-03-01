package org.entando.entando.web.pagemodel.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.agiletec.aps.system.services.pagemodel.FrameSketch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageModelFrameReq {

    @Min(value = 0)
    private int pos;

    @NotNull(message = "pageModelFrame.descr.notBlank")
    @JsonProperty("description")
    private String descr;

    private boolean mainFrame;
    private FrameSketch sketch;

    public PageModelFrameReq() {

    }

    public PageModelFrameReq(int pos, String descr) {
        this.pos = pos;
        this.descr = descr;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public boolean isMainFrame() {
        return mainFrame;
    }

    public void setMainFrame(boolean mainFrame) {
        this.mainFrame = mainFrame;
    }

    public FrameSketch getSketch() {
        return sketch;
    }

    public void setSketch(FrameSketch sketch) {
        this.sketch = sketch;
    }

}
