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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Classe di utilità per la lettura del contenuto di file.
 * @author E.Santoboni
 */
public class FileTextReader {
	
	public static String getText(InputStream is) 
			throws ApsSystemException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		return getText(br);
	}
	
	public static String getText(String filename) 
			throws ApsSystemException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			return getText(br);
		} catch (Throwable t) {
			throw new ApsSystemException("Error reading text", t);
		}
	}
	
	private static String getText(BufferedReader br) throws ApsSystemException {
		String text = null;
		try {
			String lineSep = System.getProperty("line.separator");
			String nextLine = "";
			StringBuffer sb = new StringBuffer();
			while ((nextLine = br.readLine()) != null) {
				sb.append(nextLine);
				sb.append(lineSep);
			}
			text = sb.toString();
		} catch (Throwable t) {
			throw new ApsSystemException("Error reading text", t);
		}
		return text;
	}

}
