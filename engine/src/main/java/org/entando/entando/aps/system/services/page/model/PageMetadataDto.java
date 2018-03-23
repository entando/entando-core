package org.entando.entando.aps.system.services.page.model;

import java.util.Date;
import java.util.Set;

import com.agiletec.aps.system.services.page.PageMetadata;
import com.agiletec.aps.util.ApsProperties;
import java.util.HashSet;
import org.springframework.beans.BeanUtils;

public class PageMetadataDto {

    private ApsProperties titles = new ApsProperties();
    private Set<String> extraGroups = new HashSet<>();
    private String model;
    private boolean showable = false;
    private boolean useExtraTitles = false;
    private String mimeType;
    private String charset;
    private Date updatedAt;

    public PageMetadataDto() {

    }

    public PageMetadataDto(PageMetadata src) {
        BeanUtils.copyProperties(src, this);
        this.setModel(src.getModel().getCode());
    }

    public ApsProperties getTitles() {
        return titles;
    }

    public void setTitles(ApsProperties titles) {
        this.titles = titles;
    }

    public Set<String> getExtraGroups() {
        return extraGroups;
    }

    public void setExtraGroups(Set<String> extraGroups) {
        this.extraGroups = extraGroups;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isShowable() {
        return showable;
    }

    public void setShowable(boolean showable) {
        this.showable = showable;
    }

    public boolean isUseExtraTitles() {
        return useExtraTitles;
    }

    public void setUseExtraTitles(boolean useExtraTitles) {
        this.useExtraTitles = useExtraTitles;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
