package org.entando.entando.aps.system.services.jsonpatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.entando.entando.aps.system.services.jsonpatch.validator.JsonPatchValidator;
import org.springframework.data.rest.webmvc.json.patch.JsonPatchPatchConverter;
import org.springframework.data.rest.webmvc.json.patch.Patch;

public class JsonPatchService<T> {

    private final Class<T> referenceClass;
    private final JsonPatchPatchConverter converter;
    private final JsonPatchValidator validator;

    public JsonPatchService(Class<T> referenceClass) {
        this.referenceClass = referenceClass;
        this.converter = new JsonPatchPatchConverter(new ObjectMapper());
        this.validator = new JsonPatchValidator(this.converter);
    }

    public JsonPatchPatchConverter getConverter() { return converter; }

    public JsonPatchValidator getValidator() { return validator; }

    public Class<T> getReferenceClass() { return referenceClass; }

    public T applyPatch(JsonNode patch, T source) {

        this.getValidator().validatePatch(patch);

        Patch springPatch = this.getConverter().convert(patch);
        return springPatch.apply(source, referenceClass );

    }


}
