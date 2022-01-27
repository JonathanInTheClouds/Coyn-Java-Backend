package dev.jonathandlab.com.Coyn.server.exception.controller;

import dev.jonathandlab.com.Coyn.server.exception.CoynAppException;
import dev.jonathandlab.com.Coyn.server.exception.model.CoynAppExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class CoynAppExceptionController {

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss zzz yyyy");

    @ExceptionHandler(value = CoynAppException.class)
    public ResponseEntity<CoynAppExceptionResponse> coynAppExceptionHandler(CoynAppException coynAppException) {
        String date = simpleDateFormat.format(new Date());
        CoynAppExceptionResponse exceptionResponse = new CoynAppExceptionResponse(date, coynAppException.getMessage());
        return ResponseEntity.status(coynAppException.getStatus())
                .body(exceptionResponse);
    }
}
