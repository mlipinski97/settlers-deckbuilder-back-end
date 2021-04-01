package pl.lipinski.settlers_deckbuilder.util.exception;

public class JWTException extends ControllerException{
    public JWTException(Integer errorCode) {
        super("Error with JWT occured", errorCode);
    }
}
