package org.entando.entando.aps.system.services.pagemodel.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

public class PageModelConfigurationDto {

    @JsonProperty(value = "frames")
    private List<FrameDto> frames = new ArrayList<>();

    public List<FrameDto> getFrames() {
        return frames;
    }

    public void setFrames(List<FrameDto> frames) {
        this.frames = frames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageModelConfigurationDto that = (PageModelConfigurationDto) o;
        return Objects.equals(frames, that.frames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frames);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("frames", frames)
                .toString();
    }
}
