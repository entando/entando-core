package org.entando.entando.aps.system.services.digitalexchange;

import com.agiletec.aps.system.common.*;
import org.entando.entando.aps.system.init.model.portdb.DERating;
import org.entando.entando.aps.system.services.digitalexchange.model.DERatingsSummary;
import org.entando.entando.web.common.model.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Repository
public class DERatingsDAOImpl extends AbstractDAO implements DERatingsDAO {

    private static final Logger logger = LoggerFactory.getLogger(DERatingsDAOImpl.class);

    private static final String SELECT_SUMMARY = "SELECT de.componentId, AVG(rating) AS rating, COUNT(*) AS reviews " +
            "FROM derating de ";
    private static final String GROUP_SUMMARY = "GROUP BY de.componentId";

    private static final String CREATE_RATING =
            "INSERT INTO derating (id, componentId, reviewerId, rating) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_RATING =
            "UPDATE derating SET componentId = ?, reviewerId = ?, rating = ? WHERE id = ?";
    private static final String DELETE_RATING = "DELETE FROM derating WHERE id = ?";

    private static final String FIND_ALL_SUMMARIES = SELECT_SUMMARY + GROUP_SUMMARY;

    private static final String FIND_SUMMARY = SELECT_SUMMARY + "WHERE de.componentId = ? " + GROUP_SUMMARY;

    private static final String FIND_BY_ID = "SELECT * FROM derating WHERE id = ?";


    private static final int ID_INDEX = 1;
    private static final int COMPONENT_INDEX = 2;
    private static final int REVIEWER_INDEX = 3;
    private static final int RATING_INDEX = 4;


    @Autowired
    public DERatingsDAOImpl(DataSource portDataSource) {
        setDataSource(portDataSource);
    }

    @Override
    public PagedRestResponse<DERatingsSummary> getAllRatingsSummaries() {
        List<DERatingsSummary> summaries = new ArrayList<>();

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(FIND_ALL_SUMMARIES);
            res = preparedStatement.executeQuery();

            while (res.next()) {
                summaries.add(loadSummary(preparedStatement.getResultSet()));
            }
        } catch (Exception t) {
            handleError(t, conn, "Error loading rating summaries");
        } finally{
            closeDaoResources(res, preparedStatement, conn);
        }

        PagedRestResponse<DERatingsSummary> result = new PagedRestResponse<>(
                new PagedMetadata<>(1, summaries.size(), 1, summaries.size()));
        result.setPayload(summaries);
        return result;
    }

    private DERatingsSummary loadSummary(ResultSet resultSet) throws SQLException {
        return DERatingsSummary.builder()
                .componentId(resultSet.getString(1))
                .rating(resultSet.getInt(2))
                .numberOfRatings(resultSet.getInt(3))
                .numberOfInstalls(resultSet.getInt(3))
                .build();
    }

    @Override
    public Optional<DERatingsSummary> getComponentRatingsSummary(String componentId) {
        DERatingsSummary summary = null;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(FIND_SUMMARY);
            preparedStatement.setString(1, componentId);
            res = preparedStatement.executeQuery();

            if (res.next()) {
                summary = loadSummary(preparedStatement.getResultSet());
            }
        } catch (Exception t) {
            handleError(t, conn, "Error loading summary for component " + componentId);
        } finally{
            closeDaoResources(res, preparedStatement, conn);
        }

        return Optional.ofNullable(summary);
    }

    @Override
    public void saveOrUpdate(DERating deRating) {
        if (findById(deRating.getId()).isPresent()) {
            update(deRating);
        } else {
            save(deRating);
        }
    }

    @Override
    public void save(DERating rating) {
        this.executeQueryWithoutResultset(CREATE_RATING, rating.getId(), rating.getComponentId(),
                rating.getReviewerId(), rating.getRating());
    }

    @Override
    public void update(DERating rating) {
        this.executeQueryWithoutResultset(UPDATE_RATING, rating.getComponentId(),
                rating.getReviewerId(), rating.getRating(), rating.getId());
    }

    @Override
    public void delete(long id) {
        this.executeQueryWithoutResultset(DELETE_RATING, id);
    }

    @Override
    public Optional<DERating> findById(long id) {
        DERating entity = null;

        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet res = null;
        try {
            conn = getConnection();
            preparedStatement = conn.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            res = preparedStatement.executeQuery();

            if (res.next()) {
                entity = loadEntity(preparedStatement.getResultSet());
            }
        } catch (Exception t) {
            handleError(t, conn, "Error loading DERating with id " + id);
        } finally{
            closeDaoResources(res, preparedStatement, conn);
        }

        return Optional.ofNullable(entity);
    }

    private DERating loadEntity(ResultSet resultSet) throws SQLException {
        DERating rating = new DERating();
        rating.setId(resultSet.getLong(ID_INDEX));
        rating.setComponentId(resultSet.getString(COMPONENT_INDEX));
        rating.setReviewerId(resultSet.getString(REVIEWER_INDEX));
        rating.setRating(resultSet.getInt(RATING_INDEX));
        return rating;
    }

    private void handleError(Throwable t, Connection conn, String errorMessage) {
        executeRollback(conn);

        logger.error(errorMessage,  t);
        throw new EntandoDbException(errorMessage, t);
    }
}
