package org.entando.entando.aps.system.services.jsonpatch;

import java.io.IOException;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.zjsonpatch.JsonPatch;
import org.entando.entando.aps.system.services.jsonpatch.validator.JsonPatchValidator;

public class JsonPatchService<T> {

    private ObjectMapper mapper;
    private JavaType type;
    private JsonPatchValidator validator;

    public JsonPatchService(Class<T> targetClass) {
        mapper = new ObjectMapper();
        this.type = mapper.getTypeFactory().constructType(targetClass);
        this.validator = new JsonPatchValidator();
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public JavaType getType() {
        return type;
    }

    public JsonPatchValidator getValidator() {
        return validator;
    }

    public T applyPatch(JsonNode patch, T source) {

        this.getValidator().validatePatch(patch);

        JsonNode jsonSource = this.getMapper().convertValue(source, JsonNode.class);
        JsonNode jsonOutput = JsonPatch.apply(patch, jsonSource);

        return this.getMapper().convertValue(jsonOutput, this.type);

    }


}
