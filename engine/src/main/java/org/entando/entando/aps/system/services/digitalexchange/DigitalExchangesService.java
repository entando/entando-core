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
package org.entando.entando.aps.system.services.digitalexchange;

import java.util.List;
import java.util.Map;
import org.entando.entando.aps.system.services.digitalexchange.model.DigitalExchange;
import org.entando.entando.web.common.model.RestError;

public interface DigitalExchangesService {

    List<DigitalExchange> getDigitalExchanges();

    DigitalExchange findByName(String name);

    DigitalExchange create(DigitalExchange digitalExchange);

    DigitalExchange update(DigitalExchange digitalExchange);

    void delete(String digitalExchangeName);

    List<RestError> test(String digitalExchangeName);

    Map<String, List<RestError>> testAll();
}
