package com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary;

import java.util.List;
import java.util.Properties;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import org.springframework.stereotype.Component;

@Component
public class ContentModelDictionaryProvider {

    private List<String> contentMap;
    private List<String> i18nMap;
    private List<String> infoMap;
    private List<String> commonMap;
    private Properties allowedPublicAttributeMethods;

    public List<String> getContentMap() {
        return contentMap;
    }

    public void setContentMap(List<String> contentMap) {
        this.contentMap = contentMap;
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

    public ContentModelDictionary buildDictionary() {
        return buildDictionary(null);
    }

    public ContentModelDictionary buildDictionary(Content prototype) {
        ContentModelDictionary dictionary = new ContentModelDictionary(contentMap, i18nMap, infoMap, commonMap, allowedPublicAttributeMethods, prototype);
        return dictionary;
    }
}
