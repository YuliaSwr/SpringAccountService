package app.controller;

import app.exception.ErrorResponse;
import app.exception.UserExistException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UserExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAvailibilityException(UserExistException se) {
        ErrorResponse response = new ErrorResponse(400, "Bad Request", se.getMessage());
        return response;
    }
}
