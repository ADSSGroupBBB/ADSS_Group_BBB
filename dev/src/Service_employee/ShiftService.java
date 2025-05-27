package Service_employee;


import Domain_employee.ShiftController;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller responsible for handling shift-related operations.
 * Acts as an intermediary between the presentation layer and the service layer.
 */
public class ShiftService {
    private final ShiftController shiftService;

    public ShiftService() {
        this.shiftService = new ShiftController();
    }

    /**
     * Creates shifts for an entire week starting from the given date.
     */
    public List<ShiftDTO> createShiftsForWeek(LocalDate startDate) {
        return shiftService.createShiftsForWeek(startDate);
    }

    /**
     * Gets all future shifts.
     */
    public List<ShiftDTO> getFutureShifts() {
        return shiftService.getFutureShifts();
    }

    /**
     * Gets historical shifts.
     */
    public List<ShiftDTO> getHistoricalShifts() {
        return shiftService.getHistoricalShifts();
    }

    /**
     * Gets shifts for a specific employee.
     */
    public List<ShiftDTO> getEmployeeShiftHistory(String employeeId) {
        return shiftService.getEmployeeShiftHistory(employeeId);
    }

    /**
     * Gets future shifts for a specific employee.
     */
    public List<ShiftDTO> getEmployeeFutureShifts(String employeeId) {
        return shiftService.getEmployeeFutureShifts(employeeId);
    }

    /**
     * Gets missing positions for a shift.
     */
    public List<PositionDTO> getMissingPositionsForShift(String shiftId) {
        return shiftService.getMissingPositionsForShift(shiftId);
    }

    /**
     * Gets a shift by ID.
     */
    public ShiftDTO getShiftById(String shiftId) {
        return shiftService.getShiftById(shiftId);
    }

    /**
     * Deletes a shift.
     */
    public boolean deleteShift(String shiftId) {
        return shiftService.deleteShift(shiftId);
    }
}