package org.entando.entando.aps.system.services.jsonpatch.validator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.flipkart.zjsonpatch.InvalidJsonPatchException;
import com.flipkart.zjsonpatch.JsonPatch;

public class JsonPatchValidator {

    private final Set<String> ENTANDO_SUPPORTED_OPERATIONS = Stream.of("replace", "add", "remove").collect(Collectors.toSet());

    /**
     * Validate the provided patch using Entando criteria
     * @param patch
     */
    public void validatePatch(JsonNode patch) {
        JsonPatch.validate(patch);

        for (JsonNode jsonNode : patch) {
            String operation = jsonNode.get("op").asText();
            if (!ENTANDO_SUPPORTED_OPERATIONS.contains(operation)) {
                throw new InvalidJsonPatchException("Unsupported JSON patch operation " + operation);
            }
        }

    }

}
