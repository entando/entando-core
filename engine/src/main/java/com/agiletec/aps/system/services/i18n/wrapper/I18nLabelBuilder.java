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
package com.agiletec.aps.system.services.i18n.wrapper;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;


/**
 * A builder of parameterized label.
 */
public class I18nLabelBuilder {
	
	public I18nLabelBuilder(String label) {
		this.label = label;
	}
	
	public I18nLabelBuilder addParam(String key, String value) {
		if (this.params == null) {
			this.params = new HashMap<>();
		}
		this.params.put(key, value);
		return this;
	}
	
	@Override
	public String toString() {
		if (this.label != null && this.params != null && !this.params.isEmpty()) {
			StrSubstitutor strSub = new StrSubstitutor(this.params);
			this.label = strSub.replace(this.label);
		}
		return this.label;
	}
	
	private String label;
	private Map<String, String> params;
	
}
