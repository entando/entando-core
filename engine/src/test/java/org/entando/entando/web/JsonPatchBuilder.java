package org.entando.entando.web;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonPatchBuilder {

    private List<JsonPatchOperation> operationList;
    private ObjectMapper mapper;

    public JsonPatchBuilder() {
        this.operationList = new ArrayList<>();
        this.mapper = new ObjectMapper();
    }

    public JsonPatchBuilder withOperation(String type, String path, Object value) {
        this.operationList.add(new JsonPatchOperation(type, path, value));
        return this;
    }

    public JsonPatchBuilder withReplace(String path, Object value) {
        this.operationList.add(new JsonPatchOperation("replace", path, value));
        return this;
    }

    public String getJsonPatchAsString() throws JsonProcessingException {
        return mapper.writeValueAsString(this.operationList);
    }

    @JsonPropertyOrder({"op", "path", "value"})
    private class JsonPatchOperation {

        @JsonProperty("op")
        private final String operation;

        @JsonProperty
        private final String path;

        @JsonProperty
        private final Object value;

        private JsonPatchOperation(String operation, String path, Object value) {
            this.operation = operation;
            this.path = path;
            this.value = value;
        }
    }

}
