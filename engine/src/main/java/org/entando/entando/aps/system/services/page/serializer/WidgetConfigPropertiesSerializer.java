package org.entando.entando.aps.system.services.page.serializer;

import com.agiletec.aps.util.ApsProperties;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetConfigPropertiesSerializer extends StdSerializer<ApsProperties> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public WidgetConfigPropertiesSerializer() {
        this(null);
    }

    public WidgetConfigPropertiesSerializer(Class<ApsProperties> t) {
        super(t);
    }

    /*
     * Related to EN6-183, Frontend needs all config objects to be completely valid JSON objects.
     * This Serializer converts known widget config formats, but may need to be improved case by case.
     * Also, possible conflicts may arise if different widgets use same property names and different value formats.
     * See also, WidgetConfigPropertiesDeserializer.java.
     */
    @Override
    public void serialize(ApsProperties properties, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();

        for (Entry<Object,Object> property : properties.entrySet()) {
            if(property.getKey().equals("categories")) {
                logger.warn("Serializing WidgetConfig.config.categories into JSON Format");
                writeCategories(jsonGenerator, property.getKey().toString(), property.getValue().toString());
            } else if (property.getKey().equals("contents")) {
                logger.warn("Serializing WidgetConfig.config.contents into JSON Format");
                writeContentsConfig(jsonGenerator, property.getKey().toString(), property.getValue().toString());
            } else if (property.getKey().toString().toLowerCase().contains("filters")) {
                logger.warn("Serializing WidgetConfig.config.filters into JSON Format");
                writeFiltersConfig(jsonGenerator, property.getKey().toString(), property.getValue().toString());
            } else {
                writeStringProperty(jsonGenerator, property.getKey().toString(), property.getValue().toString());
            }
        }

        jsonGenerator.writeEndObject();
    }

    private void writeCategories(JsonGenerator jsonGenerator, String key, String value) throws IOException {
        jsonGenerator.writeFieldName(key);
        jsonGenerator.writeStartArray();

        for (String category : extractCategories(value)) {
            jsonGenerator.writeString(category);
        }

        jsonGenerator.writeEndArray();
    }

    private void writeFiltersConfig(JsonGenerator jsonGenerator, String key, String value) throws IOException {
        jsonGenerator.writeFieldName(key);
        jsonGenerator.writeStartArray();

        for (Map<String, String> filter : extractConfigFilters(value)) {
            jsonGenerator.writeStartObject();
            for (Entry<String,String> entry : filter.entrySet()) {
                writeProperty(jsonGenerator, entry.getKey(), entry.getValue());
            }
            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private void writeContentsConfig(JsonGenerator jsonGenerator, String key, String value) throws IOException {
        jsonGenerator.writeFieldName(key);
        jsonGenerator.writeStartArray();

        for (Map<String, String> config : extractConfigObject(value)) {
            jsonGenerator.writeStartObject();

            for(Entry<String, String> property : config.entrySet()) {
                writeProperty(jsonGenerator, property.getKey(), property.getValue());
            }

            jsonGenerator.writeEndObject();
        }

        jsonGenerator.writeEndArray();
    }

    private void writeProperty(JsonGenerator jsonGenerator, String key, String value) throws IOException {
        if (value.equals("true") || value.equals("false")) {
            writeBooleanProperty(jsonGenerator, key, Boolean.valueOf(value));
        } else {
            writeStringProperty(jsonGenerator, key, value);
        }
    }

    private void writeStringProperty(JsonGenerator jsonGenerator, String key, String value) throws IOException {
        jsonGenerator.writeStringField(key, value);
    }

    private void writeBooleanProperty(JsonGenerator jsonGenerator, String key, Boolean value) throws IOException {
        jsonGenerator.writeBooleanField(key, value);
    }

    private List<String> extractCategories(String value) {
        return Arrays.stream(Optional.ofNullable(value)
                .orElse("")
                .split(","))
                .collect(Collectors.toList());
    }

    private List<Map<String, String>> extractConfigFilters(String value) {
        return extractProperties("\\)\\+\\(|\\(|\\)", value);
    }

    private List<Map<String, String>> extractConfigObject(String value) {
        return extractProperties("\\[\\{|\\{|\\]\\}|\\}", value);
    }

    private List<Map<String, String>> extractProperties(String regex, String value) {
        List<String> split = Arrays.stream(Optional.ofNullable(value)
                .orElse("").split(regex))
                .collect(Collectors.toList());

        List<Map<String,String>> properties = new ArrayList<>();
        for (String strProperty : split) {
            Map<String,String> property = extractProperty(strProperty);

            if (!property.entrySet().isEmpty()) {
                properties.add(property);
            }
        }

        return properties;
    }

    private Map<String, String> extractProperty(String value) {
        Map<String, String> property = new HashMap<>();

        for (String f : value.trim().split(";|,")) {
            if (f == null || f.trim().isEmpty()) {
                continue;
            }

            String[] keyValue = f.split("=|:");
            if (keyValue.length != 2) {
                logger.warn("Invalid filter format: {}", f);
                continue;
            }

            property.put(keyValue[0].trim(), keyValue[1].trim());
        }

        return property;
    }
}
