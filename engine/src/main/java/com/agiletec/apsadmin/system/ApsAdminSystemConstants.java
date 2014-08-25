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
package com.agiletec.apsadmin.system;

/**
 * Interfaccia con le principali costanti di sistema di amministrazione.
 * @author E.Santoboni
 */
public interface ApsAdminSystemConstants {
	
	public static final String SHORTCUT_MANAGER = "ShortcutAdminAreaManager";
	
	/**
	 * Identificativo dell'azione di aggiunta nuovo elemento di sistema.
	 */
	public final static int ADD = 1;
	
	/**
	 * Identificativo dell'azione di modifica elemento di sistema.
	 */
	public final static int EDIT = 2;
	
	/**
	 * Identificativo dell'azione di incollare elementi di sistema.
	 */
	public final static int PASTE = 3;
	
	/**
	 * Identificativo dell'azione rimozione elemento di sistema.
	 */
	public final static int DELETE = 4;
	
	/**
	 * Nome del parametro di configurazione (del deployment descriptor web.xml) iniziale di Struts2.
	 */
	public static final String STRUTS2_CONFIG_INIT_PARAM_NAME = "Struts2Config";
	
	public static final String CALENDAR_DATE_PATTERN = "dd/MM/yyyy";
	public static final String CALENDAR_TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss|SSS";
	
	public static final String MOVEMENT_UP_CODE = "UP";
	public static final String MOVEMENT_DOWN_CODE = "DOWN";
	
}