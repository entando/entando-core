package org.entando.entando.web.language.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.entando.entando.web.common.json.BooleanStringDeserializer;

public class LanguageRequest {

    @NotNull(message = "language.active.required")
    @JsonProperty("isActive")
    @JsonDeserialize(using = BooleanStringDeserializer.class)
    private Boolean isActive;

    public LanguageRequest() {

    }

    @JsonIgnore
    public boolean getStatus() {
        return true == this.getIsActive();
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


}
