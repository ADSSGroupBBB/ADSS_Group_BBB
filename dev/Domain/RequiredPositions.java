package Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class RequiredPositions {
    // מיפוי בין סוג המשמרת (בוקר/ערב) לבין התפקידים הנדרשים ומספרם
    private Map<Boolean, Map<Position, Integer>> shiftTypeToRequiredPositions; /// ??? למה לא שיפט טיים

    public RequiredPositions() {
        this.shiftTypeToRequiredPositions = new HashMap<>();
        shiftTypeToRequiredPositions.put(false, new HashMap<>()); // משמרת בוקר
        shiftTypeToRequiredPositions.put(true, new HashMap<>());  // משמרת ערב
    }

    public void setRequiredPosition(boolean isEveningShift, Position position, int count) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(isEveningShift);
        requiredPositions.put(position, count);
    }


    public boolean removeRequiredPosition(boolean isEveningShift, Position position) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(isEveningShift);
        return requiredPositions.remove(position) != null;
    }

    public int getRequiredCount(boolean isEveningShift, Position position) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(isEveningShift);
        return requiredPositions.getOrDefault(position, 0);
    }

    public Set<Position> getAllRequiredPositions(boolean isEveningShift) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(isEveningShift);
        return new HashSet<>(requiredPositions.keySet());
    }

    public Map<Position, Integer> getRequiredPositionsMap(boolean isEveningShift) {
        return new HashMap<>(shiftTypeToRequiredPositions.get(isEveningShift));
    }

    public boolean areAllRequiredPositionsCovered(boolean isEveningShift, Map<Position, Employee> assignedPositions) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(isEveningShift);
        for (Map.Entry<Position, Integer> entry : requiredPositions.entrySet()) {
            Position position = entry.getKey();
            int requiredCount = entry.getValue();
            long assignedCount = assignedPositions.entrySet().stream().filter(e -> e.getKey().equals(position)).count(); /// ??? למה לונג
            if (assignedCount < requiredCount) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("תפקידים נדרשים:\n");

        sb.append("Morning shift:\n");
        for (Map.Entry<Position, Integer> entry : shiftTypeToRequiredPositions.get(false).entrySet()) {
            sb.append("  ").append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(" Employees\n");
        }

        sb.append("Evening shift:\n");
        for (Map.Entry<Position, Integer> entry : shiftTypeToRequiredPositions.get(true).entrySet()) {
            sb.append("  ").append(entry.getKey().getName()).append(": ").append(entry.getValue()).append(" Employees\n");
        }
        return sb.toString();
    }
}
