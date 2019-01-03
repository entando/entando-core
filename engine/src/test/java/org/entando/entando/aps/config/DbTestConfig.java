package org.entando.entando.aps.config;

import com.google.common.collect.ImmutableList;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.DataSourceConnectionSource;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.spring.*;
import com.j256.ormlite.support.ConnectionSource;
import org.entando.entando.aps.system.init.util.ApsDerbyEmbeddedDatabaseType;
import org.entando.entando.aps.system.services.digitalexchange.*;
import org.entando.entando.web.digitalexchange.ratings.DERating;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.*;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DbTestConfig {

    @Bean
    DataSource portDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.DERBY)
                .setName("test")
                .addScript("classpath:sql/create_schema.sql")
//                .addScript("classpath:jdbc/test-data.sql")
                .build();
    }

    @Bean
    DataSourceConnectionSource connectionSource(DataSource portDataSource) throws SQLException {
        return new DataSourceConnectionSource(portDataSource, new ApsDerbyEmbeddedDatabaseType());
    }

    @Bean
    TransactionManager transactionManager(ConnectionSource connectionSource) {
        return new TransactionManager(connectionSource);
    }

    @Bean
    Dao<DERating, Long> deRatingDao(ConnectionSource connectionSource) throws SQLException {
        return DaoFactory.createDao(connectionSource, DERating.class);
    }

    @Bean
    TableCreator tableCreator(ConnectionSource connectionSource, Dao<DERating, Long> deRatingDao)
            throws SQLException {

        TableCreator tableCreator = new TableCreator(connectionSource, ImmutableList.of(deRatingDao));
        System.setProperty(TableCreator.AUTO_CREATE_TABLES, "true");

        tableCreator.initialize();

        return tableCreator;
    }

    @Bean
    DERatingsDAO deRatingsDAO(DataSource dataSource) {
        return new DERatingsDAOImpl(dataSource);
    }
}
