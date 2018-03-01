/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.web.common.model;

import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class PagedMetadata<T> {

	private int page;
	private int pageSize;
	private int lastPage;
	private int count;

	@JsonIgnore
	private List<T> body;

	public PagedMetadata() {
	}

	public PagedMetadata(RestListRequest req, SearcherDaoPaginatedResult<?> result) {
		this.page = req.getPageNum();
		this.pageSize = result.getList().size();
		Double pages = Math.ceil((new Double(result.getCount()) / new Double(req.getPageSize())));
		this.lastPage = pages.intValue() - 1;
		this.count = result.getCount();
	}

	public PagedMetadata(int page, int size, int last, int count) {
		this.page = page;
		this.pageSize = size;
		this.lastPage = last;
		this.count = count;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int size) {
		this.pageSize = size;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int last) {
		this.lastPage = last;
	}

	public List<T> getBody() {
		return body;
	}

	public void setBody(List<T> body) {
		this.body = body;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
