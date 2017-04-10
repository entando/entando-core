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

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;
import org.entando.entando.aps.system.common.command.context.BaseBulkCommandContext;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;
import org.entando.entando.aps.system.common.command.tracer.DefaultBulkCommandTracer;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.SystemConstants;

public class TestBulkCommandManager extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testAddGetCommandWOThread() {
		String owner = "test";
		Collection<String> items = Arrays.asList(new String[] { "1_succ", "2_err", "3_warn", "4_err", "5_warn", 
				"6_succ", "7_succ", "8_err", "9_succ" });
		FakeBulkCommand command = new FakeBulkCommand(new BaseBulkCommandContext<String>(items, new DefaultBulkCommandTracer<String>()));
		int numOfThreads = this.countThreads();
		BulkCommandReport<String> report = this._bulkCommandManager.addCommand(owner, command);
		assertEquals(numOfThreads, this.countThreads());
		assertNull(this.getThreadByName(command.getId()));
		this.checkCommandReport(report, 9, 9, 6, 3, 2, ApsCommandStatus.COMPLETED);
		assertNotNull(this._bulkCommandManager.getCommand(owner, command.getId()));

		items = Arrays.asList(new String[] { "1_succ", "2_err", "3_warn", "4_err", "5_warn", 
				"6_succ", "7_succ", "8_err", "9_succ", "10_err", "11_warn", "12_succ" });
		command = new FakeBulkCommand(new BaseBulkCommandContext<String>(items, new DefaultBulkCommandTracer<String>()));
		report = this._bulkCommandManager.addCommand(owner, command, false);
		assertEquals(numOfThreads, this.countThreads());
		assertNull(this.getThreadByName(command.getId()));
		this.checkCommandReport(report, 12, 12, 8, 4, 3, ApsCommandStatus.COMPLETED);
		assertNotNull(this._bulkCommandManager.getCommand(owner, command.getId()));
	}

	public void testAddGetCommandByThread() throws InterruptedException {
		String owner = "test";
		Collection<String> items = Arrays.asList(new String[] { "1_succ", "2_err", "3_warn", "4_err", "5_warn", 
				"6_succ", "7_succ", "8_err", "9_succ" });
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(1);
		BaseBulkCommandContext<String> context = new BaseBulkCommandContext<String>(items, new DefaultBulkCommandTracer<String>());
		FakeBulkCommand command = new FakeBulkCommand(context, startSignal, doneSignal);
		int numOfThreads = this.countThreads();
		BulkCommandReport<String> report = this._bulkCommandManager.addCommand(owner, command, true);
		assertEquals(numOfThreads + 1, this.countThreads());
		Thread thread = this.getThreadByName(command.getId());
		assertNotNull(thread);
		this.checkCommandReport(report, 9, 0, 0, 0, 0, ApsCommandStatus.NEW);
		startSignal.countDown();
		doneSignal.await();
		this.checkCommandReport(report, 9, 9, 6, 3, 2, ApsCommandStatus.COMPLETED);
		assertNotNull(this._bulkCommandManager.getCommand(owner, command.getId()));

		items = Arrays.asList(new String[] { "1_succ", "2_err", "3_warn", "4_err", "5_warn", 
				"6_succ", "7_succ", "8_err", "9_succ", "10_err", "11_warn", "12_succ" });
		context = new BaseBulkCommandContext<String>(items, new DefaultBulkCommandTracer<String>());
		startSignal = new CountDownLatch(1);
		doneSignal = new CountDownLatch(1);
		command = new FakeBulkCommand(context, startSignal, doneSignal);
		numOfThreads = this.countThreads();
		report = this._bulkCommandManager.addCommand(owner, command);
		assertEquals(numOfThreads + 1, this.countThreads());
		thread = this.getThreadByName(command.getId());
		assertNotNull(thread);
		this.checkCommandReport(report, 12, 0, 0, 0, 0, ApsCommandStatus.NEW);
		startSignal.countDown();
		doneSignal.await();
		this.checkCommandReport(report, 12, 12, 8, 4, 3, ApsCommandStatus.COMPLETED);
		assertNotNull(this._bulkCommandManager.getCommand(owner, command.getId()));
	}

	public Thread getThreadByName(String threadName) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (threadName.equals(t.getName())) {
				return t;
			}
		}
		return null;
	}

	private void checkCommandReport(BulkCommandReport<?> report, int total, int applyTotal, 
			int successes, int errors, int warnings, ApsCommandStatus expectedStatus) {
		assertEquals(total, report.getTotal());
		assertEquals(applyTotal, report.getApplyTotal());
		assertEquals(successes, report.getApplySuccesses());
		assertEquals(errors, report.getApplyErrors());
		
		assertEquals(expectedStatus, report.getStatus());
		assertNotNull(report.getCommandId());
		assertNull(report.getSuccesses());
		assertEquals(warnings, report.getWarnings().size());
		assertEquals(errors, report.getErrors().size());
		if (report.getApplyTotal() == report.getTotal()) {
			assertNotNull(report.getEndingTime());
		} else {
			assertNull(report.getEndingTime());
		}
	}

	private int countThreads() {
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		return threadSet.size();
	}

	private void init() throws Exception {
		try {
			this._bulkCommandManager = (IBulkCommandManager) this.getApplicationContext().getBean(SystemConstants.BULK_COMMAND_MANAGER);
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}

	private IBulkCommandManager _bulkCommandManager;

}
