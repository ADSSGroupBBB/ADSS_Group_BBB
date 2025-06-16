package Service_employee;

import dto.BranchDTO;
import DataAccessDE.EmployeeInterface.BranchDAO;
import DataAccessDE.EmployeeDAO.BranchDAOImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for branch operations.
 * Provides a thin layer between presentation and DAO for branch management.
 */
public class BranchService {
    private final BranchDAO branchDAO;

    public BranchService() {
        this.branchDAO = new BranchDAOImpl();
    }

    /**
     * Gets all available branches from the system.
     * This reads dynamically from the database since branches can be added/removed.
     *
     * @return List of all available branches
     */
    public List<BranchDTO> getAllBranches() {
        try {
            return branchDAO.findAllBranches();
        } catch (SQLException e) {
            System.err.println("Error fetching branches: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Gets a specific branch by address.
     *
     * @param address The branch address
     * @return BranchDTO if found, null otherwise
     */
    public BranchDTO getBranchByAddress(String address) {
        try {
            Optional<BranchDTO> branch = branchDAO.findBranchByAddress(address);
            return branch.orElse(null);
        } catch (SQLException e) {
            System.err.println("Error fetching branch: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if a branch exists in the system.
     *
     * @param address The branch address to check
     * @return true if branch exists, false otherwise
     */
    public boolean branchExists(String address) {
        try {
            return branchDAO.branchExists(address);
        } catch (SQLException e) {
            System.err.println("Error checking branch existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets branch names as String array for UI display.
     *
     * @return Array of branch addresses for menu display
     */
    public String[] getBranchNamesForDisplay() {
        List<BranchDTO> branches = getAllBranches();
        if (branches.isEmpty()) {
            return new String[]{"No branches available"};
        }

        return branches.stream()
                .map(branch -> branch.getAddress() + " (" + branch.getZoneName() + ")")
                .toArray(String[]::new);
    }

    /**
     * Gets branch addresses only (without zone info) for internal use.
     *
     * @return Array of branch addresses
     */
    public String[] getBranchAddresses() {
        List<BranchDTO> branches = getAllBranches();
        return branches.stream()
                .map(BranchDTO::getAddress)
                .toArray(String[]::new);
    }
}