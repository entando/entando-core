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
package org.entando.entando.web.common;

import com.agiletec.aps.system.common.FieldSearchFilter;
import java.util.ArrayList;
import java.util.List;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author paddeo
 */
public class PagedMetadataTest {

    @Test
    public void shuold_create_filters() {

        RestListRequest request = new RestListRequest();
        request.setPage(1);
        request.setPageSize(25);

        request.setSort("outerCode");
        request.setDirection(FieldSearchFilter.Order.ASC.name());

        request.addFilter(new Filter("outerCode", "code"));

        List<Outer> list = createList();

        PagedMetadata<Outer> result = new PagedMetadata<>(request, list, list.size());

        assertThat(result.getBody().size(), is(10));

        //request with inner class filter
        request = new RestListRequest();
        request.setPage(1);
        request.setPageSize(25);

        request.setSort("outerCode");
        request.setDirection(FieldSearchFilter.Order.ASC.name());

        request.addFilter(new Filter("inner.innerCode", "code"));

        result = new PagedMetadata<>(request, list, list.size());
        assertThat(result.getBody().size(), is(5));
    }

    private List<Outer> createList() {
        List<Outer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Outer dto = new Outer();
            dto.setOuterCode("code" + i);
            list.add(dto);
        }
        for (int i = 5; i < 10; i++) {
            Outer dto = new Outer();
            dto.setOuterCode("code" + i);
            dto.setInner(dto.new Inner("code" + i));
            list.add(dto);
        }
        return list;
    }

    class Outer {

        String outerCode;
        Inner inner;

        public String getOuterCode() {
            return outerCode;
        }

        public void setOuterCode(String outerCode) {
            this.outerCode = outerCode;
        }

        public Inner getInner() {
            return inner;
        }

        public void setInner(Inner inner) {
            this.inner = inner;
        }

        class Inner {

            String innerCode;

            public Inner(String innerCode) {
                this.innerCode = innerCode;
            }

            public String getInnerCode() {
                return innerCode;
            }

            public void setInnerCode(String innerCode) {
                this.innerCode = innerCode;
            }

        }
    }

}
