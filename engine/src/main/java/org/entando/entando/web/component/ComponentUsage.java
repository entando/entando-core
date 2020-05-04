package org.entando.entando.web.component;

public class ComponentUsage extends ComponentUsageEntity {

    private Integer usage;

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
            build.setType(type);
            return this;
        }

        public Builder code(String code) {
            build.setCode(code);
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
