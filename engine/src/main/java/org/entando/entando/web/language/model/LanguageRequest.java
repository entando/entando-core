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
    private Boolean active;

    public LanguageRequest() {

    }

    @JsonIgnore
    public boolean getStatus() {
        return true == this.getActive();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
