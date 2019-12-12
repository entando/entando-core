package org.entando.entando.aps.system.services.page.serializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetConfigPropertiesDeserializer extends StdConverter<Map<String, Object>, Map<String, Object>> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /*
     * Related to EN6-183, Frontend needs all config objects to be completely valid JSON objects.
     * This Deserializer converts known widget config formats, but may need to be improved case by case.
     * Also, possible conflicts may arise if different widgets use same property names and different value formats.
     * See also, WidgetConfigPropertiesSerializer.java.
     */
    @Override
    public Map<String, Object> convert(Map<String, Object> json) {
        Map<String, Object> properties = new HashMap<>();

        if (json == null) {
            return properties;
        }

        for (Entry<String, Object> property : json.entrySet()) {
            if (property.getKey().equals("categories")) {
                logger.warn("Serializing WidgetConfig.config.categories from JSON Format to custom persistence format");
                properties.put(property.getKey(), readCategories((List<String>) property.getValue()));
            } else if (property.getKey().toLowerCase().contains("filters")) {
                logger.warn("Serializing WidgetConfig.config.filters from JSON Format to custom persistence format");
                properties.put(property.getKey(), readFilters((List<Map<String, Object>>) property.getValue()));
            } else if (property.getKey().equals("contents")) {
                logger.warn("Serializing WidgetConfig.config.contents from JSON Format to custom persistence format");
                properties.put(property.getKey(), readContents((List<Map<String, Object>>) property.getValue()));
            } else {
                properties.put(property.getKey(), property.getValue().toString());
            }
        }

        return properties;
    }

    private String readCategories(List<String> categories) {
        return String.join(",", categories);
    }

    private String readFilters(List<Map<String, Object>> filters) {
        return readObjects(filters,null, null, "(", ")", "+", ";", "=");
    }

    private String readContents(List<Map<String, Object>> contents) {
        return readObjects(contents,"[", "]", "{", "}", ",", ",", "=");
    }

    private String readObjects(List<Map<String, Object>> objects, String arrayOpen, String arrayClose,
            String objOpen, String objClose, String objSeparator, String propSeparator, String propKeyValueSeparator) {
        StringBuilder sb = new StringBuilder();
        sb.append(arrayOpen == null ? "" : arrayOpen);
        String objSeparatorAux = "";
        for (Map<String, Object> object : objects) {
            sb.append(objSeparatorAux);
            sb.append(objOpen);

            String separator = "";
            for (Entry<String, Object> property : object.entrySet()) {
                sb.append(separator);
                sb.append(property.getKey().trim());
                sb.append(propKeyValueSeparator);
                sb.append(property.getValue());

                separator = propSeparator;
            }
            sb.append(objClose);

            objSeparatorAux = objSeparator;
        }

        sb.append(arrayClose == null ? "" : arrayClose);
        return sb.toString();
    }
}
