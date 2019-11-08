package org.entando.entando.aps.system.services.jsonpatch.validator;

import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableSet;
import org.springframework.data.rest.webmvc.json.patch.JsonPatchPatchConverter;
import org.springframework.data.rest.webmvc.json.patch.PatchException;
import org.springframework.stereotype.Service;

@Service
public class JsonPatchValidator {

    private final Set<String> ENTANDO_SUPPORTED_OPERATIONS = ImmutableSet.of("replace");

    private final JsonPatchPatchConverter converter;

    public JsonPatchValidator() {
        this.converter = new JsonPatchPatchConverter(new ObjectMapper());
    }

    public JsonPatchValidator(JsonPatchPatchConverter converter) {
        this.converter = converter;
    }

    /**
     * Validate the provided jsonNode using Entando criteria
     * @param jsonNode
     */
    public void validatePatch(JsonNode jsonNode) {

        // Test if the json node is generically convertible to a Patch
        this.converter.convert(jsonNode);

        // Check if the operations are supported, can't access Spring PatchOperations as they are protected
        ArrayNode opNodes = (ArrayNode) jsonNode;

        for (Iterator<JsonNode> elements = opNodes.elements(); elements.hasNext(); ) {

            JsonNode opNode = elements.next();

            String opType = opNode.get("op").textValue();
            if (!ENTANDO_SUPPORTED_OPERATIONS.contains(opType)) {
                throw new PatchException("Not supported operation type: " + opType);
            }
        }
    }

}
