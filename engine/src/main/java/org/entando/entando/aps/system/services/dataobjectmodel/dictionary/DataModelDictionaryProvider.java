package org.entando.entando.aps.system.services.dataobjectmodel.dictionary;

import java.util.List;
import java.util.Properties;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import org.entando.entando.aps.system.services.dataobjectmodel.model.IEntityModelDictionary;
import org.springframework.stereotype.Component;

@Component
public class DataModelDictionaryProvider {

    private List<String> dataMap;
    private List<String> i18nMap;
    private List<String> infoMap;
    private List<String> commonMap;
    private Properties allowedPublicAttributeMethods;

    public List<String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(List<String> dataMap) {
        this.dataMap = dataMap;
    }

    public List<String> getI18nMap() {
        return i18nMap;
    }

    public void setI18nMap(List<String> i18nMap) {
        this.i18nMap = i18nMap;
    }

    public List<String> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(List<String> infoMap) {
        this.infoMap = infoMap;
    }

    public List<String> getCommonMap() {
        return commonMap;
    }

    public void setCommonMap(List<String> commonMap) {
        this.commonMap = commonMap;
    }

    public Properties getAllowedPublicAttributeMethods() {
        return allowedPublicAttributeMethods;
    }

    public void setAllowedPublicAttributeMethods(Properties allowedPublicAttributeMethods) {
        this.allowedPublicAttributeMethods = allowedPublicAttributeMethods;
    }

    public IEntityModelDictionary buildDictionary() {
        return buildDictionary(null);
    }

    public IEntityModelDictionary buildDictionary(IApsEntity prototype) {
        IEntityModelDictionary dictionary = new DataModelDictionary(dataMap, i18nMap, infoMap, commonMap, allowedPublicAttributeMethods, prototype);
        return dictionary;
    }

}
