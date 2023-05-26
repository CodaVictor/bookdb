package cz.upce.fei.bookdb_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized access.")
public class UnauthorizedAccessException extends Exception {

    public UnauthorizedAccessException() {
    }

    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
