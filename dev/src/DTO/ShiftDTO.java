package DTO;

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
    private final String branchAddress; // New field for branch

    public ShiftDTO(String id, LocalDate date, String shiftType, LocalTime startTime, LocalTime endTime,
                    String shiftManagerId, String shiftManagerName, Map<String, String> assignments,
                    String branchAddress) {
        this.id = id;
        this.date = date;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftManagerId = shiftManagerId;
        this.shiftManagerName = shiftManagerName;
        this.assignments = assignments;
        this.branchAddress = branchAddress;
    }

    // Constructor without branch for backward compatibility
    public ShiftDTO(String id, LocalDate date, String shiftType, LocalTime startTime, LocalTime endTime,
                    String shiftManagerId, String shiftManagerName, Map<String, String> assignments) {
        this(id, date, shiftType, startTime, endTime, shiftManagerId, shiftManagerName, assignments, null);
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

    public String getBranchAddress() {
        return branchAddress;
    }

    public boolean hasShiftManager() {
        return shiftManagerId != null && !shiftManagerId.isEmpty();
    }

    public boolean hasBranch() {
        return branchAddress != null && !branchAddress.trim().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s %s shift on %s%s",
                date, shiftType.toLowerCase(),
                startTime,
                hasBranch() ? " at " + branchAddress : "");
    }
}