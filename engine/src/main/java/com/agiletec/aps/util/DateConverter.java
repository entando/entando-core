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
package com.agiletec.aps.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for date handling. 
 * @author E.Santoboni
 */
public class DateConverter {

	private static final Logger _logger = LoggerFactory.getLogger(DateConverter.class);
	/**
	 * Utility method. Return a formatted string representing the given date
	 * @param date the date object to convert into a string
	 * @param pattern the pattern to handle the date with
	 * @param langCode The lang code
	 * @return the string of the resulting date
	 */
	public static String getFormattedDate(Date date, String pattern, String langCode) {
		if (null == langCode) return getFormattedDate(date, pattern);
		String dateString = "";
		if (null != date) {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern, new Locale(langCode, ""));
			dateString = formatter.format(date);
		}
		return dateString;
	}
	
	/**
	 * Utility method. Return a formatted string representing the given date
	 * @param date the date object to convert into a string
	 * @param pattern the pattern to handle the date with
	 * @return the string of the resulting date
	 */
	public static String getFormattedDate(Date date, String pattern) {
		String dateString = "";
		if (null != date) {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			dateString = formatter.format(date);
		}
		return dateString;
	}
	
	/**
	 * Utility method. Parses the input string to a date object, given a valid pattern DATE_FORMAT. Return null
	 * if the string itself is either null or empty (or if the format is invalid of course)
	 * @param stringData the string representing the date to convert 
	 * @param pattern used to perform the conversion
	 * @return the result of the conversion
	 */
	public static Date parseDate(String stringData, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date data = null;
		try {
			if (null != stringData && stringData.length() > 0) {
				data = format.parse(stringData);
			}
		} catch (ParseException ex) {
			_logger.error("Wrong date format detected : {}", stringData, ex);
		}
		return data;
	}

}
