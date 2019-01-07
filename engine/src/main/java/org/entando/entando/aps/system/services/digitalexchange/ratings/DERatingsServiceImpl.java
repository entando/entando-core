package org.entando.entando.aps.system.services.digitalexchange.ratings;

import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.DERatingsDAO;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DERatingsServiceImpl implements DERatingsService {

    private DERatingsDAO dao;

    @Autowired
    public DERatingsServiceImpl(DERatingsDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<DERatingsSummary> getAllRatingsSummaries() {
        return dao.getAllRatingsSummaries();
    }

    @Override
    public List<DERatingsSummary> getRatingSummariesPage(long offset, long pageSize) {
        return dao.getRatingSummariesPage(offset, pageSize);
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
