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
package com.agiletec.aps.system.services.user;

import java.util.Calendar;
import java.util.Date;

/**
 * Rappresentazione di un'utente.
 * @author M.Diana - E.Santoboni
 */
public class User extends AbstractUser {
	
	@Override
	@Deprecated
	public boolean isJapsUser() {
		return this.isEntandoUser();
	}
	
	@Override
	public boolean isEntandoUser() {
		return true;
	}
	
	/**
	 * Crea una copia dell'oggetto user e lo restituisce.
	 * @return Oggetto di tipo User clonato.
	 */
	@Override
	public Object clone() {
		User cl = new User();
		cl.setUsername(this.getUsername());
		cl.setPassword("");
		cl.setAuthorities(this.getAuthorities());
		return cl;
	}

	public Date getCreationDate() {
		return _creationDate;
	}
	protected void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}

	public Date getLastAccess() {
		return _lastAccess;
	}
	public void setLastAccess(Date lastAccess) {
		this._lastAccess = lastAccess;
	}

	public Date getLastPasswordChange() {
		return _lastPasswordChange;
	}
	public void setLastPasswordChange(Date lastPasswordChange) {
		this._lastPasswordChange = lastPasswordChange;
	}

	@Override
	public boolean isDisabled() {
		return _disabled;
	}
	public void setDisabled(boolean disabled) {
		this._disabled = disabled;
	}

	protected boolean isCheckCredentials() {
		return _checkCredentials;
	}
	protected void setCheckCredentials(boolean checkCredentials) {
		this._checkCredentials = checkCredentials;
	}

	public int getMaxMonthsSinceLastAccess() {
		return _maxMonthsSinceLastAccess;
	}
	public void setMaxMonthsSinceLastAccess(int maxMonthsSinceLastAccess) {
		this._maxMonthsSinceLastAccess = maxMonthsSinceLastAccess;
	}

	public int getMaxMonthsSinceLastPasswordChange() {
		return _maxMonthsSinceLastPasswordChange;
	}
	public void setMaxMonthsSinceLastPasswordChange(int maxMonthsSinceLastPasswordChange) {
		this._maxMonthsSinceLastPasswordChange = maxMonthsSinceLastPasswordChange;
	}

	@Override
	public boolean isAccountNotExpired() {
		if (!this.isCheckCredentials()) return true;
		int maxDelay = this.getMaxMonthsSinceLastAccess();
		if (maxDelay > 0) {
			Date dateForCheck = (this.getLastAccess() != null ? this.getLastAccess() : this.getCreationDate());
			if (null != dateForCheck) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateForCheck);
				cal.add(Calendar.MONTH, maxDelay);
				Date expirationDate = cal.getTime();
				return expirationDate.after(new Date());
			}
		}
		return super.isAccountNotExpired();
	}

	@Override
	public boolean isCredentialsNotExpired() {
		if (!this.isCheckCredentials()) return true;
		int maxDelay = this.getMaxMonthsSinceLastPasswordChange();
		if (maxDelay > 0) {
			Date dateForCheck = (this.getLastPasswordChange() != null ? this.getLastPasswordChange() : this.getCreationDate());
			if (null != dateForCheck) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateForCheck);
				cal.add(Calendar.MONTH, maxDelay);
				Date expirationDate = cal.getTime();
				return expirationDate.after(new Date());
			}
		}
		return super.isCredentialsNotExpired();
	}
	
	private Date _creationDate;//CAMBIARE IN REGISTRATION DATE
	private Date _lastAccess;
	private Date _lastPasswordChange;

	private boolean _disabled;

	private boolean _checkCredentials;

	private int _maxMonthsSinceLastAccess = -1;
	private int _maxMonthsSinceLastPasswordChange = -1;

}
