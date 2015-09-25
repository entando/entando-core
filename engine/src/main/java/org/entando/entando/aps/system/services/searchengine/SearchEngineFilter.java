/*
 * Copyright 2013-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.aps.system.services.searchengine;

import com.agiletec.aps.system.common.FieldSearchFilter;

import java.io.Serializable;
import java.util.List;

/**
 * @author E.Santoboni
 */
public class SearchEngineFilter extends FieldSearchFilter implements Serializable {
	
	public SearchEngineFilter(String key, Object value) {
		this(key, value, TextSearchOption.AT_LEAST_ONE_WORD);
	}
	
	public SearchEngineFilter(String key, Object value, TextSearchOption textSearchOption) {
		super(key, value, false);
		this.setTextSearchOption(textSearchOption);
	}
	
	public SearchEngineFilter(String key, List allowedValues, TextSearchOption textSearchOption) {
		super(key, allowedValues, false);
		this.setTextSearchOption(textSearchOption);
	}
	
	public SearchEngineFilter(String key, Object start, Object end) {
		super(key, start, end);
	}
	
	public TextSearchOption getTextSearchOption() {
		if (null == this._textSearchOption && super.isNullOption()) {
			return TextSearchOption.ANY_WORD;
		}
		return _textSearchOption;
	}
	protected void setTextSearchOption(TextSearchOption textSearchOption) {
		this._textSearchOption = textSearchOption;
	}
	
	public boolean isIncludeAttachments() {
		return _includeAttachments;
	}
	public void setIncludeAttachments(boolean includeAttachments) {
		this._includeAttachments = includeAttachments;
	}
	
	private TextSearchOption _textSearchOption;
	private boolean _includeAttachments;
	
	public enum TextSearchOption {EXACT, ALL_WORDS, AT_LEAST_ONE_WORD, ANY_WORD}
	
}
