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
package org.entando.entando.aps.system.services.digitalexchange.client;

import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesService;
import org.springframework.context.MessageSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Helper class that can be used in unit tests to simulate the behavior of a
 * DigitalExchangesClient without mocking it from scratch.
 */
public class DigitalExchangesClientMocker {

    private final DigitalExchangesMocker digitalExchangesMocker;
    private final DigitalExchangesService digitalExchangesService;
    private final MessageSource messageSource;

    public DigitalExchangesClientMocker() {
        digitalExchangesMocker = new DigitalExchangesMocker();
        digitalExchangesService = mock(DigitalExchangesService.class);
        messageSource = mock(MessageSource.class);
    }

    public DigitalExchangesMocker getDigitalExchangesMocker() {
        return digitalExchangesMocker;
    }

    public DigitalExchangesClient build() {
        digitalExchangesMocker.initMocks();
        when(messageSource.getMessage(any(), any(), any())).thenReturn("Mocked Message");
        when(digitalExchangesService.getDigitalExchanges()).thenReturn(digitalExchangesMocker.getFakeExchanges());
        return new DigitalExchangesClientImpl(digitalExchangesService, digitalExchangesMocker.getRestTemplate(), messageSource);
    }
    
    public MessageSource getMessageSource() {
        return messageSource;
    }
}
