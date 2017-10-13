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
import org.entando.entando.aps.system.services.oauth.model.ConsumerRecordVO;

/**
 * @author E.Santoboni
 */
public interface IOAuthConsumerDAO {

	public List<String> getConsumerKeys(FieldSearchFilter[] filters);

	public ConsumerRecordVO getConsumerRecord(String consumerKey);

	public void addConsumer(ConsumerRecordVO consumer);

	public void updateConsumer(ConsumerRecordVO consumer);

	public void deleteConsumer(String consumerKey);

	//public OAuthConsumer getConsumer(String consumerKey);
}
