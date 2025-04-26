package Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Service responsible for initializing the system with sample data.
 * This service populates the system with employees, positions, and shifts
 * to allow testing of all functionality without manual data entry.
 */
public class DataInitializationService {
    private final EmployeeService employeeService;
    private final ShiftService shiftService;

    public DataInitializationService() {
        this.employeeService = new EmployeeService();
        this.shiftService = new ShiftService();
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
            employeeService.addPosition("Cashier", false);
            employeeService.addPosition("Stocker", false);
            employeeService.addPosition("Security", false);
            employeeService.addPosition("Floor Manager", true);
            employeeService.addPosition("Customer Service", false);

            // Set position requirements for shifts
            employeeService.addRequiredPosition("MORNING", "Cashier", 2);
            employeeService.addRequiredPosition("MORNING", "Stocker", 1);
            employeeService.addRequiredPosition("MORNING", "Security", 1);
            employeeService.addRequiredPosition("MORNING", "Floor Manager", 1);
            employeeService.addRequiredPosition("MORNING", "Customer Service", 1);

            employeeService.addRequiredPosition("EVENING", "Cashier", 3);
            employeeService.addRequiredPosition("EVENING", "Stocker", 2);
            employeeService.addRequiredPosition("EVENING", "Security", 1);
            employeeService.addRequiredPosition("EVENING", "Floor Manager", 1);
            employeeService.addRequiredPosition("EVENING", "Customer Service", 2);

            // Add regular employees
            LocalDate today = LocalDate.now();
            employeeService.addNewEmployee("111", "Yossi", "Cohen", "12345",
                    today.minusYears(2), 40.0, 10, 20, "Menora");

            employeeService.addNewEmployee("222", "Sara", "Levi", "23456",
                    today.minusYears(1), 42.0, 12, 15, "Harel");

            employeeService.addNewEmployee("333", "David", "Israeli", "34567",
                    today.minusMonths(8), 38.0, 8, 10, "Migdal");

            employeeService.addNewEmployee("444", "Noa", "Golan", "45678",
                    today.minusMonths(6), 45.0, 14, 18, "Phoenix");

            employeeService.addNewEmployee("555", "Moshe", "Peretz", "56789",
                    today.minusMonths(4), 40.0, 7, 12, "Clal");

            // Add a shift manager
            employeeService.addNewEmployee("666", "Rachel", "Mizrahi", "67890",
                    today.minusYears(3), 55.0, "SHIFT_MANAGER", "password666",
                    15, 20, "Menora");

            // Add qualifications
            employeeService.addQualificationToEmployee("111", "Cashier");
            employeeService.addQualificationToEmployee("111", "Customer Service");

            employeeService.addQualificationToEmployee("222", "Cashier");
            employeeService.addQualificationToEmployee("222", "Stocker");

            employeeService.addQualificationToEmployee("333", "Security");

            employeeService.addQualificationToEmployee("444", "Customer Service");
            employeeService.addQualificationToEmployee("444", "Cashier");

            employeeService.addQualificationToEmployee("555", "Stocker");

            employeeService.addQualificationToEmployee("666", "Floor Manager");

            // Set availability for all employees
            // By default all employees are available for all shifts
            // Let's set some specific unavailability to demonstrate the feature
            employeeService.updateEmployeeAvailability("111", DayOfWeek.FRIDAY, false, false);
            employeeService.updateEmployeeAvailability("222", DayOfWeek.SATURDAY, false, false);
            employeeService.updateEmployeeAvailability("333", DayOfWeek.SUNDAY, true, false);
            employeeService.updateEmployeeAvailability("444", DayOfWeek.MONDAY, false, true);

            // Create shifts for the upcoming week starting from the next Sunday
            LocalDate nextSunday = LocalDate.now();
            while (nextSunday.getDayOfWeek() != DayOfWeek.SUNDAY) {
                nextSunday = nextSunday.plusDays(1);
            }

            List<ShiftDTO> shifts = shiftService.createShiftsForWeek(nextSunday);

            return true;
        } catch (Exception e) {
            System.out.println("Error initializing data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}