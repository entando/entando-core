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
package org.entando.entando.aps.system.services.actionlog;

import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.common.FieldSearchFilter;
import java.util.Date;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.IActionLogRecordSearchBean;
import org.entando.entando.aps.system.services.actionlog.model.IActivityStreamSearchBean;

/**
 * @author E.Santoboni - S.Puddu
 */
public interface IActionLogDAO {
	
	public List<Integer> getActionRecords(IActionLogRecordSearchBean searchBean);
	
	public List<Integer> getActivityStreamRecords(IActivityStreamSearchBean searchBean);
	
	public List<Integer> getActionRecords(FieldSearchFilter[] filters);
	
	public List<Integer> getActionRecords(FieldSearchFilter[] filters, List<String> userGroupCodes);
	
	public void addActionRecord(ActionLogRecord actionRecord);
	
	public ActionLogRecord getActionRecord(int id);
	
	public void deleteActionRecord(int id);
    
    public Date getLastUpdate(IActionLogRecordSearchBean searchBean);
    
	public Date getLastUpdate(FieldSearchFilter[] filters, List<String> userGroupCodes);
    
	public Set<Integer> extractOldRecords(Integer maxActivitySizeByGroup);
    
	public void updateRecordDate(int id);

    public int countActionLogRecords(IActionLogRecordSearchBean searchBean);
	
}
