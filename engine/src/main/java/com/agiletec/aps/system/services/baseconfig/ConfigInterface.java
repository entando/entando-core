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
package com.agiletec.aps.system.services.baseconfig;

import com.agiletec.aps.system.exception.ApsSystemException;
import java.util.Map;
import java.util.Properties;

/**
 * Interfaccia per il servizio di configurazione.
 *
 * @author M.Diana - E.Santoboni
 */
public interface ConfigInterface {

    public static final String ALGO_TYPE_PARAM_NAME = "algo.argon2.type";
    public static final String ALGO_HASH_LENGTH_PARAM_NAME = "algo.argon2.hash.length";
    public static final String ALGO_SALT_LENGTH_PARAM_NAME = "algo.argon2.salt.length";
    public static final String ALGO_ITERATIONS_PARAM_NAME = "algo.argon2.iterations";
    public static final String ALGO_MEMORY_PARAM_NAME = "algo.argon2.memory";
    public static final String ALGO_PARALLELISM_PARAM_NAME = "algo.argon2.parallelism";
    public static final String ALGO_DEFAULT_KEY = "algo.default.key";
    public static final String LEGACY_PASSWORDS_UPDATED = "legacyPasswordsUpdated";

    public static final String CORS_ALLOWED_ORIGIN = "CORS_ACCESS_CONTROL_ALLOW_ORIGIN";
    public static final String CORS_ALLOWED_HEADERS = "CORS_ACCESS_CONTROL_ALLOW_HEADERS";
    public static final String CORS_ALLOWED_METHODS = "CORS_ACCESS_CONTROL_ALLOW_METHODS";
    public static final String CORS_ALLOWED_CREDENTIALS = "CORS_ACCESS_CONTROL_ALLOW_CREDENTIALS";
    public static final String CORS_MAX_AGE = "CORS_ACCESS_CONTROL_MAX_AGE";

    /**
     * Restituisce una voce di configurazione. La voce è un elemento di testo
     * che può essere complesso (es. XML). I valori restituiti sono relativi
     * alla versione di configurazione con cui è stato avviato il sistema.
     *
     * @param name Il codice della voce da restituire.
     * @return Il testo della voce di configurazione.
     */
    public String getConfigItem(String name);

    /**
     * Aggiorna una voce di configurazione. La voce è un elemento di testo che
     * può essere complesso (es. XML).
     *
     * @param name Il codice della voceda aggiornare.
     * @param config Il testo della voce di configurazione da aggiornare.
     * @throws ApsSystemException In caso di errore nell'aggiornamento
     */
    public void updateConfigItem(String name, String config) throws ApsSystemException;

    /**
     * Retrieves a configuration parameter. A parameter is a simple {@code String}.
     *
     * @param name Parameter key
     * @return Parameter value
     * @deprecated Use {@code getProperty method instead}
     */
    public String getParam(String name);

    /**
     * Retrieves property from Environment variable.
     * If not present fallbacks to {@code systemParams}.
     * Example: {@code MY_SYSTEM_ENV_VARIABLE} fallbacks to {@code systemParams} with key {@code my.system.env.variable}
     *
     * @param name Parameter key
     * @return Parameter value
     */
    public String getProperty(String name);

    /**
     * Retrieves property from Environment variable.
     * If not present fallbacks to manually provided {@code Property} property map.
     * Example: {@code MY_SYSTEM_ENV_VARIABLE} fallbacks to properties map with key {@code my.system.env.variable}
     *
     * @param properties Fallback property map
     * @param name Parameter key
     * @return Parameter value
     */
    public static String getProperty(Properties properties, String name) {
        String property = System.getenv(name);
        return property == null ? properties.getProperty(name.replace("_", ".").toLowerCase()) : property;
    }

    public void updateParam(String name, String value) throws ApsSystemException;

    public void updateParams(Map<String, String> params) throws ApsSystemException;

    public boolean areLegacyPasswordsUpdated();

}
