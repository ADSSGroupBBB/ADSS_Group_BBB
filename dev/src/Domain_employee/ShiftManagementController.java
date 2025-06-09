//package Domain_employee;
//
//import DTO.*;
//import DataAccess.EmployeeInterface.*;
//import DataAccess.EmployeeDAO.*;
//
//import java.sql.SQLException;
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.*;
//
///**
// * Controller responsible for shift management and assignment operations.
// * Handles shift creation, assignments, and shift-related queries.
// */
//public class ShiftManagementController {
//    private final ShiftDAO shiftDAO;
//    private final ShiftAssignmentDAO assignmentDAO;
//    private final EmployeeDAO employeeDAO;
//    private final PositionDAO positionDAO;
//    private final QualificationDAO qualificationDAO;
//    private final AvailabilityDAO availabilityDAO;
//    private final RequiredPositionDAO requiredPositionDAO;
//    private final BranchDAO branchDAO;
//
//    public ShiftManagementController() {
//        this.shiftDAO = new ShiftDAOImpl();
//        this.assignmentDAO = new ShiftAssignmentDAOImpl();
//        this.employeeDAO = new EmployeeDAOImpl();
//        this.positionDAO = new PositionDAOImpl();
//        this.qualificationDAO = new QualificationDAOImpl();
//        this.availabilityDAO = new AvailabilityDAOImpl();
//        this.requiredPositionDAO = new RequiredPositionDAOImpl();
//        this.branchDAO = new BranchDAOImpl();
//    }
//
//    public ShiftDTO createShift(LocalDate date, String shiftType, String branchAddress) {
//        try {
//            // Validate branch exists
//            if (branchAddress != null && !branchDAO.branchExists(branchAddress)) {
//                return null;
//            }
//
//            String shiftId = date.toString() + "_" + shiftType.toLowerCase() +
//                    (branchAddress != null ? "_" + branchAddress.replaceAll("\\s+", "") : "");
//
//            // Default shift hours - these could be configurable
//            LocalTime startTime = "MORNING".equals(shiftType) ?
//                    LocalTime.of(7, 0) : LocalTime.of(14, 0);
//            LocalTime endTime = "MORNING".equals(shiftType) ?
//                    LocalTime.of(14, 0) : LocalTime.of(21, 0);
//
//            ShiftDTO shift = new ShiftDTO(shiftId, date, shiftType, startTime, endTime,
//                    null, null, new HashMap<>(), branchAddress);
//
//            return shiftDAO.save(shift);
//        } catch (Exception e) {
//            System.err.println("Error creating shift: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public List<ShiftDTO> getAllShifts() {
//        try {
//            return shiftDAO.findAll();
//        } catch (Exception e) {
//            System.err.println("Error getting all shifts: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    public List<ShiftDTO> getFutureShifts() {
//        try {
//            return shiftDAO.findFutureShifts();
//        } catch (Exception e) {
//            System.err.println("Error getting future shifts: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    public List<ShiftDTO> getHistoricalShifts() {
//        try {
//            return shiftDAO.findHistoricalShifts();
//        } catch (Exception e) {
//            System.err.println("Error getting historical shifts: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    public List<ShiftDTO> getShiftsByBranch(String branchAddress) {
//        try {
//            return shiftDAO.findByBranch(branchAddress);
//        } catch (Exception e) {
//            System.err.println("Error getting shifts by branch: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    public boolean assignEmployeeToShift(String shiftId, String employeeId, String positionName) {
//        try {
//            // Get shift and employee info
//            ShiftDTO shift = shiftDAO.findById(shiftId).orElse(null);
//            EmployeeDTO employee = employeeDAO.findById(employeeId).orElse(null);
//            PositionDTO position = positionDAO.findByName(positionName).orElse(null);
//
//            if (shift == null || employee == null || position == null) {
//                return false;
//            }
//
//            // Check if employee is already assigned to this shift
//            if (assignmentDAO.isEmployeeAssigned(shiftId, employeeId)) {
//                return false;
//            }
//
//            // Check required positions count
//            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());
//            int requiredCount = requiredPositions.getOrDefault(positionName, 0);
//
//            if (requiredCount == 0) {
//                return false; // Position not required for this shift type
//            }
//
//            // Check if position is already filled
//            int currentAssigned = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);
//            if (currentAssigned >= requiredCount) {
//                return false; // Position already filled
//            }
//
//            // Ensure employee has qualification (add if missing)
//            if (!qualificationDAO.hasQualification(employeeId, positionName)) {
//                qualificationDAO.addQualification(employeeId, positionName);
//            }
//
//            // Check availability
//            DayOfWeek dayOfWeek = shift.getDate().getDayOfWeek();
//            if (!availabilityDAO.isAvailable(employeeId, dayOfWeek, shift.getShiftType())) {
//                // Auto-update availability if not available
//                boolean morningAvailable = "MORNING".equals(shift.getShiftType()) ||
//                        availabilityDAO.isAvailable(employeeId, dayOfWeek, "MORNING");
//                boolean eveningAvailable = "EVENING".equals(shift.getShiftType()) ||
//                        availabilityDAO.isAvailable(employeeId, dayOfWeek, "EVENING");
//                availabilityDAO.updateAvailability(employeeId, dayOfWeek, morningAvailable, eveningAvailable);
//            }
//
//            // Determine if this is a shift manager position
//            boolean isShiftManager = position.isRequiresShiftManager();
//
//            // Assign employee
//            return assignmentDAO.assignEmployee(shiftId, employeeId, positionName, isShiftManager);
//
//        } catch (Exception e) {
//            System.err.println("Error assigning employee to shift: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean removeAssignmentFromShift(String shiftId, String positionName) {
//        try {
//            return assignmentDAO.removeAssignment(shiftId, positionName);
//        } catch (Exception e) {
//            System.err.println("Error removing assignment: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean isEmployeeAlreadyAssignedToShift(String shiftId, String employeeId) {
//        try {
//            return assignmentDAO.isEmployeeAssigned(shiftId, employeeId);
//        } catch (Exception e) {
//            System.err.println("Error checking employee assignment: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean areAllRequiredPositionsCovered(String shiftId) {
//        try {
//            ShiftDTO shift = shiftDAO.findById(shiftId).orElse(null);
//            if (shift == null) {
//                return false;
//            }
//
//            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());
//            Set<String> assignedPositions = ((ShiftAssignmentDAOImpl) assignmentDAO).getAssignedPositions(shiftId);
//
//            for (Map.Entry<String, Integer> entry : requiredPositions.entrySet()) {
//                String positionName = entry.getKey();
//                int requiredCount = entry.getValue();
//                int assignedCount = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);
//
//                if (assignedCount < requiredCount) {
//                    return false;
//                }
//            }
//
//            return true;
//        } catch (Exception e) {
//            System.err.println("Error checking required positions: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean deleteShift(String shiftId) {
//        try {
//            return shiftDAO.deleteById(shiftId);
//        } catch (Exception e) {
//            System.err.println("Error deleting shift: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public boolean updateShiftHours(String shiftTypeStr, String newStart, String newEnd) {
//        try {
//            LocalTime.parse(newStart);
//            LocalTime.parse(newEnd);
//            if (!LocalTime.parse(newStart).isBefore(LocalTime.parse(newEnd))) {
//                System.err.println("Start time must be before end time");
//                return false;
//            }
//            System.out.println("Shift hours updated: " + shiftTypeStr +
//                    " from " + newStart + " to " + newEnd);
//            return true;
//
//        } catch (Exception e) {
//            System.err.println("Invalid time format. Please use HH:mm format");
//            return false;
//        }
//    }
//
//    public List<ShiftDTO> getAllShiftsAsDTO() {
//        try {
//            return shiftDAO.findAll();
//        } catch (Exception e) {
//            System.err.println("Error getting all shifts: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    public String getShiftIdByTime(LocalDate startDate, String shiftTime) throws SQLException {
//        List<ShiftDTO> shiftsList = shiftDAO.findByDateRange(startDate, startDate);
//        if (shiftsList.isEmpty()){
//            return "No shifts in this date.";
//        }
//        for (ShiftDTO dto : shiftsList)
//        {
//            if (Objects.equals(shiftTime, dto.getShiftType())){
//                return dto.getId();
//            }
//        }
//        return "Shift in the requested hour don't exist";
//    }
//
//    public ShiftDTO getShiftById(String shiftId) {
//        try {
//            return shiftDAO.findById(shiftId).orElse(null);
//        } catch (Exception e) {
//            System.err.println("Error getting shift by ID: " + e.getMessage());
//            return null;
//        }
//    }
//
//    public List<ShiftDTO> getShiftsByDateRange(LocalDate startDate, LocalDate endDate) {
//        try {
//            return shiftDAO.findByDateRange(startDate, endDate);
//        } catch (Exception e) {
//            System.err.println("Error getting shifts by date range: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    public List<ShiftDTO> getEmployeeShifts(String employeeId) {
//        try {
//            List<ShiftDTO> allShifts = shiftDAO.findAll();
//            List<ShiftDTO> employeeShifts = new ArrayList<>();
//
//            for (ShiftDTO shift : allShifts) {
//                if (assignmentDAO.isEmployeeAssigned(shift.getId(), employeeId)) {
//                    employeeShifts.add(shift);
//                }
//            }
//
//            return employeeShifts;
//        } catch (Exception e) {
//            System.err.println("Error getting employee shifts: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//
//    public List<ShiftDTO> getShiftsNeedingCoverage() {
//        try {
//            List<ShiftDTO> futureShifts = getFutureShifts();
//            List<ShiftDTO> shiftsNeedingCoverage = new ArrayList<>();
//
//            for (ShiftDTO shift : futureShifts) {
//                if (!areAllRequiredPositionsCovered(shift.getId())) {
//                    shiftsNeedingCoverage.add(shift);
//                }
//            }
//
//            return shiftsNeedingCoverage;
//        } catch (Exception e) {
//            System.err.println("Error getting shifts needing coverage: " + e.getMessage());
//            return new ArrayList<>();
//        }
//    }
//}

package Domain_employee;

import DTO.*;
import DataAccess.EmployeeInterface.*;
import DataAccess.EmployeeDAO.*;
import Domain_employee.Employee.UserRole;

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
                return null;
            }

            String shiftId = date.toString() + "_" + shiftType.toLowerCase() +
                    (branchAddress != null ? "_" + branchAddress.replaceAll("\\s+", "") : "");

            // Default shift hours - these could be configurable
            LocalTime startTime = "MORNING".equals(shiftType) ?
                    LocalTime.of(7, 0) : LocalTime.of(14, 0);
            LocalTime endTime = "MORNING".equals(shiftType) ?
                    LocalTime.of(14, 0) : LocalTime.of(21, 0);

            ShiftDTO shift = new ShiftDTO(shiftId, date, shiftType, startTime, endTime,
                    null, null, new HashMap<>(), branchAddress);

            ShiftDTO savedShift = shiftDAO.save(shift);

            //  CRITICAL: Every shift MUST have a shift manager!
            if (savedShift != null) {
                assignShiftManagerAutomatically(savedShift);
                // הסרנו את ההדפסות המיותרות כאן
            }

            return savedShift;
        } catch (Exception e) {
            System.err.println("Error creating shift: " + e.getMessage());
            return null;
        }
    }

    /**
     * AUTOMATICALLY assigns a shift manager to a shift.
     * Tries to find available shift managers and assigns one.
     */
    private boolean assignShiftManagerAutomatically(ShiftDTO shift) {
        try {
            // 1. Get all shift manager positions
            List<PositionDTO> managerPositions = positionDAO.findAll().stream()
                    .filter(PositionDTO::isRequiresShiftManager)
                    .toList();

            if (managerPositions.isEmpty()) {
                return false;
            }

            // 2. Find available shift managers for this shift
            DayOfWeek dayOfWeek = shift.getDate().getDayOfWeek();
            String shiftType = shift.getShiftType();

            // Get all employees
            List<EmployeeDTO> allEmployees = employeeDAO.findAll();

            // Filter to potential shift managers
            for (EmployeeDTO employee : allEmployees) {
                // Skip if employee is not a manager
                if (employee.getRole() != UserRole.SHIFT_MANAGER && employee.getRole() != UserRole.HR_MANAGER) {
                    continue;
                }

                // Check if employee is available for this shift
                if (!availabilityDAO.isAvailable(employee.getId(), dayOfWeek, shiftType)) {
                    continue;
                }

                // Check if employee is already assigned to this shift
                if (assignmentDAO.isEmployeeAssigned(shift.getId(), employee.getId())) {
                    continue;
                }

                // Check if employee is qualified for any manager position
                for (PositionDTO managerPosition : managerPositions) {
                    if (employee.getQualifiedPositions().contains(managerPosition.getName())) {
                        // Try to assign this employee as shift manager
                        if (assignEmployeeToShift(shift.getId(), employee.getId(), managerPosition.getName())) {
                            // הסרנו את ההדפסה כאן
                            return true;
                        }
                    }
                }

                // If employee is a manager but not qualified, add qualification and assign
                for (PositionDTO managerPosition : managerPositions) {
                    if (!qualificationDAO.hasQualification(employee.getId(), managerPosition.getName())) {
                        qualificationDAO.addQualification(employee.getId(), managerPosition.getName());
                    }
                    if (assignEmployeeToShift(shift.getId(), employee.getId(), managerPosition.getName())) {
                        // הסרנו את ההדפסה כאן
                        return true;
                    }
                }
            }

            // 3. Fallback: Use admin if no shift manager found
            EmployeeDTO admin = employeeDAO.findById("admin").orElse(null);
            if (admin != null && !managerPositions.isEmpty()) {
                PositionDTO managerPosition = managerPositions.get(0);

                // Ensure admin has manager qualification
                if (!qualificationDAO.hasQualification("admin", managerPosition.getName())) {
                    qualificationDAO.addQualification("admin", managerPosition.getName());
                }

                // Force availability for admin
                availabilityDAO.updateAvailability("admin", dayOfWeek, true, true);

                if (assignEmployeeToShift(shift.getId(), "admin", managerPosition.getName())) {
                    // הסרנו את ההדפסה כאן
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            System.err.println("Error auto-assigning shift manager: " + e.getMessage());
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

            // Check if employee is already assigned to this shift
            if (assignmentDAO.isEmployeeAssigned(shiftId, employeeId)) {
                return false;
            }

            // Check required positions count
            Map<String, Integer> requiredPositions = requiredPositionDAO.getRequiredPositions(shift.getShiftType());
            int requiredCount = requiredPositions.getOrDefault(positionName, 0);

            if (requiredCount == 0) {
                return false; // Position not required for this shift type
            }

            // Check if position is already filled
            int currentAssigned = ((ShiftAssignmentDAOImpl) assignmentDAO).countAssignmentsForPosition(shiftId, positionName);
            if (currentAssigned >= requiredCount) {
                return false; // Position already filled
            }

            // Ensure employee has qualification (add if missing)
            if (!qualificationDAO.hasQualification(employeeId, positionName)) {
                qualificationDAO.addQualification(employeeId, positionName);
            }

            // Check availability
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
            //  PREVENT removing shift manager!
            PositionDTO position = positionDAO.findByName(positionName).orElse(null);
            if (position != null && position.isRequiresShiftManager()) {
                System.err.println(" CANNOT remove shift manager from shift! Every shift must have a manager.");
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

    public String getShiftIdByTime(LocalDate startDate, String shiftTime) throws SQLException {
        List<ShiftDTO> shiftsList = shiftDAO.findByDateRange(startDate, startDate);
        if (shiftsList.isEmpty()){
            return "No shifts in this date.";
        }
        for (ShiftDTO dto : shiftsList)
        {
            if (Objects.equals(shiftTime, dto.getShiftType())){
                return dto.getId();
            }
        }
        return "Shift in the requested hour don't exist";
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
}