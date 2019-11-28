/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
public class SearchEngineFilter<T> extends FieldSearchFilter<T> implements Serializable {
    
    private TextSearchOption textSearchOption;
    
    private boolean fullTextSearch;
    private boolean attributeFilter;
    private String langCode;
    
    public enum TextSearchOption {EXACT, ALL_WORDS, AT_LEAST_ONE_WORD, ANY_WORD}
	
	public SearchEngineFilter(String key, T value) {
		this(key, value, TextSearchOption.AT_LEAST_ONE_WORD);
	}
	
	public SearchEngineFilter(String key, T value, TextSearchOption textSearchOption) {
		super(key, value, false);
		this.setTextSearchOption(textSearchOption);
	}
	
	public SearchEngineFilter(String key, List<T> allowedValues, TextSearchOption textSearchOption) {
		super(key, allowedValues, false);
		this.setTextSearchOption(textSearchOption);
	}
	
	public SearchEngineFilter(String key, T start, T end) {
		super(key, start, end);
	}
    
    public SearchEngineFilter(String key, boolean attributeFilter) {
        this(key, null);
        this.setAttributeFilter(attributeFilter);
    }

    public SearchEngineFilter(String key, boolean attributeFilter, T value) {
        this(key, value);
        this.setAttributeFilter(attributeFilter);
    }

    public SearchEngineFilter(String key, boolean attributeFilter, T value, TextSearchOption textSearchOption) {
        this(key, value, textSearchOption);
        this.setAttributeFilter(attributeFilter);
    }

    public static SearchEngineFilter createAllowedValuesFilter(String key, boolean attributeFilter, List allowedValues, TextSearchOption textSearchOption) {
        SearchEngineFilter filter = new SearchEngineFilter(key, attributeFilter);
        filter.setAllowedValues(allowedValues);
        filter.setTextSearchOption(textSearchOption);
        return filter;
    }

    public static SearchEngineFilter createRangeFilter(String key, boolean attributeFilter, Object start, Object end) {
        SearchEngineFilter filter = new SearchEngineFilter(key, attributeFilter);
        filter.setStart(start);
        filter.setEnd(end);
        return filter;
    }
	
	public TextSearchOption getTextSearchOption() {
		if (null == this.textSearchOption && super.isNullOption()) {
			return TextSearchOption.ANY_WORD;
		}
		return textSearchOption;
	}
	protected void setTextSearchOption(TextSearchOption textSearchOption) {
		this.textSearchOption = textSearchOption;
	}

    public boolean isFullTextSearch() {
        return fullTextSearch;
    }

    public void setFullTextSearch(boolean fullTextSearch) {
        this.fullTextSearch = fullTextSearch;
    }

    public boolean isAttributeFilter() {
        return attributeFilter;
    }

    public void setAttributeFilter(boolean attributeFilter) {
        this.attributeFilter = attributeFilter;
    }

    public String getLangCode() {
        return langCode;
    }

    public void setLangCode(String langCode) {
        this.langCode = langCode;
    }

    @Override
    public void setLikeOption(boolean likeOption) {
        if (likeOption && null != this.getTextSearchOption() && this.getTextSearchOption() == TextSearchOption.EXACT) {
            throw new RuntimeException("Error: the 'likeOption' can't be apply if textsearchOption is setted on 'EXACT'");
        }
        super.setLikeOption(likeOption);
    }
	
}
