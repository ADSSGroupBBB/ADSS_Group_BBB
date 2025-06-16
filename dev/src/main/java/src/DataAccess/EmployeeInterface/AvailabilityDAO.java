// AvailabilityDAO.java
package src.DataAccess.EmployeeInterface;

import java.sql.SQLException;
import java.time.DayOfWeek;

public interface AvailabilityDAO {
    boolean updateAvailability(String employeeId, DayOfWeek dayOfWeek,
                               boolean morningAvailable, boolean eveningAvailable) throws SQLException;
    boolean isAvailable(String employeeId, DayOfWeek dayOfWeek, String shiftType) throws SQLException;
    boolean setDefaultAvailability(String employeeId) throws SQLException;
}