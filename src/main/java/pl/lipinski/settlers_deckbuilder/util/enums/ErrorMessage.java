package pl.lipinski.settlers_deckbuilder.util.enums;

public enum ErrorMessage {

    CAN_NOT_FIND_USER_BY_ID_ERROR_MESSAGE("Can not find user with given id");

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
