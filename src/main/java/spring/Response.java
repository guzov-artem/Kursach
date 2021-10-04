package spring;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Response {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public Response(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
