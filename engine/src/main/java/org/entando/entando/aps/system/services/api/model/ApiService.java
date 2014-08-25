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
package org.entando.entando.aps.system.services.api.model;

import java.io.Serializable;

import com.agiletec.aps.util.ApsProperties;

/**
 * The representation of a service
 * @author E.Santoboni
 */
public class ApiService implements Serializable {

	protected ApiService() {}

	public ApiService(String key, ApsProperties description, ApiMethod master, ApsProperties parameters,
			String[] freeParameters, String tag, boolean isPublic, boolean isActive, boolean isMyEntando) {
		this.setKey(key);
		this.setDescription(description);
		this.setMaster(master);
		this.setParameters(parameters);
		this.setFreeParameters(freeParameters);
		this.setTag(tag);
		this.setHidden(!isPublic);
		this.setActive(isActive);
		this.setMyEntando(isMyEntando);
	}
	
	@Override
	public ApiService clone() {
		ApiService clone = new ApiService();
		clone.setDescription(this.getDescription());
		if (null != this.getFreeParameters()) {
			String[] freeParameters = new String[this.getFreeParameters().length];
			for (int i = 0; i < this.getFreeParameters().length; i++) {
				freeParameters[i] = this.getFreeParameters()[i];
			}
			clone.setFreeParameters(freeParameters);
		}
		clone.setKey(this.getKey());
		clone.setMaster(this.getMaster().clone());
		if (null != this.getParameters()) {
			clone.setParameters(this.getParameters().clone());
		}
		clone.setTag(this.getTag());
		clone.setPublicService(this.isPublicService());
		clone.setActive(this.isActive());
		clone.setMyEntando(this.isMyEntando());
		clone.setRequiredAuth(this.getRequiredAuth());
		clone.setRequiredGroup(this.getRequiredGroup());
		clone.setRequiredPermission(this.getRequiredPermission());
		return clone;
	}

	public String getKey() {
		return _key;
	}
	protected void setKey(String key) {
		this._key = key;
	}
	
	public ApsProperties getDescription() {
		return _description;
	}
	protected void setDescription(ApsProperties description) {
		this._description = description;
	}
	
	public ApsProperties getParameters() {
		return _parameters;
	}
	public void setParameters(ApsProperties parameters) {
		this._parameters = parameters;
	}
	
	public String[] getFreeParameters() {
		return _freeParameters;
	}
	protected void setFreeParameters(String[] freeParameters) {
		this._freeParameters = freeParameters;
	}
	
	public boolean isFreeParameter(String paramName) {
		if (null == this.getFreeParameters() || null == paramName) {
			return false;
		}
		for (int i = 0; i < this.getFreeParameters().length; i++) {
			String parameter = this.getFreeParameters()[i];
			if (parameter.equals(paramName)) {
				return true;
			}
		}
		return false;
	}
	
	public ApiMethod getMaster() {
		return _master;
	}
	protected void setMaster(ApiMethod master) {
		this._master = master;
	}
	
	public String getTag() {
		return _tag;
	}
	protected void setTag(String tag) {
		this._tag = tag;
	}
	
	@Deprecated
	public boolean isPublicService() {
		return !this.isHidden();
	}
	@Deprecated
	public void setPublicService(boolean publicService) {
		this.setHidden(!publicService);
	}
	
	public boolean isHidden() {
		return _hidden;
	}
	public void setHidden(boolean hidden) {
		this._hidden = hidden;
	}
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	public boolean isMyEntando() {
		return _myEntando;
	}
	protected void setMyEntando(boolean myEntando) {
		this._myEntando = myEntando;
	}
	
    public Boolean getRequiredAuth() {
		if (null != this.getRequiredGroup() || null != this.getRequiredPermission()) {
			return true;
		}
        if (null == this._requiredAuth) return false;
        return _requiredAuth;
    }
    public void setRequiredAuth(Boolean requiredAuth) {
        this._requiredAuth = requiredAuth;
    }
    
	public String getRequiredGroup() {
		return _requiredGroup;
	}
	public void setRequiredGroup(String requiredGroup) {
		this._requiredGroup = requiredGroup;
	}
	
	public String getRequiredPermission() {
		return _requiredPermission;
	}
	public void setRequiredPermission(String requiredPermission) {
		this._requiredPermission = requiredPermission;
	}
	
	private String _key;
	private ApsProperties _description;
	private ApiMethod _master;
	private ApsProperties _parameters;
	private String[] _freeParameters;
	private String _tag;
	private boolean _hidden;
	private boolean _active;
	private boolean _myEntando;
	
	private Boolean _requiredAuth;
	private String _requiredPermission;
	private String _requiredGroup;
	
}