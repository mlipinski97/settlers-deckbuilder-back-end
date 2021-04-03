package pl.lipinski.settlers_deckbuilder.util.enums;

public enum ErrorMessage {

    CAN_NOT_FIND_USER_BY_ID_ERROR_MESSAGE("Can not find user with given id"),
    EMAIL_ALREADY_TAKEN_ERROR_MESSAGE("Account with given email is already taken: "),
    USER_DONT_HAVE_PERMISSIONS_ERROR_MESSAGE("This user dont have permission to access this entity!"),
    WRONG_CREDENTIALS_ERROR_MESSAGE("Given credentials do not match any account"),
    AUTHORIZATION_ERROR_OCCURRED_ERROR_MESSAGE("Error occurred during authorization of JWT token");

    private String message;

    public String getMessage()
    {
        return this.message;
    }

    ErrorMessage(String message)
    {
        this.message = message;
    }
}
