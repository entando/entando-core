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
package org.entando.entando.aps.system.services.command;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.ApsCommandWarningCode;
import org.entando.entando.aps.system.common.command.context.BaseBulkCommandContext;

import com.agiletec.aps.system.exception.ApsSystemException;

public class FakeBulkCommand extends BaseBulkCommand<String, Object, BaseBulkCommandContext<String>> {

	public FakeBulkCommand(BaseBulkCommandContext<String> context) {
		this.setContext(context);
	}

	public FakeBulkCommand(BaseBulkCommandContext<String> context, CountDownLatch startSignal, CountDownLatch endSignal) {
		this(context);
		this._startSignal = startSignal;
		this._endSignal = endSignal;
	}

	@Override
	public void apply() {
		try {
			if (this._startSignal != null) {
				this._startSignal.await();
			}
			super.apply();
			if (this._endSignal != null) {
				this._endSignal.countDown();
			}
			if (null != fakeEndingTime) {
				this.setEndingTime(fakeEndingTime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected boolean apply(String item) throws ApsSystemException {
		boolean result = true;
		if (item.contains("err")) {
			this.getTracer().traceError(item, ApsCommandErrorCode.ERROR);
			result = false;
		} if (item.contains("warn")) {
			this.getTracer().traceWarning(item, ApsCommandWarningCode.NOT_NECESSARY);
		} else {
			this.getTracer().traceSuccess(item);
		}
		return result;
	}

	public void setFakeEndingTime(Date endingTime) {
		this.fakeEndingTime = endingTime;
	}

	private CountDownLatch _startSignal;
	private CountDownLatch _endSignal;
	private Date fakeEndingTime;

}
