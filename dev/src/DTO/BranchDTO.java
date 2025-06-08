package DTO;

/**
 * Data Transfer Object for Branch information.
 * Represents a branch/location where employees can work.
 * This connects to the delivery module's locations table.
 */
public class BranchDTO {
    private final String address;
    private final String contactName;
    private final String contactNum;
    private final String zoneName;

    public BranchDTO(String address, String contactName, String contactNum, String zoneName) {
        this.address = address;
        this.contactName = contactName;
        this.contactNum = contactNum;
        this.zoneName = zoneName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public String getZoneName() {
        return zoneName;
    }

    @Override
    public String toString() {
        return address + " (" + zoneName + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BranchDTO branchDTO = (BranchDTO) obj;
        return address.equals(branchDTO.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}