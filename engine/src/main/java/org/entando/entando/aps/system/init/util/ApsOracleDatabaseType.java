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

import java.util.List;

import com.j256.ormlite.db.OracleDatabaseType;
import com.j256.ormlite.field.FieldType;

/**
 * @author E.Santoboni
 */
public class ApsOracleDatabaseType extends OracleDatabaseType {
	
	public final static String DATABASE_NAME = "Oracle";
	
	@Override
	protected void configureId(StringBuilder sb, FieldType fieldType, 
			List<String> statementsBefore, List<String> additionalArgs, List<String> queriesAfter) {
		//nothing to do
	}
	
	@Override
	public void appendEscapedEntityName(StringBuilder sb, String name) {
		sb.append('\"').append(name.toUpperCase()).append('\"');
	}
	
	@Override
	protected void appendLongStringType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
		sb.append("CLOB");
	}
	
//	@Override
//	protected void appendDateType(StringBuilder sb, int fieldWidth) {
//		sb.append("TIMESTAMP(2)");
//	}


}