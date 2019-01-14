package org.entando.entando.web.page.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.entando.entando.web.page.JsonPatchDeserializer;
import org.springframework.data.rest.webmvc.json.patch.Patch;

@JsonDeserialize(using = JsonPatchDeserializer.class)
public class PagePatchRequest {

    private Patch patch;

    public Patch getPatch() {
        return patch;
    }

    public PagePatchRequest setPatch(Patch patch) {
        this.patch = patch;
        return this;
    }
}
