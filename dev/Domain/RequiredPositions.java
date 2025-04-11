package Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class RequiredPositions {
    // מיפוי בין סוג המשמרת לבין התפקידים הנדרשים ומספרם
    private Map<ShiftType, Map<Position, Integer>> shiftTypeToRequiredPositions;

    public RequiredPositions() {
        this.shiftTypeToRequiredPositions = new HashMap<>();
        shiftTypeToRequiredPositions.put(ShiftType.MORNING, new HashMap<>()); // משמרת בוקר
        shiftTypeToRequiredPositions.put(ShiftType.EVENING, new HashMap<>());  // משמרת ערב
    }


    public void setRequiredPosition(ShiftType shiftType, Position position, int count) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(shiftType);
        requiredPositions.put(position, count);
    }


    public boolean removeRequiredPosition(ShiftType shiftType, Position position) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(shiftType);
        return requiredPositions.remove(position) != null;
    }

    public int getRequiredCount(ShiftType shiftType, Position position) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(shiftType);
        return requiredPositions.getOrDefault(position, 0);
    }


    public Set<Position> getAllRequiredPositions(ShiftType shiftType) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(shiftType);
        return new HashSet<>(requiredPositions.keySet());
    }


    public Map<Position, Integer> getRequiredPositionsMap(ShiftType shiftType) {
        return new HashMap<>(shiftTypeToRequiredPositions.get(shiftType));
    }


    public boolean areAllRequiredPositionsCovered(ShiftType shiftType, Map<Position, Employee> assignedPositions) {
        Map<Position, Integer> requiredPositions = shiftTypeToRequiredPositions.get(shiftType);

        // בדיקה שכל התפקידים הנדרשים קיימים בשיבוץ
        for (Map.Entry<Position, Integer> entry : requiredPositions.entrySet()) {
            Position position = entry.getKey();
            int requiredCount = entry.getValue();

            // מספר העובדים שמשובצים לתפקיד זה
            // stream.count() מחזיר long ולכן נשמר המספר כ-long
            long assignedCount = assignedPositions.entrySet().stream()
                    .filter(e -> e.getKey().equals(position))
                    .count();

            if (assignedCount < requiredCount) {
                return false;
            }
        }

        return true;
    }


    public void setRequiredPosition(boolean isEveningShift, Position position, int count) {
        ShiftType shiftType = isEveningShift ? ShiftType.EVENING : ShiftType.MORNING;
        setRequiredPosition(shiftType, position, count);
    }


    public boolean removeRequiredPosition(boolean isEveningShift, Position position) {
        ShiftType shiftType = isEveningShift ? ShiftType.EVENING : ShiftType.MORNING;
        return removeRequiredPosition(shiftType, position);
    }


    public int getRequiredCount(boolean isEveningShift, Position position) {
        ShiftType shiftType = isEveningShift ? ShiftType.EVENING : ShiftType.MORNING;
        return getRequiredCount(shiftType, position);
    }


    public Set<Position> getAllRequiredPositions(boolean isEveningShift) {
        ShiftType shiftType = isEveningShift ? ShiftType.EVENING : ShiftType.MORNING;
        return getAllRequiredPositions(shiftType);
    }

    public Map<Position, Integer> getRequiredPositionsMap(boolean isEveningShift) {
        ShiftType shiftType = isEveningShift ? ShiftType.EVENING : ShiftType.MORNING;
        return getRequiredPositionsMap(shiftType);
    }


    public boolean areAllRequiredPositionsCovered(boolean isEveningShift, Map<Position, Employee> assignedPositions) {
        ShiftType shiftType = isEveningShift ? ShiftType.EVENING : ShiftType.MORNING;
        return areAllRequiredPositionsCovered(shiftType, assignedPositions);
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
}