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
package org.entando.entando.aps.system.services.widgettype;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;

/**
 * Classe di supporto all'interpretazione dell'XML che rappresenta la configurazione di un tipo di widget.
 * @author E.Santoboni
 */
public class WidgetTypeDOM {

	private static final Logger _logger =  LoggerFactory.getLogger(WidgetTypeDOM.class);
	
	/**
	 * Costruttore della classe.
	 * @param xmlText La stringa xml da interpretare.
	 * @throws ApsSystemException In caso di errore 
	 * nell'interpretazione dell'xml di configurazione.
	 */
	public WidgetTypeDOM(String xmlText) throws ApsSystemException {
		this.decodeDOM(xmlText);
	}
	
	public WidgetTypeDOM(String xmlText, List<Lang> langs) throws ApsSystemException {
		this.decodeDOM(xmlText);
		this.setLangs(langs);
	}
	
	public WidgetTypeDOM(List<WidgetTypeParameter> parameters, String action) throws ApsSystemException {
		this.setDoc(new Document());
		Element root = new Element("config");
		if (null != parameters && parameters.size() > 0) {
			for (int i = 0; i < parameters.size(); i++) {
				WidgetTypeParameter parameter = parameters.get(i);
				Element paramElement = new Element(TAB_PARAMETER);
				paramElement.setAttribute("name", parameter.getName());
				if (null != parameter.getDescr()) {
					paramElement.setText(parameter.getDescr());
				}
				root.addContent(paramElement);
			}
		}
		if (null != action) {
			Element actionElement = new Element(TAB_ACTION);
			actionElement.setAttribute("name", action);
			root.addContent(actionElement);
		}
		this.getDoc().setRootElement(root);
	}
	
	/**
	 * Restituisce la lista (in oggetti WidgetTypeParameter) 
	 * di parametri di configurazione della showlet.
	 * @return La lista dei parametri di configurazione della showlet.
	 */
	public List<WidgetTypeParameter> getParameters() {
		List<WidgetTypeParameter> parameters = null;
		List<Element> paramElements = this.getDoc().getRootElement().getChildren(TAB_PARAMETER);
		if (null != paramElements && paramElements.size() > 0) {
			parameters = new ArrayList<WidgetTypeParameter>();
			Iterator<Element> paramElementsIter = paramElements.iterator();
			while (paramElementsIter.hasNext()) {
				Element parameterElement = paramElementsIter.next();
				this.createParameters(parameters, parameterElement);
			}
		}
		return parameters;
	}
	
	protected void createParameters(List<WidgetTypeParameter> parameters, Element parameterElement) {
		String name = parameterElement.getAttributeValue("name");
		String description = parameterElement.getText();
		if (name.indexOf("{lang}") > 0) {
			for (int i=0; i<this.getLangs().size(); i++) {
				Lang lang = this.getLangs().get(i);
				String newName = name.replace("{lang}", lang.getCode());
				String newDescription = description;
				if (null != description && description.indexOf("{lang}") > 0) {
					newDescription = description.replace("{lang}", lang.getCode());
				}
				this.addParameter(parameters, newName, newDescription);
			}
		} else {
			this.addParameter(parameters, name, description);
		}
	}
	
	protected void addParameter(List<WidgetTypeParameter> parameters, String name, String description) {
		WidgetTypeParameter parameter = new WidgetTypeParameter();
		parameter.setName(name);
		if (null != description) {
			parameter.setDescr(description.trim());
		}
		parameters.add(parameter);
	}
	
	/**
	 * Restituisce la stringa identificatrice l'action specifica per quella showlet.
	 * @return La stringa identificatrice l'action specifica.
	 */
	public String getAction() {
		String action = null;
		Element actionElement = this.getDoc().getRootElement().getChild(TAB_ACTION);
		if (null != actionElement) {
			action = actionElement.getAttributeValue("name");
		}
		return action;
	}
	
	public String getXMLDocument(){
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("");
		out.setFormat(format);
		return out.outputString(this.getDoc());
	}
	
	private void decodeDOM(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		try {
			this.setDoc(builder.build(reader));
		} catch (Throwable t) {
			_logger.error("Error detected while parsing the XML {}", xmlText, t);
			//ApsSystemUtils.logThrowable(t, this, "decodeDOM");
			throw new ApsSystemException("Error detected while parsing the XML", t);
		}
	}
	
	protected Document getDoc() {
		return _doc;
	}
	protected void setDoc(Document doc) {
		this._doc = doc;
	}
	
	protected List<Lang> getLangs() {
		return _langs;
	}
	protected void setLangs(List<Lang> langs) {
		this._langs = langs;
	}
	
	private Document _doc;
	private List<Lang> _langs;
	
	private final String TAB_PARAMETER = "parameter";
	private final String TAB_ACTION = "action";
	
}
