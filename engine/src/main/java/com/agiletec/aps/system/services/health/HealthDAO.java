package com.agiletec.aps.system.services.health;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class HealthDAO implements IHealthDAO {

    private static final Logger logger = LoggerFactory.getLogger(HealthDAO.class);

    private DataSource portDataSource;
    private DataSource servDataSource;

    public HealthDAO setPortDataSource(DataSource portDataSource) {
        this.portDataSource = portDataSource;
        return this;
    }

    public HealthDAO setServDataSource(DataSource servDataSource) {
        this.servDataSource = servDataSource;
        return this;
    }

    @Override
    public boolean isServDBConnectionHealthy() {
        return isConnectionHealthy(servDataSource);
    }

    @Override
    public boolean isPortDBConnectionHealthy() {
        return isConnectionHealthy(portDataSource);
    }


    /**
     * @param dataSource the DataSource to use in order to open a connection
     * @return true if an healthy connection can be established with the received data source
     */
    private boolean isConnectionHealthy(DataSource dataSource) {

        Connection conn = null;

        try {
            conn = this.getConnection(dataSource);
            return conn.isValid(10);
        } catch (Throwable t) {
            return false;
        } finally {
            closeConnection(conn);
        }
    }



    /**
     * Restituisce una connessione SQL relativa al datasource.
     * @return La connessione richiesta.
     * @throws ApsSystemException In caso di errore in apertura di connessione.
     */
    private Connection getConnection(DataSource dataSource) throws ApsSystemException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Error getting connection to the datasource {}", dataSource, e);
            throw new ApsSystemException("Error getting connection to the datasource " + dataSource.toString(), e);
        }
        return conn;
    }


    /**
     * Chiude in modo controllato una connessione,
     * senza rilanciare eccezioni. Da usare nel finally di gestione di
     * una eccezione.
     * @param conn La connessione al db; può esser null
     */
    private void closeConnection(Connection conn) {
        try {
            if (conn != null) conn.close();
        } catch (Throwable t) {
            logger.error("Error closing the connection", t);
        }
    }
}