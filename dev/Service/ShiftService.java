package Service;

import Domain.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dedicated service for managing shifts and assignments.
 * Focuses on shift-specific operations, separating responsibility from EmployeeService.
 */
public class ShiftService {
    private final EmployeeManager employeeManager;
    private final EmployeeService employeeService;

    public ShiftService() {
        this.employeeManager = EmployeeManager.getInstance();
        this.employeeService = new EmployeeService();
    }

//    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate) {
//        List<ShiftDTO> createdShifts = new ArrayList<>();
//        LocalDate currentDate = startDate;
//
//        // Create shifts for 7 days (a week)
//        for (int i = 0; i < 7; i++) {
//            // Create morning shift
//            ShiftDTO morningShift = employeeService.createShift(currentDate, "MORNING");
//            if (morningShift != null) {
//                createdShifts.add(morningShift);
//            }
//
//            // Create evening shift
//            ShiftDTO eveningShift = employeeService.createShift(currentDate, "EVENING");
//            if (eveningShift != null) {
//                createdShifts.add(eveningShift);
//            }
//
//            // Move to the next day
//            currentDate = currentDate.plusDays(1);
//        }
//
//        return createdShifts;
//    }

//    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate) {
//        List<ShiftDTO> createdShifts = new ArrayList<>();
//        LocalDate currentDate = startDate;
//
//        // מצא את כל התפקידים שדורשים מנהל משמרת
//        List<PositionDTO> managerPositions = employeeService.getAllPositions().stream()
//                .filter(PositionDTO::isRequiresShiftManager)
//                .collect(Collectors.toList());
//
//        if (managerPositions.isEmpty()) {
//            System.out.println("No shift manager positions defined in the system.");
//            return createdShifts;
//        }
//
//        // Create shifts for 7 days (a week)
//        for (int i = 0; i < 7; i++) {
//            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
//
//            // Create morning shift
//            ShiftDTO morningShift = employeeService.createShift(currentDate, "MORNING");
//            if (morningShift != null) {
//                boolean managerAssigned = false;
//
//                // עבור כל תפקיד מנהל משמרת
//                for (PositionDTO managerPosition : managerPositions) {
//                    // מצא עובדים מוסמכים לתפקיד זה
//                    List<EmployeeDTO> qualifiedManagers = employeeService.getQualifiedEmployeesForPosition(managerPosition.getName());
//
//                    // בדוק מי מהם זמין למשמרת
//                    for (EmployeeDTO manager : qualifiedManagers) {
//                        if (employeeService.isEmployeeAvailable(manager.getId(), dayOfWeek, "MORNING")) {
//                            // נסה לשבץ את העובד לתפקיד מנהל משמרת
//                            if (employeeService.assignEmployeeToShift(morningShift.getId(), manager.getId(), managerPosition.getName())) {
//                                managerAssigned = true;
//                                createdShifts.add(morningShift);
//                                break;
//                            }
//                        }
//                    }
//                    if (managerAssigned) break;
//                }
//
//                if (!managerAssigned) {
//                    // אם אין מנהל זמין, בטל את המשמרת
//                    System.out.println("No available shift manager for morning shift on " + currentDate + ". Shift not created.");
//                    // כאן צריך למחוק את המשמרת אם היא נוצרה
//                }
//            }
//
//            // Create evening shift
//            ShiftDTO eveningShift = employeeService.createShift(currentDate, "EVENING");
//            if (eveningShift != null) {
//                boolean managerAssigned = false;
//
//                // עבור כל תפקיד מנהל משמרת
//                for (PositionDTO managerPosition : managerPositions) {
//                    // מצא עובדים מוסמכים לתפקיד זה
//                    List<EmployeeDTO> qualifiedManagers = employeeService.getQualifiedEmployeesForPosition(managerPosition.getName());
//
//                    // בדוק מי מהם זמין למשמרת
//                    for (EmployeeDTO manager : qualifiedManagers) {
//                        if (employeeService.isEmployeeAvailable(manager.getId(), dayOfWeek, "EVENING")) {
//                            // נסה לשבץ את העובד לתפקיד מנהל משמרת
//                            if (employeeService.assignEmployeeToShift(eveningShift.getId(), manager.getId(), managerPosition.getName())) {
//                                managerAssigned = true;
//                                createdShifts.add(eveningShift);
//                                break;
//                            }
//                        }
//                    }
//                    if (managerAssigned) break;
//                }
//
//                if (!managerAssigned) {
//                    // אם אין מנהל זמין, בטל את המשמרת
//                    System.out.println("No available shift manager for evening shift on " + currentDate + ". Shift not created.");
//                }
//            }
//
//            currentDate = currentDate.plusDays(1);
//        }
//
//        return createdShifts;
//    }


    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate) {
        List<ShiftDTO> createdShifts = new ArrayList<>();
        LocalDate currentDate = startDate;

        // מצא את כל התפקידים שדורשים מנהל משמרת
        List<PositionDTO> managerPositions = employeeService.getAllPositions().stream()
                .filter(PositionDTO::isRequiresShiftManager)
                .collect(Collectors.toList());

        if (managerPositions.isEmpty()) {
            System.out.println("No shift manager positions defined in the system.");
            return createdShifts;
        }

        // Create shifts for 7 days (a week)
        for (int i = 0; i < 7; i++) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();

            // Create morning shift
            ShiftDTO morningShift = employeeService.createShift(currentDate, "MORNING");
            if (morningShift != null) {
                boolean managerAssigned = false;

                // חפש עובד זמין שמוסמך לאחד מתפקידי מנהל המשמרת
                for (EmployeeDTO employee : employeeService.getAllEmployees()) {
                    if (employeeService.isEmployeeAvailable(employee.getId(), dayOfWeek, "MORNING")) {
                        // בדוק אם העובד מוסמך לאחד מתפקידי מנהל המשמרת
                        for (PositionDTO managerPosition : managerPositions) {
                            if (employee.getQualifiedPositions().contains(managerPosition.getName())) {
                                // נסה לשבץ את העובד לתפקיד מנהל המשמרת
                                if (employeeService.assignEmployeeToShift(morningShift.getId(),
                                        employee.getId(), managerPosition.getName())) {
                                    managerAssigned = true;
                                    createdShifts.add(morningShift);
                                    break;
                                }
                            }
                        }
                        if (managerAssigned) break;
                    }
                }

                if (!managerAssigned) {
                    // אם אין מנהל זמין, מחק את המשמרת
                    employeeService.deleteShift(morningShift.getId());
                    System.out.println("No available shift manager for morning shift on " + currentDate + ". Shift deleted.");
                }
            }

            // Create evening shift
            ShiftDTO eveningShift = employeeService.createShift(currentDate, "EVENING");
            if (eveningShift != null) {
                boolean managerAssigned = false;

                // חפש עובד זמין שמוסמך לאחד מתפקידי מנהל המשמרת
                for (EmployeeDTO employee : employeeService.getAllEmployees()) {
                    if (employeeService.isEmployeeAvailable(employee.getId(), dayOfWeek, "EVENING")) {
                        // בדוק אם העובד מוסמך לאחד מתפקידי מנהל המשמרת
                        for (PositionDTO managerPosition : managerPositions) {
                            if (employee.getQualifiedPositions().contains(managerPosition.getName())) {
                                // נסה לשבץ את העובד לתפקיד מנהל המשמרת
                                if (employeeService.assignEmployeeToShift(eveningShift.getId(),
                                        employee.getId(), managerPosition.getName())) {
                                    managerAssigned = true;
                                    createdShifts.add(eveningShift);
                                    break;
                                }
                            }
                        }
                        if (managerAssigned) break;
                    }
                }

                if (!managerAssigned) {
                    // אם אין מנהל זמין, מחק את המשמרת
                    employeeService.deleteShift(eveningShift.getId());
                    System.out.println("No available shift manager for evening shift on " + currentDate + ". Shift deleted.");
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        return createdShifts;
    }
    public List<ShiftDTO> getFutureShifts() {
        LocalDate today = LocalDate.now();
        return employeeService.getAllShiftsAsDTO().stream().filter(shift -> !shift.getDate().isBefore(today)).collect(Collectors.toList());
    }

    public List<ShiftDTO> getHistoricalShifts() {
        LocalDate today = LocalDate.now();
        return employeeService.getAllShiftsAsDTO().stream().filter(shift -> shift.getDate().isBefore(today)).collect(Collectors.toList());
    }

    public List<ShiftDTO> getEmployeeShiftHistory(String employeeId) {
        LocalDate today = LocalDate.now();

        return employeeService.getAllShiftsAsDTO().stream()
                .filter(shift ->
                        shift.getDate().isBefore(today) &&  // רק משמרות עבר
                                shift.getAssignments().values().stream()
                                        .anyMatch(employeeName -> {
                                            EmployeeDTO employee = employeeService.getEmployeeDetails(employeeId);
                                            return employee != null && employee.getFullName().equals(employeeName);
                                        })
                )
                .collect(Collectors.toList());
    }


    public List<ShiftDTO> getEmployeeFutureShifts(String employeeId) {
        LocalDate today = LocalDate.now();
        return employeeService.getAllShiftsAsDTO().stream()
                .filter(shift ->
                        !shift.getDate().isBefore(today) &&   // משמרות עתידיות בלבד
                                shift.getAssignments().values().contains(employeeService.getEmployeeDetails(employeeId).getFullName())
                )
                .collect(Collectors.toList());
    }


    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        // Get domain shift
        Shift shift = employeeManager.getShift(shiftId);
        if (shift == null) {
            return new ArrayList<>();
        }

        RequiredPositions requiredPositions = employeeManager.getRequiredPositions();
        Map<Position, Integer> required = requiredPositions.getRequiredPositionsMap(shift.getShiftType());
        Map<Position, Employee> assigned = shift.getAllAssignedEmployees();

        // Create a list of missing positions
        List<PositionDTO> missingPositions = new ArrayList<>();

        for (Map.Entry<Position, Integer> entry : required.entrySet()) {
            Position position = entry.getKey();
            int requiredCount = entry.getValue();

            // Number of employees currently assigned to this position
            long currentCount = assigned.keySet().stream()
                    .filter(p -> p.getName().equals(position.getName()))
                    .count();

            // If there are missing employees for this position, add to the list
            if (currentCount < requiredCount) {
                for (int i = 0; i < (requiredCount - currentCount); i++) {
                    missingPositions.add(new PositionDTO(position.getName(), position.isRequiresShiftManager()));
                }
            }
        }

        return missingPositions;
    }

    public ShiftDTO getShiftById(String shiftId) {
        Shift shift = employeeManager.getShift(shiftId);
        if (shift == null) {
            return null;
        }

        return convertShiftToDTO(shift);
    }

    private ShiftDTO convertShiftToDTO(Shift shift) {
        // Get shift manager info
        String managerId = null;
        String managerName = null;
        if (shift.getShiftManager() != null) {
            managerId = shift.getShiftManager().getId();
            managerName = shift.getShiftManager().getFullName();
        }

        // Build assignments map (position name -> employee name)
        Map<String, String> assignments = new HashMap<>();
        for (Map.Entry<Position, Employee> entry : shift.getAllAssignedEmployees().entrySet()) {
            assignments.put(entry.getKey().getName(), entry.getValue().getFullName());
        }

        return new ShiftDTO(
                shift.getId(),
                shift.getDate(),
                shift.getShiftType().toString(),
                shift.getStartTime(),
                shift.getEndTime(),
                managerId,
                managerName,
                assignments
        );
    }
}