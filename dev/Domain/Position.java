package Domain;

import java.util.Objects;

public class Position {
    private String name;
    private boolean requiresShiftManager; // האם תפקיד של מנהל משמרת


    public Position( String name, boolean requiresShiftManager) {
        this.name = name;
        this.requiresShiftManager = requiresShiftManager;
    }

    // Getters & Setters

    public String getName() {
        return name;
    }

    public boolean isRequiresShiftManager() {
        return requiresShiftManager;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equals(name, position.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
