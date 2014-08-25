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
package org.entando.entando.aps.system.services.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;

import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;

/**
 * @author E.Santoboni
 */
public class LocalStorageManager implements IStorageManager {

	private static final Logger _logger =  LoggerFactory.getLogger(LocalStorageManager.class);
	
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void saveFile(String subPath, boolean isProtectedResource, InputStream is) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		try {
			File dir = new File(fullPath).getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			byte[] buffer = new byte[1024];
			int length = -1;
			FileOutputStream outStream = new FileOutputStream(fullPath);
			while ((length = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
				outStream.flush();
			}
			outStream.close();
			is.close();
		} catch (Throwable t) {
			_logger.error("Error on saving file", t);
			throw new ApsSystemException("Error on saving file", t);
		}
	}
	
	@Override
	public boolean deleteFile(String subPath, boolean isProtectedResource) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		File file = new File(fullPath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	@Override
	public void createDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		File dir = new File(fullPath);
		if (!dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
	}
	
	@Override
	public void deleteDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		File dir = new File(fullPath);
		this.delete(dir);
	}
	
	private boolean delete(File file) throws ApsSystemException {
		if (file.exists()) {
			if (file.isDirectory()) {
				String[] filesName = file.list();
				for (int i = 0; i < filesName.length; i++) {
					File fileToDelete = new File(file.getAbsoluteFile() + File.separator + filesName[i]);
					this.delete(fileToDelete);
				}
				file.delete();
			} else {
				return file.delete();
			}
		}
		return false;
	}
	
	@Override
	public InputStream getStream(String subPath, boolean isProtectedResource) throws ApsSystemException {
		try {
			subPath = (null == subPath)? "" : subPath;
			String fullPath = this.createFullPath(subPath, isProtectedResource);
			File file = new File(fullPath);
			if (file.exists() && !file.isDirectory()) {
				return new FileInputStream(file);
			}
		} catch (Throwable t) {
			_logger.error("Error extracting stream", t);
			throw new ApsSystemException("Error extracting stream", t);
		}
		return null;
	}
	
	@Override
	public boolean exists(String subPath, boolean isProtectedResource) throws ApsSystemException {
		File file = this.getFile(subPath, isProtectedResource);
		return file.exists();
	}
	
	protected File getFile(String subPath, boolean isProtectedResource) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		return new File(fullPath);
	}
	
	@Override
	public String getResourceUrl(String subPath, boolean isProtectedResource) {
		subPath = (null == subPath)? "" : subPath;
		String baseUrl = (!isProtectedResource) ? this.getBaseURL() : this.getProtectedBaseURL();
		return this.createPath(baseUrl, subPath, true);
	}
	
	@Override
	public String readFile(String subPath, boolean isProtectedResource) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		File file = new File(fullPath);
		try {
			return FileUtils.readFileToString(file, CharEncoding.UTF_8);
		} catch (Throwable t) {
			_logger.error("Error reading File with path {}", subPath, t);
			throw new ApsSystemException("Error reading file", t);
		}
	}
	
	@Override
	public void editFile(String subPath, boolean isProtectedResource, InputStream is) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		String tempFilePath = null;
		try {
			File oldFile = new File(fullPath);
			if (oldFile.exists()) {
				String tempDir = System.getProperty("java.io.tmpdir");
				tempFilePath = tempDir + File.separator + subPath;
				FileUtils.moveFile(oldFile, new File(tempFilePath));
			}
			this.saveFile(subPath, isProtectedResource, is);
		} catch (Throwable t) {
			try {
				if (null != tempFilePath) {
					FileUtils.moveFile(new File(tempFilePath), new File(fullPath));
				}
			} catch (Throwable tr) {
				_logger.error("Error restoring File from path {} to path", tempFilePath, fullPath, tr);
			}
			_logger.error("Error writing File with path {}", subPath, t);
			throw new ApsSystemException("Error editing file", t);
		} finally {
			if (null != tempFilePath) {
				new File(tempFilePath).delete();
			}
		}
	}
	
	@Override
	public String[] list(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.list(subPath, isProtectedResource, null);
	}
	
	@Override
	public String[] listDirectory(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.list(subPath, isProtectedResource, true);
	}
	
	@Override
	public String[] listFile(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.list(subPath, isProtectedResource, false);
	}
	
	private String[] list(String subPath, boolean isProtectedResource, Boolean searchDirectory) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		File directory = new File(fullPath);
		if (directory.exists() && directory.isDirectory()) {
			String[] objects = new String[]{};
			String folder = fullPath;
			if (!folder.endsWith("/")) {
				folder += "/";
			}
			String[] contents = directory.list();
			if (null == searchDirectory) {
				objects = contents;
			} else {
				for (int i = 0; i < contents.length; i++) {
					String string = contents[i];
					File file = new File(folder + string);
					if ((file.isDirectory() && searchDirectory) || (!file.isDirectory() && !searchDirectory)) {
						objects = this.addChild(string, objects);
					}
				}
			}
			Arrays.sort(objects);
			return objects;
		}
		return null;
	}
	
	protected String[] addChild(String stringToAdd, String[] objects) {
		int len = objects.length;
		String[] newArray = new String[len + 1];
		for (int i = 0; i < len; i++) {
			newArray[i] = objects[i];
		}
		newArray[len] = stringToAdd;
		return newArray;
	}
	
	private String createFullPath(String subPath, boolean isProtectedResource) {
		subPath = (null == subPath)? "" : subPath;
		String diskRoot = (!isProtectedResource) ? this.getBaseDiskRoot() : this.getProtectedBaseDiskRoot();
		return this.createPath(diskRoot, subPath, false);
	}
	
	private String createPath(String basePath, String subPath, boolean isUrlPath) {
		subPath = (null == subPath)? "" : subPath;
		String separator = (isUrlPath) ? "/" : File.separator;
		boolean baseEndWithSlash = basePath.endsWith(separator);
		boolean subPathStartWithSlash = subPath.startsWith(separator);
		if ((baseEndWithSlash && !subPathStartWithSlash) || (!baseEndWithSlash && subPathStartWithSlash)) {
			return basePath + subPath;
		} else if (!baseEndWithSlash && !subPathStartWithSlash) {
			return basePath + separator + subPath;
		} else {
			String base = basePath.substring(0, basePath.length() - File.separator.length());
			return base + subPath;
		}
	}
	
	@Override
	public BasicFileAttributeView[] listAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.listAttributes(subPath, isProtectedResource, null);
	}
	
	@Override
	public BasicFileAttributeView[] listDirectoryAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.listAttributes(subPath, isProtectedResource, true);
	}
	
	@Override
	public BasicFileAttributeView[] listFileAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException {
		return this.listAttributes(subPath, isProtectedResource, false);
	}
	
	private BasicFileAttributeView[] listAttributes(String subPath, boolean isProtectedResource, Boolean searchDirectory) throws ApsSystemException {
		subPath = (null == subPath)? "" : subPath;
		String fullPath = this.createFullPath(subPath, isProtectedResource);
		File directory = new File(fullPath);
		if (directory.exists() && directory.isDirectory()) {
			BasicFileAttributeView[] objects = new BasicFileAttributeView[]{};
			String folder = fullPath;
			if (!folder.endsWith("/")) {
				folder += "/";
			}
			String[] contents = directory.list();
			for (int i = 0; i < contents.length; i++) {
				String string = contents[i];
				File file = new File(folder + string);
				if (null == searchDirectory || (file.isDirectory() && searchDirectory) || (!file.isDirectory() && !searchDirectory)) {
					BasicFileAttributeView bfav = new BasicFileAttributeView(file);
					objects = this.addChild(bfav, objects);
				}
			}
			Arrays.sort(objects);
			return objects;
		}
		return null;
	}
	
	protected BasicFileAttributeView[] addChild(BasicFileAttributeView elementToAdd, BasicFileAttributeView[] objects) {
		int len = objects.length;
		BasicFileAttributeView[] newArray = new BasicFileAttributeView[len + 1];
		for (int i = 0; i < len; i++) {
			newArray[i] = objects[i];
		}
		newArray[len] = elementToAdd;
		return newArray;
	}
	
	@Override
	public BasicFileAttributeView getAttributes(String subPath, boolean isProtectedResource) throws ApsSystemException {
		File file = this.getFile(subPath, isProtectedResource);
		if (!file.exists()) {
			return null;
		} else {
			return new BasicFileAttributeView(file);
		}
	}
	
	protected String getBaseURL() {
		return _baseURL;
	}
	public void setBaseURL(String baseURL) {
		this._baseURL = baseURL;
	}
	
	protected String getBaseDiskRoot() {
		return _baseDiskRoot;
	}
	public void setBaseDiskRoot(String baseDiskRoot) {
		this._baseDiskRoot = baseDiskRoot;
	}
	
	protected String getProtectedBaseDiskRoot() {
		return _protectedBaseDiskRoot;
	}
	public void setProtectedBaseDiskRoot(String protBaseDiskRoot) {
		this._protectedBaseDiskRoot = protBaseDiskRoot;
	}

	protected String getProtectedBaseURL() {
		return _protectedBaseURL;
	}
	public void setProtectedBaseURL(String protBaseURL) {
		this._protectedBaseURL = protBaseURL;
	}
	
	public void setAllowedEditExtensions(String allowedEditExtensions) {
		this._allowedEditExtensions = allowedEditExtensions;
	}
	public String getAllowedEditExtensions() {
		return _allowedEditExtensions;
	}
	
	private String _baseURL;
	private String _baseDiskRoot;
	private String _protectedBaseDiskRoot;
	private String _protectedBaseURL;
	private String _allowedEditExtensions;
	
}
