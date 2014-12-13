/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.init.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class QueryExtractor {
	
	private static final Logger _logger = LoggerFactory.getLogger(QueryExtractor.class);
	
	@Deprecated
	public static String[] extractQueries(String script) throws Throwable {
		return extractInsertQueries(script);
	}
	
	public static String[] extractInsertQueries(String script) throws Throwable {
		String startsWith = "insert into";
		String endsWith = ");";
		return extractQueries(script,startsWith, endsWith);
	}
	
	public static String[] extractDeleteQueries(String script) throws Throwable {
		String startsWith = "delete from";
		String endsWith = ";";
		return extractQueries(script,startsWith, endsWith);
	}
	
	private static String[] extractQueries(String script, String startsWith, String endsWith) throws Throwable {
		if (null == script || script.trim().length() == 0) return null;
		String[] lines = readLines(script.trim());
        if (lines.length == 0) return null;
		return extractQueries(lines, startsWith, endsWith);
	}
	
	private static String[] readLines(String text) throws Throwable {
		InputStream is = null;
		String[] lines = new String[0];
		try {
			is = new ByteArrayInputStream(text.getBytes());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				lines = addChild(lines, strLine);
			}
		} catch (Throwable t) {
			_logger.error("Error reading lines", t);
			throw new ApsSystemException("Error reading lines", t);
		} finally {
			if (null != is) is.close();
		}
		return lines;
	}
	
	private static String[] extractQueries(String[] lines, String startsWith, String endsWith) {
		String[] queries = new String[0];
		StringBuilder builder = new StringBuilder();
		int length = lines.length;
		String lastValuedLine = null;
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			builder.append(line);
			if (line.trim().length() > 0) {
				lastValuedLine = line;
			}
			if ((i+1) < length 
					&& lines[i+1].toLowerCase().trim().startsWith(startsWith) 
					&& (null != lastValuedLine && lastValuedLine.toLowerCase().trim().endsWith(endsWith))) {
				String query = purgeQuery(builder);
				queries = addChild(queries, query);
				lastValuedLine = null;
			} else {
				builder.append("\n");
			}
		}
		String query = purgeQuery(builder);
		queries = addChild(queries, query);
		return queries;
	}
	
	private static String purgeQuery(StringBuilder builder) {
		String query = builder.toString().trim();
		query = query.substring(0, query.length()-1);
		builder.delete(0, builder.length());
		return query;
	}
	
	private static String[] addChild(String[] lines, String newLine) {
		int len = lines.length;
		String[] newChildren = new String[len + 1];
		for (int i = 0; i < len; i++) {
			newChildren[i] = lines[i];
		}
		newChildren[len] = newLine;
		return newChildren;
	}
	
}
