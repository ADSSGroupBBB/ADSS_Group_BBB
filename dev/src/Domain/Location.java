package Domain;
import java.util.List;

/**
 * Represents a destination or source location in the shipment process.
 */
public class Location {
    private String contact_name; // Contact person at the location
    private String contact_num; // Contact number
    private String address; // Address of the location
    private Shipping_Zone zone; // Shipping zone to which the location belongs
    private List<Shipment_item> items_required; // List of shipment items required at this location

    // Constructor
    public Location(String contact_name, String contact_num, String address, Shipping_Zone zone) {
        this.contact_name = contact_name;
        this.contact_num = contact_num;
        this.address = address;
        this.zone = zone;
        this.items_required = null; // Initially no items assigned
    }

    // Getters and Setters
    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_num() {
        return contact_num;
    }

    public void setContact_num(String contact_num) {
        this.contact_num = contact_num;
    }

    public String getAddress() {
        return address;
    }

    public Shipping_Zone getZone() {
        return zone;
    }

    public void setZone(Shipping_Zone zone) {
        this.zone = zone;
    }

    public List<Shipment_item> getItems_required() {
        return items_required;
    }

    public void setItems_required(List<Shipment_item> items) {
        this.items_required = items;
    }

    // String representation of the location
    @Override
    public String toString() {
        return "{ Address: " + address + "\n" +
                "Contact Name: " + contact_name + "\n" +
                "Contact Number: " + contact_num + "\n" +
                "Zone: " + zone + "}";
    }
}