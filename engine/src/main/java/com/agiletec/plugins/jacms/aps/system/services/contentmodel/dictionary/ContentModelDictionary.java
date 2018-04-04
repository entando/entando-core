package com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary;

import java.util.List;
import java.util.Properties;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import org.entando.entando.aps.system.services.dataobjectmodel.dictionary.DataModelDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ContentModelDictionary extends DataModelDictionary {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String KEY_ROOT = "$content";

    @Override
    public String getEntityRootName() {
        return KEY_ROOT;
    }

    public ContentModelDictionary(List<String> contentConfig, List<String> i18nConfig, List<String> infoConfig, List<String> commonConfig, Properties publicAttributeMethods, IApsEntity prototype) {
        super(contentConfig, i18nConfig, infoConfig, commonConfig, publicAttributeMethods, prototype);
    }


}
