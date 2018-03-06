package org.entando.entando.aps.system.services.group;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class RestListRequestTest {

    @Test
    public void shuold_create_filters() {

        RestListRequest request = new RestListRequest();
        request.setPage(1);
        request.setPageSize(25);

        request.setSort("name");
        request.setDirection(FieldSearchFilter.Order.ASC.name());

        request.addFilter(new Filter("name", "jack"));
        request.addFilter(new Filter("city", "rome"));
        request.addFilter(new Filter("mobile", "+39"));

        //filters
        List<FieldSearchFilter> filters = request.buildFieldSearchFilters();
        assertThat(filters.size(), is(5));

        assertThat(filters.get(0).getKey(), is("name"));
        assertThat(filters.get(1).getKey(), is("city"));
        assertThat(filters.get(2).getKey(), is("mobile"));

        //pagination
        assertThat(filters.get(3).getKey(), is(nullValue()));
        assertThat(filters.get(3).getLimit(), is(not(nullValue())));
        assertThat(filters.get(3).getOffset(), is(not(nullValue())));

        //sort
        assertThat(filters.get(4).getKey(), is("name"));
        assertThat(filters.get(4).getOrder(), is(FieldSearchFilter.Order.ASC));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void should_create_default_pagination() {
        RestListRequest request = new RestListRequest();
        //filters
        List<FieldSearchFilter> filters = request.buildFieldSearchFilters();
        assertThat(filters.size(), is(2));
        //pagination
        assertThat(filters.get(0).getKey(), is(nullValue()));
        assertThat(filters.get(0).getLimit(), is(not(nullValue())));
        assertThat(filters.get(0).getOffset(), is(not(nullValue())));

    }

    @Test
    public void should_exclude_pagination_when_pagesize_0() {

        RestListRequest request = new RestListRequest();
        request.setPage(1);
        request.setPageSize(0);

        request.setSort("name");
        request.setDirection(FieldSearchFilter.Order.ASC.name());

        request.addFilter(new Filter("name", "jack"));
        request.addFilter(new Filter("city", "rome"));
        request.addFilter(new Filter("mobile", "+39"));

        //filters
        List<FieldSearchFilter> filters = request.buildFieldSearchFilters();
        assertThat(filters.size(), is(4));

        assertThat(filters.get(0).getKey(), is("name"));
        assertThat(filters.get(1).getKey(), is("city"));
        assertThat(filters.get(2).getKey(), is("mobile"));

        //sort
        assertThat(filters.get(3).getKey(), is("name"));
        assertThat(filters.get(3).getOrder(), is(FieldSearchFilter.Order.ASC));
    }

    @Test
    public void should_default_direction() {

        RestListRequest request = new RestListRequest();
        request.setPage(1);
        request.setPageSize(0);

        request.setSort("name");
        request.setDirection("wrong");

        //filters
        List<FieldSearchFilter> filters = request.buildFieldSearchFilters();
        assertThat(filters.size(), is(1));

        assertThat(filters.get(0).getOrder(), is(FieldSearchFilter.Order.ASC));
    }

}
