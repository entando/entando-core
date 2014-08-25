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

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

import org.jdom.CDATA;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * @author E.Santoboni
 */
public class OgnlValidationRule implements Serializable {

	private static final Logger _logger =  LoggerFactory.getLogger(OgnlValidationRule.class);
	
    public OgnlValidationRule() {}
    
    public OgnlValidationRule(Element element) {
        if (null == element) {
            throw new RuntimeException("null jdom element");
        }
        String eval = element.getAttributeValue("evalOnValuedAttribute");
        this.setEvalExpressionOnValuedAttribute(null != eval && eval.equalsIgnoreCase("true"));
        Element ognlExpressionElement = element.getChild("ognlexpression");
        this.setExpression(ognlExpressionElement.getText());
        Element errorMessageElement = element.getChild("errormessage");
        if (null != errorMessageElement) {
            this.setErrorMessage(errorMessageElement.getText());
            this.setErrorMessageKey(errorMessageElement.getAttributeValue("key"));
        }
        Element helpMessageElement = element.getChild("helpmessage");
        if (null != helpMessageElement) {
            this.setHelpMessage(helpMessageElement.getText());
            this.setHelpMessageKey(helpMessageElement.getAttributeValue("key"));
        }
    }
    
    protected OgnlValidationRule clone() {
        OgnlValidationRule clone = new OgnlValidationRule();
        clone.setErrorMessage(this.getErrorMessage());
        clone.setErrorMessageKey(this.getErrorMessageKey());
        clone.setEvalExpressionOnValuedAttribute(this.isEvalExpressionOnValuedAttribute());
        clone.setExpression(this.getExpression());
        clone.setHelpMessage(this.getHelpMessage());
        clone.setHelpMessageKey(this.getHelpMessageKey());
        return clone;
    }
    
    public Element getConfigElement() {
        if (null == this.getExpression() || this.getExpression().trim().length() == 0) {
            return null;
        }
        Element exprElement = new Element("expression");
        exprElement.setAttribute("evalOnValuedAttribute", String.valueOf(this.isEvalExpressionOnValuedAttribute()));
        Element ognlExprElement = new Element("ognlexpression");
        CDATA cdata = new CDATA(this.getExpression());
        ognlExprElement.addContent(cdata);
        exprElement.addContent(ognlExprElement);
        Element errorMessageElement = new Element("errormessage");
        if (null != this.getErrorMessageKey() && this.getErrorMessageKey().trim().length() > 0) {
            errorMessageElement.setAttribute("key", this.getErrorMessageKey());
        }
        if (null != this.getErrorMessage() && this.getErrorMessage().trim().length() > 0) {
            CDATA label = new CDATA(this.getErrorMessage());
            errorMessageElement.addContent(label);
        }
        exprElement.addContent(errorMessageElement);
        Element helpMessageElement = new Element("helpmessage");
        if (null != this.getHelpMessageKey() && this.getHelpMessageKey().trim().length() > 0) {
            helpMessageElement.setAttribute("key", this.getHelpMessageKey());
        }
        if (null != this.getHelpMessage() && this.getHelpMessage().trim().length() > 0) {
            CDATA label = new CDATA(this.getHelpMessage());
            helpMessageElement.addContent(label);
        }
        exprElement.addContent(helpMessageElement);
        return exprElement;
    }
    
    public AttributeFieldError validate(AttributeInterface attribute, AttributeTracer tracer, ILangManager langManager) {
        AttributeFieldError error = null;
        String expression = this.getExpression();
        if (null == expression || expression.trim().length() == 0) {
            return null;
        }
        if (this.isEvalExpressionOnValuedAttribute() && attribute.getStatus().equals(AttributeInterface.Status.EMPTY)) {
            return null;
        }
        try {
            Object expr = Ognl.parseExpression(expression);
            OgnlContext ctx = this.createContextForExpressionValidation(attribute, tracer, langManager);
            Boolean value = (Boolean) Ognl.getValue(expr, ctx, attribute, Boolean.class);
            if (!value) {
                error = new AttributeFieldError(attribute, AttributeFieldError.OGNL_VALIDATION, tracer);
                error.setMessage(this.getErrorMessage());
                error.setMessageKey(this.getErrorMessageKey());
            }
        } catch (OgnlException oe) {
            //ApsSystemUtils.logThrowable(oe, this, "checkExpression", "Error on evaluation of expression : " + expression);
            _logger.error("Error on evaluation of expression : {}", expression, oe);
        } catch (Throwable t) {
            //ApsSystemUtils.logThrowable(t, this, "checkExpression");
            _logger.error("Generic Error on evaluation Ognl Expression : {}", expression, t);
            throw new RuntimeException("Generic Error on evaluation Ognl Expression", t);
        }
        return error;
    }
    
    protected OgnlContext createContextForExpressionValidation(AttributeInterface attribute, AttributeTracer tracer, ILangManager langManager) {
        OgnlContext context = new OgnlContext();
        Map<String, Lang> langs = new HashMap<String, Lang>();
        List<Lang> langList = langManager.getLangs();
        for (int i = 0; i < langList.size(); i++) {
            Lang lang = langList.get(i);
            langs.put(lang.getCode(), lang);
        }
        context.put("langs", langs);
        context.put("attribute", attribute);
        context.put("entity", attribute.getParentEntity());
        if (tracer.isCompositeElement()) {
            context.put("parent", tracer.getParentAttribute());
        } else {
            if (tracer.isListElement() || tracer.isMonoListElement()) {
                context.put("parent", attribute.getParentEntity().getAttribute(attribute.getName()));
                context.put("index", tracer.getListIndex());
            }
            if (tracer.isListElement()) {
                context.put("listLang", tracer.getListLang());
            }
        }
        return context;
    }
    
    public String getExpression() {
        return _expression;
    }
    public void setExpression(String expression) {
        this._expression = expression;
    }

    public boolean isEvalExpressionOnValuedAttribute() {
        return _evalExpressionOnValuedAttribute;
    }
    public void setEvalExpressionOnValuedAttribute(boolean evalExpressionOnValuedAttribute) {
        this._evalExpressionOnValuedAttribute = evalExpressionOnValuedAttribute;
    }

    public String getErrorMessage() {
        return _errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this._errorMessage = errorMessage;
    }

    public String getErrorMessageKey() {
        return _errorMessageKey;
    }
    public void setErrorMessageKey(String errorMessageKey) {
        this._errorMessageKey = errorMessageKey;
    }

    public String getHelpMessage() {
        return _helpMessage;
    }
    public void setHelpMessage(String helpMessage) {
        this._helpMessage = helpMessage;
    }

    public String getHelpMessageKey() {
        return _helpMessageKey;
    }
    public void setHelpMessageKey(String helpMessageKey) {
        this._helpMessageKey = helpMessageKey;
    }
    
    private String _expression;
    private boolean _evalExpressionOnValuedAttribute;
    private String _errorMessage;
    private String _errorMessageKey;
    private String _helpMessage;
    private String _helpMessageKey;
    
}