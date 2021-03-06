package pl.lipinski.settlers_deckbuilder.util.enums;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
