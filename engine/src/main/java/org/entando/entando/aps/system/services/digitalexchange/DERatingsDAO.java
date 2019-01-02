package org.entando.entando.aps.system.services.digitalexchange;

import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.digitalexchange.ratings.DERating;

import java.util.Optional;

public interface DERatingsDAO {
    PagedRestResponse<DERatingsSummary> getAllRatingsSummaries();

    Optional<DERatingsSummary> getComponentRatingsSummary(String componentId);

    void saveOrUpdate(DERating deRating);
}
