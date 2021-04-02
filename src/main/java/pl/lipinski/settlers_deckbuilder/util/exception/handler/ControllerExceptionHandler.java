package pl.lipinski.settlers_deckbuilder.util.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.lipinski.settlers_deckbuilder.util.exception.*;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            ElementNotFoundByIdException.class,
            ElementNotFoundByNameException.class,
            EmailTakenException.class,
            PermissionDeniedException.class,
            JWTException.class,
            WrongCredentialsException.class
    })
    public ResponseEntity<ApiErrorResponse> handleException(ControllerException ce) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ce.getErrorCode(),
                ce.getLocalizedMessage(),
                LocalDateTime.now(),
                ce.getErrorStatus()
        );
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getErrorStatus());
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthException(Exception e) {
        System.out.println("GOWNO");
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Error during authenticating via JWT",
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN
        );
        return new ResponseEntity<>(apiErrorResponse, apiErrorResponse.getErrorStatus());
    }
}
