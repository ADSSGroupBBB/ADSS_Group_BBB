package Domain;

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

    public void setName(String name) {
        this.name = name;
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