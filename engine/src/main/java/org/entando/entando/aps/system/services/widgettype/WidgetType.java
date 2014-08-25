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