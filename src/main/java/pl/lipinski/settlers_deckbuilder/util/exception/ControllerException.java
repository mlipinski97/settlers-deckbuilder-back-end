package pl.lipinski.settlers_deckbuilder.util.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ControllerException extends Exception {
    private final Integer errorCode;
    private final HttpStatus errorStatus;

    public ControllerException(String errorMessage, Integer errorCode, HttpStatus errorStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }
}
