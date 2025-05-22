package Service_employee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * Data Transfer Object for Shift information
 * Used to pass shift data between service and presentation layers
 * without exposing domain objects
 */
public class ShiftDTO {
    private final String id;
    private final LocalDate date;
    private final String shiftType;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String shiftManagerId;
    private final String shiftManagerName;
    private final Map<String, String> assignments;


    public ShiftDTO(String id, LocalDate date, String shiftType, LocalTime startTime, LocalTime endTime,
                    String shiftManagerId, String shiftManagerName, Map<String, String> assignments) {
        this.id = id;
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        this.shiftManagerName = shiftManagerName;
        this.assignments = assignments;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getShiftType() {
        return shiftType;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getShiftManagerId() {
        return shiftManagerId;
    }

    public String getShiftManagerName() {
        return shiftManagerName;
    }
    public Map<String, String> getAssignments() {
        return assignments;
    }
    public boolean hasShiftManager() {
        return shiftManagerId != null && !shiftManagerId.isEmpty();
    }
}