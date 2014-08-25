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
package org.entando.entando.aps.system.services.api.model;

/**
 * @author Frédéric Barmes - E.Santoboni
 */
public class CDataAdapter  {
	
	/**
	 * Check whether a string is a CDATA string
	 * @param string the string to check
	 * @return true if the given strings is a CDATA string
	 */
	public static boolean isCdata(String string) {
		String s = string.trim();
		return (s.startsWith(CDATA_START) && s.endsWith(CDATA_STOP));
	}
	
	/**
	 * Parse a CDATA String.
	 * If is a CDATA, removes leading and trailing string
	 * Otherwise does nothing
	 * @param string the string to parse
	 * @return the parsed string
	 */
	public static String parse(String string)  {
		if (isCdata(string)) {
			StringBuilder sb = new StringBuilder(string.trim());
			sb.replace(0, CDATA_START.length(), "");
			int stopIndex = sb.lastIndexOf(CDATA_STOP);
			sb.replace(stopIndex, (stopIndex + CDATA_STOP.length()),"");
			string = sb.toString();
		}
		return string;
	}
	
	/**
	 * Add CDATA leading and trailing to a string if not already a CDATA
	 * @param string The string to add.
	 * @return The new String
	 */
	public static String print(String string) {
		if (isCdata(string)) {
			return string;
		} else {
			if (null != string && string.trim().length() > 0) {
				return CDATA_START + string + CDATA_STOP;
			}
			return string;
		}
	}
	
	private static final String CDATA_START = "<![CDATA[";
	private static final String CDATA_STOP = "]]>";
	
}