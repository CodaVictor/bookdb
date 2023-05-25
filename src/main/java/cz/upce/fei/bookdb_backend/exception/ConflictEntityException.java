package cz.upce.fei.bookdb_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Entity is in conflict with another one.")
public class ConflictEntityException extends Exception {

    public ConflictEntityException() {
    }

    public ConflictEntityException(String message) {
        super(message);
    }
}
