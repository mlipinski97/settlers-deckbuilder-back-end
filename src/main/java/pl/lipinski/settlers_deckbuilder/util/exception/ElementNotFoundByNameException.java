package pl.lipinski.settlers_deckbuilder.util.exception;

import org.springframework.http.HttpStatus;

public class ElementNotFoundByNameException extends ControllerException {
    public ElementNotFoundByNameException(Integer errorCode) {
        super("There is no element with such name", errorCode, HttpStatus.NOT_FOUND);
    }
    public ElementNotFoundByNameException(Integer errorCode, String errorMessage){
        super(errorMessage, errorCode, HttpStatus.NOT_FOUND);
    }
}
