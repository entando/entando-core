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
package com.agiletec.aps.system.services.group;

import java.util.List;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.FieldSearchFilter.LikeOptionType;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.apache.commons.lang3.ArrayUtils;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author E.Santoboni
 */
public class TestGroupManager extends BaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

    public void testGetGroups() {
        List<Group> groups = this._groupManager.getGroups();
        assertTrue(groups.size() >= 6);
    }

    public void testAddDeleteGroup() throws Throwable {
        int initSize = this._groupManager.getGroups().size();
        String groupCode = "Gruppo_Prova";
        Group group = new Group();
        group.setName(groupCode);
        group.setDescription("descr_gruppo_prova");
        try {
            assertNull(_groupManager.getGroup(groupCode));
            _groupManager.addGroup(group);
            List<Group> groups = _groupManager.getGroups();
            assertEquals(initSize + 1, groups.size());
            assertNotNull(_groupManager.getGroup(groupCode));
            _groupManager.removeGroup(group);
            groups = _groupManager.getGroups();
            assertEquals(initSize, groups.size());
            assertNull(_groupManager.getGroup(groupCode));
        } catch (Throwable t) {
            throw t;
        } finally {
            _groupManager.removeGroup(group);
        }
    }

    public void testUpdateGroup() throws Throwable {
        int initSize = this._groupManager.getGroups().size();
        Group group = new Group();
        String groupCode = "Gruppo_Prova";
        group.setName(groupCode);
        group.setDescription("descr_gruppo_prova");
        try {
            assertNull(_groupManager.getGroup(groupCode));
            _groupManager.addGroup(group);
            List<Group> groups = _groupManager.getGroups();
            assertEquals(initSize + 1, groups.size());

            Group groupNew = new Group();
            groupNew.setName(groupCode);
            groupNew.setDescription("Nuova_descr");
            _groupManager.updateGroup(groupNew);
            Group extracted = _groupManager.getGroup(groupCode);
            assertEquals(groupNew.getDescription(), extracted.getDescription());

            _groupManager.removeGroup(group);
            groups = _groupManager.getGroups();
            assertEquals(initSize, groups.size());
            assertNull(_groupManager.getGroup(groupCode));
        } catch (Throwable t) {
            throw t;
        } finally {
            _groupManager.removeGroup(group);
        }
    }

    @SuppressWarnings("rawtypes")
    public void test_search_should_return_all_results() throws ApsSystemException {
        FieldSearchFilter[] fieldSearchFilters = null;
        SearcherDaoPaginatedResult<Group> result = this._groupManager.getGroups(fieldSearchFilters);
        assertThat(result.getCount(), is(6));
        assertThat(result.getList().size(), is(6));

        fieldSearchFilters = new FieldSearchFilter[0];
        result = this._groupManager.getGroups(fieldSearchFilters);
        assertThat(result.getCount(), is(6));
        assertThat(result.getList().size(), is(6));
    }

    @SuppressWarnings("rawtypes")
    public void test_search_by_filter() throws ApsSystemException {
        FieldSearchFilter[] fieldSearchFilters = new FieldSearchFilter[0];

        FieldSearchFilter groupNameFilter = new FieldSearchFilter<>("groupname", "s", true, LikeOptionType.COMPLETE);
        fieldSearchFilters = ArrayUtils.add(fieldSearchFilters, groupNameFilter);

        SearcherDaoPaginatedResult<Group> result = this._groupManager.getGroups(fieldSearchFilters);
        assertThat(result.getCount(), is(3));
        assertThat(result.getList().size(), is(3));

        fieldSearchFilters = new FieldSearchFilter[0];
        FieldSearchFilter limitFilter = new FieldSearchFilter<>(2, 0);
        fieldSearchFilters = ArrayUtils.add(fieldSearchFilters, groupNameFilter);
        fieldSearchFilters = ArrayUtils.add(fieldSearchFilters, limitFilter);
        result = this._groupManager.getGroups(fieldSearchFilters);
        assertThat(result.getCount(), is(3));
        assertThat(result.getList().size(), is(2));

        fieldSearchFilters = new FieldSearchFilter[0];
        limitFilter = new FieldSearchFilter<>(2, 2);
        fieldSearchFilters = ArrayUtils.add(fieldSearchFilters, limitFilter);
        fieldSearchFilters = ArrayUtils.add(fieldSearchFilters, groupNameFilter);
        result = this._groupManager.getGroups(fieldSearchFilters);
        assertThat(result.getCount(), is(3));
        assertThat(result.getList().size(), is(1));

    }

    private void init() throws Exception {
        try {
            _groupManager = (IGroupManager) this.getService(SystemConstants.GROUP_MANAGER);
        } catch (Exception e) {
            throw e;
        }
    }

    private IGroupManager _groupManager = null;

}
