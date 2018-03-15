package org.entando.entando.web.label;

import java.util.Map;

import javax.validation.constraints.Size;

import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.hibernate.validator.constraints.NotEmpty;

public class LabelRequest extends LabelDto {

    @NotEmpty(message = "labelRequest.key.required")
    @Size(max = 50)
    @Override
    public String getKey() {
        return super.getKey();
    }

    @NotEmpty(message = "labelRequest.languages.required")
    @Override
    public Map<String, String> getLanguages() {
        return super.getLanguages();
    }

    @Override
    public void setLanguages(Map<String, String> languages) {
        super.setLanguages(languages);
    }

}
