package pl.lipinski.settlers_deckbuilder.util.exception;

import org.springframework.http.HttpStatus;

public class CardDeckIntersectionPKViolationException extends ControllerException{
    public CardDeckIntersectionPKViolationException(String errorMessage, Integer errorCode) {
        super(errorMessage, errorCode, HttpStatus.CONFLICT);
    }
}
