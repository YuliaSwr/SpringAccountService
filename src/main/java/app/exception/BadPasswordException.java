package app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadPasswordException extends RuntimeException {
    public BadPasswordException(String reason) {
        super(reason);
    }
}
