package Domain;

public class Position {
    private String name;
    private String description;
    private boolean requiresShiftManager; // האם תפקיד של מנהל משמרת


    public Position( String name, String description, boolean requiresShiftManager) {
        this.name = name;
        this.description = description;
        this.requiresShiftManager = requiresShiftManager;
    }

    // Getters & Setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequiresShiftManager() {
        return requiresShiftManager;
    }

    public void setRequiresShiftManager(boolean requiresShiftManager) {
        this.requiresShiftManager = requiresShiftManager;
    }


    @Override
    public String toString() {
        return name;
    }
}