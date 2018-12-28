package org.entando.entando.aps.system.services.digitalexchange.ratings;

import org.apache.commons.lang.NotImplementedException;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsInfo;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.digitalexchange.ratings.DERatingUpdate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DERatingsServiceImpl implements DERatingsService {

    @Override
    public PagedRestResponse<DERatingsInfo> getAllRatings() {
        throw new NotImplementedException();
    }

    @Override
    public Optional<DERatingsInfo> getComponentRatings(String componentId) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<DERatingsInfo> addRating(DERatingUpdate deRatingUpdate) {
        throw new NotImplementedException();
    }
}
