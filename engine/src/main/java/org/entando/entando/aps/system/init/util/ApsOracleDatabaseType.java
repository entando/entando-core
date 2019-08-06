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
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import java.sql.SQLException;

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
    protected void appendLongStringType(StringBuilder sb, int fieldWidth) {
        sb.append("CLOB");
    }

    @Override
    protected void appendDateType(StringBuilder sb, int fieldWidth) {
        sb.append("TIMESTAMP(2)");
    }

    @Override
    public void appendColumnArg(String tableName, StringBuilder sb, FieldType fieldType, List<String> additionalArgs,
            List<String> statementsBefore, List<String> statementsAfter, List<String> queriesAfter) throws SQLException {
        DataPersister dataPersister = fieldType.getDataPersister();
        if (!dataPersister.getSqlType().equals(SqlType.INTEGER)) {
            super.appendColumnArg(tableName, sb, fieldType, additionalArgs, statementsBefore, statementsAfter, queriesAfter);
            return;
        }
        appendEscapedEntityName(sb, fieldType.getColumnName());
        sb.append(' ');
        // first try the per-field width
        int fieldWidth = fieldType.getWidth();
        if (fieldWidth == 0) {
            // next try the per-data-type width
            fieldWidth = dataPersister.getDefaultWidth();
        }
        if (!fieldType.isGeneratedId()) {
            sb.append("INTEGER");
        } else {
            sb.append("NUMBER");
        }
        sb.append(' ');
        /*
		 * NOTE: the configure id methods must be in this order since isGeneratedIdSequence is also isGeneratedId and
		 * isId. isGeneratedId is also isId.
         */
        if (fieldType.isGeneratedIdSequence() && !fieldType.isSelfGeneratedId()) {
            configureGeneratedIdSequence(sb, fieldType, statementsBefore, additionalArgs, statementsAfter, queriesAfter);
        } else if (fieldType.isGeneratedId() && !fieldType.isSelfGeneratedId()) {
            configureGeneratedId(tableName, sb, fieldType, statementsBefore, statementsAfter, additionalArgs,
                    queriesAfter);
        } else if (fieldType.isId()) {
            configureId(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
        }
        // if we have a generated-id then neither the not-null nor the default make sense and cause syntax errors
        if (!fieldType.isGeneratedId()) {
            Object defaultValue = fieldType.getDefaultValue();
            if (defaultValue != null) {
                sb.append("DEFAULT ");
                appendDefaultValue(sb, fieldType, defaultValue);
                sb.append(' ');
            }
            if (fieldType.isCanBeNull()) {
                appendCanBeNull(sb, fieldType);
            } else {
                sb.append("NOT NULL ");
            }
            if (fieldType.isUnique()) {
                addSingleUnique(sb, fieldType, additionalArgs, statementsAfter);
            }
        }
    }

    private void appendDefaultValue(StringBuilder sb, FieldType fieldType, Object defaultValue) {
        if (fieldType.isEscapedDefaultValue()) {
            appendEscapedWord(sb, defaultValue.toString());
        } else {
            sb.append(defaultValue);
        }
    }

    private void appendCanBeNull(StringBuilder sb, FieldType fieldType) {
        // default is a noop
    }

    private void addSingleUnique(StringBuilder sb, FieldType fieldType, List<String> additionalArgs,
            List<String> statementsAfter) {
        StringBuilder alterSb = new StringBuilder();
        alterSb.append(" UNIQUE (");
        appendEscapedEntityName(alterSb, fieldType.getColumnName());
        alterSb.append(")");
        additionalArgs.add(alterSb.toString());
    }

    //@Override
    protected void configureGeneratedIdSequence(StringBuilder sb, FieldType fieldType, List<String> statementsBefore,
            List<String> additionalArgs, List<String> statementsAfter, List<String> queriesAfter) {
        String seqName = fieldType.getGeneratedIdSequence();

        if (seqName.length() > 30) {
            seqName = org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(20).toUpperCase() + "_SEQ";
        }
        String triggerName = fieldType.getTableName() + "_TRG";
        if (triggerName.length() > 30) {
            triggerName = org.apache.commons.lang3.RandomStringUtils.randomAlphabetic(20).toUpperCase() + "_TRG";
        }
        String trigger = "CREATE OR REPLACE TRIGGER " + triggerName + " "
                + "BEFORE INSERT ON " + fieldType.getTableName() + " FOR EACH ROW "
                + "BEGIN SELECT " + seqName + ".NEXTVAL "
                + "INTO :new." + fieldType.getColumnName() + " FROM dual; END;";
        statementsAfter.add(trigger);
        // needs to match dropColumnArg()
        StringBuilder seqSb = new StringBuilder(64);
        seqSb.append("CREATE SEQUENCE ");
        // when it is created, it needs to be escaped specially
        appendEscapedEntityName(seqSb, seqName);
        statementsBefore.add(seqSb.toString());

        configureId(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
    }

    /*
    CREATE OR REPLACE TRIGGER authrolepermissions
BEFORE INSERT ON authrolepermissions
FOR EACH ROW

BEGIN
  SELECT authrolepermissions_id_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
     */

 /*
    @Override
    protected void configureGeneratedIdSequence(StringBuilder sb, FieldType fieldType, List<String> statementsBefore,
            List<String> additionalArgs, List<String> queriesAfter) {
        sb.append(" GENERATED ALWAYS as IDENTITY ");
    }
     */
}
