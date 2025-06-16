// BranchDAO.java
package src.DataAccess.EmployeeInterface;

import src.DTO.BranchDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BranchDAO {
    List<BranchDTO> findAllBranches() throws SQLException;
    Optional<BranchDTO> findBranchByAddress(String address) throws SQLException;
    boolean branchExists(String address) throws SQLException;
}