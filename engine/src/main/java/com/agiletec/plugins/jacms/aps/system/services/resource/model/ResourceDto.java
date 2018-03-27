package com.agiletec.plugins.jacms.aps.system.services.resource.model;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResourceDto {

    private String id;
    private String typeCode;
    private String description;
    private String mainGroup;
    private String masterFileName;
    private List<String> categories;
    private String folder;
    private String defaultUrlPath;
    private List<String> allowedExtensions;
    private Date creationDate;
    private Date lastModified;
    private Map<String, ResourceInstanceDto> instances;
    private boolean multiInstance;

    public ResourceDto() {

    }

    public ResourceDto(AbstractResource src) {
        this.setId(src.getId());
        this.setTypeCode(src.getType());
        this.setDescription(src.getDescription());
        this.setMainGroup(src.getMainGroup());
        this.setMasterFileName(src.getMasterFileName());
        if (null != src.getCategories()) {
            this.setCategories(src.getCategories().stream().map(i -> i.getCode()).collect(Collectors.toList()));
        }
        this.setFolder(src.getFolder());
        this.setDefaultUrlPath(src.getDefaultUrlPath());
        if (null != src.getAllowedFileTypes()) {
            this.setAllowedExtensions(Arrays.asList(src.getAllowedFileTypes()));
        }
        this.setCreationDate(src.getCreationDate());
        this.setLastModified(src.getLastModified());
        this.setMultiInstance(src.isMultiInstance());
        if (isMultiInstance()) {
            this.setInstances(new HashMap<>());
            for (Map.Entry<String, ResourceInstance> entry : ((AbstractMultiInstanceResource) src).getInstances().entrySet()) {
                this.getInstances().put(entry.getKey(), new ResourceInstanceDto(entry.getValue()));
            }
        } else {
            this.setInstances(new HashMap<>());
            this.getInstances().put("0", new ResourceInstanceDto(src.getDefaultInstance()));
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainGroup() {
        return mainGroup;
    }

    public void setMainGroup(String mainGroup) {
        this.mainGroup = mainGroup;
    }

    public String getMasterFileName() {
        return masterFileName;
    }

    public void setMasterFileName(String masterFileName) {
        this.masterFileName = masterFileName;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Map<String, ResourceInstanceDto> getInstances() {
        return instances;
    }

    public void setInstances(Map<String, ResourceInstanceDto> instances) {
        this.instances = instances;
    }

    public boolean isMultiInstance() {
        return multiInstance;
    }

    public void setMultiInstance(boolean multiInstance) {
        this.multiInstance = multiInstance;
    }

    public String getDefaultUrlPath() {
        return defaultUrlPath;
    }

    public void setDefaultUrlPath(String defaultUrlPath) {
        this.defaultUrlPath = defaultUrlPath;
    }

    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    public void setAllowedExtensions(List<String> allowedExtensions) {
        this.allowedExtensions = allowedExtensions;
    }


}
