package pl.lipinski.settlers_deckbuilder.util.enums;

public enum AccessLevel {
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE");

    private final String accessLevel;

    AccessLevel(String role) {
        this.accessLevel = role;
    }

    public String getAccessLevel() {
        return accessLevel;
    }
}
