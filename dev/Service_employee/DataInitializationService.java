package Service_employee;

import Controller_employee.EmployeeController;
import Controller_employee.PositionController;
import Controller_employee.ShiftController;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Service responsible for initializing the system with sample data.
 * This service now uses Controllers for better separation of concerns.
 */
public class DataInitializationService {
    private final EmployeeController employeeController;
    private final PositionController positionController;
    private final ShiftController shiftController;

    public DataInitializationService() {
        this.employeeController = new EmployeeController();
        this.positionController = new PositionController();
        this.shiftController = new ShiftController();
    }

    /**
     * Initializes the system with sample data including employees, positions,
     * qualifications, availability settings, and shift requirements.
     *
     * @return true if initialization was successful, false otherwise
     */
    public boolean initializeWithSampleData() {
        try {
            // Add positions
            positionController.addPosition("Cashier", false);
            positionController.addPosition("Stocker", false);
            positionController.addPosition("Security", false);
            positionController.addPosition("Floor Manager", true);
            positionController.addPosition("Customer Service", false);

            // Set position requirements for shifts
            positionController.setRequiredPosition("MORNING", "Cashier", 2);
            positionController.setRequiredPosition("MORNING", "Stocker", 1);
            positionController.setRequiredPosition("MORNING", "Security", 1);
            positionController.setRequiredPosition("MORNING", "Floor Manager", 1);
            positionController.setRequiredPosition("MORNING", "Customer Service", 1);

            positionController.setRequiredPosition("EVENING", "Cashier", 3);
            positionController.setRequiredPosition("EVENING", "Stocker", 2);
            positionController.setRequiredPosition("EVENING", "Security", 1);
            positionController.setRequiredPosition("EVENING", "Floor Manager", 1);
            positionController.setRequiredPosition("EVENING", "Customer Service", 2);

            // Add regular employees
            LocalDate today = LocalDate.now();
            employeeController.addEmployee("111", "Yossi", "Cohen", "12345",
                    today.minusYears(2), 40.0, 10, 20, "Menora");

            employeeController.addEmployee("222", "Sara", "Levi", "23456",
                    today.minusYears(1), 42.0, 12, 15, "Harel");

            employeeController.addEmployee("333", "David", "Israeli", "34567",
                    today.minusMonths(8), 38.0, 8, 10, "Migdal");

            employeeController.addEmployee("444", "Noa", "Golan", "45678",
                    today.minusMonths(6), 45.0, 14, 18, "Phoenix");

            employeeController.addEmployee("555", "Moshe", "Peretz", "56789",
                    today.minusMonths(4), 40.0, 7, 12, "Clal");

            // Add a shift manager
            employeeController.addManagerEmployee("666", "Rachel", "Mizrahi", "67890",
                    today.minusYears(3), 55.0, "SHIFT_MANAGER", "password666",
                    15, 20, "Menora");

            // Add qualifications
            positionController.addQualificationToEmployee("111", "Cashier");
            positionController.addQualificationToEmployee("111", "Customer Service");

            positionController.addQualificationToEmployee("222", "Cashier");
            positionController.addQualificationToEmployee("222", "Stocker");

            positionController.addQualificationToEmployee("333", "Security");

            positionController.addQualificationToEmployee("444", "Customer Service");
            positionController.addQualificationToEmployee("444", "Cashier");

            positionController.addQualificationToEmployee("555", "Stocker");

            positionController.addQualificationToEmployee("666", "Floor Manager");

            // Set availability for all employees
            // By default all employees are available for all shifts
            // Let's set some specific unavailability to demonstrate the feature
            employeeController.updateEmployeeAvailability("111", DayOfWeek.FRIDAY, false, false);
            employeeController.updateEmployeeAvailability("222", DayOfWeek.SATURDAY, false, false);
            employeeController.updateEmployeeAvailability("333", DayOfWeek.SUNDAY, true, false);
            employeeController.updateEmployeeAvailability("444", DayOfWeek.MONDAY, false, true);

            // Create shifts for the upcoming week starting from the next Sunday
            LocalDate nextSunday = LocalDate.now();
            while (nextSunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
                nextSunday = nextSunday.plusDays(1);
            }

            List<ShiftDTO> shifts = shiftController.createShiftsForWeek(nextSunday);

            return true;
        } catch (Exception e) {
            System.out.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}