package Domain_employee;

import DTO.*;
import DataAccess.EmployeeInterface.*;
import DataAccess.EmployeeDAO.*;
import Domain_employee.Employee.UserRole;

import java.util.*;

/**
 * Controller responsible for position and qualification management.
 * Handles positions, qualifications, and required positions for shifts.
 */
public class PositionManagementController {
    private final PositionDAO positionDAO;
    private final QualificationDAO qualificationDAO;
    private final RequiredPositionDAO requiredPositionDAO;
    private final EmployeeDAO employeeDAO;
    private final ShiftDAO shiftDAO;
    private final ShiftAssignmentDAO assignmentDAO;

    public PositionManagementController() {
        this.positionDAO = new PositionDAOImpl();
        this.qualificationDAO = new QualificationDAOImpl();
        this.requiredPositionDAO = new RequiredPositionDAOImpl();
        this.employeeDAO = new EmployeeDAOImpl();
        this.shiftDAO = new ShiftDAOImpl();
        this.assignmentDAO = new ShiftAssignmentDAOImpl();
    }

    public boolean addPosition(String name, boolean isShiftManagerRole) {
        try {
            PositionDTO position = new PositionDTO(name, isShiftManagerRole);
            positionDAO.save(position);

            // If it's a shift manager position, set default requirements
            if (isShiftManagerRole) {
                requiredPositionDAO.setRequiredPosition("MORNING", name, 1);
                requiredPositionDAO.setRequiredPosition("EVENING", name, 1);
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error adding position: " + e.getMessage());
            return false;
        }
    }

    public List<PositionDTO> getAllPositions() {
        try {
            return positionDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting positions: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public PositionDTO getPositionDetails(String name) {
        try {
            return positionDAO.findByName(name).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting position details: " + e.getMessage());
            return null;
        }
    }

    public boolean addQualificationToEmployee(String employeeId, String positionName) {
        try {
            boolean success = qualificationDAO.addQualification(employeeId, positionName);

            if (success) {
                // Check if position requires shift manager role
                PositionDTO position = positionDAO.findByName(positionName).orElse(null);
                if (position != null && position.isRequiresShiftManager()) {
                    // Update employee role to SHIFT_MANAGER
                    EmployeeDTO employee = employeeDAO.findById(employeeId).orElse(null);
                    if (employee != null && employee.getRole() == UserRole.REGULAR_EMPLOYEE) {
                        EmployeeDTO updatedEmployee = new EmployeeDTO(
                                employee.getId(), employee.getFirstName(), employee.getLastName(),
                                employee.getBankAccount(), employee.getStartDate(), employee.getSalary(),
                                employee.getQualifiedPositions(), UserRole.SHIFT_MANAGER,
                                employee.getSickDays(), employee.getVacationDays(),
                                employee.getPensionFundName(), employee.getBranchAddress()
                        );
                        employeeDAO.updateEmployee(updatedEmployee);
                    }
                }
            }

            return success;
        } catch (Exception e) {
            System.err.println("Error adding qualification: " + e.getMessage());
            return false;
        }
    }

    public boolean removeQualificationFromEmployee(String employeeId, String positionName) {
        try {
            return qualificationDAO.removeQualification(employeeId, positionName);
        } catch (Exception e) {
            System.err.println("Error removing qualification: " + e.getMessage());
            return false;
        }
    }

    public List<EmployeeDTO> getQualifiedEmployeesForPosition(String positionName) {
        try {
            List<String> employeeIds = qualificationDAO.getQualifiedEmployees(positionName);
            List<EmployeeDTO> employees = new ArrayList<>();

            for (String id : employeeIds) {
                EmployeeDTO employee = employeeDAO.findById(id).orElse(null);
                if (employee != null) {
                    employees.add(employee);
                }
            }

            return employees;
        } catch (Exception e) {
            System.err.println("Error getting qualified employees: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean addRequiredPosition(String shiftType, String positionName, int count) {
        try {
            return requiredPositionDAO.setRequiredPosition(shiftType, positionName, count);
        } catch (Exception e) {
            System.err.println("Error setting required position: " + e.getMessage());
            return false;
        }
    }

    public int getRequiredPositionsCount(String shiftType, String positionName) {
        try {
            return requiredPositionDAO.getRequiredCount(shiftType, positionName);
        } catch (Exception e) {
            System.err.println("Error getting required positions count: " + e.getMessage());
            return 0;
        }
    }

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