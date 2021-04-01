package pl.lipinski.settlers_deckbuilder.util.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.lipinski.settlers_deckbuilder.util.exception.ControllerException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {
            UserNotFoundException.class
    })
    public ResponseEntity<ApiErrorResponse> handleException(ControllerException ce) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ce.getErrorCode(),
                ce.getLocalizedMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
