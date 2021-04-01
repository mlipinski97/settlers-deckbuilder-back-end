package pl.lipinski.settlers_deckbuilder.util.exception;

public class UserNotFoundException extends ControllerException {
    public UserNotFoundException(String email, Integer errorCode) {
        super("User not found with given email: " + email, errorCode);
    }

    public UserNotFoundException(Long userId, Integer errorCode) {
        super("User not found with given ID: " + userId, errorCode);
    }
}
