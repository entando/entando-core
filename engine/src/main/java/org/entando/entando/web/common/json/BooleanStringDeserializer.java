package org.entando.entando.web.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanStringDeserializer extends JsonDeserializer<Boolean> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String FALSE = "false";
    protected static final String TRUE = "true";

    @Override
    public Boolean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken currentToken = jp.getCurrentToken();

        if (currentToken.equals(JsonToken.VALUE_STRING)) {
            String text = jp.getText().trim();
            if (TRUE.equalsIgnoreCase(text)) {
                return Boolean.TRUE;
            } else if (FALSE.equalsIgnoreCase(text)) {
                return Boolean.FALSE;
            } else {
                logger.warn("only {}, {} and {}, {} values are supported as boolean input", TRUE, "\"true\"", FALSE, "\"false\"");
                ;
                return null;
            }

        } else if (currentToken.equals(JsonToken.VALUE_NULL)) {
            return null;
        } else if (currentToken.equals(JsonToken.VALUE_TRUE)) {
            return Boolean.TRUE;
        } else if (currentToken.equals(JsonToken.VALUE_FALSE)) {
            return Boolean.FALSE;

        }
        throw ctxt.mappingException(Boolean.class);
    }

    @Override
    public Boolean getNullValue() {
        return null; //Boolean.FALSE;
    }

}
