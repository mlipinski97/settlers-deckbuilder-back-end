package pl.lipinski.settlers_deckbuilder.util.exception;

import org.springframework.http.HttpStatus;

public class ElementNotFoundByIdException extends ControllerException{
    public ElementNotFoundByIdException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.NOT_FOUND);
    }
    public ElementNotFoundByIdException(Integer errorCode) {
        super("There is no element with such id", errorCode, HttpStatus.NOT_FOUND);
    }

}
