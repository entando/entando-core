package org.entando.entando.web.component;

public class ComponentUsage {
    private String type;
    private String code;
    private Integer usage;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ComponentUsage build = new ComponentUsage();

        public Builder type(String type) {
            build.type = type;
            return this;
        }

        public Builder code(String code) {
            build.code = code;
            return this;
        }

        public Builder usage(Integer usage) {
            build.usage = usage;
            return this;
        }

        public ComponentUsage build() {
            return build;
        }
    }
}
