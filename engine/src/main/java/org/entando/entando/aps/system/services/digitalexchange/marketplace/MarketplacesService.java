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
package org.entando.entando.aps.system.services.digitalexchange.marketplace;

import java.util.List;
import org.entando.entando.aps.system.services.digitalexchange.marketplace.model.Marketplace;

public interface MarketplacesService {

    List<Marketplace> getMarketplaces();

    Marketplace findByName(String name);

    Marketplace create(Marketplace marketplace);

    Marketplace update(Marketplace marketplace);

    void delete(String marketplaceName);
}
