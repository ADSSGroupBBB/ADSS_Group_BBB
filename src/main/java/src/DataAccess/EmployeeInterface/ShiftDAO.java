package src.DataAccess.EmployeeInterface;

import src.DTO.ShiftDTO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShiftDAO {
    Optional<ShiftDTO> findById(String id) throws SQLException;
    List<ShiftDTO> findAll() throws SQLException;
    List<ShiftDTO> findByDateRange(LocalDate startDate, LocalDate endDate) throws SQLException;
    List<ShiftDTO> findByBranch(String branchAddress) throws SQLException;
    ShiftDTO save(ShiftDTO shift) throws SQLException;
    boolean deleteById(String id) throws SQLException;
    List<ShiftDTO> findFutureShifts() throws SQLException;
    List<ShiftDTO> findHistoricalShifts() throws SQLException;
    List<ShiftDTO> findEmployeeShifts(String employeeId) throws SQLException;
}