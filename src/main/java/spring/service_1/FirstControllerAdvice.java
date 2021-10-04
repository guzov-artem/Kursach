package spring.service_1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spring.Response;

@ControllerAdvice
public class FirstControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleException(Exception e) {
        return new ResponseEntity<Response>(new Response(e.getMessage()), HttpStatus.OK);
    }

}