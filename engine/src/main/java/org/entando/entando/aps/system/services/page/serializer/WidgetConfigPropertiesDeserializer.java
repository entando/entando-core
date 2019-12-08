package org.entando.entando.aps.system.services.page.serializer;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WidgetConfigPropertiesDeserializer extends StdConverter<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> convert(Map<String, Object> json) {
        Map<String, Object> properties = new HashMap<>();

        for (Entry<String, Object> property : json.entrySet()) {
            if (property.getKey().equals("categories")) {
                properties.put(property.getKey(), readCategories((List<String>) property.getValue()));
            } else if (property.getKey().toLowerCase().contains("filters")) {
                properties.put(property.getKey(), readFilters((List<Map<String, Object>>) property.getValue()));
            } else if (property.getKey().equals("contents")) {
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
        return readObjects(filters,null, null, "({)", ")", "+", ";", "=");
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
