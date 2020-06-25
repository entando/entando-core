package org.entando.entando.aps.system.services.health;

public interface IHealthService {

    /**
     * @return true if the system can be considered healthy, false otherwise
     */
    boolean isHealthy();
}
