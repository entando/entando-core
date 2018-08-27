/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "mapping")
public class JaxbMetadataMapping {

    private List<MetadataFieldMapping> fields = new ArrayList<>();

    public JaxbMetadataMapping() {
    }

    public JaxbMetadataMapping(Map<String, List<String>> mapping) {
        if (null == mapping) {
            return;
        }
        List<MetadataFieldMapping> newFields = new ArrayList<>();
        mapping.keySet().stream().forEach(key -> {
            List<String> list = mapping.get(key);
            if (null != list) {
                String values = String.join(",", list);
                MetadataFieldMapping field = new MetadataFieldMapping();
                field.setKey(key);
                field.setValue(values);
                newFields.add(field);
            }
        });
        this.setFields(newFields);
    }

    @XmlElement(name = "field", required = false)
    public List<MetadataFieldMapping> getFields() {
        return fields;
    }

    public void setFields(List<MetadataFieldMapping> fields) {
        this.fields = fields;
    }

    @XmlRootElement(name = "field")
    public static class MetadataFieldMapping {

        private String key;
        private String value;

        @XmlAttribute(name = "key")
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        @XmlValue
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
