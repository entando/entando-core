package org.entando.entando.aps.system.services.digitalexchange.ratings;

import org.entando.entando.aps.system.services.digitalexchange.DERatingsDAO;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.digitalexchange.ratings.DERating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DERatingsServiceImpl implements DERatingsService {

    private DERatingsDAO dao;

    @Autowired
    public DERatingsServiceImpl(DERatingsDAO dao) {
        this.dao = dao;
    }

    @Override
    public PagedRestResponse<DERatingsSummary> getAllRatingsSummaries() {
        return dao.getAllRatingsSummaries();
    }

    @Override
    public Optional<DERatingsSummary> getComponentRatingsSummary(String componentId) {
        return dao.getComponentRatingsSummary(componentId);
    }

    @Override
    public Optional<DERatingsSummary> addRating(DERating deRating) {
        dao.saveOrUpdate(deRating);
        return dao.getComponentRatingsSummary(deRating.getComponentId());
    }
}
