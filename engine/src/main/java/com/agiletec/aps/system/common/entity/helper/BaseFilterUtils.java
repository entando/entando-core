/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.aps.system.common.entity.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;

/**
 * Provides base utility methods for entity filters.
 * @author E.Santoboni
 */
public class BaseFilterUtils {
    
    /**
     * Split the filters (in form of string) into a list of properties (eatch properties is a single filter).
     * @param filtersString The filter (in form of string)
     * @return The list of properties
     */
    public static List<Properties> getFiltersProperties(String filtersString) {
        if (null == filtersString) {
            return new ArrayList<Properties>();
        }
        String[] filterStrings = filtersString.split("\\+");
        List<Properties> properties = new ArrayList<Properties>(filterStrings.length);
        for (int i = 0; i < filterStrings.length; i++) {
            String fullFilterString = filterStrings[i];
            String filterString = fullFilterString.substring(1, fullFilterString.length() - 1);
            Properties props = getProperties(filterString, DEFAULT_FILTER_PARAM_SEPARATOR);
            properties.add(props);
        }
        return properties;
    }
    
    public EntitySearchFilter[] getFilters(IApsEntity entityPrototype, String filtersString, String langCode) {
        if (null == entityPrototype) {
            return null;
        }
        List<Properties> properties = getFiltersProperties(filtersString);
        EntitySearchFilter[] filters = new EntitySearchFilter[properties.size()];
        for (int i = 0; i < properties.size(); i++) {
            Properties props = properties.get(i);
            EntitySearchFilter filter = EntitySearchFilter.getInstance(entityPrototype, props);
            this.attachLangFilter(entityPrototype, filter, props, langCode);
            filters[i] = filter;
        }
        return filters;
    }
    
    public EntitySearchFilter getFilter(IApsEntity entityPrototype, IEntityFilterBean bean, String langCode) {
        Properties props = new Properties();
        props.setProperty(EntitySearchFilter.KEY_PARAM, bean.getKey());
        props.setProperty(EntitySearchFilter.FILTER_TYPE_PARAM, String.valueOf(bean.isAttributeFilter()));
        props.setProperty(EntitySearchFilter.LIKE_OPTION_PARAM, String.valueOf(bean.getLikeOption()));
		if (null != bean.getLikeOptionType()) {
			props.setProperty(EntitySearchFilter.LIKE_OPTION_TYPE_PARAM, String.valueOf(bean.getLikeOptionType()));
		}
        if (null != bean.getValue()) {
            props.setProperty(EntitySearchFilter.VALUE_PARAM, bean.getValue());
        }
        if (null != bean.getStart()) {
            props.setProperty(EntitySearchFilter.START_PARAM, bean.getStart());
        }
        if (null != bean.getEnd()) {
            props.setProperty(EntitySearchFilter.END_PARAM, bean.getEnd());
        }
        if (null != bean.getOrder()) {
            props.setProperty(EntitySearchFilter.ORDER_PARAM, bean.getOrder());
        }
        EntitySearchFilter filter = EntitySearchFilter.getInstance(entityPrototype, props);
        this.attachLangFilter(entityPrototype, filter, props, langCode);
        return filter;
    }
    
    private void attachLangFilter(IApsEntity entityPrototype, EntitySearchFilter filter, Properties props, String langCode) {
        String filterType = (String) props.get(EntitySearchFilter.FILTER_TYPE_PARAM);
        boolean isAttributeFilter = Boolean.parseBoolean(filterType);
        if (isAttributeFilter) {
            String attributeName = (String) props.get(EntitySearchFilter.KEY_PARAM);
            AttributeInterface attribute = (AttributeInterface) entityPrototype.getAttribute(attributeName);
            if (attribute.isMultilingual()) {
                filter.setLangCode(langCode);
            }
        }
    }
    
    public String getFilterParam(EntitySearchFilter[] filters) {
        StringBuilder param = new StringBuilder("");
        for (int i = 0; i < filters.length; i++) {
            if (i != 0) {
                param.append("+");
            }
            String element = filters[i].toString();
            param.append("(");
            param.append(element);
            param.append(")");
        }
        return param.toString();
    }
    
    public static String getToStringFilterParam(List<Properties> properties, String separator) {
        StringBuilder param = new StringBuilder("");
        for (int i = 0; i < properties.size(); i++) {
            if (i != 0) {
                param.append("+");
            }
            Properties props = properties.get(i);
            String element = createElement(props, separator);
            param.append("(");
            param.append(element);
            param.append(")");
        }
        return param.toString();
    }
    
    private static String createElement(Properties props, String separator) {
        StringBuilder param = new StringBuilder();
        Iterator<Object> keys = props.keySet().iterator();
        boolean init = true;
        while (keys.hasNext()) {
            String key = (String) keys.next();
            if (!init) {
                param.append(separator);
            }
            param.append(key).append("=").append(props.getProperty(key));
            init = false;
        }
        return param.toString();
    }
    
    protected static Properties getProperties(String toStringFilter, String separator) {
        Properties props = new Properties();
        String[] params = toStringFilter.split(separator);
        for (int i = 0; i < params.length; i++) {
            String[] elements = params[i].split("=");
            if (elements.length != 2) {
                continue;
            }
            props.setProperty(elements[0], elements[1]);
        }
        return props;
    }
    
    public static final String DEFAULT_FILTER_PARAM_SEPARATOR = ";";
    
}