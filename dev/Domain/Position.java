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


}