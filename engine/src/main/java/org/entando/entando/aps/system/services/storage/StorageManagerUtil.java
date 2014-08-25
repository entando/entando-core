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
package org.entando.entando.aps.system.services.storage;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StorageManagerUtil {

	private static final Logger _logger =  LoggerFactory.getLogger(StorageManagerUtil.class);
	
	public static boolean isValidFilename(String fullname) {
		if (StringUtils.isBlank(fullname)) return false;
		String basename = FilenameUtils.getBaseName(fullname);
		String extension = FilenameUtils.getExtension(fullname);
		return isValidFilename(basename, extension);
	}
	
	public static boolean isValidFilename(String basename, String extension) {
		if (StringUtils.isBlank(basename)) return false;
		if (!isValidPath(basename)) return false;
		if (!isValidFilenameNoExtension(basename)) return false;
		if (StringUtils.isNotBlank(extension)) {
			if (!isValidExtension(extension)) return false;
		}
		return true;
	}
	
	public static boolean isValidFilenameNoExtension(String basename) {
		if (StringUtils.isBlank(basename)) return false;
		Pattern pattern = Pattern.compile(REGEXP_FILE_BASENAME);
		Matcher matcher = pattern.matcher(basename);
		return matcher.matches();
	}

	public static boolean isValidDirName(String basename) {
		if (StringUtils.isBlank(basename)) return true;
		if (!isValidPath(basename)) return false;
		Pattern pattern = Pattern.compile(REGEXP_DIR);
		Matcher matcher = pattern.matcher(basename);
		boolean check = matcher.matches();
		if (!check) return check;
		return endWithParentDir(basename);
	}
	
	public static boolean isValidExtension(String extension) {
		if (StringUtils.isBlank(extension)) return false;
		Pattern pattern = Pattern.compile(REGEXP_FILE_EXTENSION);
		Matcher matcher = pattern.matcher(extension);
		return matcher.matches();
	}
	
	public static boolean isValidPath(String path) {
		boolean check = true;
		if (StringUtils.isBlank(path)) return check;
		if (path.contains("../") 
				|| path.contains("%2e%2e%2f") 
				|| path.contains("..%2f") 
				|| path.contains(".."+File.separator) 
				|| path.contains("%2e%2e/") 
				|| path.contains("%2e%2e"+File.separator)) {
			_logger.info("Attack avoided - requested path {}", path);
			return check = false;
		}
		return check;
	}

	private static boolean endWithParentDir(String path) {
		boolean check = true;
		if (StringUtils.isBlank(path)) return check;
		if (path.endsWith("..") 
				|| path.endsWith("..") 
				|| path.endsWith("%2e%2e")) {
			_logger.info("Attack avoided - requested path {}", path);
			return check = false;
		}
		return check;
	}
	
	private static final String REGEXP_FILE_EXTENSION = "([\\w|\\-]+?$)";
	private static final String REGEXP_FILE_BASENAME = "(^[\\w|\\.|\\-|\\_| ]+?)";
	private static final String REGEXP_DIR = "(^[\\w|\\.|\\-|\\_|/| ]+?)";
}
