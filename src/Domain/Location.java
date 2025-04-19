package Domain;
import java.util.List;

public class Location {
    private String contact_name;
    private String contact_num;
    private String address;
    private Shipping_Zone zone;
    private List<Shipment_item> items;

public Location(String contact_name, String contact_num, String address, Shipping_Zone zone, List<Shipment_item> items) {
    this.contact_name = contact_name;
    this.contact_num = contact_num;
    this.address = address;
    this.zone = zone;
    this.items = items;
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

public void setAddress(String address) {
    this.address = address;
}

public Shipping_Zone getZone() {
    return zone;
}

public void setZone(Shipping_Zone zone) {
    this.zone = zone;
}

public List<Shipment_item> getItems() {
    return items;
}

public void setItems(List<Shipment_item> items) {
    this.items = items;
}
}
