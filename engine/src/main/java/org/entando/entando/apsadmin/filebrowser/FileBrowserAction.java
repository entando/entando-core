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
package org.entando.entando.apsadmin.filebrowser;

import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.storage.BasicFileAttributeView;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.aps.system.services.storage.StorageManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru - E.Santoboni
 */
public class FileBrowserAction extends BaseAction {

	private static final Logger _logger = LoggerFactory.getLogger(FileBrowserAction.class);

	public String list() {
		return SUCCESS;
	}

	public String edit() {
		try {
			String result = this.validateTextFileExtension(this.getFilename());
			if (null != result) return result;

			String validatePath = this.validateFullPath();
			if (null != validatePath) return validatePath;

			String fullPath = this.getCurrentPath() + this.getFilename();
			BasicFileAttributeView fileAttributeView = this.getStorageManager().getAttributes(fullPath, this.getProtectedFolderBoolean());
			if (null == fileAttributeView || fileAttributeView.isDirectory()) {
				return INPUT;
			}
			String text = this.getStorageManager().readFile(fullPath, this.getProtectedFolderBoolean());
			this.setFileText(text);				
			this.setStrutsAction(ApsAdminSystemConstants.EDIT);

		} catch (Throwable t) {
			_logger.error("error editing file, fullPath: {}", this.getCurrentPath(), t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String newFile() {
		this.setStrutsAction(ADD_NEW_FILE);
		return SUCCESS;
	}

	public String uploadNewFile() {
		this.setStrutsAction(UPLOAD_NEW_FILE);
		return SUCCESS;
	}

	public String newDirectory() {
		this.setStrutsAction(ADD_NEW_DIRECTORY);
		return SUCCESS;
	}

	public String upload() {
		try {
			if (!StorageManagerUtil.isValidDirName(this.getCurrentPath())) {
				this.addActionError(this.getText("error.filebrowser.filepath.invalid"));
				return INPUT;
			}
			String result = this.checkExistingFileExtension(this.getCurrentPath(), this.getUploadFileName(), this.getProtectedFolderBoolean());
			if (null != result) return result;
			this.getStorageManager().saveFile(this.getCurrentPath() + this.getUploadFileName(), this.getProtectedFolderBoolean(), this.getInputStream());
		} catch (Throwable t) {
			_logger.error("error in upload", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String trash() {
		try {
			String validatePath = this.validateFullPath();
			if (null != validatePath) return validatePath;
			String fullPath = this.getCurrentPath() + this.getFilename();
			BasicFileAttributeView fileAttributeView = this.getStorageManager().getAttributes(fullPath, this.getProtectedFolderBoolean());
			if (null == fileAttributeView) {
				this.addActionError(this.getText("error.filebrowser.filepath.null"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String delete() {
		try {
			if (null == this.isDeleteFile()) {
				this.addActionError(this.getText("error.filebrowser.delete.missingInformation"));
				return INPUT;
			}
			String validatePath = this.validateFullPath();
			if (null != validatePath) return validatePath;
			String subPath = this.getCurrentPath() + this.getFilename();
			if (this.isDeleteFile()) {
				this.getStorageManager().deleteFile(subPath, this.getProtectedFolderBoolean());
			} else {
				this.getStorageManager().deleteDirectory(subPath, this.getProtectedFolderBoolean());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String save() {
		try {
			String validatePath = this.validateFullPath();
			if (null != validatePath) return validatePath;
			InputStream stream = new ByteArrayInputStream(this.getFileText().getBytes());
			String filename = this.getFilename();
			if (this.getStrutsAction() == ADD_NEW_FILE) {
				filename += "." + this.getTextFileExtension();
			}
			String result = this.validateTextFileExtension(filename);
			if (null != result) return result;
			boolean expectedExist = (this.getStrutsAction() == ApsAdminSystemConstants.EDIT);
			result = this.checkExistingFileExtension(this.getCurrentPath(), filename, expectedExist);
			if (null != result) return result;
			this.getStorageManager().editFile(this.getCurrentPath() + filename, this.getProtectedFolderBoolean(), stream);
		} catch (Throwable t) {
			_logger.error("error saving file, fullPath: {} text: {}", this.getCurrentPath(), this.getFileText(), t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public boolean isTextFile(String filename) {
		int index = filename.lastIndexOf(".");
		String extension = (index > 0) ? filename.substring(index+1) : null;
		if (StringUtils.isBlank(extension)) {
			return false;
		}
		String[] extensions = this.getTextFileTypes();
		for (int i = 0; i < extensions.length; i++) {
			String allowedExtension = extensions[i];
			if (allowedExtension.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	protected String validateTextFileExtension(String filename) {
		if (!this.isTextFile(filename)) {
			this.addFieldError("textFileExtension", this.getText("error.filebrowser.addTextFile.wrongExtension"));
			return INPUT;
		}
		return null;
	}

	protected String checkExistingFileExtension(String path, String filename, boolean expected) throws Throwable {
		boolean exist = this.getStorageManager().exists(path + filename, this.getProtectedFolderBoolean());
		if (exist != expected) {
			String[] args = new String[]{filename};
			if (expected) {
				this.addFieldError("filename", this.getText("error.filebrowser.file.doesNotExist", args));
			} else {
				this.addFieldError("filename", this.getText("error.filebrowser.file.exist", args));
			}
			return INPUT;
		}
		return null;
	}

	public String createDir() {
		try {
			if (!StorageManagerUtil.isValidDirName(this.getCurrentPath())) {
				this.addActionError(this.getText("error.filebrowser.filepath.invalid"));
				return INPUT;
			}
			if (!StorageManagerUtil.isValidDirName(this.getDirname()) || this.getDirname().trim().length() == 0) {
				this.addActionError(this.getText("error.filebrowser.dirname.invalid"));
				return INPUT;
			}
			this.getStorageManager().createDirectory(this.getCurrentPath() + this.getDirname(), this.getProtectedFolderBoolean());
		} catch (Throwable t) {
			_logger.error("error creating dir, fullPath: {} text: {}", this.getCurrentPath(), this.getDirname(), t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public String download() {
		try {
			String validatePath = this.validateFullPath();
			if (null != validatePath) return validatePath;
			String fullPath = this.getCurrentPath() + this.getFilename();
			InputStream is = this.getStorageManager().getStream(fullPath, this.getProtectedFolderBoolean());
			if (null == is) {
				this.addActionError(this.getText("error.filebrowser.download.missingFile"));
				return INPUT;
			}
			this.setDownloadInputStream(is);
			String contentType = URLConnection.guessContentTypeFromName(this.getFilename());
			this.setDownloadContentType(contentType);
		} catch (Throwable t) {
			_logger.error("error downloading file, fullPath: '{}' file: '{}'", this.getCurrentPath(), this.getFilename(), t);
			return FAILURE;
		}
		return SUCCESS;
	}

	public RootFolderAttributeView getRootFolder(boolean protectedFolder) {
		String folderName = (protectedFolder) ? "protected" : "public";
		return new RootFolderAttributeView(folderName, protectedFolder);
	}

	public List<SelectItem> getBreadCrumbsTargets() {
		if (null == this.getProtectedFolder()) {
			return null;
		}
		List<SelectItem> items = new ArrayList<SelectItem>();
		RootFolderAttributeView rootFolder = this.getRootFolder(this.getProtectedFolderBoolean());
		items.add(new SelectItem(null, rootFolder.getName()));
		String currentPath = this.getCurrentPath();
		if (StringUtils.isEmpty(currentPath)) {
			return items;
		}
		String[] folders = currentPath.split(File.separator);
		for (int i = 0; i < folders.length; i++) {
			String folderName = folders[i];
			String subpath = null;
			if (i == 0) {
				subpath = folderName + File.separator;
			} else if (i == (folders.length-1)) {
				subpath = currentPath;
			} else {
				int index = currentPath.indexOf(folderName) + folderName.length();
				subpath = currentPath.substring(0, index) + File.separator;
			}
			items.add(new SelectItem(subpath, folderName));
		}
		return items;
	}

	public BasicFileAttributeView[] getFilesAttributes() {
		try {
			if (!StorageManagerUtil.isValidDirName(this.getCurrentPath())) {
				_logger.info("invalid path specified: {}", this.getCurrentPath());
				this.setCurrentPath("");
			}
			if (null == this.getProtectedFolder()) {
				BasicFileAttributeView[] bfav = new BasicFileAttributeView[2];
				bfav[0] = this.getRootFolder(false);
				bfav[1] = this.getRootFolder(true);
				return bfav;
			} else {
				return this.getStorageManager().listAttributes(this.getCurrentPath(), this.getProtectedFolderBoolean());
			}
		} catch (Throwable t) {
			_logger.error("error extraction file attributes, fullPath: {} ", this.getCurrentPath(), t);
			return null;
		}
	}

	public String getCurrentPath() {
		if (StringUtils.isBlank(_currentPath) || null == this.getProtectedFolder()) {
			_currentPath = "";
		} else if (!_currentPath.endsWith(File.separator)) {
			_currentPath = _currentPath + File.separator;
		}
		return _currentPath;
	}

	/**
	 * Check the provided filename and currentPath parameters and raises an error if one of these parameters are invalid
	 * @return INPUT one or more errors are found. Otherwise null.
	 */
	protected String validateFullPath() {
		if (!StorageManagerUtil.isValidFilenameNoExtension(this.getFilename())) {
			this.addActionError(this.getText("error.filebrowser.filename.invalid"));
			return INPUT;
		}
		if (!StorageManagerUtil.isValidDirName(this.getCurrentPath())) {
			this.addActionError(this.getText("error.filebrowser.filepath.invalid"));
			return INPUT;
		}
		return null;
	}

	public void setCurrentPath(String currentPath) {
		this._currentPath = currentPath;
	}

	protected boolean getProtectedFolderBoolean() {
		if (null == this.getProtectedFolder()) {
			return false;
		}
		return Boolean.parseBoolean(this.getProtectedFolder());
	}

	public String getProtectedFolder() {
		return _protectedFolder;
	}
	public void setProtectedFolder(String protectedFolder) {
		this._protectedFolder = protectedFolder;
	}

	public String getFileText() {
		return _fileText;
	}
	public void setFileText(String fileText) {
		fileText = (null != fileText) ? fileText : "";
		this._fileText = fileText;
	}

	public String getTextFileExtension() {
		return _textFileExtension;
	}
	public void setTextFileExtension(String textFileExtension) {
		this._textFileExtension = textFileExtension;
	}

	public int getStrutsAction() {
		return _strutsAction;
	}
	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}

	public void setUpload(File file) {
		this._file = file;
	}
	public File getUpload() {
		return this._file;
	}

	public int getFileSize() {
		return (int) this._file.length() / 1000;
	}

	public File getFile() {
		return _file;
	}

	public InputStream getInputStream() throws Throwable {
		if (null == this.getFile()) {
			return null;
		}
		return new FileInputStream(this.getFile());
	}

	public String getFilename() {
		if (StringUtils.isBlank(_filename)) {
			_filename = "";
		}
		return _filename;
	}
	public void setFilename(String filename) {
		this._filename = filename;
	}

	public String getDirname() {
		return _dirname;
	}
	public void setDirname(String dirname) {
		this._dirname = dirname;
	}

	public Boolean isDeleteFile() {
		return _deleteFile;
	}
	public void setDeleteFile(Boolean deleteFile) {
		this._deleteFile = deleteFile;
	}

	public String getUploadFileName() {
		return _uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this._uploadFileName = uploadFileName;
	}

	public InputStream getUploadInputStream() {
		return _uploadInputStream;
	}
	public void setUploadInputStream(InputStream uploadInputStream) {
		this._uploadInputStream = uploadInputStream;
	}

	public String[] getTextFileTypes() {
		return this.getTextFileTypesCSV().split(",");
	}

	protected String getTextFileTypesCSV() {
		return _textFileTypesCSV;
	}
	public void setTextFileTypesCSV(String textFileTypesCSV) {
		this._textFileTypesCSV = textFileTypesCSV;
	}

	public InputStream getDownloadInputStream() {
		return _downloadInputStream;
	}
	public void setDownloadInputStream(InputStream downloadInputStream) {
		this._downloadInputStream = downloadInputStream;
	}

	public String getDownloadContentType() {
		return _downloadContentType;
	}
	public void setDownloadContentType(String downloadContentType) {
		this._downloadContentType = downloadContentType;
	}

	protected IStorageManager getStorageManager() {
		return _storageManager;
	}
	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}

	private String _currentPath;
	private String _protectedFolder = null;

	private String _fileText;
	private String _textFileExtension;
	private String _filename;
	private String _dirname;
	private Boolean _deleteFile;
	private int _strutsAction;

	//variables for file upload
	private File _file;
	private String _uploadFileName;
	private InputStream _uploadInputStream;
	private String _textFileTypesCSV;

	private InputStream _downloadInputStream;
	private String _downloadContentType;

	private IStorageManager _storageManager;

	public static final int ADD_NEW_FILE = 11;
	public static final int ADD_NEW_DIRECTORY = 12;
	public static final int UPLOAD_NEW_FILE = 13;

}
