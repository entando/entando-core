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
package com.agiletec.apsadmin.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author E.Santoboni
 */
public class CheckFormatUtil {
	
    /**
	 * Controlla la validità della stringa rappresentante un numero. 
     * Restituisce true nel caso che la stringa immessa 
     * corrisponda ad un numero, false in caso contrario.
     * @param numberString La stringa da controllare.
     * @return true nel caso che la stringa immessa 
     * corrisponda ad un numero, false in caso contrario.
     */
    public static boolean isValidNumber(String numberString) {
    	boolean validate = false;
		if (numberString != null && numberString.length()>0) {
			Pattern pattern = Pattern.compile("\\d+");
			Matcher matcher = pattern.matcher(numberString.trim());
			validate = matcher.matches();
		}
		return validate;
	}
    
    /**
	 * Controlla la validità della stringa rappresentante una data. 
	 * Restituisce true nel caso che la stringa immessa 
     * corrisponda ad una data nel formato dd/MM/yyyy, false in caso contrario.
	 * @param dateString La stringa rappresentante una data. 
	 * @return true nel caso che la stringa immessa 
     * corrisponda ad una data nel formato dd/MM/yyyy, false in caso contrario.
	 */
    public static boolean isValidDate(String dateString) {
		return isValidDate(dateString, "dd/MM/yyyy");
	}
	
	public static boolean isValidDate(String dateString, String dateFormat) {
		if (dateString != null && (dateString.length() > 0)) {
			try {
				DateFormat df = new SimpleDateFormat(dateFormat);
				df.setLenient(false);
				df.parse(dateString);
				return true;
			} catch (ParseException e) {
				return false;
			}
		}
		return false;
	}
	
}