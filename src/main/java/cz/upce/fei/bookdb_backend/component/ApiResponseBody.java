package cz.upce.fei.bookdb_backend.component;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiResponseBody {

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
