package org.entando.entando.web.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodeFormatter;

/**
 * Extends the {@link DefaultMessageCodesResolver} in order to append a custom error code into the code array.
 * <p>
 * When and errorCode is found into <code>validationErrorCodeMapping</code> then the specific value is appended to the returned code array
 * <p>
 * The purpose in to translate {@link javax.validation.constraints} codes into custom codes
 * 
 * @author spuddu
 *
 */
public class EntandoMessageCodesResolver extends DefaultMessageCodesResolver {

    public static final String ERR_CODE_URI_PARAMETER = "40";

    private static final MessageCodeFormatter DEFAULT_FORMATTER = Format.PREFIX_ERROR_CODE;
    private MessageCodeFormatter formatter = DEFAULT_FORMATTER;

    private Map<String, String> validationErrorCodeMapping = new HashMap<String, String>();

    @Override
    public String[] resolveMessageCodes(String errorCode, String objectName, String field, Class<?> fieldType) {
        Set<String> codeList = new LinkedHashSet<String>();
        List<String> fieldList = new ArrayList<String>();
        buildFieldList(field, fieldList);
        addCodes(codeList, errorCode, objectName, fieldList);
        int dotIndex = field.lastIndexOf('.');
        if (dotIndex != -1) {
            buildFieldList(field.substring(dotIndex + 1), fieldList);
        }
        addCodes(codeList, errorCode, null, fieldList);
        if (fieldType != null) {
            addCode(codeList, errorCode, null, fieldType.getName());
        }
        addCode(codeList, errorCode, null, null);

        if (this.getValidationErrorCodeMapping().containsKey(errorCode)) {
            addCode(codeList, this.getValidationErrorCodeMapping().get(errorCode), null, null);
        }

        return StringUtils.toStringArray(codeList);
    }

    private void addCodes(Collection<String> codeList, String errorCode, String objectName, Iterable<String> fields) {
        for (String field : fields) {
            addCode(codeList, errorCode, objectName, field);
        }
    }

    private void addCode(Collection<String> codeList, String errorCode, String objectName, String field) {
        codeList.add(postProcessMessageCode(this.formatter.format(errorCode, objectName, field)));
    }

    public Map<String, String> getValidationErrorCodeMapping() {
        return validationErrorCodeMapping;
    }

    public void setValidationErrorCodeMapping(Map<String, String> validationErrorCodeMapping) {
        this.validationErrorCodeMapping = validationErrorCodeMapping;
    }

}
