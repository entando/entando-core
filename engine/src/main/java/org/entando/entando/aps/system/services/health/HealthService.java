package org.entando.entando.aps.system.services.health;

import com.agiletec.aps.system.services.health.IHealthDAO;
import org.entando.entando.web.common.exceptions.EntandoHealthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthService implements IHealthService {

    private IHealthDAO healthDAO;

    @Autowired
    public HealthService(IHealthDAO healthDAO) {
        this.healthDAO = healthDAO;
    }

    public HealthService setHealthDAO(IHealthDAO healthDAO) {
        this.healthDAO = healthDAO;
        return this;
    }

    @Override
    public boolean isHealthy() {

        // check port schema connectivity
        if (! this.healthDAO.isPortDBConnectionHealthy()) {
            throw new EntandoHealthException("Can't establish connection with Port database schema");
        }

        // check serv schema connectivity
        if (! this.healthDAO.isServDBConnectionHealthy()) {
            throw new EntandoHealthException("Can't establish connection with Serv database schema");
        }

        return true;
    }
}
