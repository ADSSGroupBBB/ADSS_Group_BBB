package Service_employee;

/**
 * Data Transfer Object for Position information
 * Used to pass position data between service and presentation layers
 * without exposing domain objects
 */
public class PositionDTO {
    private final String name;
    private final boolean requiresShiftManager;

    public PositionDTO(String name, boolean requiresShiftManager) {
        this.name = name;
        this.requiresShiftManager = requiresShiftManager;
    }

    public String getName() {
        return name;
    }

    public boolean isRequiresShiftManager() {
        return requiresShiftManager;
    }
}