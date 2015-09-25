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
package com.agiletec.apsadmin.system.conversion;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.XWorkException;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 * @author S.Loru
 */
public class ApsTimestampTypeConverter extends StrutsTypeConverter {
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toType) {
		if (null == values) return null;
		if (values.length > 1) {
			throw new XWorkException("Multiple values");
		}
		Date result = null;
		String value = values[0];
        if (value instanceof String && value != null && ((String) value).length() > 0) {
            String sa = (String) value;
            DateFormat df = new SimpleDateFormat(ApsAdminSystemConstants.CALENDAR_TIMESTAMP_PATTERN);
            try {
                df.setLenient(false); // let's use strict parsing (XW-341)
                result = df.parse(sa);
                if (!(Date.class == toType)) {
                    try {
                        Constructor constructor = toType.getConstructor(new Class[]{long.class});
                        return constructor.newInstance(new Object[]{Long.valueOf(result.getTime())});
                    } catch (Exception e) {
                        throw new XWorkException("Couldn't create class " + toType + " using default (long) constructor", e);
                    }
                }
            } catch (ParseException e) {
                throw new XWorkException("Could not parse date", e);
            }
        }
        return result;
	}
	
	@Override
	public String convertToString(Map context, Object object) {
		String dateString = null;
		if (null != object) {
			dateString = DateConverter.getFormattedDate((Date)object, ApsAdminSystemConstants.CALENDAR_TIMESTAMP_PATTERN); 
		}
		return dateString;
	}
	
}
