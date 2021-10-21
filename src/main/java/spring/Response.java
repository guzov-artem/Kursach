package spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

public class Response {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude
    private String status;

    public Response(String message, HttpStatus status) {
        this.message = message;
        this.status = status.toString();
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message, HttpStatus status) {
        this.message = message;
    }
}
