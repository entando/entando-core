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
package com.agiletec.aps.util;

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