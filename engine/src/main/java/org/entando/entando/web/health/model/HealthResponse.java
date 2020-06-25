package org.entando.entando.web.health.model;

public class HealthResponse {

    private String status;

    public HealthResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public HealthResponse setStatus(String status) {
        this.status = status;
        return this;
    }
}
