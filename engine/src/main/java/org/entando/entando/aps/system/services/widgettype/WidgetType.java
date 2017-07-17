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
package org.entando.entando.aps.system.services.widgettype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.util.ApsProperties;

/**
 * Rappresenta un tipo di oggetto visuale che può essere inserito in una
 * pagina, in uno dei frames specificati dal modello di pagina.
 * A questa rappresentazione corrisponde una jsp che implementa
 * effettivamente l'oggetto visuale.
 * @author M.Diana - E.Santoboni
 */
public class WidgetType implements Serializable {
	
	@Override
	public WidgetType clone() {
		WidgetType clone = new WidgetType();
		clone.setAction(this.getAction());
		clone.setCode(this.getCode());
		if (null != this.getConfig()) {
			clone.setConfig(this.getConfig().clone());
		}
		clone.setLocked(this.isLocked());
		clone.setParentType(this.getParentType());
		clone.setParentTypeCode(this.getParentTypeCode());
		clone.setPluginCode(this.getPluginCode());
		if (null != this.getTitles()) {
			clone.setTitles(this.getTitles().clone());
		}
		if (null != this.getTypeParameters()) {
			List<WidgetTypeParameter> params = new ArrayList<WidgetTypeParameter>();
			for (int i = 0; i < this.getTypeParameters().size(); i++) {
				params.add(this.getTypeParameters().get(i).clone());
			}
			clone.setTypeParameters(params);
		}
		clone.setMainGroup(this.getMainGroup());
		return clone;
	}
	
	/**
	 * Restituisce il codice del tipo di widget.
	 * @return Il codice del tipo di widget
	 */
	public String getCode() {
		return _code;
	}

	/**
	 * Imposta il codice del tipo di widget.
	 * @param code Il codice del tipo di widget
	 */
	public void setCode(String code) {
		this._code = code;
	}

	public ApsProperties getTitles() {
		return _titles;
	}
	public void setTitles(ApsProperties titles) {
		this._titles = titles;
	}
	
	/**
	 * Restituisce la lista dei parametri previsti per il tipo di widget.
	 * @return La lista di parametri in oggetti del tipo WidgetTypeParameter.
	 */
	public List<WidgetTypeParameter> getTypeParameters() {
		return _parameters;
	}

	/**
	 * Imposta la lista dei parametri previsti per il tipo di widget.
	 * La lista deve essere composta da oggetti del tipo WidgetTypeParameter.
	 * @param typeParameters The parameters to set.
	 */
	public void setTypeParameters(List<WidgetTypeParameter> typeParameters) {
		this._parameters = typeParameters;
	}
	
	public boolean hasParameter(String paramName) {
		if (null == this.getTypeParameters()) {
			return false;
		}
		boolean startWith = false;
		boolean endWith = false;
		if (paramName.endsWith("%")) {
			paramName = paramName.substring(0, paramName.length()-1);
			startWith = true;
		}
		if (paramName.startsWith("%")) {
			paramName = paramName.substring(1);
			endWith = true;
		}
		for (int i = 0; i < this.getTypeParameters().size(); i++) {
			WidgetTypeParameter param = this.getTypeParameters().get(i);
			String name = (null != param) ? param.getName() : null;
			if (null == name) {
				continue;
			}
			if (startWith && endWith && name.contains(paramName)) {
				return true;
			} else if (startWith && name.startsWith(paramName)) {
				return true;
			} else if (endWith && name.endsWith(paramName)) {
				return true;
			} else if (paramName.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Restituisce il nome della action specifica che gestisce questo tipo di widget.
	 * @return Il nome della action specifica, null se non vi è nessun action specifica.
	 */
	public String getAction() {
		return _action;
	}

	/**
	 * Setta il nome della action specifica che gestisce questo tipo di widget.
	 * @param action Il nome della action specifica.
	 */
	public void setAction(String action) {
		this._action = action;
	}
	
	/**
	 * Return the code of the plugin owner of widget type.
	 * The field is null if the showlet type belong to Entando Core.
	 * @return The plugin code.
	 */
	public String getPluginCode() {
		return _pluginCode;
	}
	
	/**
	 * Set the code of the plugin owner of widget type.
	 * @param pluginCode The plugin code. 
	 */
	public void setPluginCode(String pluginCode) {
		this._pluginCode = pluginCode;
	}
	
	protected String getParentTypeCode() {
		return _parentTypeCode;
	}
	protected void setParentTypeCode(String parentTypeCode) {
		this._parentTypeCode = parentTypeCode;
	}
	
	public WidgetType getParentType() {
		return _parentType;
	}
	public void setParentType(WidgetType parentType) {
		this._parentType = parentType;
		if (null != parentType) {
			this.setParentTypeCode(parentType.getCode());
		}
	}
	
	public ApsProperties getConfig() {
		return _config;
	}
	public void setConfig(ApsProperties config) {
		this._config = config;
	}
	
	public boolean isLogic() {
		return (null != this.getParentType());
	}
	
	public boolean isUserType() {
		return (this.isLogic() && !this.isLocked() || (!this.isLogic() && !this.isLocked()));
	}
	
	public boolean isLocked() {
		return _locked;
	}
	public void setLocked(boolean locked) {
		this._locked = locked;
	}
	
	public String getMainGroup() {
		return _mainGroup;
	}
	public void setMainGroup(String mainGroup) {
		this._mainGroup = mainGroup;
	}
	
	public String getJspPath() {
		WidgetType widgetType = (this.isLogic()) ? this.getParentType(): this;
		return getJspPath(widgetType.getCode(), widgetType.getPluginCode());
	}
	
	public static String getJspPath(String code, String pluginCode) {
		StringBuilder jspPath = new StringBuilder("/WEB-INF/");
		boolean isWidgetPlugin = (null != pluginCode && pluginCode.trim().length() > 0);
		if (isWidgetPlugin) {
			jspPath.append("plugins/").append(pluginCode.trim()).append("/");
		}
		jspPath.append(WIDGET_LOCATION).append(code).append(".jsp");
		return jspPath.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_action == null) ? 0 : _action.hashCode());
		result = prime * result + ((_code == null) ? 0 : _code.hashCode());
		result = prime * result + ((_config == null) ? 0 : _config.hashCode());
		result = prime * result + (_locked ? 1231 : 1237);
		result = prime * result + ((_mainGroup == null) ? 0 : _mainGroup.hashCode());
		result = prime * result + ((_parameters == null) ? 0 : _parameters.hashCode());
		result = prime * result + ((_parentType == null) ? 0 : _parentType.hashCode());
		result = prime * result + ((_parentTypeCode == null) ? 0 : _parentTypeCode.hashCode());
		result = prime * result + ((_pluginCode == null) ? 0 : _pluginCode.hashCode());
		result = prime * result + ((_titles == null) ? 0 : _titles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WidgetType other = (WidgetType) obj;
		if (_action == null) {
			if (other._action != null)
				return false;
		} else if (!_action.equals(other._action))
			return false;
		if (_code == null) {
			if (other._code != null)
				return false;
		} else if (!_code.equals(other._code))
			return false;
		if (_config == null) {
			if (other._config != null)
				return false;
		} else if (!_config.equals(other._config))
			return false;
		if (_locked != other._locked)
			return false;
		if (_mainGroup == null) {
			if (other._mainGroup != null)
				return false;
		} else if (!_mainGroup.equals(other._mainGroup))
			return false;
		if (_parameters == null) {
			if (other._parameters != null)
				return false;
		} else if (!_parameters.equals(other._parameters))
			return false;
		if (_parentType == null) {
			if (other._parentType != null)
				return false;
		} else if (!_parentType.equals(other._parentType))
			return false;
		if (_parentTypeCode == null) {
			if (other._parentTypeCode != null)
				return false;
		} else if (!_parentTypeCode.equals(other._parentTypeCode))
			return false;
		if (_pluginCode == null) {
			if (other._pluginCode != null)
				return false;
		} else if (!_pluginCode.equals(other._pluginCode))
			return false;
		if (_titles == null) {
			if (other._titles != null)
				return false;
		} else if (!_titles.equals(other._titles))
			return false;
		return true;
	}

	/**
	 * Il codice del tipo di widget.
	 */
	private String _code;
	
	private ApsProperties _titles;
	
	/**
	 * La lista dei parametri previsti per il tipo di widget.
	 */
	private List<WidgetTypeParameter> _parameters;
	
	/**
	 * Il nome della action specifica che gestisce questo tipo di widget.
	 * null se non vi è nessun action specifica.
	 */
	private String _action;
	
	/**
	 * The code of the plugin owner of widget type.
	 */
	private String _pluginCode;
	
	private String _parentTypeCode;
	
	private WidgetType _parentType;
	
	private ApsProperties _config;
	
	private boolean _locked;
	
	private String _mainGroup;
	
	public final static String WIDGET_LOCATION = "aps/jsp/widgets/";
	
}