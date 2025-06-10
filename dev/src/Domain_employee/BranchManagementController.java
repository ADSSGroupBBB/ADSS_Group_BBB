package Domain_employee;

import DTO.BranchDTO;
import DataAccess.EmployeeInterface.BranchDAO;
import DataAccess.EmployeeDAO.BranchDAOImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for branch management operations.
 * Handles branch queries, validation, and branch-related business logic.
 * Note: Branch data comes from the delivery module's locations table.
 */
public class BranchManagementController {
    private final BranchDAO branchDAO;

    public BranchManagementController() {
        this.branchDAO = new BranchDAOImpl();
    }

    /**
     * Gets all available branches from the system.
     * This reads dynamically from the database since branches can be added/removed.
     */
    public List<BranchDTO> getAllBranches() {
        try {
            return branchDAO.findAllBranches();
        } catch (Exception e) {
            System.err.println("Error getting branches: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gets a specific branch by address.
     */
    public BranchDTO getBranchByAddress(String address) {
        try {
            Optional<BranchDTO> branch = branchDAO.findBranchByAddress(address);
            return branch.orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting branch: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if a branch exists in the system.
     */
    public boolean branchExists(String address) {
        try {
            return branchDAO.branchExists(address);
        } catch (Exception e) {
            System.err.println("Error checking branch existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets all branch addresses as a list of strings.
     */
    public List<String> getAllBranchAddresses() {
        try {
            List<BranchDTO> branches = branchDAO.findAllBranches();
            List<String> addresses = new ArrayList<>();
            for (BranchDTO branch : branches) {
                addresses.add(branch.getAddress());
            }
            return addresses;
        } catch (Exception e) {
            System.err.println("Error getting branch addresses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gets branches filtered by zone name.
     */
    public List<BranchDTO> getBranchesByZone(String zoneName) {
        try {
            List<BranchDTO> allBranches = branchDAO.findAllBranches();
            List<BranchDTO> branchesInZone = new ArrayList<>();

            for (BranchDTO branch : allBranches) {
                if (zoneName.equals(branch.getZoneName())) {
                    branchesInZone.add(branch);
                }
            }

            return branchesInZone;
        } catch (Exception e) {
            System.err.println("Error getting branches by zone: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gets the total number of branches in the system.
     */
    public int getBranchCount() {
        try {
            return branchDAO.findAllBranches().size();
        } catch (Exception e) {
            System.err.println("Error getting branch count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Validates that a branch address is valid and exists.
     */
    public boolean validateBranchAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return branchExists(address.trim());
    }

    /**
     * Gets all unique zone names from all branches.
     */
    public List<String> getZoneNames() {
        try {
            List<BranchDTO> branches = branchDAO.findAllBranches();
            List<String> zoneNames = new ArrayList<>();

            for (BranchDTO branch : branches) {
                String zoneName = branch.getZoneName();
                if (!zoneNames.contains(zoneName)) {
                    zoneNames.add(zoneName);
                }
            }

            return zoneNames;
        } catch (Exception e) {
            System.err.println("Error getting zone names: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gets branch names formatted for UI display (includes zone info).
     */
    public String[] getBranchNamesForDisplay() {
        List<BranchDTO> branches = getAllBranches();
        if (branches.isEmpty()) {
            return new String[]{"No branches available"};
        }

        String[] displayNames = new String[branches.size()];
        for (int i = 0; i < branches.size(); i++) {
            BranchDTO branch = branches.get(i);
            displayNames[i] = branch.getAddress() + " (" + branch.getZoneName() + ")";
        }
        return displayNames;
    }

    /**
     * Gets branch addresses only (without zone info) for internal use.
     */
    public String[] getBranchAddressesArray() {
        List<BranchDTO> branches = getAllBranches();
        return branches.stream()
                .map(BranchDTO::getAddress)
                .toArray(String[]::new);
    }

    /**
     * Finds the branch with the most employees (requires integration with employee data).
     */
    public BranchDTO findLargestBranchByEmployeeCount() {
        // This would require integration with EmployeeDAO to count employees per branch
        // For now, just return the first branch as a placeholder
        List<BranchDTO> branches = getAllBranches();
        return branches.isEmpty() ? null : branches.get(0);
    }

    /**
     * Gets branch statistics including employee count and other metrics.
     */
    public BranchStatistics getBranchStatistics(String branchAddress) {
        try {
            BranchDTO branch = getBranchByAddress(branchAddress);
            if (branch == null) {
                return null;
            }

            // Create statistics object with basic branch info
            // Additional statistics would require integration with other DAOs
            return new BranchStatistics(
                    branch.getAddress(),
                    branch.getZoneName(),
                    branch.getContactName(),
                    branch.getContactNum(),
                    0, // employeeCount - would need EmployeeDAO integration
                    0, // futureShiftsCount - would need ShiftDAO integration
                    0  // activeDeliveriesCount - would need delivery module integration
            );
        } catch (Exception e) {
            System.err.println("Error getting branch statistics: " + e.getMessage());
            return null;
        }
    }

    /**
     * Inner class for branch statistics
     */
    public static class BranchStatistics {
        private final String address;
        private final String zoneName;
        private final String contactName;
        private final String contactNum;
        private final int employeeCount;
        private final int futureShiftsCount;
        private final int activeDeliveriesCount;

        public BranchStatistics(String address, String zoneName, String contactName,
                                String contactNum, int employeeCount, int futureShiftsCount,
                                int activeDeliveriesCount) {
            this.address = address;
            this.zoneName = zoneName;
            this.contactName = contactName;
            this.contactNum = contactNum;
            this.employeeCount = employeeCount;
            this.futureShiftsCount = futureShiftsCount;
            this.activeDeliveriesCount = activeDeliveriesCount;
        }

        // Getters
        public String getAddress() { return address; }
        public String getZoneName() { return zoneName; }
        public String getContactName() { return contactName; }
        public String getContactNum() { return contactNum; }
        public int getEmployeeCount() { return employeeCount; }
        public int getFutureShiftsCount() { return futureShiftsCount; }
        public int getActiveDeliveriesCount() { return activeDeliveriesCount; }

        @Override
        public String toString() {
            return String.format("Branch: %s (%s) - Employees: %d, Future Shifts: %d, Active Deliveries: %d",
                    address, zoneName, employeeCount, futureShiftsCount, activeDeliveriesCount);
        }
    }

    /**
     * Validates that a branch has all required information.
     */
    public boolean validateBranchData(BranchDTO branch) {
        if (branch == null) {
            return false;
        }

        return branch.getAddress() != null && !branch.getAddress().trim().isEmpty() &&
                branch.getContactName() != null && !branch.getContactName().trim().isEmpty() &&
                branch.getContactNum() != null && !branch.getContactNum().trim().isEmpty() &&
                branch.getZoneName() != null && !branch.getZoneName().trim().isEmpty();
    }

    /**
     * Gets branches that match a search term in their address or zone name.
     */
    public List<BranchDTO> searchBranches(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllBranches();
        }

        try {
            List<BranchDTO> allBranches = getAllBranches();
            List<BranchDTO> matchingBranches = new ArrayList<>();
            String lowerSearchTerm = searchTerm.toLowerCase();

            for (BranchDTO branch : allBranches) {
                if (branch.getAddress().toLowerCase().contains(lowerSearchTerm) ||
                        branch.getZoneName().toLowerCase().contains(lowerSearchTerm) ||
                        branch.getContactName().toLowerCase().contains(lowerSearchTerm)) {
                    matchingBranches.add(branch);
                }
            }

            return matchingBranches;
        } catch (Exception e) {
            System.err.println("Error searching branches: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Checks if there are any branches available in the system.
     */
    public boolean hasBranches() {
        return getBranchCount() > 0;
    }

    /**
     * Gets the default branch (first one alphabetically) if available.
     */
    public BranchDTO getDefaultBranch() {
        List<BranchDTO> branches = getAllBranches();
        if (branches.isEmpty()) {
            return null;
        }

        // Return the first branch alphabetically by address
        return branches.stream()
                .min((b1, b2) -> b1.getAddress().compareToIgnoreCase(b2.getAddress()))
                .orElse(branches.get(0));
    }
}