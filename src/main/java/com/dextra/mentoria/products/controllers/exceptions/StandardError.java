package com.dextra.mentoria.products.controllers.exceptions;

import javax.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

public class StandardError implements Serializable {
    @Serial
    private static final long serialVersionUID = -4194486122289054213L;

    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

    @Deprecated
    public StandardError() {
    }

    public StandardError(Exception exception, HttpServletRequest request) {
        this.timestamp = Instant.now();
        this.message = exception.getMessage();
        this.path = request.getRequestURI();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public StandardError setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public StandardError setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getError() {
        return error;
    }

    public StandardError setError(String error) {
        this.error = error;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public StandardError setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getPath() {
        return path;
    }

    public StandardError setPath(String path) {
        this.path = path;
        return this;
    }
}
