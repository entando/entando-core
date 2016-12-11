package com.agiletec.aps.system.services.i18n.wrapper;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

/**
 * A builder of parameterized label.
 * 
 * @author E.Mezzano
 *
 */
public class I18nLabelBuilder {
	
	public I18nLabelBuilder(String label) {
		this._label = label;
	}
	
	public I18nLabelBuilder addParam(String key, String value) {
		if (this._params == null) {
			this._params = new HashMap<String, String>();
		}
		this._params.put(key, value);
		return this;
	}
	
	@Override
	public String toString() {
		if (this._label != null && this._params != null && !this._params.isEmpty()) {
			StrSubstitutor strSub = new StrSubstitutor(this._params);
			this._label = strSub.replace(this._label);
		}
		return this._label;
	}
	
	private String _label;
	private Map<String, String> _params;
	
}
