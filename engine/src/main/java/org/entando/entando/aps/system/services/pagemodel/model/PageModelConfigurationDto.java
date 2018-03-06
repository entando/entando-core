package org.entando.entando.aps.system.services.pagemodel.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageModelConfigurationDto {

    @JsonProperty(value = "frames")
    private List<FrameDto> frames = new ArrayList<>();

    public List<FrameDto> getFrames() {
        return frames;
    }

    public void setFrames(List<FrameDto> frames) {
        this.frames = frames;
    }
}
