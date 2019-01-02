package org.entando.entando.aps.system.services.digitalexchange;

import org.apache.commons.lang.NotImplementedException;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.digitalexchange.ratings.DERating;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DERatingsDAOImpl implements DERatingsDAO {

    @Override
    public PagedRestResponse<DERatingsSummary> getAllRatingsSummaries() {
        throw new NotImplementedException();
    }

    @Override
    public Optional<DERatingsSummary> getComponentRatingsSummary(String componentId) {
        throw new NotImplementedException();
    }

    @Override
    public void saveOrUpdate(DERating deRating) {
        throw new NotImplementedException();
    }
}
