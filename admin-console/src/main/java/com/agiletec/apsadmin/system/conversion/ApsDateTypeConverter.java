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
package com.agiletec.apsadmin.system.conversion;

import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.XWorkException;

/**
 * Class converter for data type property. 
 * The converter uses a pattern fixed by the constant CALENDAR_DATE_PATTERN 
 * of class {@link ApsAdminSystemConstants}
 * @version 1.0
 * @author E.Santoboni
 */
public class ApsDateTypeConverter extends StrutsTypeConverter {
	
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
            DateFormat df = new SimpleDateFormat(ApsAdminSystemConstants.CALENDAR_DATE_PATTERN);
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
			dateString = DateConverter.getFormattedDate((Date)object, ApsAdminSystemConstants.CALENDAR_DATE_PATTERN); 
		}
		return dateString;
	}
	
}
