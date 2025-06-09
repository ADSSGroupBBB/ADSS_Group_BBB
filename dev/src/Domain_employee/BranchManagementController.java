package Domain_employee;

import DTO.BranchDTO;
import DataAccess.EmployeeInterface.BranchDAO;
import DataAccess.EmployeeDAO.BranchDAOImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for branch management operations.
 * Handles branch queries and validation.
 */
public class BranchManagementController {
    private final BranchDAO branchDAO;

    public BranchManagementController() {
        this.branchDAO = new BranchDAOImpl();
    }

    public List<BranchDTO> getAllBranches() {
        try {
            return branchDAO.findAllBranches();
        } catch (Exception e) {
            System.err.println("Error getting branches: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public BranchDTO getBranchByAddress(String address) {
        try {
            return branchDAO.findBranchByAddress(address).orElse(null);
        } catch (Exception e) {
            System.err.println("Error getting branch: " + e.getMessage());
            return null;
        }
    }

    public boolean branchExists(String address) {
        try {
            return branchDAO.branchExists(address);
        } catch (Exception e) {
            System.err.println("Error checking branch existence: " + e.getMessage());
            return false;
        }
    }

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

    public int getBranchCount() {
        try {
            return branchDAO.findAllBranches().size();
        } catch (Exception e) {
            System.err.println("Error getting branch count: " + e.getMessage());
            return 0;
        }
    }

    public boolean validateBranchAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return branchExists(address.trim());
    }

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
}