package com.agiletec.plugins.jacms.aps.system.services.resource.model;


public class ResourceInstanceDto {

    private int size = 1;
    private String langCode = null;
    private String fileName;
    private String fileLength;
    private String mimeType;

    public ResourceInstanceDto() {

    }

    public ResourceInstanceDto(ResourceInstance resourceInstance) {
        this.setSize(resourceInstance.getSize());
        this.setLangCode(resourceInstance.getLangCode());
        this.setFileName(resourceInstance.getFileName());
        this.setFileLength(resourceInstance.getFileLength());
        this.setMimeType(resourceInstance.getMimeType());
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLength() {
        return fileLength;
    }

    public void setFileLength(String fileLength) {
        this.fileLength = fileLength;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

}
