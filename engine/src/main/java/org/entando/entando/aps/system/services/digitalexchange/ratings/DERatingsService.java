package org.entando.entando.aps.system.services.digitalexchange.ratings;

import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsInfo;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.digitalexchange.ratings.DERatingUpdate;

import java.util.Optional;

public interface DERatingsService {
    PagedRestResponse<DERatingsInfo> getAllRatings();

    Optional<DERatingsInfo> getComponentRatings(String componentId);

    Optional<DERatingsInfo> addRating(DERatingUpdate deRatingUpdate);
}
