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
package org.entando.entando.aps.system.services.i18n.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.entando.entando.aps.system.services.api.model.CDataXmlTypeAdapter;

import com.agiletec.aps.util.ApsProperties;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "i18nLabel")
@XmlType(propOrder = {"key", "labels"})
public class JAXBI18nLabel implements Serializable {
    
    public JAXBI18nLabel() {
        super();
    }
    
    public JAXBI18nLabel(String key, ApsProperties labels) {
        this.setKey(key);
		if (null != labels) {
			List<JAXBLabel> jaxbLabels = new ArrayList<JAXBLabel>();
			Iterator<Object> labelCodeIter = labels.keySet().iterator();
			while (labelCodeIter.hasNext()) {
				Object langCode = labelCodeIter.next();
				JAXBLabel jaxbLabel = new JAXBLabel(langCode.toString(), labels.get(langCode).toString());
				jaxbLabels.add(jaxbLabel);
			}
			this.setLabels(jaxbLabels);
		}
    }
	
	public ApsProperties extractLabels() {
		ApsProperties properties = new ApsProperties();
		if (null != this.getLabels()) {
			for (int i = 0; i < this.getLabels().size(); i++) {
				JAXBLabel jAXBLabel = this.getLabels().get(i);
				properties.put(jAXBLabel.getLangCode(), jAXBLabel.getValue());
			}
		}
		return properties;
	}
    
	@XmlElement(name = "key", required = true)
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}
	
	@XmlElement(name = "label", required = true)
    @XmlElementWrapper(name = "labels")
	public List<JAXBLabel> getLabels() {
		return _labels;
	}
	public void setLabels(List<JAXBLabel> labels) {
		this._labels = labels;
	}
	
	private String _key;
	private List<JAXBLabel> _labels;
	
	@XmlRootElement(name = "label")
	@XmlType(propOrder = {"langCode", "value"})
	public static class JAXBLabel {
		
		public JAXBLabel() {}
		
		public JAXBLabel(String langCode, String value) {
			this.setLangCode(langCode);
			this.setValue(value);
		}
		
		@XmlElement(name = "langCode", required = true)
		public String getLangCode() {
			return _langCode;
		}
		public void setLangCode(String langCode) {
			this._langCode = langCode;
		}
		
		@XmlJavaTypeAdapter(CDataXmlTypeAdapter.class)
		@XmlElement(name = "value", required = true)
		public String getValue() {
			return _value;
		}
		public void setValue(String value) {
			this._value = value;
		}
		
		private String _langCode;
		private String _value;
		
	}
	
}