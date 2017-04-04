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