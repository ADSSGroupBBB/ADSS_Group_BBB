// ShiftAssignmentDAO.java
package DataAccess.EmployeeInterface;

import java.sql.SQLException;
import java.util.Map;

public interface ShiftAssignmentDAO {
    boolean assignEmployee(String shiftId, String employeeId, String positionName, boolean isShiftManager) throws SQLException;
    boolean removeAssignment(String shiftId, String positionName) throws SQLException;
    Map<String, String> getShiftAssignments(String shiftId) throws SQLException;
    boolean isEmployeeAssigned(String shiftId, String employeeId) throws SQLException;
    String getShiftManager(String shiftId) throws SQLException;
    String getAssignedEmployee(String shiftId, String positionName) throws SQLException;
}
