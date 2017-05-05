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
package org.entando.entando.plugins.jacms.apsadmin.content.bulk.util;

import java.util.Date;

import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;

public class SmallBulkCommandReport {
	
	public String getCommandId() {
		return commandId;
	}
	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public int getApplyTotal() {
		return applyTotal;
	}
	public void setApplyTotal(int applyTotal) {
		this.applyTotal = applyTotal;
	}
	
	public int getApplySuccesses() {
		return applySuccesses;
	}
	public void setApplySuccesses(int applySuccesses) {
		this.applySuccesses = applySuccesses;
	}
	
	public int getApplyErrors() {
		return applyErrors;
	}
	public void setApplyErrors(int applyErrors) {
		this.applyErrors = applyErrors;
	}
	
	public ApsCommandStatus getStatus() {
		return status;
	}
	public void setStatus(ApsCommandStatus status) {
		this.status = status;
	}
	
	public Date getEndingTime() {
		return endingTime;
	}
	public void setEndingTime(Date endingTime) {
		this.endingTime = endingTime;
	}
	
	private String commandId;
	private int total;
	private int applyTotal;
	private int applySuccesses;
	private int applyErrors;
	private ApsCommandStatus status;
	private Date endingTime;
	
}