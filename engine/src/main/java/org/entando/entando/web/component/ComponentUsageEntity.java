package org.entando.entando.web.component;

public class ComponentUsageEntity {

    public static final String TYPE_PAGE = "page";
    public static final String TYPE_WIDGET = "widget";
    public static final String TYPE_FRAGMENT = "fragment";
    public static final String TYPE_PAGE_TEMPLATE = "pageTemplate";
    public static final String TYPE_CONTENT = "content";

    private String type;
    private String code;
    private String status;

    public ComponentUsageEntity() {
    }

    public ComponentUsageEntity(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public ComponentUsageEntity(String type, String code, String status) {
        this.status = status;
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public ComponentUsageEntity setType(String type) {
        this.type = type;
        return this;
    }

    public String getCode() {
        return code;
    }

    public ComponentUsageEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public ComponentUsageEntity setStatus(String status) {
        this.status = status;
        return this;
    }
}
