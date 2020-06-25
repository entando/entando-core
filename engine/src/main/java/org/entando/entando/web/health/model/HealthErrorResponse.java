package org.entando.entando.web.health.model;

public class HealthErrorResponse {

    private Long timestamp;
    private String error;
    private Integer status;
    private String message;

    public Long getTimestamp() {
        return timestamp;
    }

    public HealthErrorResponse setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getError() {
        return error;
    }

    public HealthErrorResponse setError(String error) {
        this.error = error;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public HealthErrorResponse setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public HealthErrorResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
