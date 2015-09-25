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
package com.agiletec.aps.system.services.role;

import java.io.Serializable;

/**
 * Rappresentazione di un permesso, per il sistema di autorizzazione.
 * @author M.Diana
 */
public class Permission implements Comparable, Serializable {
	
	/**
	 * Restituisce il nome del permesso.
	 * @return Il nome del permesso.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Setta il nome del permesso.
	 * @param name Il nome del permesso.
	 */
	public void setName(String name) {
		this._name = name;
	}

	/**
	 * Restituisce la descrizione del permesso.
	 * @return La descrizione del permesso.
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Setta la descrizione del permesso.
	 * @param description La descrizione del permesso.
	 */
	public void setDescription(String description) {
		this._description = description;
	}
	
	@Override
	public int compareTo(Object permission) {
		return this.getName().compareTo(((Permission)permission).getName());
	}

	private String _name;
	private String _description;

	/**
	 * Nome del permesso di Super User
	 */
	public static final String SUPERUSER = "superuser";

	/**
	 * Nome del permesso base per l'accesso all'area di amministrazione
	 */
	public static final String BACKOFFICE = "enterBackend";

	public static final String CONTENT_EDITOR = "editContents";
	public static final String CONTENT_SUPERVISOR = "validateContents";
	
	public static final String VIEW_USERS = "viewUsers";
	public static final String MANAGE_USERS = "editUsers";
	public static final String MANAGE_USER_PROFILES = "editUserProfile";
	
	public static final String MANAGE_RESOURCES = "manageResources";
	public static final String MANAGE_PAGES = "managePages";
	public static final String MANAGE_CATEGORIES = "manageCategories";
	public static final String ENTER_BACKEND = "enterBackend";
	
	/**
	 * Nome del permesso di Supervisore contenuto
	 * @deprecated use CONTENT_SUPERVISOR
	 */
	public static final String SUPERVISOR = CONTENT_SUPERVISOR;
	
	/**
	 * Nome del permesso di configurazione pagine portale
	 * @deprecated user MANAGE_PAGES
	 */
	public static final String CONFIG = MANAGE_PAGES;
	
}
