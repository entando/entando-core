/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.aps.system.common.entity.model.attribute.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.CDATA;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.FieldError;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * @author E.Santoboni
 */
public class TextAttributeValidationRules extends AbstractAttributeValidationRules {

	private static final Logger _logger =  LoggerFactory.getLogger(TextAttributeValidationRules.class);
	
    @Override
    public IAttributeValidationRules clone() {
        TextAttributeValidationRules clone = (TextAttributeValidationRules) super.clone();
        clone.setMaxLength(this.getMaxLength());
        clone.setMinLength(this.getMinLength());
        clone.setRegexp(this.getRegexp());
        return clone;
    }
    
    @Override
    protected void fillJDOMConfigElement(Element configElement) {
        super.fillJDOMConfigElement(configElement);
        if (null != this.getMinLength() && this.getMinLength() > -1) {
            Element element = new Element("minlength");
            element.setText(String.valueOf(this.getMinLength()));
            configElement.addContent(element);
        }
        if (null != this.getMaxLength() && this.getMaxLength() > -1) {
            Element element = new Element("maxlength");
            element.setText(String.valueOf(this.getMaxLength()));
            configElement.addContent(element);
        }
        if (null != this.getRegexp() && this.getRegexp().trim().length() > 0) {
            Element regexpElem = new Element("regexp");
            CDATA cdata = new CDATA(this.getRegexp());
            regexpElem.addContent(cdata);
            configElement.addContent(regexpElem);
        }
        String toStringEqualValue = (this.getValue() != null) ? String.valueOf(this.getValue()) : null;
        this.insertJDOMConfigElement("value", this.getValueAttribute(), toStringEqualValue, configElement);
        String toStringStartValue = (this.getRangeStart() != null) ? String.valueOf(this.getRangeStart()) : null;
        this.insertJDOMConfigElement("rangestart", this.getRangeStartAttribute(), toStringStartValue, configElement);
        String toStringEndValue = (this.getRangeEnd() != null) ? String.valueOf(this.getRangeEnd()) : null;
        this.insertJDOMConfigElement("rangeend", this.getRangeEndAttribute(), toStringEndValue, configElement);
    }
    
    @Override
    protected void extractValidationRules(Element validationElement) {
        super.extractValidationRules(validationElement);
        String maxLength = this.extractValue(validationElement, "maxlength");
        if (null != maxLength) {
            this.setMaxLength(Integer.parseInt(maxLength));
        }
        String minLength = this.extractValue(validationElement, "minlength");
        if (null != minLength) {
            this.setMinLength(Integer.parseInt(minLength));
        }
        String regexp = this.extractValue(validationElement, "regexp");
        if (null != regexp && regexp.trim().length() > 0) {
            this.setRegexp(regexp);
        }
        Element valueElement = validationElement.getChild("value");
        if (null != valueElement) {
            this.setValue(valueElement.getText());
            this.setValueAttribute(valueElement.getAttributeValue("attribute"));
        }
        Element rangeStartElement = validationElement.getChild("rangestart");
        if (null != rangeStartElement) {
            this.setRangeStart(rangeStartElement.getText());
            this.setRangeStartAttribute(rangeStartElement.getAttributeValue("attribute"));
        }
        Element rangeEndElement = validationElement.getChild("rangeend");
        if (null != rangeEndElement) {
            this.setRangeEnd(rangeEndElement.getText());
            this.setRangeEndAttribute(rangeEndElement.getAttributeValue("attribute"));
        }
    }
    
	@Override
    public List<AttributeFieldError> validate(AttributeInterface attribute, AttributeTracer tracer, ILangManager langManager) {
        List<AttributeFieldError> errors = super.validate(attribute, tracer, langManager);
        if (this.isEmpty()) {
			return errors;
		}
        try {
            List<Lang> langs = langManager.getLangs();
            for (int i = 0; i < langs.size(); i++) {
                Lang lang = langs.get(i);
                if (!attribute.isMultilingual() && !lang.isDefault()) continue;
                AttributeTracer textTracer = (AttributeTracer) tracer.clone();
                textTracer.setLang(lang);
                this.checkTextLengths(attribute, textTracer, lang, errors);
                this.checkRegExp(attribute, textTracer, lang, errors);
            }
        } catch (Throwable t) {
            //ApsSystemUtils.logThrowable(t, this, "validate");
            _logger.error("Error validating text attribute", t);
            throw new RuntimeException("Error validating text attribute", t);
        }
        return errors;
    }
    
    protected void checkTextLengths(AttributeInterface attribute, AttributeTracer tracer, Lang lang, List<AttributeFieldError> errors) {
        Integer maxLength = this.getMaxLength();
        Integer minLength = this.getMinLength();
        if ((null != maxLength && maxLength > -1) || (null != minLength && minLength > -1)) {
            String text = this.getTextForCheckLength(attribute, lang);
            if (text != null && text.trim().length() > 0) {
                text = text.trim();
                if ((null != maxLength && maxLength > -1) && text.length() > maxLength) {
					AttributeFieldError error = new AttributeFieldError(attribute, FieldError.INVALID_MAX_LENGTH, tracer);
                    error.setMessage("Lang '" + lang.getDescr() + "' -  length " + text.length() + " upper than " + maxLength);
                    errors.add(error);
                }
                if ((null != minLength && minLength > -1) && text.length() < minLength) {
                    AttributeFieldError error = new AttributeFieldError(attribute, FieldError.INVALID_MIN_LENGTH, tracer);
                    error.setMessage("Lang '" + lang.getDescr() + "' -  length " + text.length() + " lower than " + minLength);
                    errors.add(error);
                }
            }
        }
    }
    
    protected String getTextForCheckLength(AttributeInterface attribute, Lang lang) {
        String text = ((ITextAttribute) attribute).getTextForLang(lang.getCode());
        if (text != null && attribute instanceof HypertextAttribute) {
            // remove HTML tags, entities an multiple spaces
            text = text.replaceAll("<[^<>]+>", " ").replaceAll("&nbsp;", " ").replaceAll("\\&[^\\&;]+;", "_").replaceAll("([\t\n\r\f ])++", " ").trim();
        }
        return text;
    }
    
    protected void checkRegExp(AttributeInterface attribute, AttributeTracer tracer, Lang lang, List<AttributeFieldError> errors) {
        String text = ((ITextAttribute)attribute).getTextForLang(lang.getCode());
        if (null != text && text.trim().length() > 0 && null != this.getRegexp() && this.getRegexp().trim().length() > 0) {
            Pattern pattern = Pattern.compile(this.getRegexp());
            Matcher matcher = pattern.matcher(text);
            if (!matcher.matches()) {
                AttributeFieldError error = new AttributeFieldError(attribute, FieldError.INVALID_FORMAT, tracer);
                error.setMessage("Lang '" + lang.getDescr() + "' - invalid format");
                errors.add(error);
            }
        }
    }
    
	@Override
    public boolean isEmpty() {
        return (super.isEmpty()
                && (null == this.getMaxLength() || this.getMaxLength() < 0)
                && (null == this.getMinLength() || this.getMinLength() < 0)
                && (null == this.getRegexp() || this.getRegexp().trim().length() == 0));
    }
    
    public Integer getMaxLength() {
        return _maxLength;
    }
    public void setMaxLength(Integer maxLength) {
        this._maxLength = maxLength;
    }

    public Integer getMinLength() {
        return _minLength;
    }
    public void setMinLength(Integer minLength) {
        this._minLength = minLength;
    }

    public String getRegexp() {
        return _regexp;
    }
    public void setRegexp(String regexp) {
        this._regexp = regexp;
    }
    
    private Integer _maxLength;
    private Integer _minLength;
    private String _regexp;
    
}
