package org.entando.entando.web.common.model;

import java.util.List;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

public class PagedMetadata<T> {

	private int page;
	private int size;
	private int last;
    private int count;

    //@JsonIgnore
	private List<T> body;

    public PagedMetadata() {
		//
	}


    public PagedMetadata(RestListRequest req, SearcherDaoPaginatedResult<?> result) {
        this.page = req.getPageNum();
        this.size = result.getList().size();
        Double pages = Math.ceil((new Double(result.getCount()) / new Double(req.getPageSize())));
        this.last = pages.intValue() - 1;
        this.count = result.getCount();
    }

    public PagedMetadata(int page, int size, int last, int count) {
		this.page = page;
		this.size = size;
		this.last = last;
        this.count = count;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
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
