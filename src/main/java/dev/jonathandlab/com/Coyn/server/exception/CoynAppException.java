package dev.jonathandlab.com.Coyn.server.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class CoynAppException extends RuntimeException {
    private HttpStatus status;

    public CoynAppException(HttpStatus status) {
        super("Error");
        this.status = status;
    }

    public CoynAppException(HttpStatus httpStatus, String message) {
        super(message);
        this.status = httpStatus;
    }
}
