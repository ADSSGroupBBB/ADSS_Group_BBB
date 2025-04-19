package Domain;

public class Shipping_Zone {
    public int zone_num;
    private String zone_name;

    public Shipping_Zone(int num, String name) {
        this.zone_name = name;
        this.zone_num = num;
    }
    public int getNum() {
        return zone_num;
    }

    public void setNum(int num) {
        this.zone_num = num;
    }

    public String getName() {
        return zone_name;
    }

    public void setName(String name) {
        this.zone_name = name;
    }
}
