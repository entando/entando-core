package org.entando.entando.web.pagemodel.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

public class PageModelConfigurationRequest {

    @Valid
    @NotEmpty(message = "pageModel.configuration.frames.required")
    private List<PageModelFrameReq> frames = new ArrayList<>();

    public List<PageModelFrameReq> getFrames() {
        return frames;
    }

    public void setFrames(List<PageModelFrameReq> frames) {
        this.frames = frames;
    }

    public void add(PageModelFrameReq frame) {
        this.getFrames().add(frame);
    }
}
