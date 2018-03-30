package com.agiletec.plugins.jacms.aps.system.services.contentmodel.dictionary;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.agiletec.aps.system.common.entity.model.attribute.AbstractAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.fasterxml.jackson.annotation.JsonValue;


public class ContentModelDictionary {


    private LinkedHashMap<String, Object> data = new LinkedHashMap<>();

    private static final String KEY_CONTENT = "$content";
    private static final String KEY_I18N = "$i18n";
    private static final String KEY_INFO = "$info";


    public ContentModelDictionary(List<String> contentConfig, List<String> i18nConfig, List<String> infoConfig, List<String> commonConfig, Properties publicAttributeMethods, Content prototype) {
        super();
        this.putAsMap(KEY_CONTENT, contentConfig);
        if (null != prototype) {
            this.addAttributes(prototype, publicAttributeMethods);
        }
        //
        this.putAsMap(KEY_I18N, i18nConfig);
        this.putAsMap(KEY_INFO, infoConfig);
        this.putAsList(commonConfig);
    }



    public void putAsMap(String key, List<String> list) {
        Map<String, String> result = list.stream().collect(HashMap::new, (m, v) -> m.put(v, null), HashMap::putAll);
        this.getData().put(key, result);
    }

    public void putAsList(List<String> list) {
        Map<String, String> result = list.stream().collect(HashMap::new, (m, v) -> m.put(v, null), HashMap::putAll);
        this.getData().putAll(result);
    }

    private void addAttributes(Content prototype, Properties publicAttributeMethods) {
        for (AttributeInterface attribute : prototype.getAttributeList()) {
            List<String> attibuteMethodList = this.getAllowedAttributeMethods(attribute, publicAttributeMethods);
            this.putAsMap(attribute.getName(), attibuteMethodList);
        }
    }

    public List<String> getAllowedAttributeMethods(AttributeInterface attribute, Properties publicAttributeMethods) {
        List<String> methods = new ArrayList<String>();
        try {
            String methodsString = publicAttributeMethods.getProperty(attribute.getType());
            if (null != methodsString) {
                String[] methodsArray = methodsString.split(";");
                methods = Arrays.asList(methodsArray);
            } else {
                BeanInfo beanInfo = Introspector.getBeanInfo(attribute.getClass(), AbstractAttribute.class);
                PropertyDescriptor[] prDescrs = beanInfo.getPropertyDescriptors();
                for (int i = 0; i < prDescrs.length; i++) {
                    PropertyDescriptor propertyDescriptor = prDescrs[i];
                    if (null != propertyDescriptor.getReadMethod()) {
                        methods.add(propertyDescriptor.getDisplayName());
                    }
                }
            }
            //}
        } catch (Throwable t) {
            //_logger.error("error in getAllowedAttributeMethods", t);
            //ApsSystemUtils.logThrowable(t, this, "getAllowedAttributeMethods");
        }
        return methods;
    }


    @JsonValue
    public LinkedHashMap<String, Object> getData() {
        return data;
    }


    public void setData(LinkedHashMap<String, Object> data) {
        this.data = data;
    }
}
