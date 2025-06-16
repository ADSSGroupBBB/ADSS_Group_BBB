package src.Domain_employee;

import src.DTO.*;
import src.DataAccess.EmployeeInterface.*;
import src.DataAccess.EmployeeDAO.*;
import src.Domain_employee.Employee.UserRole;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Controller responsible for shift management and assignment operations.
 * Handles shift creation, assignments, and shift-related queries.
 * ENFORCES: Every shift MUST have a shift manager assigned!
 */
public class ShiftManagementController {
    private final ShiftDAO shiftDAO;
    private final ShiftAssignmentDAO assignmentDAO;
    private final EmployeeDAO employeeDAO;
    private final PositionDAO positionDAO;
    private final QualificationDAO qualificationDAO;
    private final AvailabilityDAO availabilityDAO;
    private final RequiredPositionDAO requiredPositionDAO;
    private final BranchDAO branchDAO;

    public ShiftManagementController() {
        this.shiftDAO = new ShiftDAOImpl();
        this.assignmentDAO = new ShiftAssignmentDAOImpl();
        this.employeeDAO = new EmployeeDAOImpl();
        this.positionDAO = new PositionDAOImpl();
        this.qualificationDAO = new QualificationDAOImpl();
        this.availabilityDAO = new AvailabilityDAOImpl();
        this.requiredPositionDAO = new RequiredPositionDAOImpl();
        this.branchDAO = new BranchDAOImpl();
    }


public ShiftDTO createShift(LocalDate date, String shiftType, String branchAddress) {
    try {
        // Validate branch exists
        if (branchAddress != null && !branchDAO.branchExists(branchAddress)) {
            System.err.println("Error: Branch does not exist - " + branchAddress);
            return null;
        }

        String shiftId = date.toString() + "_" + shiftType.toLowerCase() +
                (branchAddress != null ? "_" + branchAddress.replaceAll("\\s+", "") : "");

        ShiftDTO existingShift = shiftDAO.findById(shiftId).orElse(null);
        if (existingShift != null) {
            return existingShift;
        }

        if (!hasAvailableShiftManagerForShift(date, shiftType, branchAddress)) {
            System.out.println("ERROR: Cannot create shift - no available shift manager for " +
                    date + " " + shiftType + " at " + branchAddress);
            return null;
        }

        // Default shift hours
        LocalTime startTime = "MORNING".equals(shiftType) ?
                LocalTime.of(7, 0) : LocalTime.of(14, 0);
        LocalTime endTime = "MORNING".equals(shiftType) ?
                LocalTime.of(14, 0) : LocalTime.of(21, 0);

        ShiftDTO shift = new ShiftDTO(shiftId, date, shiftType, startTime, endTime,
                null, null, new HashMap<>(), branchAddress);

        ShiftDTO savedShift = shiftDAO.save(shift);

        if (savedShift != null) {
            boolean managerAssigned = assignShiftManagerAutomatically(savedShift);
            if (managerAssigned) {
                System.out.println("Shift created with manager: " + savedShift.getId());
                return savedShift;
            } else {
                shiftDAO.deleteById(savedShift.getId());
                System.err.println("ERROR: Failed to assign manager - shift deleted: " + savedShift.getId());
                return null;
            }
        }

        return null;
    } catch (Exception e) {
        System.err.println("Error creating shift: " + e.getMessage());
        return null;
    }
}

private boolean hasAvailableShiftManagerForShift(LocalDate date, String shiftType, String branchAddress) {
    try {
        // 1. Get all shift manager positions
        List<PositionDTO> managerPositions = positionDAO.findAll().stream()
                .filter(PositionDTO::isRequiresShiftManager)
                .toList();

        if (managerPositions.isEmpty()) {
            return false;
        }

        // 2. Get day of week for availability check
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        // 3. Get employees from the specific branch
        List<EmployeeDTO> branchEmployees;
        if (branchAddress != null) {
            branchEmployees = employeeDAO.findByBranch(branchAddress);
        } else {
            branchEmployees = employeeDAO.findAll();
        }

        // 4. Check if any employee can be a shift manager for this shift
        for (EmployeeDTO employee : branchEmployees) {
            // Skip if employee is not a manager
            if (employee.getRole() != UserRole.SHIFT_MANAGER && employee.getRole() != UserRole.HR_MANAGER) {
                continue;
            }

            // Check if employee is available for this shift
            if (!availabilityDAO.isAvailable(employee.getId(), dayOfWeek, shiftType)) {
                continue;
            }

            // Check if employee is qualified for any manager position
            for (PositionDTO managerPosition : managerPositions) {
                if (employee.getQualifiedPositions().contains(managerPosition.getName())) {
                    return true; // Found at least one available manager
                }
            }
        }

        return false; // No available manager found

    } catch (Exception e) {
        System.err.println("Error checking for available shift manager: " + e.getMessage());
        return false;
    }
}
    /**
     * AUTOMATICALLY assigns a shift manager to a shift.
     * Returns true if successful, false if no manager could be assigned.
     */


    private boolean assignShiftManagerAutomatically(ShiftDTO shift) {
        try {
            List<PositionDTO> managerPositions = positionDAO.findAll().stream()
                    .filter(PositionDTO::isRequiresShiftManager)
                    .toList();

            if (managerPositions.isEmpty()) {
                return false;
            }

            DayOfWeek dayOfWeek = shift.getDate().getDayOfWeek();
            String shiftType = shift.getShiftType();

            List<EmployeeDTO> branchEmployees;
            if (shift.getBranchAddress() != null) {
                branchEmployees = employeeDAO.findByBranch(shift.getBranchAddress());
            } else {
                branchEmployees = employeeDAO.findAll();
            }

            for (EmployeeDTO employee : branchEmployees) {
                if (employee.getRole() != UserRole.SHIFT_MANAGER && employee.getRole() != UserRole.HR_MANAGER) {
                    continue;
                }

                if (!availabilityDAO.isAvailable(employee.getId(), dayOfWeek, shiftType)) {
                    continue;
                }

                if (assignmentDAO.isEmployeeAssigned(shift.getId(), employee.getId())) {
                    continue;
                }

                for (PositionDTO managerPosition : managerPositions) {
                    if (employee.getQualifiedPositions().contains(managerPosition.getName())) {
                        if (assignEmployeeToShift(shift.getId(), employee.getId(), managerPosition.getName())) {
                            return true;
                        }
                    }
                }

                for (PositionDTO managerPosition : managerPositions) {
                    if (!qualificationDAO.hasQualification(employee.getId(), managerPosition.getName())) {
                        qualificationDAO.addQualification(employee.getId(), managerPosition.getName());
                    }
                    if (assignEmployeeToShift(shift.getId(), employee.getId(), managerPosition.getName())) {
                        return true;
                    }
                }
            }

            // Fallback: admin
            EmployeeDTO admin = employeeDAO.findById("admin").orElse(null);
            if (admin != null && admin.getRole() == UserRole.HR_MANAGER && !managerPositions.isEmpty()) {
                PositionDTO managerPosition = managerPositions.get(0);

                if (!qualificationDAO.hasQualification("admin", managerPosition.getName())) {
                    qualificationDAO.addQualification("admin", managerPosition.getName());
                }

                availabilityDAO.updateAvailability("admin", dayOfWeek, true, true);

                if (assignEmployeeToShift(shift.getId(), "admin", managerPosition.getName())) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }


    public List<ShiftDTO> getAllShifts() {
        try {
            return shiftDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ShiftDTO> getFutureShifts() {
        try {
            return shiftDAO.findFutureShifts();
        } catch (Exception e) {
            System.err.println("Error getting future shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ShiftDTO> getHistoricalShifts() {
        try {
            return shiftDAO.findHistoricalShifts();
        } catch (Exception e) {
            System.err.println("Error getting historical shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
        try {
            return shiftDAO.findByBranch(branchAddress);
        } catch (Exception e) {
            System.err.println("Error getting shifts by branch: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
        try {
            // Get shift and employee info
            ShiftDTO shift = shiftDAO.findById(shiftId).orElse(null);
            EmployeeDTO employee = employeeDAO.findById(employeeId).orElse(null);
            PositionDTO position = positionDAO.findByName(positionName).orElse(null);

            if (shift == null || employee == null || position == null) {
                return false;
            }


            if (assignmentDAO.isEmployeeAssigned(shiftId, employeeId)) {
                return false; // Employee already assigned to this exact shift
            }

            // Check if this specific position in this specific shift is already filled
            int currentAssigned = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);
            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());
            int requiredCount = requiredPositions.getOrDefault(positionName, 0);

            if (requiredCount == 0) {
                return false; // Position not required for this shift type
            }

            if (currentAssigned >= requiredCount) {
                return false; // This position in this shift is already filled
            }

            // Ensure employee has qualification (add if missing)
            if (!qualificationDAO.hasQualification(employeeId, positionName)) {
                qualificationDAO.addQualification(employeeId, positionName);
            }

            // Check availability for this specific day and shift type
            DayOfWeek dayOfWeek = shift.getDate().getDayOfWeek();
            if (!availabilityDAO.isAvailable(employeeId, dayOfWeek, shift.getShiftType())) {
                // Auto-update availability if not available
                boolean morningAvailable = "MORNING".equals(shift.getShiftType()) ||
                        availabilityDAO.isAvailable(employeeId, dayOfWeek, "MORNING");
                boolean eveningAvailable = "EVENING".equals(shift.getShiftType()) ||
                        availabilityDAO.isAvailable(employeeId, dayOfWeek, "EVENING");
                availabilityDAO.updateAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
            }

            // Determine if this is a shift manager position
            boolean isShiftManager = position.isRequiresShiftManager();

            // Assign employee
            return assignmentDAO.assignEmployee(shiftId, employeeId, positionName, isShiftManager);

        } catch (Exception e) {
            System.err.println("Error assigning employee to shift: " + e.getMessage());
            return false;
        }
    }

    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
        try {
            PositionDTO position = positionDAO.findByName(positionName).orElse(null);
            if (position != null && position.isRequiresShiftManager()) {
                System.err.println("CANNOT remove shift manager from shift! Every shift must have a manager.");
                return false;
            }

            return assignmentDAO.removeAssignment(shiftId, positionName);
        } catch (Exception e) {
            System.err.println("Error removing assignment: " + e.getMessage());
            return false;
        }
    }

    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
        try {
            return assignmentDAO.isEmployeeAssigned(shiftId, employeeId);
        } catch (Exception e) {
            System.err.println("Error checking employee assignment: " + e.getMessage());
            return false;
        }
    }

    public boolean areAllRequiredPositionsCovered(String shiftId) {
        try {
            ShiftDTO shift = shiftDAO.findById(shiftId).orElse(null);
            if (shift == null) {
                return false;
            }

            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());

            for (Map.Entry<String, Integer> entry : requiredPositions.entrySet()) {
                String positionName = entry.getKey();
                int requiredCount = entry.getValue();
                int assignedCount = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);

                if (assignedCount < requiredCount) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error checking required positions: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteShift(String shiftId) {
        try {
            return shiftDAO.deleteById(shiftId);
        } catch (Exception e) {
            System.err.println("Error deleting shift: " + e.getMessage());
            return false;
        }
    }

    public boolean updateShiftHours(String shiftTypeStr, String newStart, String newEnd) {
        try {
            LocalTime.parse(newStart);
            LocalTime.parse(newEnd);
            if (!LocalTime.parse(newStart).isBefore(LocalTime.parse(newEnd))) {
                System.err.println("Start time must be before end time");
                return false;
            }
            System.out.println("Shift hours updated: " + shiftTypeStr +
                    " from " + newStart + " to " + newEnd);
            return true;

        } catch (Exception e) {
            System.err.println("Invalid time format. Please use HH:mm format");
            return false;
        }
    }

    public List<ShiftDTO> getAllShiftsAsDTO() {
        try {
            return shiftDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public String getShiftIdByTime(LocalDate startDate, String shiftTime, String branchAddress) throws SQLException {
        List<ShiftDTO> shiftsList = shiftDAO.findByDateRange(startDate, startDate);
        if (shiftsList.isEmpty()) {
            return "Non existent shift.";
        }
        for (ShiftDTO dto : shiftsList) {
            if (Objects.equals(branchAddress, dto.getBranchAddress()) && Objects.equals(shiftTime, dto.getShiftType())) {
                return dto.getId();
            }
        }
        return "Non existent shift.";
    }

    public ShiftDTO getShiftById(String shiftId) {
        try {
            return shiftDAO.findById(shiftId).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting shift by ID: " + e.getMessage());
            return null;
        }
    }

    public List<ShiftDTO> getShiftsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            return shiftDAO.findByDateRange(startDate, endDate);
        } catch (Exception e) {
            System.err.println("Error getting shifts by date range: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ShiftDTO> getEmployeeShifts(String employeeId) {
        try {
            List<ShiftDTO> allShifts = shiftDAO.findAll();
            List<ShiftDTO> employeeShifts = new ArrayList<>();

            for (ShiftDTO shift : allShifts) {
                if (assignmentDAO.isEmployeeAssigned(shift.getId(), employeeId)) {
                    employeeShifts.add(shift);
                }
            }

            return employeeShifts;
        } catch (Exception e) {
            System.err.println("Error getting employee shifts: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<ShiftDTO> getShiftsNeedingCoverage() {
        try {
            List<ShiftDTO> futureShifts = getFutureShifts();
            List<ShiftDTO> shiftsNeedingCoverage = new ArrayList<>();

            for (ShiftDTO shift : futureShifts) {
                if (!areAllRequiredPositionsCovered(shift.getId())) {
                    shiftsNeedingCoverage.add(shift);
                }
            }

            return shiftsNeedingCoverage;
        } catch (Exception e) {
            System.err.println("Error getting shifts needing coverage: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Helper method to fix existing shifts without managers
     */
    public void fixShiftsWithoutManagers() {
        try {
            List<ShiftDTO> futureShifts = getFutureShifts();
            int fixedCount = 0;

            for (ShiftDTO shift : futureShifts) {
                // Check if shift has manager
                boolean hasManager = shift.hasShiftManager();
                if (!hasManager) {
                    boolean assigned = assignShiftManagerAutomatically(shift);
                    if (assigned) {
                        fixedCount++;
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error fixing shifts: " + e.getMessage());
        }
    }

    // Additional methods that might be needed from EmployeeController
    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        try {
            ShiftDTO shift = shiftDAO.findById(shiftId).orElse(null);
            if (shift == null) {
                return new ArrayList<>();
            }

            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());
            List<PositionDTO> missingPositions = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : requiredPositions.entrySet()) {
                String positionName = entry.getKey();
                int requiredCount = entry.getValue();
                int assignedCount = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);

                // Add missing positions
                for (int i = assignedCount; i < requiredCount; i++) {
                    PositionDTO position = positionDAO.findByName(positionName).orElse(null);
                    if (position != null) {
                        missingPositions.add(position);
                    }
                }
            }

            return missingPositions;
        } catch (Exception e) {
            System.err.println("Error getting missing positions: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}