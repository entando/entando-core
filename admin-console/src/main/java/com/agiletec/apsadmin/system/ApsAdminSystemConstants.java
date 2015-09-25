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