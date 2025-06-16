package Domain;

import Domain.Location;
import java.util.List;

/**
 * Represents a shipment document used for tracking deliveries.
 */
public class Document {
    private List<Shipment_item> items; // List of items being shipped
    private String date; // Date of shipment
    private String truck_id; // Truck identifier
    private String dep_hour; // Departure hour
    private String driver_id; // Driver identifier
    private String dep_from; // Source location
    private List<Location> destinations; // List of destination locations
    private final String document_id; // Unique document ID
    private String eventMessage; // Optional event message or notes

    /**
     * Constructor for Document.
     */
    public Document(int id, List<Shipment_item> items, String date, String truck_id,
                    String dep_hour, String driver_id, String dep_from, List<Location> destinations, String eventMessage) {
        this.document_id = "DOC-" + String.format("%03d", id);
        this.items = items;
        this.date = date;
        this.truck_id = truck_id;
        this.dep_hour = dep_hour;
        this.driver_id = driver_id;
        this.dep_from = dep_from;
        this.destinations = destinations;
        this.eventMessage = eventMessage;
    }

    // Getters and Setters

    public List<Shipment_item> getItems() {
        return items;
    }

    public void setItems(List<Shipment_item> items) { // if in midway need to change items
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public String getDep_hour() {
        return dep_hour;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDep_from() {
        return dep_from;
    }

    public List<Location> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Location> destinations) {
        this.destinations = destinations;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Document{")
                .append("document_id=").append(document_id).append("\n")
                .append("date='").append(date).append("\n")
                .append("truck_id='").append(truck_id).append("\n")
                .append("dep_hour='").append(dep_hour).append("\n")
                .append("driver_id='").append(driver_id).append("\n")
                .append("dep_from='").append(dep_from).append("\n")
                .append("eventMessage='").append(eventMessage).append("\n")
                .append("items=").append(items).append("\n")
                .append("destinations=[");

        for (int i = 0; i < destinations.size(); i++) {
            sb.append("\n").append(destinations.get(i));
            if (i < destinations.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("\n]}");
        return sb.toString();
    }
}//