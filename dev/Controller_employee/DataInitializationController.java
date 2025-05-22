package Controller_employee;

import Service_employee.DataInitializationService;

/**
 * Controller responsible for initializing the system with sample data.
 * Acts as an intermediary between the presentation layer and the service layer.
 */
public class DataInitializationController {
    private final DataInitializationService dataInitializationService;

    public DataInitializationController() {
        this.dataInitializationService = new DataInitializationService();
    }

    /**
     * Initializes the system with sample data.
     * @return true if initialization was successful, false otherwise
     */
    public boolean initializeWithSampleData() {
        return dataInitializationService.initializeWithSampleData();
    }
}