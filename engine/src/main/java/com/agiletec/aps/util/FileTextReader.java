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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Utility class for reading the contents of files.
 * @author E.Santoboni
 */
public class FileTextReader {
	
	public static String getText(InputStream is) throws ApsSystemException {
		return getText(is, null);
	}
	
	public static String getText(InputStream is, String charset) throws ApsSystemException {
		try {
			Reader reader = (null != charset) ? new InputStreamReader(is, charset) : new InputStreamReader(is);
			BufferedReader br = new BufferedReader(reader);
			return getText(br);
		} catch (Throwable t) {
			throw new ApsSystemException("Error reading text", t);
		}
	}
	
	public static String getText(String filename) throws ApsSystemException {
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
			StringBuilder sb = new StringBuilder();
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
