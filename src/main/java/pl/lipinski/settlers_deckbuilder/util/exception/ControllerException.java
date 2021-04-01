package pl.lipinski.settlers_deckbuilder.util.exception;

import lombok.Getter;

@Getter
public class ControllerException extends Exception {
    private final Integer errorCode;

    public ControllerException( String errorMessage, Integer errorCode) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
