package Domain;

/**
 * The Position class represents a job position or role within the organization.
 * Each position has a unique name and may require shift manager privileges to perform.
 */

public class Position {
    private String name;
    private boolean requiresShiftManager;


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