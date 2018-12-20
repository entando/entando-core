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
package org.entando.entando.aps.system.services.digitalexchange.component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeMockedRequest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.RestResponse;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponent;

public class DigitalExchangeComponentsMocker {

    public static Function<DigitalExchangeMockedRequest, RestResponse<?, ?>> mock(String... names) {

        return request -> {
            Stream<DigitalExchangeComponent> stream
                    = Arrays.stream(names).map(name
                            -> new DigitalExchangeComponentBuilder(name)
                            .setLastUpdate(new Date()).build());

            return mock(request, stream);
        };
    }

    public static Function<DigitalExchangeMockedRequest, RestResponse<?, ?>> mock(DigitalExchangeComponent... components) {
        return request -> mock(request, Arrays.stream(components));
    }

    private static PagedRestResponse<DigitalExchangeComponent> mock(DigitalExchangeMockedRequest request, Stream<DigitalExchangeComponent> stream) {

        stream = new DigitalExchangeComponentListProcessor(request.getRestListRequest(), stream)
                .filterAndSort().getStream();

        List<DigitalExchangeComponent> components = stream.collect(Collectors.toList());
        PagedMetadata<DigitalExchangeComponent> pagedMetadata
                = new PagedMetadata<>(new RestListRequest(), components, components.size());

        return new PagedRestResponse<>(pagedMetadata);
    }
}
