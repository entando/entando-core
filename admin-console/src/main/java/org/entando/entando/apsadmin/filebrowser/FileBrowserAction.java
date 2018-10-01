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
package org.entando.entando.apsadmin.filebrowser;

import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.storage.BasicFileAttributeView;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.aps.system.services.storage.RootFolderAttributeView;
import org.entando.entando.aps.system.services.storage.StorageManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author S.Loru - E.Santoboni
 */
public class FileBrowserAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(FileBrowserAction.class);

    public static final int ADD_NEW_FILE = 11;
    public static final int ADD_NEW_DIRECTORY = 12;
    public static final int UPLOAD_NEW_FILE = 13;

    private String currentPath;
    private String protectedFolder = null;

    private String fileText;
    private String textFileExtension;
    private String filename;
    private String dirname;
    private Boolean deleteFile;
    private int strutsAction;

    //variables for file upload
    private List<File> file;
    private List<String> uploadFileName;
    private List<InputStream> uploadInputStream;

    private String textFileTypesCSV;

    private InputStream downloadInputStream;
    private String downloadContentType;

    private IStorageManager storageManager;

    public String list() {
        return SUCCESS;
    }

    public String edit() {
        try {
            String result = this.validateTextFileExtension(this.getFilename());
            if (null != result) {
                return result;
            }

            String validatePath = this.validateFullPath();
            if (null != validatePath) {
                return validatePath;
            }

            String fullPath = this.getCurrentPath() + this.getFilename();
            BasicFileAttributeView fileAttributeView = this.getStorageManager().getAttributes(fullPath, this.getProtectedFolderBoolean());
            if (null == fileAttributeView || fileAttributeView.isDirectory()) {
                return INPUT;
            }
            String text = this.getStorageManager().readFile(fullPath, this.getProtectedFolderBoolean());
            this.setFileText(text);
            this.setStrutsAction(ApsAdminSystemConstants.EDIT);

        } catch (Throwable t) {
            logger.error("error editing file, fullPath: {}", this.getCurrentPath(), t);
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

    public String upload() throws Throwable {
        logger.debug("upload :" + this.getUploadFileName());

        if (null != this.getFile()) {
            logger.debug("Upload {} files ", this.getFile().size());
        } else {
            logger.debug("Upload null files list, nothing to save");
        }

        return uploadFiles(this.getFile(),
                this.getUploadFileName(),
                this.getInputStream(),
                this.getCurrentPath(),
                this.getProtectedFolderBoolean());

    }

    public String uploadFiles(List<File> files, List<String> fileNames, List<InputStream> inputStreams, String currentPath, boolean protectedFolder) {
        if (null != files) {
            logger.debug("Upload Files :" + files.size());
        }
        try {
            int index = 0;
            for (File file : files) {
                logger.debug("Upload file {} with filename ", index, fileNames.get(index));

                if (!StorageManagerUtil.isValidDirName(currentPath)) {
                    this.addActionError(this.getText("error.filebrowser.filepath.invalid"));
                    return INPUT;
                }
                String result = this.checkExistingFileExtension(currentPath, fileNames.get(index), false, protectedFolder);

                if (null != result) {
                    return result;
                }

                this.getStorageManager().saveFile(currentPath + fileNames.get(index), protectedFolder, inputStreams.get(index));

                index++;
            }
        } catch (Throwable t) {
            logger.error("error in upload", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String trash() {
        try {
            String validatePath = this.validateFullPath();
            if (null != validatePath) {
                return validatePath;
            }
            String fullPath = this.getCurrentPath() + this.getFilename();
            BasicFileAttributeView fileAttributeView = this.getStorageManager().getAttributes(fullPath, this.getProtectedFolderBoolean());
            if (null == fileAttributeView) {
                this.addActionError(this.getText("error.filebrowser.filepath.null"));
                return INPUT;
            }
            this.setStrutsAction(ApsAdminSystemConstants.DELETE);
        } catch (Throwable t) {
            logger.error("error in trash", t);
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
            if (null != validatePath) {
                return validatePath;
            }
            String subPath = this.getCurrentPath() + this.getFilename();
            if (this.isDeleteFile()) {
                this.getStorageManager().deleteFile(subPath, this.getProtectedFolderBoolean());
            } else {
                this.getStorageManager().deleteDirectory(subPath, this.getProtectedFolderBoolean());
            }
        } catch (Throwable t) {
            logger.error("error in delete", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String save() {
        logger.debug("Save file {}", filename);
        try {
            String validatePath = this.validateFullPath();
            if (null != validatePath) {
                return validatePath;
            }
            InputStream stream = new ByteArrayInputStream(this.getFileText().getBytes(StandardCharsets.UTF_8));
            String filename = this.getFilename();
            if (this.getStrutsAction() == ADD_NEW_FILE) {
                filename += "." + this.getTextFileExtension();
            }
            String result = this.validateTextFileExtension(filename);
            if (null != result) {
                return result;
            }
            boolean expectedExist = (this.getStrutsAction() == ApsAdminSystemConstants.EDIT);
            result = this.checkExistingFileExtension(this.getCurrentPath(), filename, expectedExist, this.getProtectedFolderBoolean());
            if (null != result) {
                return result;
            }
            this.getStorageManager().editFile(this.getCurrentPath() + filename, this.getProtectedFolderBoolean(), stream);
        } catch (Throwable t) {
            logger.error("error saving file, fullPath: {} text: {}", this.getCurrentPath(), this.getFileText(), t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public boolean isTextFile(String filename) {
        int index = filename.lastIndexOf(".");
        String extension = (index > 0) ? filename.substring(index + 1) : null;
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
            logger.error("error creating dir, fullPath: {} text: {}", this.getCurrentPath(), this.getDirname(), t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String download() {
        try {
            String validatePath = this.validateFullPath();
            if (null != validatePath) {
                return validatePath;
            }
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
            logger.error("error downloading file, fullPath: '{}' file: '{}'", this.getCurrentPath(), this.getFilename(), t);
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
        String[] folders = currentPath.split("/");
        for (int i = 0; i < folders.length; i++) {
            String folderName = folders[i];
            String subpath = null;
            if (i == 0) {
                subpath = folderName + "/";
            } else if (i == (folders.length - 1)) {
                subpath = currentPath;
            } else {
                int index = currentPath.indexOf(folderName) + folderName.length();
                subpath = currentPath.substring(0, index) + "/";
            }
            items.add(new SelectItem(subpath, folderName));
        }
        return items;
    }

    public BasicFileAttributeView[] getFilesAttributes() {
        try {
            if (!StorageManagerUtil.isValidDirName(this.getCurrentPath())) {
                logger.info("invalid path specified: {}", this.getCurrentPath());
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
            logger.error("error extraction file attributes, fullPath: {} ", this.getCurrentPath(), t);
            return null;
        }
    }

    public String getCurrentPath() {
        if (StringUtils.isBlank(currentPath) || null == this.getProtectedFolder()) {
            currentPath = "";
        } else if (!currentPath.endsWith("/")) {
            currentPath = currentPath + "/";
        }
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getProtectedFolder() {
        return protectedFolder;
    }

    public void setProtectedFolder(String protectedFolder) {
        this.protectedFolder = protectedFolder;
    }

    public String getFileText() {
        return fileText;
    }

    public void setFileText(String fileText) {
        fileText = (null != fileText) ? fileText : "";
        this.fileText = fileText;
    }

    public String getTextFileExtension() {
        return textFileExtension;
    }

    public void setTextFileExtension(String textFileExtension) {
        this.textFileExtension = textFileExtension;
    }

    public int getStrutsAction() {
        return strutsAction;
    }

    public void setStrutsAction(int strutsAction) {
        this.strutsAction = strutsAction;
    }

    public void setUpload(List<File> file) {
        this.file = file;
    }

    public List<File> getUpload() {
        return this.file;
    }

    public int getFileSize(int index) {
        return (int) this.file.get(index).length() / 1000;
    }

    public File getFile(int index) {
        return file.get(index);
    }

    public InputStream getInputStream(int index) throws Throwable {
        if (null == this.getFile(index)) {
            return null;
        }
        return new FileInputStream(this.getFile(index));
    }

    public List<InputStream> getInputStream() throws Throwable {
        if (null == this.getFile()) {
            return null;
        }
        List<InputStream> inputStreamList = new ArrayList<InputStream>();

        for (File file : this.getFile()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            inputStreamList.add(fileInputStream);
        }
        return inputStreamList;
    }

    public String getFilename() {
        if (StringUtils.isBlank(filename)) {
            filename = "";
        }
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDirname() {
        return dirname;
    }

    public void setDirname(String dirname) {
        this.dirname = dirname;
    }

    public Boolean isDeleteFile() {
        return deleteFile;
    }

    public void setDeleteFile(Boolean deleteFile) {
        this.deleteFile = deleteFile;
    }

    public String getUploadFileName(int index) {
        return uploadFileName.get(index);
    }

    public List<String> getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(List<String> uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public InputStream getUploadInputStream(int index) {
        return this.uploadInputStream.get(index);
    }

    public void setUploadInputStream(List<InputStream> uploadInputStream) {
        this.uploadInputStream = uploadInputStream;
    }

    public String[] getTextFileTypes() {
        return this.getTextFileTypesCSV().split(",");
    }

    protected String getTextFileTypesCSV() {
        return textFileTypesCSV;
    }

    public void setTextFileTypesCSV(String textFileTypesCSV) {
        this.textFileTypesCSV = textFileTypesCSV;
    }

    public InputStream getDownloadInputStream() {
        return downloadInputStream;
    }

    public void setDownloadInputStream(InputStream downloadInputStream) {
        this.downloadInputStream = downloadInputStream;
    }

    public String getDownloadContentType() {
        return downloadContentType;
    }

    public void setDownloadContentType(String downloadContentType) {
        this.downloadContentType = downloadContentType;
    }

    public void setStorageManager(IStorageManager storageManager) {
        this.storageManager = storageManager;
    }

    public List<File> getFile() {
        return file;
    }

    public void setFile(List<File> file) {
        this.file = file;
    }

    protected IStorageManager getStorageManager() {
        return storageManager;
    }

    protected boolean getProtectedFolderBoolean() {
        if (null == this.getProtectedFolder()) {
            return false;
        }
        return Boolean.parseBoolean(this.getProtectedFolder());
    }

    protected String validateTextFileExtension(String filename) {
        if (!this.isTextFile(filename)) {
            this.addFieldError("textFileExtension", this.getText("error.filebrowser.addTextFile.wrongExtension"));
            return INPUT;
        }
        return null;
    }

    protected String checkExistingFileExtension(String path, String filename, boolean expected, boolean protectedFolder) throws Throwable {
        boolean exist = this.getStorageManager().exists(path + filename, protectedFolder);
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
    
  
    /**
     * Check the provided filename and currentPath parameters and raises an
     * error if one of these parameters are invalid
     *
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

    private List<InputStream> getUploadInputStream() {
        return uploadInputStream;
    }

}
