package pl.lipinski.settlers_deckbuilder.util.enums;

public enum ErrorCode {

    CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE(520),
    CAN_NOT_FIND_USER_BY_ID_ERROR_CODE(521),
    EMAIL_ALREADY_TAKEN_ERROR_CODE(522),
    USER_DONT_HAVE_PERMISSIONS_ERROR_CODE(523),
    WRONG_CREDENTIALS_ERROR_CODE(524);

    private Integer value;

    public Integer getValue(){
        return this.value;
    }

    ErrorCode(Integer value) {
        this.value = value;
    }
}