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
package com.agiletec.aps.system.common.entity.model.attribute;

/**
 * This interface is for 'Text' Attributes that support multiple languages.
 * @author E.Santoboni
 */
public interface ITextAttribute {
	
	/**
	 * Return the 'text' contained in this attribute in the current or in the 
	 * default language if the former is not available.
	 * @return The associated text.
	 */
	public String getText();
	
	/**
	 * Return the 'text' contained in this attribute in the given language.
	 * @param langCode The code of the language.
	 * @return The requested text.
	 */
	public String getTextForLang(String langCode);
	
	/**
	 * Associate a text in a given language to this attribute. 
	 * @param text The text to associate.
	 * @param langCode The code of the language.
	 */
	public void setText(String text, String langCode);
	
	/**
	 * Check whether the texts of this attribute needs a the conversion of 
	 * the 'special characters' before the rendering process (in the presentation layer).
	 * @return true if the text need special treatment for the HTML entities, false otherwise.
	 */
	public boolean needToConvertSpecialCharacter();
	
	/**
	 * Return the max length of the text.
	 * @return The maximum length of the text.
	 */
	public int getMaxLength();
	
	/**
	 * Set up the maximum length of the text.
	 * @param maxLength The maximum length of the text.
	 * @deprecated to guaranted compatibility with previsous version of jAPS 2.0.12 
	 */
	public void setMaxLength(int maxLength);
	
	/**
	 * Get the minimum length of the text.
	 * @return The minimum length of the text.
	 */
	public int getMinLength();
	
	/**
	 * Set up the minimum length of the text.
	 * @param minLength The minimum length of the text.
	 * @deprecated to guaranted compatibility with previsous version of jAPS 2.0.12 
	 */
	public void setMinLength(int minLength);
	
	/**
	 * Get the regular expression.
	 * @return The regular expression.
	 */
	public String getRegexp();
	
	/**
	 * Return the regular expression.
	 * @param regexp The regular expression.
	 * @deprecated to guaranted compatibility with previsous version of jAPS 2.0.12
	 */
	public void setRegexp(String regexp);
	
}