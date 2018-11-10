/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.apsadmin.api;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.aps.system.services.oauth2.IOAuthConsumerManager;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;

public class TestConsumerAction extends ApsAdminBaseTestCase {

    private IOAuthConsumerManager consumerManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.consumerManager = (IOAuthConsumerManager) super.getApplicationContext().getBean(SystemConstants.OAUTH_CONSUMER_MANAGER, IOAuthConsumerManager.class);
    }

    public void testNew() throws Throwable {
        String result = this.executeNew("developersConf");
        assertEquals("apslogin", result);
        result = this.executeNew("admin");
        assertEquals(Action.SUCCESS, result);
        ConsumerAction action = (ConsumerAction) this.getAction();
        assertNull(action.getConsumerKey());
        assertEquals(ApsAdminSystemConstants.ADD, action.getStrutsAction());
    }

    public void testEdit() throws Throwable {
        String result = this.executeEdit("developersConf", "test_consumer");
        assertEquals("apslogin", result);

        result = this.executeEdit("admin", "test_wrong_key");
        assertEquals("list", result);
        ConsumerAction action = (ConsumerAction) this.getAction();
        assertEquals(1, action.getActionErrors().size());

        result = this.executeEdit("admin", "test1_consumer");
        assertEquals(Action.SUCCESS, result);
        action = (ConsumerAction) this.getAction();
        assertEquals("test1_consumer", action.getConsumerKey());
        assertEquals("Test 1 Consumer", action.getName());
    }

    public void testSaveNew_1() throws Throwable {
        String key = "key_test_1";
        try {
            List<String> grantTypes = Arrays.asList(new String[]{"implicit", "password"});
            String result = this.executeSaveNew("admin", key, "secret",
                    "Name", "Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.SUCCESS, result);
            ConsumerRecordVO record = this.consumerManager.getConsumerRecord(key);
            assertNotNull(record);
            assertEquals(key, record.getKey());
            assertEquals("Name", record.getName());
            assertNotNull(record.getIssuedDate());
            assertTrue(record.getSecret().startsWith("{bcrypt}"));
        } catch (Throwable t) {
            throw t;
        } finally {
            this.consumerManager.deleteConsumer(key);
        }
    }

    public void testSaveNew_2() throws Throwable {
        String key_existing = "test1_consumer";
        String key_invalid = "key_&_test";
        String key_long = StringUtils.repeat("test", 50);
        String key_short = "sh";
        try {
            List<String> grantTypes = Arrays.asList(new String[]{"implicit", "password"});
            String result = this.executeSaveNew("admin", key_existing, "secret",
                    "Name", "Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.INPUT, result);
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("consumerKey").size());

            result = this.executeSaveNew("admin", null, "secret",
                    "Name", "Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.INPUT, result);
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("consumerKey").size());

            result = this.executeSaveNew("admin", key_invalid, null,
                    "Name", "Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.INPUT, result);
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("consumerKey").size());
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("secret").size());

            result = this.executeSaveNew("admin", key_long, "invalid&$",
                    null, "Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.INPUT, result);
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("consumerKey").size());
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("secret").size());
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("name").size());

            result = this.executeSaveNew("admin", key_short, "secret",
                    "Name", null, "url", "read,write", grantTypes, null);
            assertEquals(Action.INPUT, result);
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("consumerKey").size());
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("description").size());
        } catch (Throwable t) {
            throw t;
        } finally {
            this.consumerManager.deleteConsumer(key_invalid);
            this.consumerManager.deleteConsumer(key_long);
            this.consumerManager.deleteConsumer(key_short);
        }
    }

    public void testSaveEdit() throws Throwable {
        String key_test = "key_test_2";
        String key_notexists = "wrong_consumer";
        try {
            List<String> grantTypes = Arrays.asList(new String[]{"implicit", "password"});
            this.addConsumer(key_test, "secret",
                    "Name", "Description", "url", "read,write", grantTypes, null);
            ConsumerRecordVO record = this.consumerManager.getConsumerRecord(key_test);
            assertNotNull(record);
            String oldSecret = record.getSecret();

            String result = this.executeSaveEdit("admin", key_notexists, "secret",
                    "Name", "Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.INPUT, result);
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("consumerKey").size());

            result = this.executeSaveEdit("admin", key_test, "secret",
                    "", "", "url", "read,write", grantTypes, null);
            assertEquals(Action.INPUT, result);
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("name").size());
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("description").size());

            result = this.executeSaveEdit("admin", key_notexists, "secret&invalid",
                    "Name", "Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.INPUT, result);
            assertEquals(1, ((ActionSupport) this.getAction()).getFieldErrors().get("secret").size());

            result = this.executeSaveEdit("admin", key_test, "",
                    "New Name", "New Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.SUCCESS, result);
            record = this.consumerManager.getConsumerRecord(key_test);
            assertNotNull(record);
            assertEquals("New Name", record.getName());
            assertEquals("New Description", record.getDescription());
            assertEquals(oldSecret, record.getSecret());

            result = this.executeSaveEdit("admin", key_test, "new_secret",
                    "Other New Name", "Other New Description", "url", "read,write", grantTypes, null);
            assertEquals(Action.SUCCESS, result);
            record = this.consumerManager.getConsumerRecord(key_test);
            assertNotNull(record);
            assertEquals("Other New Name", record.getName());
            assertEquals("Other New Description", record.getDescription());
            assertFalse(oldSecret.equals(record.getSecret()));
        } catch (Throwable t) {
            throw t;
        } finally {
            this.consumerManager.deleteConsumer(key_notexists);
            this.consumerManager.deleteConsumer(key_test);
        }
    }

    public void testTrash() throws Throwable {
        String key_test = "key_test_3";
        try {
            List<String> grantTypes = Arrays.asList(new String[]{"implicit", "password", "refresh_token"});
            this.addConsumer(key_test, "secret",
                    "Name", "Description", "url", "read,write", grantTypes, null);
            assertNotNull(this.consumerManager.getConsumerRecord(key_test));

            String result = this.executeTrash("admin", "wrong_key");
            assertEquals("list", result);
            assertEquals(1, this.getAction().getActionErrors().size());
            assertNotNull(this.consumerManager.getConsumerRecord(key_test));

            result = this.executeTrash("admin", key_test);
            assertEquals(Action.SUCCESS, result);
            assertNotNull(this.consumerManager.getConsumerRecord(key_test));
        } catch (Throwable t) {
            throw t;
        } finally {
            this.consumerManager.deleteConsumer(key_test);
        }
    }

    public void testDelete() throws Throwable {
        String key_test = "key_test_4";
        try {
            List<String> grantTypes = Arrays.asList(new String[]{"implicit", "password", "refresh_token"});
            this.addConsumer(key_test, "secret",
                    "Name", "Description", "url", "read,write", grantTypes, null);
            assertNotNull(this.consumerManager.getConsumerRecord(key_test));

            String result = this.executeDelete("admin", "wrong_key");
            assertEquals("list", result);
            assertEquals(1, this.getAction().getActionErrors().size());
            assertNotNull(this.consumerManager.getConsumerRecord(key_test));

            result = this.executeDelete("admin", key_test);
            assertEquals(Action.SUCCESS, result);
            assertNull(this.consumerManager.getConsumerRecord(key_test));
        } catch (Throwable t) {
            this.consumerManager.deleteConsumer(key_test);
            throw t;
        }
    }

    private String executeNew(String currentUser) throws Throwable {
        this.setUserOnSession(currentUser);
        this.initAction("/do/Api/Consumer", "new");
        return this.executeAction();
    }

    private String executeEdit(String currentUser, String consumerKey) throws Throwable {
        this.setUserOnSession(currentUser);
        this.initAction("/do/Api/Consumer", "edit");
        this.addParameter("consumerKey", consumerKey);
        return this.executeAction();
    }

    private String executeTrash(String currentUser, String consumerKey) throws Throwable {
        this.setUserOnSession(currentUser);
        this.initAction("/do/Api/Consumer", "trash");
        this.addParameter("consumerKey", consumerKey);
        return this.executeAction();
    }

    private String executeDelete(String currentUser, String consumerKey) throws Throwable {
        this.setUserOnSession(currentUser);
        this.initAction("/do/Api/Consumer", "delete");
        this.addParameter("consumerKey", consumerKey);
        return this.executeAction();
    }

    private String executeSaveNew(String currentUser, String consumerKey, String secret, String name,
            String description, String callbackUrl, String scope, List<String> grantTypes, String expirationDate) throws Throwable {
        this.setUserOnSession(currentUser);
        this.initAction("/do/Api/Consumer", "save");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.ADD));
        return this.addParamsAndExecuteSave(consumerKey, secret, name, description, callbackUrl, scope, grantTypes, expirationDate);
    }

    private String executeSaveEdit(String currentUser, String consumerKey, String secret, String name,
            String description, String callbackUrl, String scope, List<String> grantTypes, String expirationDate) throws Throwable {
        this.setUserOnSession(currentUser);
        this.initAction("/do/Api/Consumer", "save");
        this.addParameter("strutsAction", String.valueOf(ApsAdminSystemConstants.EDIT));
        return this.addParamsAndExecuteSave(consumerKey, secret, name, description, callbackUrl, scope, grantTypes, expirationDate);
    }

    private String addParamsAndExecuteSave(String consumerKey, String secret, String name,
            String description, String callbackUrl, String scope, List<String> grantTypes, String expirationDate) throws Throwable {
        this.addParameter("consumerKey", consumerKey);
        this.addParameter("secret", secret);
        this.addParameter("description", description);
        this.addParameter("name", name);
        this.addParameter("callbackUrl", callbackUrl);
        this.addParameter("scope", scope);
        this.addParameter("grantTypes", grantTypes);
        this.addParameter("expirationDate", expirationDate);
        return this.executeAction();
    }

    private void addConsumer(String consumerKey, String secret, String name, String description,
            String callbackUrl, String scope, List<String> grantTypes, Date expirationDate) throws ApsSystemException {
        ConsumerRecordVO consumer = new ConsumerRecordVO();
        String grantTypesCsv = (null != grantTypes) ? StringUtils.join(grantTypes, ",") : null;
        consumer.setAuthorizedGrantTypes(grantTypesCsv);
        consumer.setCallbackUrl(callbackUrl);
        consumer.setDescription(description);
        consumer.setExpirationDate(expirationDate);
        consumer.setKey(consumerKey);
        consumer.setName(name);
        consumer.setScope(scope);
        consumer.setSecret(secret);
        this.consumerManager.addConsumer(consumer);
    }

}
