package app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserException extends RuntimeException {
    public UserException(String reason) {
        super(reason);
    }

    public UserException() {
        super("User not exist!");
    }
}
