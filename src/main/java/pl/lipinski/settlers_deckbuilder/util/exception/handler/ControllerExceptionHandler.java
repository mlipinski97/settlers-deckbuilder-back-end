package pl.lipinski.settlers_deckbuilder.util.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.lipinski.settlers_deckbuilder.util.exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            UserNotFoundException.class,
            ElementNotFoundByIdException.class,
            ElementNotFoundByNameException.class,
            EmailTakenException.class,
            PermissionDeniedException.class,
            JWTException.class,
            WrongCredentialsException.class,
            CardDeckIntersectionPKViolationException.class
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
}
