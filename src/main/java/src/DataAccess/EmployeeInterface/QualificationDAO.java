// QualificationDAO.java
package src.DataAccess.EmployeeInterface;

import java.sql.SQLException;
import java.util.List;

public interface QualificationDAO {
    boolean addQualification(String employeeId, String positionName) throws SQLException;
    boolean removeQualification(String employeeId, String positionName) throws SQLException;
    List<String> getEmployeeQualifications(String employeeId) throws SQLException;
    List<String> getQualifiedEmployees(String positionName) throws SQLException;
    boolean hasQualification(String employeeId, String positionName) throws SQLException;
}
