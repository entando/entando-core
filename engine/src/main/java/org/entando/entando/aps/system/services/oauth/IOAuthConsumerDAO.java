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
package org.entando.entando.aps.system.services.oauth;

import com.agiletec.aps.system.common.FieldSearchFilter;
import java.util.List;

import org.apache.oltu.oauth2.common.domain.client.ClientInfo;
import org.entando.entando.aps.system.services.oauth.model.ConsumerRecordVO;

/**
 * @author E.Santoboni
 */
public interface IOAuthConsumerDAO {

	List<String> getConsumerKeys(FieldSearchFilter[] filters);

	ConsumerRecordVO getConsumerRecord(String clientId);

	void addConsumer(ConsumerRecordVO consumer);

	void updateConsumer(ConsumerRecordVO consumer);

	void deleteConsumer(String clientId);

	ClientInfo getConsumer(String clientId);
}
