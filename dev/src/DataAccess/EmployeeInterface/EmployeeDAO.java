package DataAccess.EmployeeInterface;

import DTO.EmployeeDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmployeeDAO {
    Optional<EmployeeDTO> findById(String id) throws SQLException;
    List<EmployeeDTO> findAll() throws SQLException;
    List<EmployeeDTO> findByBranch(String branchAddress) throws SQLException;
    EmployeeDTO save(EmployeeDTO employee) throws SQLException;
    boolean deleteById(String id) throws SQLException;
    boolean updateEmployee(EmployeeDTO employee) throws SQLException;
    List<EmployeeDTO> findByRole(String role) throws SQLException;
}