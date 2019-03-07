/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.util.crypto;

import java.security.Key;

public abstract class KeyHolder {

    private Key key;

    public synchronized void setKey(String keyString) {
        key = getKey(keyString);
    }

    protected synchronized Key getKey() {
        if (key == null) {
            key = getKey(System.getProperties().getProperty("key.string.encryption"));
        }
        return key;
    }

    protected abstract Key getKey(String keyString);
}
