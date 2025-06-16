// RequiredPositionDAO.java
package DataAccess.EmployeeInterface;

import java.sql.SQLException;
import java.util.Map;

public interface RequiredPositionDAO {
    boolean setRequiredPosition(String shiftType, String positionName, int count) throws SQLException;
    int getRequiredCount(String shiftType, String positionName) throws SQLException;
    Map<String, Integer> getRequiredPositions(String shiftType) throws SQLException;
    boolean removeRequiredPosition(String shiftType, String positionName) throws SQLException;
}