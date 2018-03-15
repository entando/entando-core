package org.entando.entando.web.label;

import java.util.Map;

import org.entando.entando.aps.system.services.label.model.LabelDto;
import org.hibernate.validator.constraints.NotEmpty;

public class LabelRequest extends LabelDto {

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return super.getKey();
    }

    @Override
    public void setKey(String key) {
        // TODO Auto-generated method stub
        super.setKey(key);
    }

    @NotEmpty
    @Override
    public Map<String, String> getLanguages() {
        // TODO Auto-generated method stub
        return super.getLanguages();
    }

    @Override
    public void setLanguages(Map<String, String> languages) {
        // TODO Auto-generated method stub
        super.setLanguages(languages);
    }

}
