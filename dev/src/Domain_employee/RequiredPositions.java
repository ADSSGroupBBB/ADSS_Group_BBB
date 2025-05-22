package Domain_employee;

import java.util.HashMap;
import java.util.Map;

/**
 * The RequiredPositions class manages the staffing requirements for different shift types.
 * It stores information about how many employees with specific qualifications are needed
 * for each type of shift (morning and evening).

 */
public class RequiredPositions {
    private Map<ShiftType, Map<Position, Integer>> shiftTypeToRequiredPositions;// Mapping between shift type and the required positions with their counts


    /**
     * Constructs a new RequiredPositions instance.
     * Initializes empty requirement maps for both morning and evening shifts.
     */
    public RequiredPositions() {
        this.shiftTypeToRequiredPositions = new HashMap<>();
        shiftTypeToRequiredPositions.put(ShiftType.MORNING, new HashMap<>());
        shiftTypeToRequiredPositions.put(ShiftType.EVENING, new HashMap<>());
    }


    public void setRequiredPosition(ShiftType shiftType, Position position, int count) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(shiftType);
        requiredPositions.put(position, count);
    }



    public Map<Position, Integer> getRequiredPositionsMap(ShiftType shiftType) {
        return new HashMap<>(shiftTypeToRequiredPositions.get(shiftType));
    }

    /**
     * Checks if all required positions for a shift type are adequately covered by assigned employees.
     * @param shiftType The type of shift (MORNING or EVENING)
     * @param assignedPositions Map of positions to assigned employees in the shift
     * @return true if all required positions have enough employees assigned, false otherwise
     */
    public boolean areAllRequiredPositionsCovered(ShiftType shiftType, Map<Position, Employee> assignedPositions) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(shiftType);
        for (Map.Entry<Position, Integer> entry : requiredPositions.entrySet()) {
            Position position = entry.getKey();
            int requiredCount = entry.getValue();
            long assignedCount = assignedPositions.entrySet().stream().filter(e -> e.getKey().equals(position)).count();
            if (assignedCount < requiredCount) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Required Positions:\n");

        sb.append("Morning shift:\n");
        for (Map.Entry<Position, Integer> entry : shiftTypeToRequiredPositions.get(ShiftType.MORNING).entrySet()) {sb.append("  ").append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(" Employees\n");
        }

        sb.append("Evening shift:\n");
        for (Map.Entry<Position, Integer> entry : shiftTypeToRequiredPositions.get(ShiftType.EVENING).entrySet()) {
            sb.append("  ").append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(" Employees\n");
        }

        return sb.toString();
    }
    public int getRequiredCount(ShiftType shiftType, Position position) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(shiftType);
        return requiredPositions.getOrDefault(position, 0);
    }

}