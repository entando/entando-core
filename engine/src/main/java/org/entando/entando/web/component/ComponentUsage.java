package org.entando.entando.web.component;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponentUsage {
    private String type;
    private String code;
    private Integer usage;
}
