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
package org.entando.entando.aps.system.common.command.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.ApsCommand;
import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;
import org.entando.entando.aps.system.common.command.constants.ApsCommandWarningCode;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;


/**
 * The default report for a bulk {@link ApsCommand}.
 * It's a wrapper of the {@link BaseBulkCommand} and of its {@link BulkCommandTracer}.
 * @author E.Mezzano
 *
 * @param <I> The type of items on which the command is applied.
 */
public class DefaultBulkCommandReport<I> implements BulkCommandReport<I> {

	/**
	 * The constructor for the report.
	 * @param command The bulk command.
	 */
	public DefaultBulkCommandReport(BaseBulkCommand<I, ?, ?> command) {
		this._command = command;
	}

	@Override
	public String getCommandId() {
		return this._command.getId();
	}

	@Override
	public int getTotal() {
		return this._command.getTotal();
	}

	@Override
	public int getApplyTotal() {
		return this._command.getApplySuccesses() + this._command.getApplyErrors();
	}

	@Override
	public int getApplySuccesses() {
		return this._command.getApplySuccesses();
	}

	@Override
	public int getApplyErrors() {
		return this._command.getApplyErrors();
	}

	@Override
	public List<I> getSuccesses() {
		return this._command.getTracer().getSuccesses();
	}

	@Override
	public Map<I, ApsCommandWarningCode> getWarnings() {
		return this._command.getTracer().getWarnings();
	}

	@Override
	public Map<I, ApsCommandErrorCode> getErrors() {
		return this._command.getTracer().getErrors();
	}

	@Override
	public ApsCommandStatus getStatus() {
		return this._command.getStatus();
	}

	@Override
	public Date getEndingTime() {
		return this._command.getEndingTime();
	}
	
	private BaseBulkCommand<I, ?, ?> _command;

}
