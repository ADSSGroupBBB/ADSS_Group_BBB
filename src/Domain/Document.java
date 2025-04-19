package Domain;

import java.util.List;

public class Document {
    private List<Shipment_item> items;
    private String date;
    private String truck_id;
    private int dep_hour;
    private String driver_id;
    private String dep_from;
    private String destinations;
    private String document_id;

    public Document(String document_id, List<Shipment_item> items, String date, String truck_id,
                    int dep_hour, String driver_id, String dep_from, String destinations) {
        this.document_id = document_id;
        this.items = items;
        this.date = date;
        this.truck_id = truck_id;
        this.dep_hour = dep_hour;
        this.driver_id = driver_id;
        this.dep_from = dep_from;
        this.destinations = destinations;
    }

    // Getters and Setters
    public List<Shipment_item> getItems() {
        return items;
    }

    public void setItems(List<Shipment_item> items) {
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public int getDep_hour() {
        return dep_hour;
    }

    public void setDep_hour(int dep_hour) {
        this.dep_hour = dep_hour;
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

    public void setDep_from(String dep_from) {
        this.dep_from = dep_from;
    }

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public String getDocument_id() {
        return document_id;
    }

    public void setDocument_id(String document_id) {
        this.document_id = document_id;
    }
}