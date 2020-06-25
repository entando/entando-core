package com.agiletec.aps.system.services.health;

public interface IHealthDAO {

    /**
     * @return true if an healthy connection can be established with the ServDB schema
     */
    boolean isServDBConnectionHealthy();

    /**
     * @return true if an healthy connection can be established with the PortDB schema
     */
    boolean isPortDBConnectionHealthy();
}
