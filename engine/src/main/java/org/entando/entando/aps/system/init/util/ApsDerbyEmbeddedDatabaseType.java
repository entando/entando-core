/*
 * Copyright 2015-Present Entando S.r.l. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.init.util;

import com.j256.ormlite.db.DerbyEmbeddedDatabaseType;

/**
 * @author E.Santoboni
 */
public class ApsDerbyEmbeddedDatabaseType extends DerbyEmbeddedDatabaseType {

    @Override
    public void appendEscapedEntityName(StringBuilder sb, String name) {
        sb.append(" ").append(name.toUpperCase()).append(" ");
    }

    @Override
    protected void appendCharType(StringBuilder sb, int fieldWidth) {
        sb.append("VARCHAR(1)");
    }

    @Override
    protected void appendLongStringType(StringBuilder sb, int fieldWidth) {
        sb.append("CLOB");
    }

}
