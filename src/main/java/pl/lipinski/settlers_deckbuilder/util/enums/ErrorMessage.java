package pl.lipinski.settlers_deckbuilder.util.enums;

public enum ErrorMessage {

    CAN_NOT_FIND_USER_BY_ID_ERROR_MESSAGE("Can not find user with given id: "),
    CAN_NOT_FIND_CARD_BY_ID_ERROR_MESSAGE("Can not find card with given id: "),
    CAN_NOT_FIND_DECK_BY_ID_ERROR_MESSAGE("Can not find deck with given id: "),
    CAN_NOT_FIND_CARD_DECK_INTERSECTION_BY_ID_ERROR_MESSAGE("There is no such card in this deck"),
    EMAIL_ALREADY_TAKEN_ERROR_MESSAGE("Account with given email is already taken: "),
    USER_DONT_HAVE_PERMISSIONS_ERROR_MESSAGE("This user dont have permission to access this entity!"),
    WRONG_CREDENTIALS_ERROR_MESSAGE("Given credentials do not match any account"),
    AUTHORIZATION_ERROR_OCCURRED_ERROR_MESSAGE("Error occurred during authorization of JWT token"),
    CARD_DECK_INTERSECTION_ALREADY_EXISTS_ERROR_MESSAGE("That card already exists in this deck");

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
