package Service_employee;

import Domain_employee.DataInitializationController;

/**
 * Controller responsible for initializing the system with sample data.
 * Acts as an intermediary between the presentation layer and the service layer.
 */
public class DataInitialization {
    private final DataInitializationController dataInitializationService;

    public DataInitialization() {
        this.dataInitializationService = new DataInitializationController();
    }

    /**
     * Initializes the system with sample data.
     * @return true if initialization was successful, false otherwise
     */
    public boolean initializeWithSampleData() {
        return dataInitializationService.initializeWithSampleData();
    }
}