package org.entando.entando.aps.system.services.digitalexchange;

import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.web.common.model.PagedRestResponse;

import java.util.Optional;

public interface DERatingsDAO  {
    PagedRestResponse<DERatingsSummary> getAllRatingsSummaries();

    Optional<DERatingsSummary> getComponentRatingsSummary(String componentId);

    void saveOrUpdate(DERating deRating);

    void save(DERating rating);

    Optional<DERating> findById(long i);

    void update(DERating rating);

    void delete(long id);
}
