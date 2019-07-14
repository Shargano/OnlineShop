package net.thumbtack.onlineshop.dto.response;

import java.util.List;

public class ErrorResponse {
    private List<ErrorItem> errors;

    public ErrorResponse(List<ErrorItem> errors) {
        this.errors = errors;
    }

    public List<ErrorItem> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorItem> errors) {
        this.errors = errors;
    }
}
