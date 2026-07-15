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
package org.entando.entando.ent.util;

import java.util.ArrayList;
import com.agiletec.aps.util.ApsProperties;

/**
 * Neutralizes stored XSS in short free-text <em>label</em> fields (titles, names,
 * descriptions) by removing the characters that allow HTML element injection.
 */
public final class LabelSanitizer {

    private LabelSanitizer() {
    }

    /**
     * The single quote is intentionally preserved, being legitimate and common in
     * titles/descriptions; all known attribute sinks for these fields are double-quoted.
     * Null-safe and idempotent.
     */
    public static String stripMarkup(String value) {
        return (value == null) ? null
                : value.replace("<", "")
                .replace(">", "")
                .replace("\"", "")
                .replace("&lt;", "")
                .replace("&gt;", "")
                .replace("&quot;", "");
    }

    /**
     * Applies {@link #stripMarkup(String)} to every String value of the given properties
     * in place (e.g. the per-language titles/names maps).
     */
    public static void stripMarkup(ApsProperties properties) {
        if (null == properties) {
            return;
        }
        for (Object key : new ArrayList<>(properties.keySet())) {
            Object value = properties.get(key);
            if (value instanceof String) {
                properties.put(key, stripMarkup((String)value));
            }
        }
    }
}