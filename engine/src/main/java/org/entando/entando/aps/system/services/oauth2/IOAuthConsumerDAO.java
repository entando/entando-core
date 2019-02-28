/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General  License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General  License for more
 * details.
 */
package org.entando.entando.aps.system.services.oauth2;

import com.agiletec.aps.system.common.FieldSearchFilter;
import org.entando.entando.aps.system.services.oauth2.model.ConsumerRecordVO;

import java.util.List;

public interface IOAuthConsumerDAO {

    ConsumerRecordVO getConsumer(String clientId);

    List<String> getConsumerKeys(FieldSearchFilter<?>[] filters);
    
    List<ConsumerRecordVO> getConsumers(FieldSearchFilter<?>[] filters);

    ConsumerRecordVO addConsumer(ConsumerRecordVO consumer);

    ConsumerRecordVO updateConsumer(ConsumerRecordVO consumer);

    void deleteConsumer(String clientId);
}
