package org.entando.entando.web.component;

public class ComponentUsageEntity {

    private String type;
    private String code;

    public ComponentUsageEntity() {
    }

    public ComponentUsageEntity(String type, String code) {
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
}
