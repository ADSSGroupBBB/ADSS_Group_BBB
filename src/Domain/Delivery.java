package Domain;

public class Delivery {
    private String date;
    private int dep_hour;
    private String truck_id;
    private String driver_name;
    private Route route;

    // Constructor
    public Delivery(String date, int dep_hour, String truck_id, String driver_name, Route route) {
        this.date = date;
        this.dep_hour = dep_hour;
        this.truck_id = truck_id;
        this.driver_name = driver_name;
        this.route = route;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDep_hour() {
        return dep_hour;
    }

    public void setDep_hour(int dep_hour) {
        this.dep_hour = dep_hour;
    }

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
